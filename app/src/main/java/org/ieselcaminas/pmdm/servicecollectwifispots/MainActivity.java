package org.ieselcaminas.pmdm.servicecollectwifispots;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_SCAN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startWifiService(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_SCAN);
            Toast.makeText(this, "Asked permision", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, CollectWifiService.class);
            startService(intent);
        }

    }

    public void stopService(View view) {
        Intent intent = new Intent(this, CollectWifiService.class);
        stopService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SCAN: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED
                        ) {
                    Intent intent = new Intent(this, CollectWifiService.class);
                    startService(intent);
                } else {
                    // permission denied
                    Dialog d = new AlertDialog.Builder(MainActivity.this).
                            setTitle("Error").
                            setMessage("I need permission to scan").
                            create();
                    d.show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
