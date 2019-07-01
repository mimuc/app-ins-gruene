package de.lmu.treeapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.WantedPosterDetailsActivity;

public class TreeSelectionFragment extends Fragment {

    public TreeSelectionFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tree_selection, container, false);
        Button treeSelectionWantedPosterButton = view.findViewById(R.id.tree_selection_wanted_poster_button);

        treeSelectionWantedPosterButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast qrToast = Toast.makeText(getContext(), "Wanted Poster Button clicked", Toast.LENGTH_LONG );
                qrToast.show();

                Intent intent = new Intent(getContext(), WantedPosterDetailsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
