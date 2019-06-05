package com.example.bwildd.soundboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.example.bwildd.soundboard.db.DatabaseHelper;

import java.io.File;
import java.io.IOException;

import static android.app.PendingIntent.getActivity;

public class crudActivity extends MainActivity{

    private Button btnSave;
    private Button btnRecord;
    private TextView lblRecord;
    private TextView txtName;
    private MediaRecorder mRecorder;
    private CheckBox cbRecord;
    private String mFileName;
    private int favoriteAsInt;
    public static final  String LOG_TAG = "Record_log";
    DatabaseHelper mDatabaseHelper;

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mDatabaseHelper = new DatabaseHelper(this);
        btnSave = findViewById(R.id.btnSave);
        btnRecord = findViewById(R.id.btnRecord);
        lblRecord = findViewById(R.id.lblRecord);
        txtName = findViewById(R.id.txtName);
        cbRecord = findViewById(R.id.cbRecord);

        btnRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    requestAudioPermissions();
                    lblRecord.setText("Recording started...");

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    stopRecording();
                    lblRecord.setText("Recording finished...");

                }

                return false;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lblRecord.setText("Recording saved");

                String name = txtName.getText().toString();
                Boolean favorite = cbRecord.isChecked();
                String path = mFileName;

                if (favorite) {
                    favoriteAsInt = 1;
                }
                else {
                    favoriteAsInt = 0;
                }

                if (name.length() != 0) {
                    AddData(name, path, favoriteAsInt);
                    txtName.setText("");
                } else {
                    toastMessage("put something in the field");
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void startRecording() {

        // Record to the external cache directory for visibility
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/" + txtName.getText().toString() + ".3gp";

        File mFile = new File(mFileName);

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e ) {
            Log.e(LOG_TAG, "prepare() failed");
            Log.e(LOG_TAG, e.toString());
        }

        mRecorder.start();
    }

    public void stopRecording() {

        try {
            mRecorder.stop();
            mRecorder.release();
        } catch (Exception e ) {
            Log.e(LOG_TAG, e.toString());
        }

        mRecorder = null;
    }

    public void AddData(String name, String path, int favorite) {
        Boolean insertData;
        insertData = mDatabaseHelper.addData(name, path, favorite);

        if (insertData) {
            toastMessage("Data Inserted");
        } else {
            toastMessage("Something went wrong");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //Requesting run-time permissions
    //Create placeholder for user's consent to record_audio permission.
    //This will be used in handling callback
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now
            startRecording();
        }
    }

    //Handling callback
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    startRecording();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
