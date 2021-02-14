package de.lmu.treeapp.activities.minigames.baumory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

import de.lmu.treeapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BaumorySelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BaumorySelectionFragment extends Fragment {

    private ViewFlipper viewFlipper;
    private Boolean multiPlayer = false;
    private Boolean hard = false;


    public BaumorySelectionFragment() {
        // Required empty public constructor
    }

    public static BaumorySelectionFragment newInstance(String param1, String param2) {
        BaumorySelectionFragment fragment = new BaumorySelectionFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_baumory_selection, container, false);
        viewFlipper = rootView.findViewById(R.id.viewFlipperBaumorySelection);
        // set the animation type to ViewFlipper
        viewFlipper.setInAnimation(getActivity(), R.anim.fragment_fade_enter);
        viewFlipper.setOutAnimation(getActivity(), R.anim.fragment_fade_exit);
        ImageButton multiPlayerButton = rootView.findViewById(R.id.btn_multiplayer);
        ImageButton singlePlayerButton = rootView.findViewById(R.id.btn_singleplayer);
        ImageButton easyButton = rootView.findViewById(R.id.btn_easy);
        ImageButton hardButton = rootView.findViewById(R.id.btn_hard);
        multiPlayerButton.setOnClickListener(v ->
        {
            multiPlayer = true;
            viewFlipper.showNext(); // Switch to next View
        });

        singlePlayerButton.setOnClickListener(v ->
        {
            multiPlayer = false;
            viewFlipper.showNext(); // Switch to next View
        });

        easyButton.setOnClickListener(v ->
        {
            hard = false;
            ((GameActivity_Baumory) getActivity()).startGame(multiPlayer, hard);
        });

        hardButton.setOnClickListener(v ->
        {
            hard = true;
            ((GameActivity_Baumory) getActivity()).startGame(multiPlayer, hard);
        });
        return rootView;
    }
}