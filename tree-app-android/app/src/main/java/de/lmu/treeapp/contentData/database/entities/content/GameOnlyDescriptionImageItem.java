package de.lmu.treeapp.contentData.database.entities.content;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentClasses.minigames.MediaType;
import de.lmu.treeapp.contentClasses.trees.TreeComponent;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;

@Entity
public class GameOnlyDescriptionImageItem {
    @PrimaryKey
    public int id;
    @Nullable
    public Integer gameId;
    public String content;
    public int w, h;
    @Nullable
    public Integer amount;
    @Nullable
    public Integer treeId;
    @Ignore
    public float x, y;

    public GameOnlyDescriptionImageItem() {

    }

    public GameOnlyDescriptionImageItem(GameOnlyDescriptionImageItem item) {
        this.id = item.id;
        this.gameId = item.gameId;
        this.content = item.content;
        this.w = item.w;
        this.h = item.h;
        this.amount = item.amount;
        this.treeId = item.treeId;
        this.x = item.x;
        this.y = item.y;
    }

    public void setId(int iD) {
        this.id = iD;
    }
}