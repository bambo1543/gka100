package it.bambo.gka100;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.bambo.gka100.model.GpsInfo;
import it.bambo.gka100.model.MapViewCamera;
import it.bambo.gka100.service.GpsService;
import it.bambo.gka100.sms.SMSReceiver;
import it.bambo.gka100.sms.SMSSender;


public class MainActivity extends ActionBarActivity {

    public static final String NAME_TO_GSP_INFO_MAP = "nameToGspInfoMap";
    public static final String LAST_MAP_VIEW_CAMERA = "lastMapViewCamera";

    private SMSReceiver smsReceiver;

    private GoogleMap googleMap;

    private Map<String, Marker> nameToMarkerMap = new HashMap<>();
    private Map<Marker, GpsInfo> markerToGpsInfoMap = new HashMap<>();
    private HashMap<String, List<GpsInfo>> nameToGpsInfoMap = new HashMap<>();

    private boolean rectangleMode = false;
    private LatLng rectangle1 = null;
    private LatLng rectangle2 = null;
    private Polygon polygon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LIFECYCLE", "onCreate");
        setContentView(R.layout.activity_main);

        if(Env.smsReceiver == null) {
            registerSMSReceiver();
        }
        smsReceiver.setMainActivityHandler(this);

        if(savedInstanceState != null) {
            nameToGpsInfoMap = (HashMap<String, List<GpsInfo>>) savedInstanceState.getSerializable(NAME_TO_GSP_INFO_MAP);
        }
        initMapView(savedInstanceState != null ? (MapViewCamera) savedInstanceState.getSerializable(LAST_MAP_VIEW_CAMERA) : null);
    }

    private void registerSMSReceiver() {
        Log.i("SMSReceiver", "Register SMSReceiver...");
        smsReceiver = new SMSReceiver();
        IntentFilter callInterceptorIntentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, callInterceptorIntentFilter);
    }

    private void unregisterSMSReceiver() {
        if(smsReceiver != null)
            unregisterReceiver(smsReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_update:
                if (Env.isMock) {
                    try {
                        GpsInfo gpsInfo = GpsService.getInstance().handleResponse(Env.getNextTestGpsResponse());
                        updateGpsInfo(gpsInfo);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    SMSSender smsSender = SMSSender.getInstance();
                    smsSender.sendAction(getApplicationContext(), "TEST GPS");
                }
                return true;
            case R.id.action_rectangle:
                if(rectangleMode) {
                    rectangleMode = false;
                } else {
                    rectangleMode = true;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void initMapView(final MapViewCamera mapViewCamera) {
        Log.i("MAPVIEW", "Create MapView...");
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if (null == googleMap) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();
                googleMap.setMyLocationEnabled(true);

                UiSettings uiSettings = googleMap.getUiSettings();
                uiSettings.setAllGesturesEnabled(true);
                uiSettings.setCompassEnabled(true);
                uiSettings.setMapToolbarEnabled(true);
                uiSettings.setMyLocationButtonEnabled(true);
                uiSettings.setZoomControlsEnabled(true);

                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        return null;
                    }
                });
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        if (rectangleMode) {
                            if (rectangle1 == null) {
                                rectangle1 = latLng;
//                                googleMap.addMarker()
                            } else {
                                rectangle2 = latLng;
                                polygon = googleMap.addPolygon(new PolygonOptions().add(rectangle1, rectangle2)
                                );

                            }
                        }
                    }
                });
                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if (googleMap == null) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }

                for (String name : nameToGpsInfoMap.keySet()) {
                    List<GpsInfo> gpsInfos = nameToGpsInfoMap.get(name);
                    if(!gpsInfos.isEmpty()) {
                        GpsInfo lastGps = gpsInfos.get(gpsInfos.size() - 1);
                        updateGpsInfo(lastGps, true);
                    }
                }

                MapViewCamera camera = mapViewCamera;
                if(camera == null) {
                    Location location = getLocation();
                    camera = new MapViewCamera(location.getLatitude(), location.getLongitude(), 10f, 0f, 0f);
                }
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition(camera.getLatLng(), camera.getZoom(),
                                camera.getTilt(), camera.getBearing())));
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }
    }

    private Location getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        return locationManager.getLastKnownLocation(provider);
    }

    public void updateGpsInfo(GpsInfo gpsInfo) {
        updateGpsInfo(gpsInfo, false);
    }

    public void updateGpsInfo(GpsInfo gpsInfo, boolean silent) {
        if (null != googleMap) {
            Marker marker = nameToMarkerMap.get(gpsInfo.getName());
            if (marker != null) {
                markerToGpsInfoMap.remove(marker);
                marker.remove();
            }
            marker = googleMap.addMarker(new MarkerOptions()
                            .position(gpsInfo.getLatLng())
                            .title(gpsInfo.getName())
                            .snippet(gpsInfo.getSnippet())
            );

            nameToMarkerMap.put(gpsInfo.getName(), marker);
            markerToGpsInfoMap.put(marker, gpsInfo);

            List<GpsInfo> gpsInfos = nameToGpsInfoMap.get(gpsInfo.getName());
            if (gpsInfos == null) {
                gpsInfos = new ArrayList<>();
                nameToGpsInfoMap.put(gpsInfo.getName(), gpsInfos);
            }
            gpsInfos.add(gpsInfo);

            if(!silent)
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(gpsInfo.getLatLng()));
        }
    }

    @Override
    protected void onPause() {
        Log.i("LIFECYCLE", "onPause");
        super.onPause();
//        unregisterSMSReceiver();
    }

    @Override
    protected void onStop() {
        Log.i("LIFECYCLE", "onStop");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.i("LIFECYCLE", "onResume");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.i("LIFECYCLE", "onRestart");
        super.onRestart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("LIFECYCLE", "onSaveInstanceState");
        outState.putSerializable(NAME_TO_GSP_INFO_MAP, nameToGpsInfoMap);

        CameraPosition cp = googleMap.getCameraPosition();
        outState.putSerializable(LAST_MAP_VIEW_CAMERA, new MapViewCamera(cp.target.latitude, cp.target.longitude,
                cp.zoom, cp.tilt, cp.bearing));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("LIFECYCLE", "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        Log.i("LIFECYCLE", "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }



    //    private class PopupAdapter implements GoogleMap.InfoWindowAdapter {
//        private View popup=null;
//        private LayoutInflater inflater=null;
//
//        PopupAdapter(LayoutInflater inflater) {
//            this.inflater=inflater;
//        }
//
//        @Override
//        public View getInfoWindow(Marker marker) {
//            return(null);
//        }
//
//        @SuppressLint("InflateParams")
//        @Override
//        public View getInfoContents(Marker marker) {
//            if (popup == null) {
//                popup=inflater.inflate(R.layout.popup, null);
//            }
//
//            TextView tv=(TextView)popup.findViewById(R.id.title);
//
//            tv.setText(marker.getTitle());
//            tv=(TextView)popup.findViewById(R.id.snippet);
//            tv.setText(marker.getSnippet());
//
//            return(popup);
//        }
//  }
}
