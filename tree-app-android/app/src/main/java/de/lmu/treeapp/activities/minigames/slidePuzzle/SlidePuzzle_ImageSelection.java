package de.lmu.treeapp.activities.minigames.slidePuzzle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import de.lmu.treeapp.R;

import static de.lmu.treeapp.utils.language.LanguageUtils.getTreeGenitiveGerman;

public class SlidePuzzle_ImageSelection extends Fragment{

    private ImageButton btnMale;
    private ImageButton btnFemale;

    public SlidePuzzle_ImageSelection() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_slide_puzzle_select_image, container, false);
        btnMale = rootView.findViewById(R.id.btn_male);
        btnFemale = rootView.findViewById(R.id.btn_female);
        Bundle bundle = getArguments();
        btnMale.setImageResource(bundle.getInt("imgM"));
        btnFemale.setImageResource(bundle.getInt("imgF"));
        btnMale.setOnClickListener(arg0 -> ((GameActivity_SlidePuzzle) getActivity()).startPuzzle(GameActivity_SlidePuzzle.BlossomType.male));
        btnFemale.setOnClickListener(arg0 -> ((GameActivity_SlidePuzzle) getActivity()).startPuzzle(GameActivity_SlidePuzzle.BlossomType.female));

        //String treeName = getTreeGenitiveGerman(bundle.getString("treeName"));

        //description.setText(getString(R.string.game_catchFruits_trees_description_text, treeName));

        return rootView;
    }
    //Buche, Fichte, Hasel, Kiefer, Tanne (M/W)
    //1, 9, 5, 3, 8
    //Ahorn, Birke, Eberesche, Eiche, Linde
    //0, 6, 7, 4, 2
}