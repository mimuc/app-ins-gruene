package de.lmu.treeapp.activities.minigames.takePicture;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.AppDatabase;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureImage;
import de.lmu.treeapp.contentData.database.entities.content.GameTakePictureRelations;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class GameActivity_TakePicture extends GameActivity_Base {

    private GameTakePictureRelations takePictureGame;
    //private List<GameStateTakePictureImage> imageStates;
    private Button sendButton;
    private ImageView previewPicture;
    private ImageButton imageExample;
    // PopUp:
    Dialog popupWindow;
    Button btnAccept, btnWiki;
    TextView popupTitle, popupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        popupWindow = new Dialog(this);
        takePictureGame = (GameTakePictureRelations) gameContent;
        sendButton = findViewById(R.id.game_takePicture_sendButton);
        previewPicture = findViewById(R.id.game_takePicture_previewPicture);
        imageExample = findViewById(R.id.game_takePicture_imageExample);

        sendButton.setEnabled(false);

        previewPicture.setOnClickListener(view -> dispatchTakePictureIntent());

        imageExample.setOnClickListener(view -> {
            imageExample.setVisibility(View.GONE);
            dispatchTakePictureIntent();
            sendButton.setEnabled(true);
        });

        sendButton.setOnClickListener(view -> showPositivePopup());

        // Load state
        //AppDatabase.getInstance(getApplicationContext()).gameTakePictureDao().getImages(gameId, treeId, parentCategory).subscribeOn(Schedulers.io()).subscribe(s -> imageStates = s);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__take_picture;
    }

    String currentPhotoPath;
    Uri photoURI;

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getApplicationContext(), "Fehler", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "de.lmu.treeapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
        sendButton.setAlpha(1);
    }

    private File createImageFile() throws IOException {
        String imageFileName = "AppInsGruene_"
                + takePictureGame.GetPictureName()
                + "_g" + gameId
                + "_t" + treeId
                + "_" + parentCategory.name()
                + "_" + (System.currentTimeMillis() / 1000);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir + File.separator + imageFileName + ".jpg");
        if (!image.exists()) {
            boolean success = image.createNewFile();
            currentPhotoPath = success ? image.getAbsolutePath() : null;
        }
        return image;
    }

    public static int getOrientation(Context context, Uri uri) {
        int rotate = 0;
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            FileInputStream input = new FileInputStream(fileDescriptor);
            File tempFile = File.createTempFile("exif", "tmp");
            String tempFilename = tempFile.getPath();
            FileOutputStream output = new FileOutputStream(tempFile.getPath());
            int read;
            byte[] bytes = new byte[4096];
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }
            input.close();
            output.close();
            ExifInterface exif = new ExifInterface(tempFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return rotate;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // TODO: Rotate now. Portrait gives 90, Landscape gives 0.
            previewPicture.setImageURI(photoURI);
            previewPicture.setVisibility(View.VISIBLE);
        }
    }

    private void showPositivePopup() {
        popupWindow.setContentView(R.layout.popup_neutral);
        btnAccept = popupWindow.findViewById(R.id.btn_forward);
        btnWiki = popupWindow.findViewById(R.id.btn_wiki);
        popupTitle = popupWindow.findViewById(R.id.popup_title);
        popupText = popupWindow.findViewById(R.id.popup_text);

        btnAccept.setVisibility(View.VISIBLE);
        btnWiki.setVisibility(View.VISIBLE);
        ViewCompat.animate(popupText).setStartDelay(150).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();
        ViewCompat.animate(btnAccept).setStartDelay(300).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();
        ViewCompat.animate(btnWiki).setStartDelay(300).alpha(1).setDuration(300).setInterpolator(new DecelerateInterpolator(1.2f)).start();

        //close the popup and open the tree profile
        btnWiki.setOnClickListener(view -> {
            popupWindow.dismiss();
            // Set success without going back to overview
            DataManager.getInstance(getApplicationContext()).setGameCompleted(parentCategory, gameContent.getId(), parentTree);
            saveGameState().subscribe();
            showTreeProfile();
        });

        //close the popup and finish the game
        btnAccept.setOnClickListener(view -> {
            popupWindow.dismiss();
            saveGameState().subscribe();
            onSuccess();
        });

        popupWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.show();

        Window window = popupWindow.getWindow();
        window.setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
    }

    /**
     * Write game state in background.
     */
    protected Completable saveGameState() {
        if (currentPhotoPath != null) {
            GameStateTakePictureImage gameStateTakePictureImage = new GameStateTakePictureImage(gameId, treeId, parentCategory, currentPhotoPath, new Date());
            return AppDatabase.getInstance(getApplicationContext()).gameTakePictureDao()
                    .insertImage(gameStateTakePictureImage)
                    .subscribeOn(Schedulers.io()).flatMapCompletable(s -> Completable.fromAction(() -> {
                        gameStateTakePictureImage.id = s.intValue(); // Update id afterwards
                        parentTree.appData.takePictureImages.add(gameStateTakePictureImage);
                    }));
        }
        return Completable.complete();
    }
}
