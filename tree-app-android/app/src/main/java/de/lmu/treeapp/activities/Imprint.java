package de.lmu.treeapp.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.lmu.treeapp.R;


public class Imprint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprint);

        TextView imprint_title = findViewById(R.id.imprint_title);
        TextView imprint_text = findViewById(R.id.imprint_text);
        TextView publisher_title = findViewById(R.id.publisher_title);
        TextView publisher_text = findViewById(R.id.publisher_text);
        TextView tec_implementation_title = findViewById(R.id.tec_implementation_title);
        TextView tec_implementation_text = findViewById(R.id.tec_implementation_text);
        TextView disclaimer_title = findViewById(R.id.disclaimer_title);
        TextView disclaimer_text = findViewById(R.id.disclaimer_text);
        TextView copyright_title = findViewById(R.id.copyright_title);
        TextView copyright_text = findViewById(R.id.copyright_text);
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
