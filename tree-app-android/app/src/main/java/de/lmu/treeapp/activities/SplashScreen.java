package de.lmu.treeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import de.lmu.treeapp.R;
import de.lmu.treeapp.contentData.DataManager;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.exceptions.UndeliverableException;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

import java.util.concurrent.TimeUnit;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_splash_screen);

        /*
        ImageView image_appinsgruene = findViewById(R.id.image_appinsgruene);
        ImageView image_logos = findViewById(R.id.image_logos);
        TextView splash_screen_text = findViewById(R.id.splash_screen_text);
        */

        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                e = e.getCause();
            }
            if (e instanceof InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                Log.w("JavaRX", e);
                return;
            }
            throw e;
        });

        // Load all data on splash screen
        Single.zip(
                // Set minimum delay, but handle DataManager simultaneously
                Single.timer(2, TimeUnit.SECONDS),
                DataManager.getInstanceAsync(getApplicationContext()),
                (time, dataManager) -> dataManager
        ).subscribe(dataManager -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
