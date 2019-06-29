package de.lmu.treeapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import de.lmu.treeapp.R;

public class WantedPosterDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanted_poster_details);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.wanted_poster_details_title_text);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
