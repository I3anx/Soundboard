package com.example.bwildd.soundboard;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mAddNewAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddNewAudio = (FloatingActionButton) findViewById(R.id.addActionBtn);

        mAddNewAudio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), crudActivity.class);
                //intent.putExtra("badiId", selected.getId());
                //intent.putExtra("badiName", selected.
                startActivity(intent);
            }
        });
    }



}
