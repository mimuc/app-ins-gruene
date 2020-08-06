package de.lmu.treeapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.lmu.treeapp.R;


public class Imprint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_imprint);
        super.onCreate(savedInstanceState);

        TextView imprint_txt = findViewById(R.id.game_onlyDescription_sendButton);
        Button btn_forward = findViewById(R.id.game_onlyDescription_sendButton);

        // Close the last activity on the stack (the imprint) to come back to the tree overview page
        btn_forward.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
