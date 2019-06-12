package com.example.bwildd.soundboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.example.bwildd.soundboard.db.DatabaseHelper;


import java.io.IOException;

public class addActivity extends AppCompatActivity {

    private Button btnSave;
    private Button btnRecord;
    private TextView lblRecord;
    private TextView txtName;
    private MediaRecorder mRecorder;
    private CheckBox cbRecord;
    private String mFileName;
    private int favoriteAsInt;
    private String name;
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



        requestAudioPermissions();

        btnRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    startRecording();
                    lblRecord.setText("Aufnahme gestartet");
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    stopRecording();
                    lblRecord.setText("Aufnahme beendet");
                }
                return false;
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lblRecord.setText("Aufnahme gespeichert");

                name = txtName.getText().toString();
                Boolean favorite = cbRecord.isChecked();


                if (favorite) {
                    favoriteAsInt = 1;
                }
                else {
                    favoriteAsInt = 0;
                }

                if (name.length() == 0) {
                    toastMessage("Name darf nicht leer sein!");
                } else if (mFileName == null){
                    toastMessage("Es muss eine Aufnahme gemacht werden!");
                } else {
                    AddData(name, mFileName, favoriteAsInt);
                    txtName.setText("");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public String getName() {
        return name;
    }

    public void startRecording() {

        // Record to the external cache directory for visibility
        mFileName = getDir("sounds", MODE_PRIVATE).getAbsolutePath();
        mFileName += "/" + txtName.getText().toString() + ".3gp";

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
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
                btnRecord.setEnabled(false);
                lblRecord.setText("Sie können keine Aufnahme tätigen, da Sie die Berechtigungen abgelehnt haben!");

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

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
            btnRecord.setEnabled(true);
        }
    }

    //Handling callback
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    //recordAudio();
                    Intent intent = new Intent(getApplicationContext(), addActivity.class);
                    startActivity(intent);
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
