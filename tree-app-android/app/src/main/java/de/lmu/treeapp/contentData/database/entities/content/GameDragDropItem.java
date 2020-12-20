package de.lmu.treeapp.contentData.database.entities.content;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import de.lmu.treeapp.contentClasses.minigames.MediaType;

@Entity
public class GameDragDropItem {
    @PrimaryKey
    public int id;
    @Nullable
    public Integer gameId;
    public MediaType type = MediaType.TEXT;
    public String content;
    @Nullable
    public Integer matchId;
    public int w, h;
    @Ignore
    public float x, y;
}
