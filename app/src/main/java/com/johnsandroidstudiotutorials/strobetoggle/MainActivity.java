package com.johnsandroidstudiotutorials.strobetoggle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private String cameraId;
    private boolean isTorchActivated;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleButton strobeToggleButton = (ToggleButton) findViewById(R.id.strobe_toggle_button);

        boolean isFlashAvailable = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {

            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error !!");
            alert.setMessage("Your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                    System.exit(0);
                }
            });
            alert.show();
            return;
        }

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        strobeToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isTorchActivated) {
                        turnOffFlashLight();
                        isTorchActivated = false;
                    } else {
                        strobe();
                        isTorchActivated = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void turnOnFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void turnOffFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void strobe() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
            }
        };

        new Thread(new Runnable() {
            public void run() {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        while (isTorchActivated) {

                            try {
                                cameraManager.setTorchMode(cameraId, true);
                                Thread.sleep(10);
                                cameraManager.setTorchMode(cameraId, false);


                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        turnOffFlashLight();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isTorchActivated) {
            turnOffFlashLight();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isTorchActivated) {
            turnOffFlashLight();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTorchActivated) {
            turnOnFlashLight();
        }
    }
}
