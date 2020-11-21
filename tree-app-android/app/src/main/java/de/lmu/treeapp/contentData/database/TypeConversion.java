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
}
