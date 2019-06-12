package com.example.bwildd.soundboard;

import android.content.Intent;
import android.database.Cursor;
import android.support.constraint.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bwildd.soundboard.db.DatabaseHelper;

import java.util.ArrayList;

public class favoriteActivity extends AppCompatActivity {

    private DatabaseHelper mDatabaseHelper;
    private ListAdapter listAdapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ListView lvFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        mDatabaseHelper = new DatabaseHelper(this);
        lvFavorites = findViewById(R.id.lvFavorites);
        showOverview();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);

        MenuItem itemSwitch = menu.findItem(R.id.myFavoriteSwitch);
        MenuItem label = menu.findItem(R.id.myFavoriteLabel);
        final MenuItem item1 = itemSwitch.setActionView(R.layout.use_switch);
        final MenuItem item2 = label.setActionView(R.layout.use_label);
        final Switch sw = (Switch) menu.findItem(R.id.myFavoriteSwitch).getActionView().findViewById(R.id.switchFavorite);
        final TextView tv = (TextView) menu.findItem(R.id.myFavoriteLabel).getActionView().findViewById(R.id.lblFavorite);
        sw.setChecked(true);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked != true) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        return true;
    }

    public void showOverview() {
        Cursor data = mDatabaseHelper.getFavoriteSounds();

        if (data.getCount() == 0) {
            ConstraintLayout layout;
            layout = findViewById(R.id.constraintLayout);
            TextView textView = new TextView(this);
            textView.setText("Sie haben noch keine Sounds erfasst! Klicken Sie auf das Plus-Symbol in der unteren rechten Ecke.");
            textView.setTextSize(18);
            textView.setPadding(150, 100, 0,0);

            layout.addView(textView);

        } else {
            while (data.moveToNext()) {
                arrayList.add(data.getString(1));
                listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
                lvFavorites.setAdapter(listAdapter);
                registerForContextMenu(lvFavorites);
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Aus Favoriten löschen");
        menu.add("Sound löschen");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();


        int itemID = 0;
        int id = info.position;
        String name = arrayList.get(id);

        if (item.getTitle() == "Sound löschen") {
            Cursor data = mDatabaseHelper.getItemID(name);

            while (data.moveToNext()) {
                itemID = data.getInt(0);
            }

            mDatabaseHelper.deleteFromFavorites(itemID, name);
            Intent intent = new Intent(getApplicationContext(), favoriteActivity.class);
            startActivity(intent);
        } else if (item.getTitle() == "Aus Favoriten löschen"){

            Cursor data = mDatabaseHelper.getItemID(name);
            while (data.moveToNext()){
                itemID = data.getInt(0);
            }
            mDatabaseHelper.removeFavorites(itemID, name);
            Intent intent = new Intent(getApplicationContext(), favoriteActivity.class);
            startActivity(intent);
        }
        return true;
    }

    private void toastMessage (String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
