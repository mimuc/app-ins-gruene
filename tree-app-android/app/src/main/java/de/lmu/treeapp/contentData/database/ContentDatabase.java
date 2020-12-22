package de.lmu.treeapp.contentData.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentData.database.daos.content.GameBaumoryDao;
import de.lmu.treeapp.contentData.database.daos.content.GameChooseAnswerDao;
import de.lmu.treeapp.contentData.database.daos.content.GameDragDropDao;
import de.lmu.treeapp.contentData.database.daos.content.GameOnlyDescriptionDao;
import de.lmu.treeapp.contentData.database.daos.content.GameTakePictureDao;
import de.lmu.treeapp.contentData.database.daos.content.TreeDao;
import de.lmu.treeapp.contentData.database.daos.content.TreeProfileDao;
import de.lmu.treeapp.contentData.database.entities.content.GameBase;
import de.lmu.treeapp.contentData.database.entities.content.GameBaumoryCard;
import de.lmu.treeapp.contentData.database.entities.content.GameChooseAnswer;
import de.lmu.treeapp.contentData.database.entities.content.GameChooseAnswerOption;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropItem;
import de.lmu.treeapp.contentData.database.entities.content.GameDragDropZone;
import de.lmu.treeapp.contentData.database.entities.content.TreeImage;
import de.lmu.treeapp.contentData.database.entities.content.TreeModel;
import de.lmu.treeapp.contentData.database.entities.content.TreeProfileCard;
import de.lmu.treeapp.contentData.database.entities.content.Tree_x_Game;

@Database(entities = {
        GameBase.class,
        GameBaumoryCard.class,
        GameChooseAnswer.class,
        GameChooseAnswerOption.class,
        GameDragDropItem.class,
        GameDragDropZone.class,
        Tree_x_Game.class,
        TreeImage.class,
        TreeModel.class,
        TreeProfileCard.class
}, version = 1, exportSchema = false)
@TypeConverters({TypeConversion.class})
public abstract class ContentDatabase extends RoomDatabase {
    private static final String ASSET_DB_PATH = "databases";
    private static final String DB_NAME = "content";
    private static final Object sLock = new Object();
    private static ContentDatabase INSTANCE;

    public static synchronized ContentDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ContentDatabase.class, DB_NAME)
                        .createFromAsset(ASSET_DB_PATH + "/" + DB_NAME + ".db")
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract TreeDao treeDao();

    public abstract TreeProfileDao treeProfileDao();

    public abstract GameChooseAnswerDao gameChooseAnswerDao();

    public abstract GameBaumoryDao gameBaumoryDao();

    public abstract GameDragDropDao gameDragDropDao();

    public abstract GameOnlyDescriptionDao gameOnlyDescriptionDao();

    public abstract GameTakePictureDao gameTakePictureDao();
}

