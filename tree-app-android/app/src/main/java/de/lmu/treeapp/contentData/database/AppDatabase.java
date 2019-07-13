package de.lmu.treeapp.contentData.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentData.database.daos.PlayerDao;
import de.lmu.treeapp.contentData.database.daos.TreeDao;
import de.lmu.treeapp.contentData.database.daos.TreeProfileDao;
import de.lmu.treeapp.contentData.database.entities.PlayerModel;
import de.lmu.treeapp.contentData.database.entities.TreeModel;
import de.lmu.treeapp.contentData.database.entities.TreeProfileModel;

@Database(entities = {TreeModel.class, TreeProfileModel.class, PlayerModel.class}, version = 1, exportSchema = false)
@TypeConverters({TypeConversion.class})
public abstract class AppDatabase extends RoomDatabase {
    private  static AppDatabase INSTANCE;
    private static final Object sLock = new Object();

    public abstract TreeDao treeDao();
    public abstract TreeProfileDao treeProfileDao();
    public abstract PlayerDao playerDao();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "AppDatabase")
                        .build();
            }
            return INSTANCE;
        }
    }
}
