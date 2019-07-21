package de.lmu.treeapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.fragments.OverviewFragment;
import de.lmu.treeapp.fragments.TreeSelectionFragment;
import de.lmu.treeapp.service.FragmentManagerService;


public class MainActivity extends AppCompatActivity {

    private final int BARCODE_READER_REQUEST_CODE = 1;

    private DataManager dm;

    private FragmentManagerService fragmentManager = FragmentManagerService.getInstance(getSupportFragmentManager());
    private final Fragment treeSelectionFragment = new TreeSelectionFragment();
    private final Fragment overviewFragment = new OverviewFragment(fragmentManager, treeSelectionFragment);
    private FloatingActionButton qrCodeButton;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setStatusBarTextColor();
        this.hideActionBar();
        this.findViewsById();
        this.setOnClickListener();
        this.getContentFromDataManager();
        this.registerFragmentManagerTransactions();
    }

    private void setStatusBarTextColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void hideActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void findViewsById() {
        this.qrCodeButton = this.findViewById(R.id.qr_code_button);
        this.bottomNavigationView = this.findViewById(R.id.bottom_navigation);
    }

    private void setOnClickListener() {
        this.qrCodeButton.setOnClickListener(this.getQrCodeButtonOnClickListener());
        this.bottomNavigationView.setOnNavigationItemSelectedListener(fragmentManager.getOnNavigationItemSelectedListener(overviewFragment, treeSelectionFragment));
    }

    private void registerFragmentManagerTransactions() {
        Fragment[] bottomNavigationFragments = new Fragment[]{ this.overviewFragment, this.treeSelectionFragment };
        fragmentManager.registerTransactions(bottomNavigationFragments);
    }

    private Button.OnClickListener getQrCodeButtonOnClickListener() {
        return new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("QR Code Button clicked");
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        };
    }

    private void getContentFromDataManager() {
        dm = DataManager.getInstance(getApplicationContext());
        while (!dm.loaded) {
        } //Wait for everything to be loaded --> A Future/Promise/Callback may be better in the future
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
            this.showToast(getString(R.string.no_barcode_captured));
            return;
        }

        Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
        Tree tree = dm.GetTreeByQR(barcode.displayValue);
        if (tree != null) {
            dm.UnlockTree(tree);
            this.showToast(tree.name);
        } else
            this.showToast(String.format("%s%s", getString(R.string.main_activity_qr_code_no_tree_found_text), barcode.displayValue));
    }

    // Helper-Function -> Show a Toast from any Thread
    private void showToast(final String toastText) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
            }
        });

    }
}
