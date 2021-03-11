package de.lmu.treeapp.activities.minigames.takePicture;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.daos.app.GameStateTakePictureDao;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureImage;
import de.lmu.treeapp.contentData.database.entities.content.GameTakePictureRelations;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;
import io.reactivex.rxjava3.core.Completable;


public class GameActivity_TakePicture extends GameActivity_Base implements PopupInterface {

    protected static final int REQUEST_TAKE_PHOTO = 1;
    protected String currentPhotoPath;
    protected Popup popup;
    protected Uri photoURI;
    //protected List<GameStateTakePictureImage> imageStates;
    private GameTakePictureRelations takePictureGame;
    private Button sendButton;
    private ImageView previewPicture;
    private ImageButton imageExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        popup = new Popup(this, treeId);
        popup.setButtonSecondary(true);
        popup.setButtonAcceptText(getString(R.string.popup_btn_finished));
        popup.setButtonSecondaryText(getString(R.string.popup_btn_wiki));

        takePictureGame = (GameTakePictureRelations) gameContent;
        sendButton = findViewById(R.id.game_takePicture_sendButton);
        previewPicture = findViewById(R.id.game_takePicture_previewPicture);
        imageExample = findViewById(R.id.game_takePicture_imageExample);
        sendButton.setEnabled(false);
        previewPicture.setOnClickListener(view -> dispatchTakePictureIntent());

        sendButton.setOnClickListener(view -> popup.show(PopupType.NEUTRAL));

        imageExample.setOnClickListener(view -> {
            imageExample.setVisibility(View.GONE);
            dispatchTakePictureIntent();
            sendButton.setEnabled(true);
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__take_picture;
    }

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

    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if (action == PopupAction.ACCEPT) {
            saveGameState().subscribe();
            onSuccess();
        } else {
            // Set success without going back to overview
            DataManager.getInstance(getApplicationContext()).setGameCompleted(parentCategory, gameContent.getId(), parentTree);
            saveGameState().subscribe();
            if(getIntent().getExtras().getInt("GameId") == 303){
                showTreeProfileCrafting(true);
                return;
            }
            showTreeProfile(true);
        }
    }


    @Override
    protected Completable saveGameState() {
        if (currentPhotoPath != null) {
            GameStateTakePictureImage gameStateTakePictureImage = new GameStateTakePictureImage(treeId, gameId, parentCategory, currentPhotoPath, new Date(), specialGameName);
            return DataManager.getInstance(getApplicationContext()).insertGameState(gameStateTakePictureImage, GameStateTakePictureDao.class).flatMapCompletable(s -> Completable.fromAction(() -> {
                parentTree.appData.takePictureImages.add(gameStateTakePictureImage);
            }));
        }
        return Completable.complete();
    }
}
