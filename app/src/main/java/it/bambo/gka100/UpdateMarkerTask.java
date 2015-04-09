package it.bambo.gka100;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import it.bambo.gka100.model.GpsInfo;

/**
 * Created by andreas on 09.04.2015.
 */
public class UpdateMarkerTask extends AsyncTask<Void, GpsInfo, Void> {

    private boolean updateMarkerTaskRunning = true;

    private MainActivity mainActivity;

    public UpdateMarkerTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (updateMarkerTaskRunning) {
            SharedPreferences p = mainActivity.getSharedPreferences();
            if (p.contains("device_changed_flag")) {
                GpsInfo gpsInfo = mainActivity.getGpsInfoFromPreferences(p);
                publishProgress(gpsInfo);
                p.edit().remove("device_changed_flag").apply();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(GpsInfo... values) {
        super.onProgressUpdate(values);
        if (values.length == 1) {
            mainActivity.updateMarker(values[0]);
        }
    }

    public boolean isUpdateMarkerTaskRunning() {
        return updateMarkerTaskRunning;
    }

    public void setUpdateMarkerTaskRunning(boolean updateMarkerTaskRunning) {
        this.updateMarkerTaskRunning = updateMarkerTaskRunning;
    }
}
