package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentData.database.TypeConversion;

@Entity
public class GameChooseAnswerOption {
    public enum OptionTypes {
        TEXT, IMAGE
    }

    @PrimaryKey
    public int id;
    public int questionId;
    @TypeConverters({TypeConversion.class})
    public GameChooseAnswerOption.OptionTypes optionType;
    public String content;
    public boolean isRight;
}
