package de.lmu.treeapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Objects;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeComponent;
import de.lmu.treeapp.service.MainActivityViewModel;

public class OverviewRecyclerViewAdapter extends RecyclerView.Adapter<OverviewRecyclerViewAdapter.ViewHolder> {
    private final List<Tree> treeValues;
    private final de.lmu.treeapp.service.FragmentManagerService fragmentManager;
    private final Fragment selectedTreeFragment;
    private Activity currentActivity;
    private final MainActivityViewModel viewModel;
    public static ImageButton firstTree;
    public static ImageView treeLocked;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView treeName;
        public final ImageButton treeImage;
        public final ImageView treeUnlockedStatus;

        ViewHolder(View v) {
            super(v);
            treeName = v.findViewById(R.id.overview_single_tree_text);
            treeImage = v.findViewById(R.id.overview_single_tree_image);
            treeUnlockedStatus = v.findViewById(R.id.overview_single_tree_state_image);
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
    public OverviewRecyclerViewAdapter(List<Tree> treeData, de.lmu.treeapp.service.FragmentManagerService fragmentManager, Fragment selectedTreeFragment, MainActivityViewModel viewModel) {
        this.treeValues = treeData;
        this.fragmentManager = fragmentManager;
        this.selectedTreeFragment = selectedTreeFragment;
        this.viewModel = viewModel;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OverviewRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        v = inflater.inflate(R.layout.overview_single_tree_layout, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (position == 0) {
            firstTree = holder.treeImage;
            treeLocked = holder.treeUnlockedStatus;
        }

        Tree tree = treeValues.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.treeName.setText(tree.getName());

        if (Math.round(tree.GetGameProgressionPercent(Tree.GameCategories.total)) >= 100) {
            holder.treeUnlockedStatus.setVisibility(View.VISIBLE);
        }

        // Set Tree-Image here:
        Context context = holder.treeImage.getContext();
        int imageTreeId = context.getResources().getIdentifier(
                tree.getTreeImage(TreeComponent.TREE).imageResource,
                "drawable", context.getPackageName());
        Glide.with(context).load(imageTreeId).into(holder.treeImage);
        holder.treeImage.setOnClickListener(arg0 -> {
            viewModel.setCurrentPagerIndex(position);
            fragmentManager.showFragment(selectedTreeFragment);
            ((BottomNavigationView) Objects.requireNonNull(currentActivity).findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.action_tree_selection);
        });
    }

    public void setActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    // Return the size of your dataset (invoked by the layout manager);
    @Override
    public int getItemCount() {
        return treeValues.size();
    }
}
