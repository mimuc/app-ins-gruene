package de.lmu.treeapp.activities.minigames.baumory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentData.database.entities.content.GameBaumoryCard;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.utils.glide.BackgroundTarget;

public class Baumory_Cards_RecyclerViewAdapter extends RecyclerView.Adapter<Baumory_Cards_RecyclerViewAdapter.ViewHolder> {

    private final List<GameBaumoryCard> options;
    private final Context context;
    private final Tree tree;
    private final Tree.GameCategories category;

    public interface OptionClickInterface {
        void optionClicked(GameBaumoryCard option, ViewHolder holder);
    }

    private final OptionClickInterface mOnClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageButton button;

        ViewHolder(View v) {
            super(v);
            button = v.findViewById(R.id.game_baumory_card_button);
        }
    }

    public void add(int position, GameBaumoryCard item) {
        options.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        options.remove(position);
        notifyItemRemoved(position);
    }

    public Baumory_Cards_RecyclerViewAdapter(OptionClickInterface mOnClickListener, List<GameBaumoryCard> _cards, Context _context, Tree _tree, Tree.GameCategories _category) {
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
        final GameBaumoryCard option = options.get(position);

        Glide.with(context).load(R.drawable.ic_singleplayer_squirrel).into(holder.button);
        Glide.with(context).load(R.drawable.forst_card_background).into(new BackgroundTarget(holder.button));
        holder.button.setOnClickListener(arg0 -> mOnClickListener.optionClicked(option, holder));
    }


    @Override
    public int getItemCount() {
        return options.size();
    }

}
