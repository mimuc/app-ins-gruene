package de.lmu.treeapp.activities.minigames.onlyDescription;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lmu.treeapp.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private final List<OnlyDescriptionElement> data;
    private final Context context;
    int x;

    public RecyclerAdapter(List<OnlyDescriptionElement> data, int x, Context context) {
        this.data = data;
        this.x = x;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.only_description_cardview, parent, false);

        // set a different color for every second text
        if (x % 2 == 1) {
            itemView.setBackgroundTintList(ContextCompat.getColorStateList(context,
                    R.color.forest_transparent));
        }
        x++;
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.content.setText(data.get(position).getText());
        data.get(position).setHolder();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<OnlyDescriptionElement> getItems() {
        return data;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView content;

        public MyViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.game_onlyDescription_element);
        }

        public TextView getContent() {
            return content;
        }
    }
}

