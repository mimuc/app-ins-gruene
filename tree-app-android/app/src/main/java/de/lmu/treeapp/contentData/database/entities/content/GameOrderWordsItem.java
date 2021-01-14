package de.lmu.treeapp.contentData.database.entities.content;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GameOrderWordsItem {
    @PrimaryKey
    public int id;
    @Nullable
    public Integer gameId;
    public String content;
}

