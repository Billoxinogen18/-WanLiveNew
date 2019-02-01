package com.ogalo.partympakache.wanlive;

import android.app.ProgressDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;
import com.duowan.mobile.netroid.cache.BitmapImageCache;
import com.duowan.mobile.netroid.cache.DiskCache;
import com.duowan.mobile.netroid.request.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.ogalo.partympakache.wanlive.adapter.WanAdapter;
import com.ogalo.partympakache.wanlive.app.SelfImageLoader;
import com.ogalo.partympakache.wanlive.app.WanLive;
import com.ogalo.partympakache.wanlive.data.Model1;
import com.ogalo.partympakache.wanlive.data.WanItem;
import com.ogalo.partympakache.wanlive.groupchannel.CreateGroupChannelActivity;
import com.ogalo.partympakache.wanlive.groupchannel.GroupChannelActivity;
import com.ogalo.partympakache.wanlive.utils.PreferenceUtils;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.util.List;


public class WanMaps extends BaseActivity
        implements OnMapReadyCallback,  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener,
        View.OnClickListener {
    private GoogleApiClient mGoogleApiClient;
    Bundle intent;
    private List<WanItem> markerItems = new ArrayList<WanItem>();
    public double latitude;
    int PERMISSION_REQUEST_CODE;
    public String ratingso;
    public String timeso;
    private SweetAlertDialog pDialog;

    public double longitude;
    public LocationManager locationManager;
    public String titleso;
    public String contentso;

    public String imgso;
    public String costso;


    public Criteria criteria;
    public String bestProvider;

    private String URL_FEED = "http://butterknife.000webhostapp.com/wanrest/wan_alldata.php";


    private static final String TAG = WanMaps.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private ImageView settings;
    private ImageView inbox;
    private ImageView message;
    private ImageView location;
    private AVLoadingIndicatorView avi;
    private Button logout;
    private FirebaseAuth mAuth;
    private Button places;
    private double longitudess;
    private double latitudess;
    private Double latitSelected;
    private Double longitSelected;
    private String latits;
    private String longits;
    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    Context wanmaps=WanMaps.this;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    Location currentLocation;
    private String[] mLikelyPlaceNames;
    private SwipeRefreshLayout swipeContainer;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;
    LatLng latLngi;


    @Override
    protected void initNetroid() {
        File diskCacheDir = new File(getCacheDir(), "WanLIve");
        int diskCacheSize = 50 * 1024 * 1024; // 50MB
        WanLive.init(new DiskCache(diskCacheDir, diskCacheSize));
        WanLive.setImageLoader(new SelfImageLoader(WanLive.getRequestQueue(),
                new BitmapImageCache(diskCacheSize), getResources(), getAssets()));
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient  =new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View view) {
        Log.v(TAG,"view click event");
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, "onMarkerClick", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Toast.makeText(this, "onMarkerDragStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Toast.makeText(this, "onMarkerDrag", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // getting the Co-ordinates
        latitudess = marker.getPosition().latitude;
        longitudess = marker.getPosition().longitude;

        //move to current position
        moveMap();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        getDeviceLocation();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        buildGoogleApiClient();





        mAuth = FirebaseAuth.getInstance();

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_wan_maps);




        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

avi=findViewById(R.id.avi);


processEventListener();






//        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        places=(Button)findViewById(R.id.places);

        message=(ImageView)findViewById(R.id.message);
        settings=(ImageView)findViewById(R.id.settings);
        inbox=(ImageView)findViewById(R.id.createmessage);
        location=(ImageView)findViewById(R.id.navigate);
        logout=(Button)findViewById(R.id.logout);


        PlaceAutocompleteFragment placesb= (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .build();

        placesb.setFilter(typeFilter);
        placesb.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                LatLng latLng=place.getLatLng();


//                mMap.addMarker(new MarkerOptions()
////                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
//                        .title(place.getName().toString())
////                            .snippet(feedObj.getString("matime"))
//                        .snippet(place.getAddress().toString())
//
//                        .position(latLng));


                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(13).build();

                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

                Toast.makeText(getApplicationContext(),place.getName(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {

                Toast.makeText(getApplicationContext(),status.toString(),Toast.LENGTH_SHORT).show();

            }
        });



        places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainFeed.class));
            }
        });





        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GroupChannelActivity.class));
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WanSettings.class));
            }
        });

        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateGroupChannelActivity.class));
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialogFragmentPost();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                disconnect();
            }
        });



















    }





    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        if (mMap != null) {
//            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
//            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
           super.onSaveInstanceState(outState);
//




    }

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.current_place_menu, menu);
        return true;
    }

    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            showCurrentPlace();
        }
        return true;
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;


        // Add a marker in Sydney and move the camera
        LatLng nairobi = new LatLng(36.78168639999999, -1.280738900000000);
        mMap.addMarker(new MarkerOptions().position(nairobi).title("Nairobi"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nairobi));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);


        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

