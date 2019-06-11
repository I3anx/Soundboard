package com.example.bwildd.soundboard;

import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bwildd.soundboard.db.DatabaseHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    private Button btnAddAudio;
    private ListView listView;
    private Button test;
    private ListAdapter listAdapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    private crudActivity crud;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddAudio = findViewById(R.id.btnAddAudio);
        listView = findViewById(R.id.listView);
        test = findViewById(R.id.btnTest);
        mDatabaseHelper = new DatabaseHelper(this);
        crudActivity crud = new crudActivity();


        showOverview();

        btnAddAudio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), crudActivity.class);
                startActivity(intent);
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String fileName = crudActivity.getName();
                //loadAudio(fileName);


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name  = listAdapter.getItem(position).toString();
                loadAudio(name);

            }
        });
    }

    public void showOverview() {
        Cursor data = mDatabaseHelper.getListContents();

        if (data.getCount() == 0) {
            toastMessage("DB is empty!");
        } else {
            while(data.moveToNext()) {
                arrayList.add(data.getString(1));
                listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(listAdapter);

                registerForContextMenu(listView);
            }
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add("Löschen");
        menu.add("Zu Favoriten hinzufügen");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int itemID = 0;
        int id = info.position;
        String name = arrayList.get(id);

        if(item.getTitle() == "Löschen"){


            Cursor data = mDatabaseHelper.getItemID(name);
            while(data.moveToNext()){
                itemID = data.getInt(0);
            }
            mDatabaseHelper.deleteName(itemID, name);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        } else if (item.getTitle() == "Zu Favoriten hinzufügen") {
            Cursor data = mDatabaseHelper.getItemID(name);
            while (data.moveToNext()){
                itemID = data.getInt(0);
            }
            mDatabaseHelper.updateFavorites(itemID, name);

        }

        return true;
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    MediaPlayer mPlayer;

    // Your Media Player will be called with Audio file here..
    private File loadAudio(String fileName){

        String completePath = getExternalCacheDir().getAbsolutePath() + "/" + fileName + ".3gp";

        File file = new File(completePath);

        Uri myUri1 = Uri.fromFile(file);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(getApplicationContext(), myUri1);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mPlayer.start();
        return file;
    }

}
