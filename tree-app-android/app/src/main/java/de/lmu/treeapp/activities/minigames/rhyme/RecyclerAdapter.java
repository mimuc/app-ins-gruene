package de.lmu.treeapp.activities.minigames.rhyme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import de.lmu.treeapp.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private final ArrayList<RhymeElement> data;
    private final OptionClickInterface mOnClickListener;

    public RecyclerAdapter(ArrayList<RhymeElement> data, OptionClickInterface mOnClickListener) {
        this.data = data;
        this.mOnClickListener = mOnClickListener;
    }

    public void add(int position, RhymeElement item) {
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rhyme_element, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.content.setOnClickListener(arg0 -> mOnClickListener.optionClicked(position, holder));
        holder.content.setText(data.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ArrayList<RhymeElement> getItems() {
        return data;
    }

    public RhymeElement getItemById(int position) {
        return data.get(position);
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {

    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
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
            content = itemView.findViewById(R.id.rhyme_element);
        }

        public RecyclerAdapter getOuterClass() {
            return RecyclerAdapter.this;
        }
    }
}

