package de.lmu.treeapp.contentData.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentData.database.daos.app.GameStateScoresDao;
import de.lmu.treeapp.contentData.database.daos.app.GameStateTakePictureDao;
import de.lmu.treeapp.contentData.database.daos.app.PlayerDao;
import de.lmu.treeapp.contentData.database.daos.app.TreeDao;
import de.lmu.treeapp.contentData.database.entities.app.GameStateScore;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureImage;
import de.lmu.treeapp.contentData.database.entities.app.PlayerState;
import de.lmu.treeapp.contentData.database.entities.app.TreeProfileState;
import de.lmu.treeapp.contentData.database.entities.app.TreeState;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;

@Database(entities = {
        GameStateTakePictureImage.class,
        TreeState.class,
        TreeProfileState.class,
        PlayerState.class,
        GameStateScore.class
}, version = 1, exportSchema = false)
@TypeConverters({TypeConversion.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "app";
    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) { // double checked locking
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME + ".db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract TreeDao treeDao();

    public abstract PlayerDao playerDao();

    public abstract GameStateTakePictureDao gameTakePictureDao();

    public abstract GameStateScoresDao gameStateScoresDao();
}

