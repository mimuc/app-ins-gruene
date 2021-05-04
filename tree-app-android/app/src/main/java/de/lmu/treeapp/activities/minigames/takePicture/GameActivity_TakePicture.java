package de.lmu.treeapp.activities.minigames.takePicture;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.activities.minigames.takePicture.adapter.TakePictureRecyclerViewAdapter;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.AppDatabase;
import de.lmu.treeapp.contentData.database.daos.app.GameStateTakePictureDao;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePicture;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureImage;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureRelations;
import de.lmu.treeapp.contentData.database.entities.content.GameTakePictureRelations;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;
import de.lmu.treeapp.utils.permission.PermissionRequest;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.util.Date;


public class GameActivity_TakePicture extends GameActivity_Base implements PopupInterface, View.OnClickListener {

    protected static final int REQUEST_TAKE_PHOTO = 1;
    protected String currentPhotoPath;
    protected Popup popup;
    protected TakePictureRecyclerViewAdapter takePictureRecyclerViewAdapter;
    private GameTakePictureRelations takePictureGame;
    private Button sendButton;
    private ImageView previewPicture;
    private ImageButton imageExample;
    private RecyclerView viewPager;
    private GameStateTakePictureRelations gameStateTakePictureRelations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popup = new Popup(this, treeId);
        popup.setButtonSecondary(true);
        popup.setButtonAcceptText(getString(R.string.popup_btn_finished));
        popup.setButtonSecondaryText(getString(R.string.popup_btn_wiki));

        PermissionRequest takePicturePermissionRequest = new PermissionRequest(this, Manifest.permission.CAMERA, isGranted -> {
            if (isGranted) {
                dispatchTakePictureIntent();
            }
        }, getString(R.string.permission_rationale_camera));

        takePictureGame = (GameTakePictureRelations) gameContent;
        sendButton = findViewById(R.id.game_takePicture_sendButton);
        previewPicture = findViewById(R.id.game_takePicture_previewPicture);
        imageExample = findViewById(R.id.game_takePicture_imageExample);
        viewPager = findViewById(R.id.image_view_pager);
        viewPager.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        sendButton.setEnabled(false);
        previewPicture.setOnClickListener(view ->
                takePicturePermissionRequest.requestPermission());

        sendButton.setOnClickListener(view -> popup.show(PopupType.NEUTRAL));

        imageExample.setOnClickListener(view -> {
            takePicturePermissionRequest.requestPermission();
        });

        getGameState().subscribe();
    }

    protected void setPreviewPicture() {
        if (currentPhotoPath != null) {
            sendButton.setEnabled(true);
            sendButton.setAlpha(1);
            File file = new File(currentPhotoPath);
            Uri photoURI = Uri.fromFile(file);
            Glide.with(this).load(photoURI).into(previewPicture);
            imageExample.setVisibility(View.GONE);
            previewPicture.setVisibility(View.VISIBLE);
        } else {
            imageExample.setVisibility(View.VISIBLE);
            previewPicture.setVisibility(View.GONE);
        }
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
                Uri photoURI = FileProvider.getUriForFile(this, "de.lmu.treeapp.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = "AppInsGruene_"
                + takePictureGame.getPictureName()
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPreviewPicture();
            if (currentPhotoPath != null) {
                GameStateTakePictureImage gameStateTakePictureImage = new GameStateTakePictureImage(gameStateTakePictureRelations.getId(), currentPhotoPath, new Date(), specialGameName);
                AppDatabase.getInstance(getApplicationContext()).gameStateTakePictureImageDao().insertOne(gameStateTakePictureImage).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .flatMapCompletable(s -> {
                            GameStateTakePicture gsTakePicture = gameStateTakePictureRelations.gameState;
                            gameStateTakePictureImage.id = s.intValue();
                            takePictureRecyclerViewAdapter.add(gameStateTakePictureImage);
                            gsTakePicture.selectedImageId = s.intValue();
                            // Don't save yet the selected image as the user may not want it as default
                            return Completable.complete();
                        }).subscribe();
            }
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
            if (getIntent().getExtras().getInt("GameId") == 303) {
                showTreeProfileCrafting(true);
                return;
            }
            showTreeProfile(true);
        }
    }

    @Override
    protected Completable getGameState() {
        return DataManager.getInstance(getApplicationContext()).getOrCreateGameStateSingle(treeId, gameId, parentCategory, GameStateTakePictureDao.class).flatMapCompletable(s -> {
            gameStateTakePictureRelations = s;
            takePictureRecyclerViewAdapter = new TakePictureRecyclerViewAdapter(gameStateTakePictureRelations, this);
            viewPager.setAdapter(takePictureRecyclerViewAdapter);
            GameStateTakePictureImage selectedImage = gameStateTakePictureRelations.getSelectedImage();
            if (selectedImage != null) {
                currentPhotoPath = selectedImage.imagePath;
                setPreviewPicture();
            }
            return Completable.complete();
        });
    }

    @Override
    protected Completable saveGameState() {
        return AppDatabase.getInstance(getApplicationContext()).gameStateDao(GameStateTakePictureDao.class).update(gameStateTakePictureRelations.gameState).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.image || v.getId() == R.id.badge) {
            // Set preview image on click on scroll view image
            GameStateTakePictureImage selectedImage = gameStateTakePictureRelations.getSelectedImage();
            if (selectedImage != null) {
                currentPhotoPath = selectedImage.imagePath;
            } else {
                currentPhotoPath = null;
            }
            setPreviewPicture();
        }
    }
}
