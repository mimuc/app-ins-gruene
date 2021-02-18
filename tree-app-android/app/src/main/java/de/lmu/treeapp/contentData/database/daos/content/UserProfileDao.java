package de.lmu.treeapp.contentData.database.daos.content;

import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.contentClasses.UserProfileCategory;
import de.lmu.treeapp.contentData.database.entities.content.UserProfileOption;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserProfileDao {

    @Transaction
    @Query("SELECT * FROM UserProfileOption")
    Single<List<UserProfileOption>> getAll();

    @Transaction
    @Query("SELECT * FROM UserProfileOption WHERE id = :id LIMIT 1")
    Single<UserProfileOption> getById(int id);


    @Ignore
    static UserProfileOption getByPositionAndCategory(int position, UserProfileCategory category, List<UserProfileOption> options) {
        UserProfileOption fallbackOption = null;
        for (UserProfileOption option : options) {
            if (option.category == category) {
                if (option.position == position)
                    return option;
                fallbackOption = option;
            }
        }
        if (fallbackOption != null) {
            return fallbackOption;
        }
        if (options.size() > 0) {
            return options.get(0);
        }
        return null;
    }

    @Ignore
    static List<UserProfileOption> getByCategory(UserProfileCategory category, List<UserProfileOption> options) {
        List<UserProfileOption> filtered = new ArrayList<>();
        for (UserProfileOption option : options) {
            if (option.category == category)
                filtered.add(option);
        }
        return filtered;
    }
}
