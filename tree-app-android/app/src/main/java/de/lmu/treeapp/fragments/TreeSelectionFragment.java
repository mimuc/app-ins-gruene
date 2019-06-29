package de.lmu.treeapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import de.lmu.treeapp.R;

public class TreeSelectionFragment extends Fragment {

    private TextView treeSelectionTextView;

    public TreeSelectionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tree_selection, container, false);
        treeSelectionTextView =  view.findViewById(R.id.textView_tree_selection);

        return view;
    }
}
