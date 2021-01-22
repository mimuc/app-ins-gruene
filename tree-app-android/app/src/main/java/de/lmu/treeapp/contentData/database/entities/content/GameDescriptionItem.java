package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class GameDescriptionItem {
    @PrimaryKey
    public int id;
    public int gameId;
    public String content;
    @Ignore
    public boolean isRight = true;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final GameDescriptionItem other = (GameDescriptionItem) obj;

        return this.id == other.id;
    }
}

