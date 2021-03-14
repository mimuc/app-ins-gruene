package de.lmu.treeapp.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import de.lmu.treeapp.R;


public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView about_title = findViewById(R.id.about_title);
        TextView about_subtitle_creation = findViewById(R.id.about_subtitle_creation);
        TextView about_text = findViewById(R.id.about_text);
        TextView about_subtitle_intention = findViewById(R.id.about_subtitle_intention);
        TextView about_intention_text = findViewById(R.id.about_intention_text);
        TextView about_subtitle_logos = findViewById(R.id.about_subtitle_logos);
        ImageView about_logos = findViewById(R.id.about_logos);
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
