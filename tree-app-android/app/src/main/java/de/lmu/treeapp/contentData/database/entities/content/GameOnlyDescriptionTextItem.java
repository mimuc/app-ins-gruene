package de.lmu.treeapp.contentData.database.entities.content;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GameOnlyDescriptionTextItem {
    @PrimaryKey
    public int id;
    @Nullable
    public Integer gameId;
    public String content;

    public GameOnlyDescriptionTextItem() {
    }

    public GameOnlyDescriptionTextItem(GameOnlyDescriptionTextItem item) {
        this.id = item.id;
        this.gameId = item.gameId;
        this.content = item.content;
    }

    public void setId(int iD) {
        this.id = iD;
    }

}

