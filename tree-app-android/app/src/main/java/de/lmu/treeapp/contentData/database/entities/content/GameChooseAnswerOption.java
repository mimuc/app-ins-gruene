package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentClasses.minigames.MediaType;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;

@Entity
public class GameChooseAnswerOption {
    @PrimaryKey
    public int id;
    public int questionId;
    @TypeConverters({TypeConversion.class})
    public MediaType optionType;
    public String content;
    public boolean isRight;
}
