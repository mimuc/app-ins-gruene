package de.lmu.treeapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.GameSelectionActivity;
import de.lmu.treeapp.activities.WantedPosterDetailsActivity;
import de.lmu.treeapp.contentClasses.trees.Tree;

public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder> {

    private List<Tree> treeValues;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView treeName;
        public final ImageView treeImage;
        public final Button treeProfileButton;
        public final ImageButton leafButton;
        public final ImageButton fruitButton;
        public final ImageButton trunkButton;
        ViewHolder(View v) {
            super(v);
            treeName = v.findViewById(R.id.detail_single_tree_text);
            treeImage = v.findViewById(R.id.detail_single_tree_image);
            treeProfileButton = v.findViewById(R.id.detail_single_tree_profileButton);
            leafButton = v.findViewById(R.id.detail_single_tree_leafButton);
            fruitButton = v.findViewById(R.id.detail_single_tree_fruitButton);
            trunkButton = v.findViewById(R.id.detail_single_tree_trunkButton);
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
        final Tree tree = treeValues.get(position);
        holder.treeName.setText(tree.name);
        final Context context = holder.treeProfileButton.getContext();

        holder.treeProfileButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,WantedPosterDetailsActivity.class);
                intent.putExtra("TreeId",tree.uid);
                context.startActivity(intent);
            }
        });
        holder.leafButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GameSelectionActivity.class);
                intent.putExtra("TreeId",tree.uid);
                intent.putExtra("Category", Tree.GameCategories.leaf);
                context.startActivity(intent);
            }
        });
        holder.fruitButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GameSelectionActivity.class);
                intent.putExtra("TreeId",tree.uid);
                intent.putExtra("Category", Tree.GameCategories.fruit);
                context.startActivity(intent);
            }
        });
        holder.trunkButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GameSelectionActivity.class);
                intent.putExtra("TreeId",tree.uid);
                intent.putExtra("Category", Tree.GameCategories.trunk);
                context.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return treeValues.size();
    }

}
