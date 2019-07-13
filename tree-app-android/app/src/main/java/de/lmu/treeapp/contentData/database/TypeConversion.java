package de.lmu.treeapp.contentData.database;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;


public class TypeConversion {


    @TypeConverter
    public static List<Integer> intListFromString(String value) {
        String[] strArr = value.split(" ");
        int length = strArr.length;
        List<Integer> intVals = new ArrayList<Integer>();
        for (int i = 0; i < length; i++){
            if (strArr[i] == " " || strArr[i] == "") continue;
            intVals.add(Integer.parseInt(strArr[i]));
        }
        return intVals;
    }

    @TypeConverter
    public static String stringFromIntList(List<Integer> list) {
        String stringVal = "";
        for (int i = 0; i < list.size(); i++){
            stringVal += list.get(i);
            stringVal += " ";
        }
        return stringVal;
    }

    @TypeConverter
    public static int[] intArrayFromString(String value) {
        String[] strArr = value.split(" ");
        int length = strArr.length;
        int[] intVals = new int[length];
        for (int i = 0; i < length; i++){
            if (strArr[i] == " " || strArr[i] == "") continue;
            intVals[i] = Integer.parseInt(strArr[i]);
        }
        return intVals;
    }

    @TypeConverter
    public static String stringFromIntArray(int[] list) {

        String stringVal = "";
        for (int i = 0; i < list.length; i++){
            stringVal += list[i];
            stringVal += " ";
        }
        return stringVal;
    }

    @TypeConverter
    public static Boolean[] boolArrayFromString(String value) {
        String[] strArr = value.split(" ");
        int length = strArr.length;
        Boolean[] boolVals = new Boolean[length];
        for (int i = 0; i < length; i++){
            if (strArr[i] == " " || strArr[i] == "") continue;
            boolVals[i] = Boolean.getBoolean(strArr[i]);
        }
        return boolVals;
    }

    @TypeConverter
    public static String stringFromBoolArray(Boolean[] list) {

        String stringVal = "";
        for (int i = 0; i < list.length; i++){
            stringVal += list[i];
            stringVal += " ";
        }
        return stringVal;
    }
}
