package de.lmu.treeapp.activities.minigames.baumory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.minigames.components.BaumoryCard;
import de.lmu.treeapp.contentClasses.trees.Tree;

public class Baumory_Cards_RecyclerViewAdapter extends RecyclerView.Adapter<Baumory_Cards_RecyclerViewAdapter.ViewHolder> {

    private final List<BaumoryCard> options;
    private final Context context;
    private final Tree tree;
    private final Tree.GameCategories category;

    public interface OptionClickInterface {
        void optionClicked(BaumoryCard option, ViewHolder holder);
    }

    private final OptionClickInterface mOnClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageButton button;

        ViewHolder(View v) {
            super(v);
            button = v.findViewById(R.id.game_baumory_card_button);
        }
    }

    public void add(int position, BaumoryCard item) {
        options.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        options.remove(position);
        notifyItemRemoved(position);
    }

    public Baumory_Cards_RecyclerViewAdapter(OptionClickInterface mOnClickListener, List<BaumoryCard> _cards, Context _context, Tree _tree, Tree.GameCategories _category) {
        this.mOnClickListener = mOnClickListener;
        this.options = _cards;
        this.context = _context;
        this.tree = _tree;
        this.category = _category;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.activity_game__baumory__card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BaumoryCard option = options.get(position);

        holder.button.setImageResource(R.drawable.ic_question_big);
        holder.button.setBackgroundResource(R.drawable.dark_grey_gradient);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mOnClickListener.optionClicked(option, holder);
            }
        });
    }


    @Override
    public int getItemCount() {
        return options.size();
    }

}
