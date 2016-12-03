package com.nightson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapFragmentNew extends Fragment
        implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener
{
    private GoogleMap mMap;
    Marker currLocationMarker;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation = null;
    HashMap<Marker, JSONObject> map = new HashMap<Marker, JSONObject>();
    private HeatmapTileProvider mProvider = null;
    private TileOverlay toverlay = null;
    ArrayList<WeightedLatLng> heatmap_locations = new ArrayList<WeightedLatLng>();

    public MapFragmentNew()
    {

    }

    public void clearMap()
    {
        mMap.clear();
    }

    public void addCurrentPositionMarker(LatLng latLng)
    {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        mMap.addCircle(new CircleOptions().center(latLng)
                .radius(Constants.DEFAULT_RADIUS).strokeWidth(0)
                .fillColor(0x25808080));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if(mLastLocation != null && mLastLocation.distanceTo(location) < Constants.REDRAW_METERS)
            return;

        mLastLocation = location;

        clearMap();
        LatLng latLng = new LatLng(location.getLatitude(),
                location.getLongitude());
        addCurrentPositionMarker(latLng);
        loadnearbyparties(latLng);
    }

    private void loadnearbyparties(LatLng latlng)
    {
        float zoomlev = mMap.getCameraPosition().zoom;
        double lat = latlng.latitude;
        double lng = latlng.longitude;
        double radius = Math.exp((16 - zoomlev) * Math.log(2)) * 500;

        // volley code
        String url =
                "http://vswamy.net:8888/search?latitude=" + lat + "&longitude="
                        + lng + "&radius=2000";
        if(super.getContext() == null)
            return;
        SharedPreferences preferences = super.getContext()
                .getSharedPreferences(Constants.PREF_FILE_NAME, 0);
        final String token = preferences.getString("x-auth-token", "None");

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                double latitude, longitude;
                String party_name;
                Double rsvp_count;
                // the response is already constructed as a JSONObject!
                Log.d(" responsesindhu ", response.toString());
                try
                {
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String loc = jsonObject.getString("location");
                        String arr[] = loc.split(",");
                        longitude = Double
                                .parseDouble((arr[1].split("\\["))[1]);
                        latitude = Double.parseDouble(
                                arr[2].substring(0, arr[2].length() - 2));
                        party_name = jsonObject.getString("name");
                        rsvp_count = Double.parseDouble(jsonObject.getString("rsvp"));

                        // adding marker
                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(latitude, longitude);
                        WeightedLatLng wtlatlng = new WeightedLatLng(latLng,rsvp_count);
                        heatmap_locations.add(wtlatlng);
                        markerOptions.icon(BitmapDescriptorFactory
                                .defaultMarker(
                                        BitmapDescriptorFactory.HUE_AZURE));
                        markerOptions.position(latLng);
                        markerOptions.title(party_name);

                        Marker x = mMap.addMarker(markerOptions);
                        map.put(x, jsonObject);

                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("x-auth-token", token);
                return headers;
            }
        };

        VolleyHelper.getInstance(getActivity().getBaseContext())
                .add(jsonRequest);

    }

    protected void add_heatmap(ArrayList<WeightedLatLng> locations) {
        if(toverlay == null) {
            int[] colors = {
                    Color.rgb(0, 0, 128), // navy
                    Color.rgb(255, 0, 0)    // red
            };
            float[] startPoints = {
                    0.2f, 1f
            };
            Gradient gradient = new Gradient(colors, startPoints);
            mProvider = new HeatmapTileProvider.Builder().weightedData(locations).gradient(gradient).build();
            mProvider.setRadius(100);
            toverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
        else
        {
            toverlay.remove();
            toverlay=null;
        }
    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        clearMap();
        addCurrentPositionMarker(latLng);
        loadnearbyparties(latLng);
    }

    public boolean onMarkerClick(Marker marker)
    {
        JSONObject jsob = map.get(marker);
        Intent i = new Intent(getActivity(), RsvpDescActivity.class);
        try
        {
            i.putExtra("name", jsob.getString("name"));
            i.putExtra("starttime", jsob.getString("start_time"));
            i.putExtra("endtime", jsob.getString("end_time"));
            i.putExtra("rsvp", jsob.getInt("rsvp"));
            i.putExtra("id", jsob.getString("id"));
            i.putExtra("url", jsob.getString("photo_url"));

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        startActivity(i);
        return true;
    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(
                getActivity().getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED)
                    {

                        if (mGoogleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                }
                else
                {
                    Toast.makeText(getActivity().getBaseContext(),
                            "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        super.onAttach(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        FloatingActionButton btn, heatmapbtn;
        btn = (FloatingActionButton) view.findViewById(R.id.addEvents);
        heatmapbtn = (FloatingActionButton) view.findViewById(R.id.addHeatmap);
        btn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), AddEventActivity.class);
                startActivity(i);

            }
        });

        heatmapbtn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                add_heatmap(heatmap_locations);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view,
            @Nullable
                    Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onMyLocationButtonClick()
    {
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            return false;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        clearMap();
        addCurrentPositionMarker(latLng);
        loadnearbyparties(latLng);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else
        {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setMapStyle(MapStyleOptions
                .loadRawResourceStyle(getActivity().getBaseContext(),
                        R.raw.ub_map));


        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(location == null)
        {
            location = new Location("");
            location.setLatitude(34.0224);
            location.setLongitude(118.2851);
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        clearMap();
        addCurrentPositionMarker(latLng);
        loadnearbyparties(latLng);
    }

    @Override
    public void onConnected(
            @Nullable
                    Bundle bundle)
    {
        LocationRequest mLocationRequest;
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity().getBaseContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, mLocationRequest,
                            this);
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(
            @NonNull
                    ConnectionResult connectionResult)
    {

    }
}
