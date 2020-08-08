package de.lmu.treeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.lmu.treeapp.R;

import de.lmu.treeapp.activities.minigames.chooseAnswer.GameActivity_ChooseAnswer;
import de.lmu.treeapp.adapter.OverviewRecyclerViewAdapter;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.fragments.OverviewFragment;
import de.lmu.treeapp.fragments.TreeSelectionFragment;
import de.lmu.treeapp.service.FragmentManagerService;

/**
 * The MainActivity consists of the two fragments (overview and detail) and the BottomNavigation-Bar.
 * It acts as the manager of everything else, but does not include a lot of functionality itself, except the basic set-ups needed for everything to work (e.g. Start loading the data from the database, etc.)
 */
public class MainActivity extends AppCompatActivity {

    private DataManager dm; // The Singleton holding all the data of the CMS and Database
    private FragmentManagerService fragmentManager = FragmentManagerService.getInstance(getSupportFragmentManager()); // The FragmentManager we need to register and launch fragments
    private final Fragment treeSelectionFragment = new TreeSelectionFragment(); // The Trees-Detail-Fragment (The one were we can see the trees in big and click on games or the profile)
    private final Fragment overviewFragment = new OverviewFragment(fragmentManager, treeSelectionFragment); // The Trees-Overview-Fragment (The one which all the trees in small squares on one screen)
    private BottomNavigationView bottomNavigationView;  // The NavigationBar on the bottom
    public static Context mainContext;

    // QR-Code Stuff (Disabled)
    private FloatingActionButton qrCodeButton;  // The Button to start the QR-Code/Camera-Functionality
    private final int BARCODE_READER_REQUEST_CODE = 1;  // The Code we expect the Barcode-reader to return on success.

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
        mainContext = getApplicationContext();
    }

    /**
     * Set the Status-Bar to the Light-Version.
     */
    private void setStatusBarTextColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * Hide the Action-Bar
     */
    private void hideActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    /**
     * Find the Views we need inside the MainActivity.
     */
    private void findViewsById() {
        /*  DISABLED: QR
        this.qrCodeButton = this.findViewById(R.id.qr_code_button);
         */
        this.bottomNavigationView = this.findViewById(R.id.bottom_navigation);
    }

    /**
     * Hook up the functions to the buttons.
     */
    private void setOnClickListener() {
        /* DISABLED: QR
        this.qrCodeButton.setOnClickListener(this.getQrCodeButtonOnClickListener());
        */
        this.bottomNavigationView.setOnNavigationItemSelectedListener(fragmentManager.getOnNavigationItemSelectedListener(overviewFragment, treeSelectionFragment));
    }

    /**
     * Register the Fragments with the custom FragmentManager.
     */
    Fragment[] bottomNavigationFragments;
    private void registerFragmentManagerTransactions() {
        bottomNavigationFragments = new Fragment[]{ this.overviewFragment, this.treeSelectionFragment };
        fragmentManager.registerTransactions(bottomNavigationFragments);
    }

    /**
     * Get the instance of the DataManager-Singleton and wait for everything of the CMS and Database to be loaded.
     * TODO: This can be optimized by handling it asynchronously instead of waiting for it. Load times are fine, though.
     */
    private void getContentFromDataManager() {
        dm = DataManager.getInstance(getApplicationContext());
        while (!dm.loaded) {
        } //Wait for everything to be loaded.
    }

    /**
     * OnClickListener that starts the QR-Functionality Activity.
     */
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

    /** Override of OnActivityResult that handles the return of the QR-Activity.
     *
     * @param requestCode   RequestCode coming from the QR-Activity.
     * @param resultCode    ResultCode coming from the QR-Activity.
     * @param data          Data coming from the QR-Activity (containing the name of the found tree).
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != BARCODE_READER_REQUEST_CODE) {   // If this is not called by the return of the QR-Activity, just do the usual.
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (resultCode != CommonStatusCodes.SUCCESS) { // If the QR-Activity was not successful.
            Log.e("MainActivity", String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
            return;
        }
        if (data == null) { // If no barcode was captured.
            this.showToast(getString(R.string.no_barcode_captured));
            return;
        }
        Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
        Tree tree = dm.GetTreeByQR(barcode.displayValue);
        if (tree != null) {     // If the barcode captured does correspond with a tree.
            dm.UnlockTree(tree);
            this.showToast(tree.name);
        } else                  // If the barcode captured does not correspond with any tree.
            this.showToast(String.format("%s%s", getString(R.string.main_activity_qr_code_no_tree_found_text), barcode.displayValue));
    }

    /**
     * Helper function to show a Toast from any thread.
     * @param toastText String containing the text to toast.
     */
    private void showToast(final String toastText) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
            }
        });
    }

    // Android hardware back button is pressed
    @Override
    public void onBackPressed() {
        fragmentManager.showOverview(bottomNavigationFragments);
    }

    public void showImprint(){
        Intent intent = new Intent(getApplicationContext(), Imprint.class);
        //finish();
        startActivity(intent);
    }


}
