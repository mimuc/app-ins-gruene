package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentClasses.UserProfileCategory;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;

@Entity
public class UserProfileOption {

    @PrimaryKey
    public int id;

    public int position;

    public String imageResource;

    public String name;

    @TypeConverters(TypeConversion.class)
    public UserProfileCategory category;
}
