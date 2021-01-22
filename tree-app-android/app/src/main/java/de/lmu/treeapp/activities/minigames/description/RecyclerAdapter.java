package de.lmu.treeapp.activities.minigames.description;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.lmu.treeapp.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private final ArrayList<DescriptionElement> data;
    private final OptionClickInterface mOnClickListener;


    public RecyclerAdapter(ArrayList<DescriptionElement> data, OptionClickInterface mOnClickListener) {
        this.data = data;
        this.mOnClickListener = mOnClickListener;
    }

    public void add(int position, DescriptionElement item) {
        data.add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, data.size());
        notifyDataSetChanged();

    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.description_element, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.content.setOnClickListener(arg0 -> mOnClickListener.optionClicked(position, holder));
        holder.content.setText(data.get(position).getText());
        data.get(position).setHolder(holder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ArrayList<DescriptionElement> getItems() {
        return data;
    }

    public DescriptionElement getItemById(int position) {
        return data.get(position);
    }


    public interface OptionClickInterface {
        void optionClicked(int position, MyViewHolder holder);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView content;
        View rowView;

        public MyViewHolder(View itemView) {
            super(itemView);
            rowView = itemView;
            content = itemView.findViewById(R.id.game_description_element);
        }

        public TextView getContent() {
            return content;
        }

        public RecyclerAdapter getOuterClass() {
            return RecyclerAdapter.this;
        }
    }
}

