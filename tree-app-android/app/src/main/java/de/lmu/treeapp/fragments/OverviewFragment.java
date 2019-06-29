package de.lmu.treeapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import de.lmu.treeapp.R;

public class OverviewFragment extends Fragment {

    private TextView overviewTextView;

    public OverviewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        overviewTextView =  view.findViewById(R.id.textView_overview);

        return view;
    }
}
