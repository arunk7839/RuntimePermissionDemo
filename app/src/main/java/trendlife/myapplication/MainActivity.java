package trendlife.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button check_permission =  findViewById(R.id.check_permission);
        Button request_permission =  findViewById(R.id.request_permission);
        check_permission.setOnClickListener(this);
        request_permission.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {


        int id = v.getId();
        switch (id) {
            case R.id.check_permission:
                if (checkPermission()) {

                    Toast.makeText(this, "Permission already granted.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "Please request permission.", Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.request_permission:
                if (!checkPermission()) {

                    requestPermission();

                } else {
                    Toast.makeText(this, "Permission already granted.", Toast.LENGTH_LONG).show();


                }
                break;
        }

    }

    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean AcceptedFineLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean AcceptedCamera = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (AcceptedFineLocation && AcceptedCamera)
                        Toast.makeText(this, "Permission Granted, Now you can access location data and camera.", Toast.LENGTH_LONG).show();

                    else {
                        Toast.makeText(this, "Permission Denied, You cannot access location data and camera.", Toast.LENGTH_LONG).show();


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(CAMERA)) {

                                showMessageGrantCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            } else {
                                openSettingsDialog();
                            }
                        }

                    }
                }


                break;
        }

    }


    private void showMessageGrantCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("GRANT", okListener)
                .setNegativeButton("CANCEL", null)
                .create()
                .show();
    }

    private void openSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Required Permissions");
        builder.setMessage("This app require permission to use awesome feature. Grant them in app settings.");
        builder.setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
}


