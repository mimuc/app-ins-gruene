package de.lmu.treeapp.contentData.database.entities.content;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;
import de.lmu.treeapp.wantedPoster.WantedPosterTab;
import de.lmu.treeapp.wantedPoster.WantedPosterTextType;

@Entity
public class WantedPoster {
    @PrimaryKey
    public int id;

    public int treeId;

    @TypeConverters(TypeConversion.class)
    public WantedPosterTab tab;

    @TypeConverters(TypeConversion.class)
    public WantedPosterTextType name;

    public String description;
}
