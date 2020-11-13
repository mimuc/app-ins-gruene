package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.minigames.Minigame_ChooseAnswer;
import de.lmu.treeapp.contentClasses.minigames.components.AnswerOption;
import de.lmu.treeapp.contentClasses.trees.Tree;

public class ChooseAnswer_Options_RecyclerViewAdapter extends RecyclerView.Adapter<ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder> {

    public static List<AnswerOption> options;
    private final Minigame_ChooseAnswer game;
    private final Context context;
    private final Tree tree;
    private final Tree.GameCategories category;
    private static int resultImageId;
    private static String resultText;


    public interface OptionClickInterface {
        void optionClicked(AnswerOption option);
    }

    private final OptionClickInterface mOnClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final Button button_img;
        public final Button button_txt;

        ViewHolder(View v) {
            super(v);
            button_img = v.findViewById(R.id.button_img);
            button_txt = v.findViewById(R.id.button_txt);
        }
    }

    public void add(int position, AnswerOption item) {
        options.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        options.remove(position);
        notifyItemRemoved(position);
    }

    public static AnswerOption findResult(List<AnswerOption> options) {
        for (AnswerOption option : options) {
            if (option.right) {
                return option;
            }
        }
        return null;
    }

    public static boolean isImage(List<AnswerOption> options) {
        AnswerOption option = options.get(0);
        return option.type == AnswerOption.OptionTypes.image;
    }

    public ChooseAnswer_Options_RecyclerViewAdapter(OptionClickInterface mOnClickListener, Minigame_ChooseAnswer _game, Context _context, Tree _tree, Tree.GameCategories _category) {
        this.mOnClickListener = mOnClickListener;
        options = _game.options;
        this.game = _game;
        this.context = _context;
        this.tree = _tree;
        this.category = _category;
    }

    @Override
    public ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                  int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());

        View v = inflater.inflate(R.layout.activity_game__choose_answer__option, parent, false);

        return new ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder holder, final int position) {
        final AnswerOption option = options.get(position);

        if (option.type == AnswerOption.OptionTypes.text) {
            holder.button_txt.setVisibility(View.VISIBLE);
            holder.button_txt.setText(option.content);
            if (option.right) {
                resultText = holder.button_txt.getText().toString();
                GameActivity_ChooseAnswer.resultText = resultText;
            }
        } else if (option.type == AnswerOption.OptionTypes.image) {
            holder.button_img.setVisibility(View.VISIBLE);
            int imageId = context.getResources().getIdentifier(option.content, "drawable", context.getPackageName());

            if (option.right) {
                resultImageId = imageId;
                GameActivity_ChooseAnswer.resultImage = resultImageId;
            }
            holder.button_img.setBackgroundResource(imageId);
            holder.button_img.setText("");
        }

        holder.button_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mOnClickListener.optionClicked(option);
            }
        });

        holder.button_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mOnClickListener.optionClicked(option);
            }
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }
}
