package com.nightson;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapFragmentNew extends Fragment
        implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private GoogleMap mMap;
    Marker currLocationMarker;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    public MapFragmentNew()
    {

    }

    @Override
    public void onLocationChanged(Location location)
    {
        Toast.makeText(getActivity().getApplicationContext(),
                "HELLO LOC change", Toast.LENGTH_LONG).show();
        mLastLocation = location;
        if (currLocationMarker != null)
        {
            currLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(),
                location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        mMap.addCircle(
                new CircleOptions().center(latLng).radius(300).strokeWidth(0)
                        .fillColor(0x25808080));
        mMap.setMapStyle(MapStyleOptions
                .loadRawResourceStyle(getActivity().getBaseContext(),
                        R.raw.ub_map));

        //stop location updates
        if (mGoogleApiClient != null)
        {
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient, this);
        }
        loadnearbyparties(location, mMap.getCameraPosition().zoom);

    }

    private void loadnearbyparties(Location location, float zoomlev)
    {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        double radius = Math.exp((16 - zoomlev) * Math.log(2)) * 500;
        Log.d("radius", Double.toString(radius));
        // volley code
        String url =
                "http://vswamy.net:8888/search?latitude=" + lat + "&longitude="
                        + lng + "&radius=20000";

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                double latitude, longitude;
                String party_name;
                // the response is already constructed as a JSONObject!
                Log.d(" responsesindhu ", response.toString());
                try
                {
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String loc = jsonObject.getString("location");
                        String arr[] = loc.split(",");
                        latitude = Double.parseDouble((arr[1].split("\\["))[1]);
                        //longitude = Double.parseDouble(arr[2].substring(0,arr[2].length()-2));
                        if (i == 0)
                            longitude = Double.parseDouble("-118.2811084");
                        else
                            longitude = Double.parseDouble("-118.2804536");
                        party_name = jsonObject.getString("name");

                        // adding marker
                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(latitude, longitude);
                        markerOptions.position(latLng);
                        markerOptions.title(party_name);

                        mMap.addMarker(markerOptions);
                    }
                    //Toast.makeText(getBaseContext(),lat,Toast.LENGTH_LONG).show();
                    //response = response.getJSONObject(0);
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
                headers.put("x-auth-token",
                        "$2b$12$freZc8ldmGXka/5O40YDcuZ8uqQ.WwzVaDsFB0imjX3BaTqV0AlTC");
                return headers;
            }
        };

        VolleyHelper.getInstance(getActivity().getBaseContext())
                .add(jsonRequest);

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
        //  Toast.makeText(this, requestCode, Toast.LENGTH_LONG).show();
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    // Permission was granted.
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

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getActivity().getBaseContext(),
                            "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity().getBaseContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            if (ActivityCompat
                    .shouldShowRequestPermissionRationale(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION))
            {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }
            else
            {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        //View view = inflater.inflate(R.layout.map_view, vg, false);
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm
                .findFragmentById(R.id.map2);
        //SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
        // .findFragmentById(R.id.map2);
        //        if(mapFragment!=null)
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(View view,
            @Nullable
                    Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //        MapFragment mFragment = (MapFragment)getChildFragmentManager().findFragmentById(R.id.map2);
        //        mFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
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
    }

    @Override
    public void onConnected(
            @Nullable
                    Bundle bundle)
    {
        Toast.makeText(getActivity().getBaseContext(), "HELLO",
                Toast.LENGTH_LONG).show();
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
