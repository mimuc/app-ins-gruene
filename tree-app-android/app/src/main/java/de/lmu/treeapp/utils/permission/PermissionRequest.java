package de.lmu.treeapp.utils.permission;

import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import de.lmu.treeapp.R;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;

public class PermissionRequest implements PopupInterface {

    protected ComponentActivity activity;
    protected Popup popup;
    protected String permission;
    protected String rationale;
    protected ActivityResultCallback<Boolean> callback;
    protected ActivityResultLauncher<String> requestPermissionLauncher;

    public PermissionRequest(ComponentActivity activity, String permission, ActivityResultCallback<Boolean> callback) {
        this(activity, permission, callback, activity.getString(R.string.permission_rationale_default));
    }

    public PermissionRequest(ComponentActivity activity, String permission, ActivityResultCallback<Boolean> callback, String rationale) {
        this.permission = permission;
        this.activity = activity;
        this.rationale = rationale;
        this.callback = isGranted -> {
            // Override callback
            if (!isGranted) {
                popup.setButtonSecondary(false);
                popup.showWithButtonText(PopupType.NEGATIVE, activity.getString(R.string.popup_neutral_ok), activity.getString(R.string.permission_denied_default));
            }
            callback.onActivityResult(isGranted);
        };

        requestPermissionLauncher = activity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), this.callback);
        popup = new Popup(activity, this, 0);
        popup.setButtonSecondaryText(activity.getString(R.string.cancel));
        popup.setNeutralTitle(activity.getString(R.string.permission));
    }

    public void requestPermission() {
        int checkSelfPermission = ContextCompat.checkSelfPermission(activity, permission);
        if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            this.callback.onActivityResult(true);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.shouldShowRequestPermissionRationale(permission)) {
            popup.setButtonSecondary(true);
            popup.showWithButtonText(PopupType.NEUTRAL, activity.getString(R.string.popup_grant), rationale);
        } else {
            requestPermissionLauncher.launch(permission);
        }
    }

    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if (type == PopupType.NEUTRAL && action == PopupAction.ACCEPT) {
            requestPermissionLauncher.launch(permission);
        } else {
            // Do not call callback, which will end in an infinite loop;
        }
    }
}
