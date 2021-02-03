package de.lmu.treeapp.activities.minigames.catchFruits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import de.lmu.treeapp.R;

import static de.lmu.treeapp.utils.language.LanguageUtils.getTreeGenitiveGerman;


public class CatchFruits_StartScreen extends Fragment {

    private Button startButton;
    private TextView description;

    public CatchFruits_StartScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_catch_fruits_start_screen, container, false);
        startButton = rootView.findViewById(R.id.start_catchfruits_button);
        description = rootView.findViewById(R.id.text_description1);
        startButton.setOnClickListener(arg0 -> ((GameActivity_CatchFruits) getActivity()).startGame());
        Bundle bundle = getArguments();

        String treeName = getTreeGenitiveGerman(bundle.getString("treeName"));

        description.setText(getString(R.string.game_catchFruits_trees_description_text, treeName));

        return rootView;
    }
}