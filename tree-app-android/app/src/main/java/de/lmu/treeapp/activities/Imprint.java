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

        TextView imprint_title = findViewById(R.id.imprint_title);
        TextView imprint_text = findViewById(R.id.imprint_text);
        TextView publisher_title = findViewById(R.id.publisher_title);
        TextView publisher_text = findViewById(R.id.publisher_text);
        TextView authority_title = findViewById(R.id.authority_title);
        TextView authority_text = findViewById(R.id.authority_text);
        TextView tax_id_title = findViewById(R.id.tax_id_title);
        TextView tax_id_text = findViewById(R.id.tax_id_text);
        TextView tec_implementation_title = findViewById(R.id.tec_implementation_title);
        TextView tec_implementation_text = findViewById(R.id.tec_implementation_text);
        TextView text_title = findViewById(R.id.text_title);
        TextView text_text = findViewById(R.id.text_text);
        TextView graphics_title = findViewById(R.id.graphics_title);
        TextView graphics_text = findViewById(R.id.graphics_text);
        TextView disclaimer_title = findViewById(R.id.disclaimer_title);
        TextView disclaimer_text = findViewById(R.id.disclaimer_text);

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
