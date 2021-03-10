package de.lmu.treeapp.contentData.database.typeconversion;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

import de.lmu.treeapp.contentClasses.UserProfileCategory;
import de.lmu.treeapp.contentClasses.minigames.MediaType;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeComponent;
import de.lmu.treeapp.wantedPoster.WantedPosterImageType;
import de.lmu.treeapp.wantedPoster.WantedPosterTab;
import de.lmu.treeapp.wantedPoster.WantedPosterTextType;


public class TypeConversion {

    @TypeConverter
    public static List<Integer> intListFromString(String value) {
        String[] strArr = value.split(" ");
        int length = strArr.length;
        List<Integer> intVals = new ArrayList<>();
        for (String s : strArr) {
            if (s.equals(" ") || s.equals("")) continue;
            intVals.add(Integer.parseInt(s));
        }
        return intVals;
    }

    @TypeConverter
    public static String stringFromIntList(List<Integer> list) {
        StringBuilder stringVal = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringVal.append(list.get(i));
            stringVal.append(" ");
        }
        return stringVal.toString();
    }

    @TypeConverter
    public static int[] intArrayFromString(String value) {
        String[] strArr = value.split(" ");
        int length = strArr.length;
        int[] intVals = new int[length];
        for (int i = 0; i < length; i++) {
            if (strArr[i].equals(" ") || strArr[i].equals("")) continue;
            intVals[i] = Integer.parseInt(strArr[i]);
        }
        return intVals;
    }

    @TypeConverter
    public static String stringFromIntArray(int[] list) {

        StringBuilder stringVal = new StringBuilder();
        for (int value : list) {
            stringVal.append(value);
            stringVal.append(" ");
        }
        return stringVal.toString();
    }

    @TypeConverter
    public static Boolean[] boolArrayFromString(String value) {
        String[] strArr = value.split(" ");
        int length = strArr.length;
        Boolean[] boolVals = new Boolean[length];
        for (int i = 0; i < length; i++) {
            if (strArr[i].equals(" ") || strArr[i].equals("")) continue;
            boolVals[i] = Boolean.getBoolean(strArr[i]);
        }
        return boolVals;
    }

    @TypeConverter
    public static String stringFromBoolArray(Boolean[] list) {

        StringBuilder stringVal = new StringBuilder();
        for (Boolean aBoolean : list) {
            stringVal.append(aBoolean);
            stringVal.append(" ");
        }
        return stringVal.toString();
    }

    @TypeConverter
    public static Tree.GameCategories gameCategoryFromString(String category) {
        if (category == null) return Tree.GameCategories.none;
        return Tree.GameCategories.valueOf(category.toLowerCase());
    }

    @TypeConverter
    public static String gameCategoryToString(Tree.GameCategories category) {
        return category.name();
    }

    @TypeConverter
    public static TreeComponent treeComponentFromString(String component) {
        if (component == null) return TreeComponent.OTHER;
        return TreeComponent.valueOf(component.toUpperCase());
    }

    @TypeConverter
    public static String treeComponentToString(TreeComponent component) {
        return component.name();
    }

    @TypeConverter
    public static MediaType answerOptionTypeFromString(String optionType) {
        if (optionType == null) return MediaType.TEXT;
        return MediaType.valueOf(optionType.toUpperCase());
    }

    @TypeConverter
    public static String answerOptionTypeToString(MediaType optionType) {
        return optionType.name();
    }

    @TypeConverter
    public static Minigame_Base.MinigameTypes gameTypeFromString(String optionType) {
        if (optionType == null) return Minigame_Base.MinigameTypes.Undefined;
        return Minigame_Base.MinigameTypes.valueOf(optionType);
    }

    @TypeConverter
    public static String gameTypeToString(Minigame_Base.MinigameTypes optionType) {
        return optionType.name();
    }

    //region UserProfile

    @TypeConverter
    public static UserProfileCategory userCategoryFromString(String optionType) {
        if (optionType == null) return UserProfileCategory.UNDEFINED;
        return UserProfileCategory.valueOf(optionType.toUpperCase());
    }

    @TypeConverter
    public static String userCategoryToString(UserProfileCategory optionType) {
        return optionType.name();
    }

    //endregion UserProfile

    //region Wanted Poster

    @TypeConverter
    public static WantedPosterTab wantedPosterTabFromString(String str) {
        if (str == null) return WantedPosterTab.UNKNOWN;
        return WantedPosterTab.valueOf(str.toUpperCase());
    }

    @TypeConverter
    public static String wantedPosterTabToString(WantedPosterTab tab) {
        return tab.name();
    }

    @TypeConverter
    public static WantedPosterImageType wantedPosterImageTypeFromString(String str) {
        if (str == null) return WantedPosterImageType.UNKNOWN;
        return WantedPosterImageType.valueOf(str.toUpperCase());
    }

    @TypeConverter
    public static String wantedPosterImageTypeToString(WantedPosterImageType imgType) {
        return imgType.name();
    }

    @TypeConverter
    public static WantedPosterTextType wantedPosterTextTypeFromString(String str) {
        if (str == null) return WantedPosterTextType.UNKNOWN;
        return WantedPosterTextType.valueOf(str.toUpperCase());
    }

    @TypeConverter
    public static String wantedPosterTextTypeToString(WantedPosterTextType textType) {
        return textType.name();
    }

    //endregion Wanted Poster
}