processEventListener();

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.

    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
//         try {
//             if (mLocationPermissionGranted) {

//                 locationManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);
//                 criteria = new Criteria();
//                 bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

//                 //You can still do this if you like, you might get lucky:
//                 Location location = locationManager.getLastKnownLocation(bestProvider);
//                 if (location != null) {
//                     Log.e("TAG", "GPS is on");
//                     latitude = location.getLatitude();
//                     longitude = location.getLongitude();

//                     mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                             new LatLng(latitude,
//                                     longitude), DEFAULT_ZOOM));

//                 }
//                 else{
//                     //This is what you need:
//                     Log.d(TAG, "Current location is null. Using defaults.");

// //                    Toast.makeText(this, "Location is null", Toast.LENGTH_SHORT).show();

//                     mMap.moveCamera(CameraUpdateFactory
//                             .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//                     mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                 }
















// //                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
// //                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
// //                    @Override
// //                    public void onComplete(@NonNull Task<Location> task) {
// //                        if (task.isSuccessful()) {
// //                            // Set the map's camera position to the current location of the device.
// //                            mLastKnownLocation = task.getResult();
// //                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
// //                                    new LatLng(mLastKnownLocation.getLatitude(),
// //                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
// //                        } else {
// //                            Log.d(TAG, "Current location is null. Using defaults.");
// //                            Log.e(TAG, "Exception: %s", task.getException());
// //                            mMap.moveCamera(CameraUpdateFactory
// //                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
// //                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
// //                        }
// //                    }
// //                });
//             }
//         } catch (SecurityException e)  {
//             Log.e("Exception: %s", e.getMessage());
//         }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitudess = location.getLongitude();
            latitudess = location.getLatitude();

            //moving the map to location
            moveMap();
        }
    }






    private void processEventListener() {







        StringRequest request = new StringRequest(URL_FEED, new Listener<String>() {
            ProgressDialog mPrgsDialog;

            @Override
            public void onPreExecute() {

startAnim();


                          }

            // cancel the dialog with onFinish() callback
            @Override
            public void onFinish() {
                stopAnim();
            }

            @Override
            public void onSuccess(String response) {


                try {
                    JSONObject responses=new JSONObject(response);
                    parseJsonFeed(responses);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(getApplicationContext(), "response is ： " + response, 2000).show();
            }

            @Override
            public void onError(NetroidError error) {
                stopAnim();
                Toast.makeText(getApplicationContext(), error.getMessage(), 2000).show();
            }

            @Override
            public void onCancel() {
                stopAnim();
                Toast.makeText(getApplicationContext(), "request was cancel", 2000).show();
            }
        });

// add the request to RequestQueue, will execute quickly if has idle thread
        WanLive.add(request);
    }


    void stopAnim(){
        avi.hide();
        // or avi.smoothToHide();
    }

    void startAnim(){
        avi.show();
        // or avi.smoothToShow();
    }
    private void moveMap() {

        /**
         * Creating the latlng object to store lat, long coordinates
         * adding marker to map
         * move the camera with animation
         */
        LatLng latLng = new LatLng(latitudess, longitudess);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title("Your location"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }



    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }




    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                // Set the count, handling cases where less than 5 entries are returned.
                                int count;
                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                    count = likelyPlaces.getCount();
                                } else {
                                    count = M_MAX_ENTRIES;
                                }

                                int i = 0;
                                mLikelyPlaceNames = new String[count];
                                mLikelyPlaceAddresses = new String[count];
                                mLikelyPlaceAttributions = new String[count];
                                mLikelyPlaceLatLngs = new LatLng[count];

                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                    // Build a list of likely places to show the user.
                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
                                            .getAddress();
                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                            .getAttributions();
                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                                    i++;
                                    if (i > (count - 1)) {
                                        break;
                                    }
                                }

                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();

                                // Show a dialog offering the user the list of likely places, and add a
                                // marker at the selected place.
                                openPlacesDialog();

                            } else {
                                Log.e(TAG, "Exception: %s", task.getException());
                            }
                        }
                    });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));






            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
                String markerSnippet = mLikelyPlaceAddresses[which];
                if (mLikelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(mLikelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(mLikelyPlaceNames, listener)
                .show();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void disconnect() {
        SendBird.unregisterPushTokenAllForCurrentUser(new SendBird.UnregisterPushTokenHandler() {
            @Override
            public void onUnregistered(SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();

                    // Don't return because we still need to disconnect.
                } else {
//                    Toast.makeText(MainActivity.this, "All push tokens unregistered.", Toast.LENGTH_SHORT).show();
                }

                ConnectionManager.logout(new SendBird.DisconnectHandler() {
                    @Override
                    public void onDisconnected() {
                        PreferenceUtils.setConnected(false);
                        mAuth.signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }








    private void parseJsonFeed(JSONObject response) {
        try {


            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);



                final WanItem item = new WanItem();
                Double latitude=Double.parseDouble(feedObj.getString("latitude"));
                Double longitude=Double.parseDouble(feedObj.getString("longitude"));

                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("title"));
//                Toast.makeText(this, ischecked, Toast.LENGTH_SHORT).show();
                item.setCost(feedObj.getString("cost"));

                item.setTimes(feedObj.getString("time"));
                item.setStatus(feedObj.getString("status"));

                item.setRating(feedObj.getString("rating"));
                item.setLatitude(feedObj.getString("latitude"));
                item.setLongitude(feedObj.getString("longitude"));


                markerItems.add(item);




                String rating=item.getRating();
                Double rates=Double.parseDouble(rating);
                Double hi=4.5;
                Double himidhi=4.0;
                Double himid=3.5;
                Double himidmid=3.0;
                Double lowmid=2.0;
                Double low=1.0;
                Double what=0.0;
                String io=Integer.toString(i);


                LatLng latLng = new LatLng(latitude,
                        longitude);






                if (i == feedArray.length()-1) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng).zoom(13).build();

                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));

                    getDeviceLocation();


                }


                if(rates>=hi) {

                    // Create a marker for each city in the JSON data.
                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .title(feedObj.getString("title"))
//                            .snippet(feedObj.getString("matime"))
                            .snippet(io)

                            .position(latLng));



//                    Model1 model1 = new Model1(feedObj.getString("title"), feedObj.getString("status"), feedObj.getString("rating"), feedObj.getString("title"),  feedObj.getString("cost"));
//                    Map<String, Object> postValues = model1.toMap();
//
//                    Map<String, Object> childUpdates = new HashMap<>();
//                    childUpdates.put( "map",postValues);

                }
                else
                if(rates>=himidhi&&rates<hi){

                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                            .title(feedObj.getString("title"))
                            .snippet(io)
                            .position(latLng));


                }

                else
                if(rates>=himid&&rates<himidhi){

                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                            .title(feedObj.getString("title"))
                            .snippet(io)
                            .position(latLng));


                }

                else
                if(rates>=himidmid&&rates<himid){

                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .title(feedObj.getString("title"))
                            .snippet(io)
                            .position(latLng));


                }

                else
                if(rates>=lowmid&&rates<himidmid){

                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                            .title(feedObj.getString("title"))
                            .snippet(io)
                            .position(latLng));


                }
                else
                if(rates>=low&&rates<lowmid){

                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .title(feedObj.getString("title"))

                            .snippet(io)
                            .position(latLng));














                }

                else
                if(rates>=what&&rates<lowmid){

                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .title(feedObj.getString("title"))

                            .snippet(io)
                            .position(latLng));














                }








                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoContents(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoWindow(Marker arg0) {

                        View v = getLayoutInflater().inflate(R.layout.customlayout, null);

                        TextView tLocation = (TextView) v.findViewById(R.id.location);

                        TextView tSnippet = (TextView) v.findViewById(R.id.population);


                        Integer posis=Integer.parseInt(arg0.getSnippet());


                        WanItem billistired=markerItems.get(posis);

                        tLocation.setText(arg0.getTitle());
                        setContentso(billistired.getStatus());
                        setTitleso(billistired.getName());
                        setTimeso(billistired.getTimes());
                        setRatingso(billistired.getRating());
                        setImgso(billistired.getImge());
                        setLatits(billistired.getLatitude());
                        setLongits(billistired.getLongitude());
                        setCostso(billistired.getCost());




                        setTitleso(arg0.getTitle());






                        tSnippet.setText(billistired.getTimes());

//                        Toast.makeText(WanMaps.this, "Title is "+titleso, Toast.LENGTH_SHORT).show();


//                            titleso=arg0.getTitle();
//
//
//
//
//                            contentso=tSnippet.toString();
                        return v;

                    }
                });


                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                    }
                });


                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        WanItem itemo=new WanItem();
                        Bundle intent=new Bundle();
                        intent.putString("title", marker.getTitle());

                        showBottomSheetDialogFragment();




                    }
                });

                String longitudef=feedObj.getString("latitude");
