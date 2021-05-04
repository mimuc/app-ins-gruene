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
        setTitle(R.string.menu_about_us);
        setContentView(R.layout.activity_about);
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
