package de.lmu.treeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import de.lmu.treeapp.R;


public class WantedPosterActivity extends AppCompatActivity {

    Button wantedPosterDetailsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanted_poster);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.wanted_poster_title_text);
        }

        wantedPosterDetailsButton = findViewById(R.id.wanted_poster_button);
        wantedPosterDetailsButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast qrToast = Toast.makeText(getApplicationContext(), "Details Button clicked", Toast.LENGTH_LONG );
                qrToast.show();

                Intent intent = new Intent(getApplicationContext(), WantedPosterDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
