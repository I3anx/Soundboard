package com.example.bwildd.soundboard;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bwildd.soundboard.db.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mAddNewAudio;

    DatabaseHelper mDatabaseHelper;
    private Button btnAdd;
    private EditText txtAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDatabaseHelper = new DatabaseHelper(this);

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
        txtAdd = findViewById(R.id.txtTitel);
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = txtAdd.getText().toString();

                if (newEntry.length() != 0) {
                    AddData(newEntry);
                    txtAdd.setText("");
                } else {
                    toastMessage("put something in the field");
                }
            }
        });
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
}
