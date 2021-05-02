package de.lmu.treeapp.utils.language;

import android.content.Context;
import android.os.Build;
import de.lmu.treeapp.utils.io.AssetUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class ProfanityFilter {
    private static volatile ProfanityFilter mInstance;
    List<String> swearWords = new ArrayList<>();

    public static ProfanityFilter getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ProfanityFilter.class) {
                if (mInstance == null) {
                    mInstance = new ProfanityFilter();
                    mInstance.loadSwearWords(context);
                }
            }
        }
        return mInstance;
    }

    private void loadSwearWords(Context context) {
        try {
            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(AssetUtils.loadStringFromAsset("swearwords.json", context)));
            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray arr = (JSONArray) jsonObject.get(key);
                for (int i = 0; i < arr.length(); i++) {
                    swearWords.add(arr.optString(i).toLowerCase());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public boolean checkProfanity(String toString) {
        // add words from the input string into an array "words"
        List<String> words = new ArrayList<>(Arrays.asList(toString.toLowerCase().split("[\\s\\\\\\/§\\%@&.?;:,`'\"$+-]+")));

        // for API >= 24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return words.stream().noneMatch(textWord -> swearWords.stream().anyMatch(textWord::startsWith));
        } else {
            // for API < 24
            // Check for each word of the input if it is in the list of swearwords
            for (String word : words) {
                for (String profane : swearWords) {
                    if (word.startsWith(profane)) {
                        // there is a swearword
                        return false;
                    }
                }
            }
        }
        // no swearword
        return true;
    }
}
