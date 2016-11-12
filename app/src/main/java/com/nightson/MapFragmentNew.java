package com.nightson;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nightson.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragmentNew extends Fragment implements OnMapReadyCallback
{
    private GoogleMap mMap;

    public MapFragmentNew()
    {
        // Required empty public constructor

}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_map, container, false);
        //View view = inflater.inflate(R.layout.map_view, vg, false);
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map2);
        //SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
               // .findFragmentById(R.id.map2);
//        if(mapFragment!=null)
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        MapFragment mFragment = (MapFragment)getChildFragmentManager().findFragmentById(R.id.map2);
//        mFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(34.0224, -118.2851);

        mMap.addMarker(
                new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        mMap.addCircle(
                new CircleOptions().center(sydney).radius(300).strokeWidth(0)
                        .fillColor(0x25808080));
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.ub_map));
    }


}
