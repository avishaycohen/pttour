package com.avishaycohen.petahtikvatour_followthefounders;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //the map
    private GoogleMap mMap;
    private HashMap markers;
    private HashMap urls;
    // starting zoom value
    public static final float START_ZOOM = 15;
    //center of town
    public static final LatLng CENTER = new LatLng(32.087948,34.883956);
    // a class to make textView clickable
    public void openBrowser(View view){

        //Get url from tag
        String url = (String)view.getTag();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);

        //pass the url to intent data
        intent.setData(Uri.parse(url));

        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markers = new HashMap();
        urls = new HashMap();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Setting a custom info window adapter for the google map
        googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
                TypedArray imgs = getResources().obtainTypedArray(R.array.locations_img);
                // Getting the position from the marker
                LatLng latLng = arg0.getPosition();

                // Getting reference to the TextView to set title
                TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
                // Getting reference to the TextView to set info
                TextView tvInfo = (TextView) v.findViewById(R.id.tv_info);
                // Getting reference to the ImageView to set the img for location
                ImageView ivImage = (ImageView) v.findViewById(R.id.iv_image);
                // Getting reference to the TextView to set link to external url
//                TextView tvLink = (TextView) v.findViewById(R.id.tv_link);
                // Setting the title
                tvTitle.setText(arg0.getTitle());
                // Setting the info
                tvInfo.setText(arg0.getSnippet());
                // Setting the image
                ivImage.setImageResource((int) markers.get(arg0.getId()));
                // make the link clickable
//                tvLink.setClickable(true);
//                tvLink.setMovementMethod(LinkMovementMethod.getInstance());
//                String text = (String) urls.get(arg0.getTitle());
//                Log.i("url = ", text);
                // Setting the link url
//                tvLink.setText(Html.fromHtml(text));
                // Returning the view containing InfoWindow contents
                return v;
            }
        });

        final Button btnOpenPopup = (Button)findViewById(R.id.openAboutButton);
        btnOpenPopup.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater
                        = (LayoutInflater)getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup_about_window,null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

                Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
                btnDismiss.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAtLocation(btnOpenPopup, Gravity.START, 50, -30);

            }});

        final Button btnReCenter = (Button)findViewById(R.id.centerMapToTrip);
        btnReCenter.setOnClickListener(new Button.OnClickListener() {
            @Override

            public void onClick(View arg0) {
                mMap.moveCamera(CameraUpdateFactory.zoomTo(START_ZOOM));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(CENTER));
            }});

        //the following will hold the coordinates for the points of inerest in the city
        ArrayList<LatLng> interestPoints = new ArrayList<LatLng>();
        PolylineOptions route = new PolylineOptions();

        //hold the north values of the coordinate map
        double[] northPoints = new double[]{
                32.089483,
                32.088805,
                32.086431,
                32.085204,
                32.084938,
                32.086554,
                32.087391,
                32.087582,
                32.087987,
                32.089743,
                32.089344,
                32.089075,
                32.087962,
                32.087566,
                32.087844,
                32.090166
        };

        //hold the east values of the coordinate map
        double[] eastPoints = new double[]{
                34.88095,
                34.880871,
                34.881796,
                34.88371,
                34.885445,
                34.886906,
                34.887457,
                34.887537,
                34.887127,
                34.885697,
                34.885375,
                34.884646,
                34.882902,
                34.883288,
                34.882317,
                34.881963,
        };

        //get the names of all the points to load them into the map
        String[] pointsNames = getResources().getStringArray(R.array.locations_names);
        String[] pointsInfo = getResources().getStringArray(R.array.locations_snippets);
        String[] pointsUrls = getResources().getStringArray(R.array.location_urls);
        TypedArray imgs;
        imgs = getResources().obtainTypedArray(R.array.locations_img);
        // just to have how many points i've stored
        int size = northPoints.length;
        //iterate over the points create them as data and create them as markers
        for (int i = 0; i < size; i++) {
            interestPoints.add(i, new LatLng(northPoints[i], eastPoints[i]));
            route.add(interestPoints.get(i));
            Marker currMarker = mMap.addMarker(new MarkerOptions()
                    .position(interestPoints.get(i))
                    .alpha(0.7f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
                    .snippet(pointsInfo[i])
                    .title(pointsNames[i]));
            markers.put(currMarker.getId(), imgs.getResourceId(i, 0));
//            urls.put(currMarker.getTitle(), pointsUrls[i]);
        }
        /** add the route options: **/
        route.geodesic(true);
        route.color(Color.LTGRAY);
        /** paint it on map **/
        mMap.addPolyline(route);

        // Instantiates a new Polygon object and adds points to define a rectangle
        /* the following is just a sqare */
        /*PolygonOptions rectOptions = new PolygonOptions()
                .add(new LatLng(32.091228, 34.880080),
                        new LatLng(32.091228, 34.887770),
                        new LatLng(32.084765, 34.887770),
                        new LatLng(32.084765,34.880080));
        */
        PolygonOptions rectOptions = new PolygonOptions()
                .add(new LatLng(32.092162, 34.878083),
                        new LatLng(32.088398, 34.877472),
                        new LatLng(32.087162, 34.878180),
                        new LatLng(32.084363, 34.878416),
                        new LatLng(32.084308, 34.879832),
                        new LatLng(32.083363, 34.881227),
                        new LatLng(32.083381, 34.882944),
                        new LatLng(32.085726, 34.891462),
                        new LatLng(32.091107, 34.890025),
                        new LatLng(32.092434, 34.888909),
                        new LatLng(32.093852, 34.890711),
                        new LatLng(32.095288, 34.889123));

        // Get back the mutable Polygon
        Polygon polygon = mMap.addPolygon(rectOptions);

        //move the camera to the center of PT city and zoom in
        mMap.moveCamera(CameraUpdateFactory.zoomTo(START_ZOOM));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(CENTER));



        /**
         * this is not good coding - i'm bypassing google's permission policy
         * assuming i'll not have any users using Android 6 or above (M or later)
         * so it works only between 4.X.X to 5.X.X
         */
        mMap.setMyLocationEnabled(true);

        /**
         * This is me trying to enforce boundries and failing, need to understand better...
         * ****************************************************************************************
         * Set the camera to the greatest possible zoom level that includes the                   *
         * bounds                                                                                 *
         * mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(PETAH_TIKVA, (int) START_ZOOM));   *
         * ****************************************************************************************
         */

    }

}
