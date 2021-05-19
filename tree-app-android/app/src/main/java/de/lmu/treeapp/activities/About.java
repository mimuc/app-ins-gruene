package de.lmu.treeapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import de.lmu.treeapp.R;

public class About extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.menu_about_us);
        setContentView(R.layout.activity_about);

        LinearLayout layout = findViewById(R.id.open_source_wrapper);
        layout.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.open_source_wrapper) {
            Uri uri = Uri.parse(getString(R.string.open_source_link));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
