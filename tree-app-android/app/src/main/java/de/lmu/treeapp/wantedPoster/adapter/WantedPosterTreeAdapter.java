package de.lmu.treeapp.wantedPoster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.lmu.treeapp.R;

public class WantedPosterTreeAdapter extends RecyclerView.Adapter<WantedPosterTreeAdapter.ViewHolder> {

    public RecyclerView parentRecycler;
    private final List<Integer> iconList;
    public static ImageView firstButton;

    public WantedPosterTreeAdapter(List<Integer> iconList) {
        this.iconList = iconList;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecycler = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_tree_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(position == 1){
            firstButton = holder.imageView;
        }

        Integer listOfIcons = iconList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(listOfIcons)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.category_image);

            itemView.findViewById(R.id.container).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            parentRecycler.smoothScrollToPosition(getAdapterPosition());
        }
    }
}
