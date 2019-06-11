package com.example.bwildd.soundboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class favoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
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
}
