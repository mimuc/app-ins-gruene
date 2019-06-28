package de.lmu.treeapp.activities;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.lmu.treeapp.R;


public class MainActivity extends AppCompatActivity {

    private final int BARCODE_READER_REQUEST_CODE = 1;
    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton qrCodeButton = findViewById(R.id.qr_code_button);
        welcomeTextView = findViewById(R.id.textView);

        qrCodeButton.setOnClickListener(getQrCodeButtonOnClickListener());
    }

    private Button.OnClickListener getQrCodeButtonOnClickListener() {
        return new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast qrToast = Toast.makeText(getApplicationContext(), "QR Code Button clicked", Toast.LENGTH_LONG );
                qrToast.show();

                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != BARCODE_READER_REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (resultCode != CommonStatusCodes.SUCCESS) {
            Log.e("MainActivity", String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
            return;
        }
        if (data == null) {
            welcomeTextView.setText(R.string.no_barcode_captured);
            return;
        }

        Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
        welcomeTextView.setText(barcode.displayValue);
    }
}
