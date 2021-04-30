package de.lmu.treeapp.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.lmu.treeapp.R;

public class Privacy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        TextView privacy_title = findViewById(R.id.privacy_title);
        TextView publisher_title = findViewById(R.id.publisher_title);
        TextView publisher_text = findViewById(R.id.publisher_text);
        TextView access_title = findViewById(R.id.privacy_access_title);
        TextView access_text = findViewById(R.id.privacy_access_text);
        TextView data_title = findViewById(R.id.privacy_data_title);
        TextView data_text = findViewById(R.id.privacy_data_text);
    }

    // Remove the current activity from the stack to switch to the previous one
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // Android hardware back button is pressed
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
