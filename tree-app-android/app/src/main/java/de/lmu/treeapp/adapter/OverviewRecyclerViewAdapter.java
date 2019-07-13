package de.lmu.treeapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lmu.treeapp.R;

public class OverviewRecyclerViewAdapter extends RecyclerView.Adapter<OverviewRecyclerViewAdapter.ViewHolder> {
    private List<String> treeValues;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View v) {
            super(v);
        }
    }

    public void add(int position, String item) {
        treeValues.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        treeValues.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OverviewRecyclerViewAdapter(List<String> treeData) {
        treeValues = treeData;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OverviewRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());

        View v = inflater.inflate(R.layout.overview_single_tree_layout, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = treeValues.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return treeValues.size();
    }
}
