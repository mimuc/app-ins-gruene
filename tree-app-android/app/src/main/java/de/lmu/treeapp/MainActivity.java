package de.lmu.treeapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton qrCodeButton = findViewById(R.id.qr_code_button);


        qrCodeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast qrToast = Toast.makeText(getApplicationContext(), "QR Code Button clicked", Toast.LENGTH_LONG );
                qrToast.show();
            }
        });
    }
}
