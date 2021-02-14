package de.lmu.treeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentData.DataManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_splash_screen);

        ImageView image_appinsgruene = findViewById(R.id.image_appinsgruene);
        ImageView image_logos = findViewById(R.id.image_logos);
        TextView splash_screen_text = findViewById(R.id.splash_screen_text);

        // Load all data on splash screen
        DataManager.getInstanceAsync(getApplicationContext()).subscribe(dataManager -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
