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
    public Integer angle;
    @Ignore
    public float x, y;

    public GameDragDropItem() {

    }

    public GameDragDropItem(GameDragDropItem item) {
        this.id = item.id;
        this.gameId = item.gameId;
        this.type = item.type;
        this.content = item.content;
        this.matchId = item.matchId;
        this.w = item.w;
        this.h = item.h;
        this.angle = item.angle;
        this.x = item.x;
        this.y = item.y;
    }

    public void setId(int iD) {
        this.id = iD;
    }
}
