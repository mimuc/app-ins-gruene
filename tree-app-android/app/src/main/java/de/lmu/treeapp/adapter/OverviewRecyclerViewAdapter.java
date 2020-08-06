package de.lmu.treeapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Objects;

import de.lmu.treeapp.R;
import de.lmu.treeapp.service.MainActivityViewModel;
import de.lmu.treeapp.activities.Imprint;
import de.lmu.treeapp.activities.MainActivity;
import de.lmu.treeapp.contentClasses.trees.Tree;

public class OverviewRecyclerViewAdapter extends RecyclerView.Adapter<OverviewRecyclerViewAdapter.ViewHolder> {
    private List<Tree> treeValues;
    private de.lmu.treeapp.service.FragmentManagerService fragmentManager;
    private Fragment selectedTreeFragment;
    private Activity currentActivity;
    private MainActivityViewModel viewModel;
    public static boolean isImprint = false;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView treeName;
        public final ImageButton treeImage;
        public final ImageView treeUnlockedStatus;
        public final Button btn_imprint;

        ViewHolder(View v) {
            super(v);
            treeName = v.findViewById(R.id.overview_single_tree_text);
            treeImage = v.findViewById(R.id.overview_single_tree_image);
            treeUnlockedStatus = v.findViewById(R.id.overview_single_tree_state_image);
            btn_imprint = v.findViewById(R.id.btn_imprint);
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

    public static void isImprint(){
        isImprint = true;
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
    public OverviewRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        if(viewType == R.layout.overview_single_tree_layout){
            v = inflater.inflate(R.layout.overview_single_tree_layout, parent, false);
        } else {
            v = inflater.inflate(R.layout.overview_button_imprint, parent, false);
        }
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if(position == treeValues.size()) {

            holder.btn_imprint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent imprint = new Intent(MainActivity.mainContext, Imprint.class);
                    //finish();
                    //startActivity(new Intent(OverviewRecyclerViewAdapter.this, Imprint.class));
                    //startActivity(imprint);
                    Toast.makeText(MainActivity.mainContext, "Button Clicked", Toast.LENGTH_LONG).show();
                }
            });

        } else {

            Tree tree = treeValues.get(position);
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.treeName.setText(tree.name);

            if (Math.round(tree.GetGameProgressionPercent(Tree.GameCategories.total)) >= 100) {
                holder.treeUnlockedStatus.setImageResource(R.drawable.ic_checked_mark);
            } else if (tree.changeable.unlocked) {
                // Change to Unlocked-Symbol
                holder.treeUnlockedStatus.setImageResource(R.drawable.ic_question_mark);
            } else {
                holder.treeUnlockedStatus.setImageResource(R.drawable.ic_locked_mark);
            }
            // Set Tree-Image here:
            Context context = holder.treeImage.getContext();
            int imageTreeId = context.getResources().getIdentifier(tree.imageTree, "drawable", context.getPackageName());
            holder.treeImage.setImageResource(imageTreeId);
            holder.treeImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    viewModel.setCurrentPagerIndex(position);
                    fragmentManager.showFragment(selectedTreeFragment);
                    ((BottomNavigationView) Objects.requireNonNull(currentActivity).findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.action_tree_selection);
                }

            });
        }
    }

    public void setActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    // Return the size of your dataset (invoked by the layout manager), +1 for the imprint button at the end
    @Override
    public int getItemCount() {
        return treeValues.size() + 1;
    }

    // Check if the current position is past the last item in the list, if so then it will return the button layout value
    String viewType;
    @Override
    public int getItemViewType(int position) {
        //return (position == treeValues.size()) ? R.layout.overview_button_imprint : R.layout.overview_single_tree_layout;
        if (position == treeValues.size()) {
            isImprint();
            return R.layout.overview_button_imprint;
        } else {
            return R.layout.overview_single_tree_layout;
        }
    }
}
