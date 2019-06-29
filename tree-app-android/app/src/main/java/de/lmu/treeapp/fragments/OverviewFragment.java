package de.lmu.treeapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.WantedPosterActivity;

public class OverviewFragment extends Fragment {

    private TextView overviewTextView;
    private Button overviewWantedPosterButton;

    public OverviewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        overviewTextView =  view.findViewById(R.id.textView_overview);
        overviewWantedPosterButton = view.findViewById(R.id.overview_wanted_poster_button);

        overviewWantedPosterButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast qrToast = Toast.makeText(getContext(), "Wanted Poster Button clicked", Toast.LENGTH_LONG );
                qrToast.show();

                Intent intent = new Intent(getContext(), WantedPosterActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
