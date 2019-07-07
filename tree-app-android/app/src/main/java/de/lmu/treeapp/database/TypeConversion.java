package de.lmu.treeapp.database;

import androidx.room.TypeConverter;



public class TypeConversion {

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
