package de.lmu.treeapp.contentData.database.entities.content;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class GameDragDropZone {
    @PrimaryKey
    public int id;
    @Nullable
    public Integer gameId;
    public String content;
    @Nullable
    public Integer matchId;
    public float x, y;
    public int w, h;

    @Ignore
    public Boolean validMatch = false;

    @Ignore
    public GameDragDropItem currentItem = null;

    public void setValidMatch(Boolean validMatch) {
        this.validMatch = validMatch;
    }

    public boolean isFilled() {
        return currentItem != null;
    }
}
