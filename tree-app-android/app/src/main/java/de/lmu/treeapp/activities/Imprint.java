package de.lmu.treeapp.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.lmu.treeapp.R;


public class Imprint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_imprint);
        super.onCreate(savedInstanceState);

        //Button btn_forward = findViewById(R.id.game_onlyDescription_sendButton);

        TextView imprint_title = findViewById(R.id.impressum_title);
        TextView imprint_text = findViewById(R.id.impressum_text);
        TextView publisher_title = findViewById(R.id.herausgeber_title);
        TextView publisher_text = findViewById(R.id.herausgeber_text);

        // Close the last activity on the stack (the imprint) to come back to the tree overview page
        /*
        btn_forward.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
    }

    // Remove the current activity from the stack to switch to the previous one
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    // Android hardware back button is pressed
    @Override
    public void onBackPressed(){
        finish();
        super.onBackPressed();
    }
}
