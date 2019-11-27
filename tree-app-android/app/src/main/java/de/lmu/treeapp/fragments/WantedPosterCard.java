package de.lmu.treeapp.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import de.lmu.treeapp.R;

public class WantedPosterCard {

    public enum CardTypes{
        //Different layout types of cards can be entered here and handled differently in future
    }

    private final String header;
    private final String subHeader;
    private final Drawable picture;
    private final Uri gamePicture;
    private final String infotext;
    private final boolean unlocked;
    private Drawable collapse;


    public WantedPosterCard(boolean unlocked, String header, Drawable picture, String infoText, Uri gamePicture, Context context) {
        this.header = header;
        this.picture = picture;
        this.infotext = infoText;
        this.unlocked = unlocked;
        this.gamePicture = gamePicture;

        if(unlocked){
            //TODO: collapse arrow here to be safe
            subHeader = "Bereits freigespielt";
            collapse = context.getDrawable(R.drawable.ic_arrow_left);

        } else {
            subHeader = "Nicht freigespielt";
            collapse = context.getDrawable(R.drawable.ic_locked);

        }
    }

    public String getHeader() {
        return header;
    }

    public String getSubHeader() {
        return subHeader;
    }

    public Drawable getPicture() {
        return picture;
    }

    public Uri getGamePicture() {return gamePicture;}

    public String getInfotext() {
        return infotext;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public Drawable getCollapse() {
        return collapse;
    }
}