//                 String longituded=feedObj.getString("longitude");
//                Toast.makeText(this, item.getLatitude(), Toast.LENGTH_SHORT).show();



                String name=feedObj.getString("title");
//                String longitude=feedObj.getString("longitude");




                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
//                item.setTimeStamp(feedObj.getString("timeStamp"));
//                item.setTimeStamp(feedObj.getString("timeStamp"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("location");
                item.setUrl(feedUrl);




            }

            // notify data changes to list adapater

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void setMarkerClickListener(GoogleMap mMap) {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                showBottomSheetDialogFragment();












                return false;
            }
        });
    }



    public void showBottomSheetDialogFragment() {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();




        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_container);


        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
        //left, top, right, bottom


        bottomSheetFragment.setArguments(intent);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

//        params.setMargins(10, 0, 10, 0); //setting margin left to 10px
        frameLayout.setLayoutParams(params);

    }







    public void showBottomSheetDialogFragmentPost() {
        BottomSheetNewPost bottomSheetFragmentPost = new BottomSheetNewPost();




        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_container);


        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
        //left, top, right, bottom


        bottomSheetFragmentPost.setArguments(intent);
        bottomSheetFragmentPost.show(getSupportFragmentManager(), bottomSheetFragmentPost.getTag());

//        params.setMargins(10, 0, 10, 0); //setting margin left to 10px
        frameLayout.setLayoutParams(params);

    }



    public void setTitleso(String titleso)
    {
        this.titleso=titleso;

    }



    public void setImgso(String imgso)
    {
        this.imgso=imgso;

    }
    public String getImgso()
    {
        return imgso;
    }


    public void setCostso(String costso)
    {
        this.costso=costso;

    }
    public String getCostso()
    {
        return costso;
    }


    public void setLatits(String latits)
    {
        this.latits=latits;

    }
    public String getLatits()
    {
        return latits;
    }



    public void setLongits(String longits)
    {
        this.longits=longits;

    }
    public String getLongits()
    {
        return longits;
    }




    public void setContentso(String contentso)
    {
        this.contentso=contentso;

    }

    public String getTitleso()
    {
        return titleso;
    }



    public String getContentso()
    {
        return contentso;
    }



    public String getRatingso()
    {
        return ratingso;
    }



    public String getTimeso()
    {
        return timeso;
    }




    public void setRatingso(String ratingso)
    {
        this.ratingso=ratingso;

    }

    public void setTimeso(String timeso)
    {
        this.timeso=timeso;

    }

    public String getTitles()
    {
        return titleso;
    }
    public void setLatitSelected(Double latitSelected)
    {

        this.latitSelected=latitSelected;

    }

    public void setLongitSelected(Double longitSelected)
    {

        this.longitSelected=longitSelected;

    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.

            mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {


                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub

                        mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("You"));
                    }
                });

            }
        }
    }























}