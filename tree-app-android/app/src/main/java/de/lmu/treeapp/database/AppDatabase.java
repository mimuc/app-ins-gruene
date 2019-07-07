package de.lmu.treeapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.lmu.treeapp.database.daos.TreeDao;
import de.lmu.treeapp.database.entities.TreeModel;
import de.lmu.treeapp.database.entities.TreeProfileModel;

@Database(entities = {TreeModel.class, TreeProfileModel.class}, version = 1)
@TypeConverters({TypeConversion.class})
public abstract class AppDatabase extends RoomDatabase {
    private  static AppDatabase INSTANCE;
    private static final Object sLock = new Object();

    public abstract TreeDao treeDao();

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

