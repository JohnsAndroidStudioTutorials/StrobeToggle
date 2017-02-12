package com.johnsandroidstudiotutorials.strobetoggle;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    ToggleButton strobeToggleButton;

    private CameraManager mCameraManager;
    private String mCameraId;
    private boolean hasFlash;
    private Boolean isTorchOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        strobeToggleButton = (ToggleButton) findViewById(R.id.strobe_toggle_button);
        isTorchOn = false;

        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application

            displayError();
        }

        strobeToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isTorchOn) {
                   // turnOffFlash();
                } else {
                  //  turnOnFlash();
                }
            }
        });
    }

    mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
    try {
        mCameraId = mCameraManager.getCameraIdList()[0];
    } catch (CameraAccessException e) {
        e.printStackTrace();
    }

    mTorchOnOffButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (isTorchOn) {
                    turnOffFlashLight();
                    isTorchOn = false;
                } else {
                    turnOnFlashLight();
                    isTorchOn = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
}

    private void displayError() {
        // device doesn't support flash and/or camera.
        // Show alert message and close the application
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Sorry, your device doesn't support flash light!");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

