package de.lmu.treeapp.utils.io;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class AssetUtils {

    public static String loadStringFromAsset(String fileName, Context context) {
        String out;
        try {
            InputStream is = context.getResources().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            out = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return out;
    }
}
