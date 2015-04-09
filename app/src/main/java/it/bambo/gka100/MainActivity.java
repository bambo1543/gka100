package it.bambo.gka100;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.bambo.gka100.manager.GpsManager;
import it.bambo.gka100.model.GpsInfo;
import it.bambo.gka100.model.MapViewCamera;
import it.bambo.gka100.sms.SMSSender;


public class MainActivity extends ActionBarActivity {

    public static final String NAME_TO_GSP_INFO_MAP = "nameToGspInfoMap";
    public static final String LAST_MAP_VIEW_CAMERA = "lastMapViewCamera";

    private SMSSender smsSender = SMSSender.getInstance();

    private GoogleMap googleMap;

    private Map<String, Marker> nameToMarkerMap = new HashMap<>();
    private Map<Marker, GpsInfo> markerToGpsInfoMap = new HashMap<>();
    private HashMap<String, List<GpsInfo>> nameToGpsInfoMap = new HashMap<>();

    private UpdateMarkerTask updateMarkerTask;

    private boolean rectangleMode = false;
    private LatLng rectangle1 = null;
    private LatLng rectangle2 = null;
    private Polygon polygon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LIFECYCLE", "onCreate");
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            nameToGpsInfoMap = (HashMap<String, List<GpsInfo>>) savedInstanceState.getSerializable(NAME_TO_GSP_INFO_MAP);
        }
        initMapView(savedInstanceState != null ? (MapViewCamera) savedInstanceState.getSerializable(LAST_MAP_VIEW_CAMERA) : null);

        SharedPreferences p = getSharedPreferences();
        if(p.contains("gps_time")) {
            GpsInfo gpsInfo = getGpsInfoFromPreferences(p);
            updateMarker(gpsInfo);
        }

        updateMarkerTask = new UpdateMarkerTask(this);
    }

    public GpsInfo getGpsInfoFromPreferences(SharedPreferences p) {
        return new GpsInfo(p.getString("gps_name", ""), new Date(p.getLong("gps_time", 0)),
                                    p.getInt("gps_speed", 0), p.getFloat("gps_lat", 0), p.getFloat("gps_lng", 0), p.getFloat("gps_alt", 0), p.getInt("gps_sat_count", 0));
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
            case R.id.action_update_position:
                if (Env.isMock) {
                    try {
                        GpsInfo gpsInfo = GpsManager.getInstance().handleResponse(Env.getNextTestGpsResponse(), getSharedPreferences());
                        updateMarker(gpsInfo);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    smsSender.sendAction(getApplicationContext(), "TEST GPS");
                }
                return true;
            case R.id.action_update_status:
                smsSender.sendAction(getApplicationContext(), "STATUS");
                return true;
            case R.id.action_alarm_on:
                smsSender.sendAction(getApplicationContext(), "ALARM ENABLE");
                return true;
            case R.id.action_alarm_off:
                smsSender.sendAction(getApplicationContext(), "ALARM DISABLE");
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
        Log.i("MainActivity", "Create MapView...");
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

                googleMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
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
                        updateMarker(lastGps, true);
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

    public void updateMarker(GpsInfo gpsInfo) {
        updateMarker(gpsInfo, false);
    }

    public void updateMarker(GpsInfo gpsInfo, boolean silent) {
        Log.i("MainActivity", "updateMarker: " +gpsInfo.toString() + " silent:" + silent);
        if (null != googleMap) {
            Marker marker = nameToMarkerMap.get(gpsInfo.getName());
            if (marker != null) {
                markerToGpsInfoMap.remove(marker);
                marker.remove();
            }
            boolean alarm = getSharedPreferences().getBoolean("deviceStatus_alarm", false);
            boolean alarmReleased = getSharedPreferences().contains("alarm_released");
            BitmapDescriptor icon;
            if (alarmReleased) {
                icon = BitmapDescriptorFactory.fromResource(R.drawable.yellow_car);
            } else if (alarm) {
                icon = BitmapDescriptorFactory.fromResource(R.drawable.green_car);
            } else {
                icon = BitmapDescriptorFactory.fromResource(R.drawable.red_car);
            }

            marker = googleMap.addMarker(new MarkerOptions()
                            .position(gpsInfo.getLatLng())
                            .title(gpsInfo.getName())
                            .snippet(gpsInfo.getSnippet())
                            .icon(icon)
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
                CameraUpdateFactory.newCameraPosition(
                        new CameraPosition(gpsInfo.getLatLng(), 10f, 0f, 0f));
//                googleMap.animateCamera(CameraUpdateFactory.newLatLng(gpsInfo.getLatLng()));
        }
    }

    private void startUpdateMarkerTask() {
        if(updateMarkerTask == null || updateMarkerTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            updateMarkerTask = new UpdateMarkerTask(this);
        }
        if(!updateMarkerTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            updateMarkerTask.execute();
        }
    }

    private void stopUpdateMarkerTask() {
        if(updateMarkerTask != null) {
            updateMarkerTask.setUpdateMarkerTaskRunning(false);
            updateMarkerTask = null;
        }
    }

    @Override
    protected void onStart() {
        Log.i("LIFECYCLE", "onStart");
        super.onStart();
        startUpdateMarkerTask();
    }

    @Override
    protected void onResume() {
        Log.i("LIFECYCLE", "onResume");
        super.onResume();
        startUpdateMarkerTask();
    }

    @Override
    protected void onRestart() {
        Log.i("LIFECYCLE", "onRestart");
        super.onRestart();
        startUpdateMarkerTask();
    }

    @Override
    protected void onPause() {
        Log.i("LIFECYCLE", "onPause");
        super.onPause();
        stopUpdateMarkerTask();
    }

    @Override
    protected void onStop() {
        Log.i("LIFECYCLE", "onStop");
        super.onStop();
        stopUpdateMarkerTask();
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

    private class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View view;

        private MarkerInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.marker_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
//            if (MainActivity.this.marker != null
//                    && MainActivity.this.marker.isInfoWindowShown()) {
//                MainActivity.this.marker.hideInfoWindow();
//                MainActivity.this.marker.showInfoWindow();
//            }
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            final GpsInfo gpsInfo = markerToGpsInfoMap.get(marker);
            SharedPreferences preferences = getSharedPreferences();

            TextView nameView = (TextView) view.findViewById(R.id.deviceNameValue);
            nameView.setText(gpsInfo.getName());

            final TextView alarmView = (TextView) view.findViewById(R.id.alarmValue);
            if(preferences.contains("deviceStatus_alarm")) {
                alarmView.setText((preferences.getBoolean("deviceStatus_alarm", false) ? "on" : "off"));
            } else {
                alarmView.setText("unknown");
            }

            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(6);
            final TextView latView = (TextView) view.findViewById(R.id.latValue);
            latView.setText(numberFormat.format(gpsInfo.getLatLng().latitude));
            
            final TextView lngView = (TextView) view.findViewById(R.id.lngValue);
            lngView.setText(numberFormat.format(gpsInfo.getLatLng().longitude));
            
            final TextView speedView = (TextView) view.findViewById(R.id.speedValue);
            speedView.setText(gpsInfo.getSpeed() + "km/h");

            final TextView timeGpsView = (TextView) view.findViewById(R.id.timeGpsValue);
            timeGpsView.setText(Constants.TIME_DATE_FORMAT.format(gpsInfo.getTime()));


            final TextView accuView = (TextView) view.findViewById(R.id.accuValue);
            if(preferences.contains("deviceStatus_accu")) {
                accuView.setText(preferences.getInt("deviceStatus_accu", 0) + "%");
            } else {
                accuView.setText("unknown");
            }

            final TextView gsmView = (TextView) view.findViewById(R.id.gsmValue);
            if(preferences.contains("deviceStatus_gsm")) {
                gsmView.setText(preferences.getInt("deviceStatus_gsm", 0) + "%");
            } else {
                gsmView.setText("unknown");
            }

            final TextView shockView = (TextView) view.findViewById(R.id.shockValue);
            if(preferences.contains("deviceStatus_shock")) {
                shockView.setText(preferences.getString("deviceStatus_shock", "") + "/10");
            } else {
                shockView.setText("unknown");
            }

            final TextView timeStatusView = (TextView) view.findViewById(R.id.timeStatusValue);
            if(preferences.contains("deviceStatus_time")) {
                timeStatusView.setText(Constants.TIME_DATE_FORMAT.format(new Date(preferences.getLong("deviceStatus_time", 0))));
            } else {
                timeStatusView.setText("unknown");
            }

            final TextView alarmReleasedView = (TextView) view.findViewById(R.id.alarmReleasedValue);
            if(preferences.contains("alarm_released")) {
                alarmReleasedView.setText(Constants.TIME_DATE_FORMAT.format(new Date(preferences.getLong("alarm_released", 0))));
            } else {
                alarmReleasedView.setText("no");
            }

//            final Button clipboardButton = (Button) view.findViewById(R.id.clipboardButton);
//            clipboardButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ClipboardManager clipboardService = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                    String content = new String();
//                    content += "Name: " + gpsInfo.getName() + "\n";
//                    content += "Alarm: " + alarmView.getText() + "\n";
//                    content += "Lng/Lat: " + latLngView.getText() + "\n";
//                    content += "Speed: " + speedView.getText() + "\n";
//                    content += "Accu: " + accuView.getText() + "\n";
//                    content += "GSM: " + gsmView.getText() + "\n";
//                    content += "Time: " + timeView.getText() + "\n";
//                    clipboardService.setPrimaryClip(ClipData.newPlainText("Status:" + gpsInfo.getName(), content));
//
//                    Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_LONG);
//                }
//            });

            return view;
        }
    }

    public SharedPreferences getSharedPreferences() {
        return getApplicationContext().getSharedPreferences("it.bambo.gka100_preferences", Context.MODE_PRIVATE);
    }
}
