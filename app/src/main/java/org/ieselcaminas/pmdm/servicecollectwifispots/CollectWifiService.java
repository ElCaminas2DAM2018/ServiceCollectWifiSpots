package org.ieselcaminas.pmdm.servicecollectwifispots;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class CollectWifiService extends Service {
    private MyAsyncTasc myAsyncTask = null;

    private class MyAsyncTasc extends AsyncTask<Void, Void, Void> {

        public boolean exit;


        @Override
        protected Void doInBackground(Void... params) {

            final WifiManager wifiManager = (WifiManager) CollectWifiService.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context c, Intent intent) {
                    if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                        List<ScanResult> mScanResults = wifiManager.getScanResults();
                        saveWifiSpots(mScanResults);
                    }
                }

                private void saveWifiSpots(List<ScanResult> mScanResults) {
                    for (ScanResult scanResult : mScanResults) {
                        Log.d("Wifiscan", "MyFiwi" + scanResult.SSID);

                        ContentValues values = new ContentValues();
                        values.put("bssid", scanResult.BSSID);
                        values.put("ssid", scanResult.SSID);
                        values.put("security", scanResult.capabilities);
                        values.put("desc", "");

                        String uriStr = "content://org.ieselcaminas.victor.wifidataprovider/Wifi";
                        Uri wifiUri = Uri.parse(uriStr);
                        ContentResolver cr = getContentResolver();
                        cr.insert(wifiUri, values);
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            registerReceiver(mWifiScanReceiver, intentFilter);

            exit = false;
            while (!exit) {
                wifiManager.startScan();
                try {
                    Thread.sleep(1000 * 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public CollectWifiService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myAsyncTask = new MyAsyncTasc();
        myAsyncTask.execute();
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        myAsyncTask.exit = true;
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }
}