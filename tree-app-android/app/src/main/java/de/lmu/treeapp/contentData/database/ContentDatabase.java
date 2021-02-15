package de.lmu.treeapp.contentData.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentData.database.daos.content.*;
import de.lmu.treeapp.contentData.database.entities.content.*;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;

@Database(entities = {
        GameBase.class,
        GameBaumoryCard.class,
        GameCatchFruitsItem.class,
        GameChooseAnswer.class,
        GameChooseAnswerOption.class,
        GameContourCheckpoint.class,
        GameDescriptionItem.class,
        GameDragDropItem.class,
        GameDragDropZone.class,
        GameLeafOrder.class,
        GameOrderWordsItem.class,
        Tree_x_Game.class,
        TreeImage.class,
        TreeModel.class,
        WantedPoster.class,
        WantedPosterImage.class

}, version = 1, exportSchema = false)
@TypeConverters({TypeConversion.class})
public abstract class ContentDatabase extends RoomDatabase {
    private static final String ASSET_DB_PATH = "databases";
    private static final String DB_NAME = "content";
    private static ContentDatabase INSTANCE;

    public static ContentDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ContentDatabase.class) {
                if (INSTANCE == null) { // double checked locking
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ContentDatabase.class, DB_NAME)
                            .createFromAsset(ASSET_DB_PATH + "/" + DB_NAME + ".db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract TreeDao treeDao();

    public abstract WantedPosterTextDao wantedPosterTextDao();

    public abstract WantedPosterImageDao wantedPosterImageDao();

    public abstract Tree_x_GameDao tree_x_gameDao();

    public abstract GameChooseAnswerDao gameChooseAnswerDao();

    public abstract GameBaumoryDao gameBaumoryDao();

    public abstract GameDragDropDao gameDragDropDao();

    public abstract GameLeafOrderDao gameLeafOrderDao();

    public abstract GameOnlyDescriptionDao gameOnlyDescriptionDao();

    public abstract GameDescriptionDao gameDescriptionDao();

    public abstract GameTakePictureDao gameTakePictureDao();

    public abstract GameCatchFruitsDao gameCatchFruitsDao();

    public abstract GameOrderWordsDao gameOrderWordsDao();

    public abstract GamePuzzleDao gamePuzzleDao();

    public abstract GameInputStringDao gameInputStringDao();

    public abstract GameContourDao gameContourDao();

    public abstract GameSlidePuzzleDao gameSlidePuzzleDao();
}

