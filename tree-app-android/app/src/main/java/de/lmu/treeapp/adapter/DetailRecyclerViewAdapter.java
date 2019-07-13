package de.lmu.treeapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.trees.Tree;

public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder> {

    private List<Tree> treeValues;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView treeName;
        public final ImageView treeImage;
        ViewHolder(View v) {
            super(v);
            treeName = v.findViewById(R.id.detail_single_tree_text);
            treeImage = v.findViewById(R.id.detail_single_tree_image);
        }
    }
    public void add(int position, Tree item) {
        treeValues.add(position, item);
        notifyItemInserted(position);
    }
    public void remove(int position) {
        treeValues.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DetailRecyclerViewAdapter(List<Tree> treeData) {
        this.treeValues = treeData;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DetailRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());

        View v = inflater.inflate(R.layout.detail_single_tree_layout, parent, false);
        return new DetailRecyclerViewAdapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DetailRecyclerViewAdapter.ViewHolder holder, final int position) {
        Tree tree = treeValues.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.treeName.setText(tree.name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return treeValues.size();
    }

}
