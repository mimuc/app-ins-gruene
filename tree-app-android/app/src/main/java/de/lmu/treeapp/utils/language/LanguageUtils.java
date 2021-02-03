package de.lmu.treeapp.utils.language;

public class LanguageUtils {

    public static String getTreeGenitiveGerman(String treeName) {
        if (treeName.equals("Ahorn")) {
            treeName = "des " + treeName + "s";
        } else {
            treeName = "der " + treeName;
        }
        return treeName;
    }
}
