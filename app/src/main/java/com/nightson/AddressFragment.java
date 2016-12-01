package com.nightson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
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

public class AddressFragment extends Fragment
        implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private GoogleMap mMap;
    Marker currLocationMarker;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private onBackButtonClickListener listener;
    Activity activity;
    private MarkerOptions addressmarker;
    private Marker currlocation;
    Double currlat, currlng;

    public AddressFragment()
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
    public void onMapClick(LatLng latLng)
    {

        MarkerOptions addressmarker = new MarkerOptions();

        addressmarker.position(latLng);
        addressmarker.title(latLng.latitude + " : " + latLng.longitude);
        mMap.clear();
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        currlocation = mMap.addMarker(addressmarker);
        LatLng lalng = currlocation.getPosition();
        currlat = lalng.latitude;
        currlng = lalng.longitude;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater
                .inflate(R.layout.fragment_address, container, false);
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm
                .findFragmentById(R.id.map3);
        mapFragment.getMapAsync(this);
        // Setting a click event handler for the map

        FloatingActionButton btn;
        btn = (FloatingActionButton) view.findViewById(R.id.savebtn);
        btn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                listener.onmapclick(currlat, currlng);

            }
        });
        return view;
    }

    public void onAttach(Activity activity)
    {

        this.activity = activity;
        super.onAttach(activity);
        if (activity instanceof onBackButtonClickListener)
        {
            listener = (onBackButtonClickListener) activity;
        }
        else
        {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    public interface onBackButtonClickListener
    {
        public void onmapclick(Double lat, Double lng);
    }

    @Override
    public void onViewCreated(View view,
            @Nullable
                    Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
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
