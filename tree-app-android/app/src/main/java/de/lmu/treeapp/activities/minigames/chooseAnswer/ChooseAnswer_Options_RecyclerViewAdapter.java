package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.database.entities.content.GameChooseAnswerOption;
import de.lmu.treeapp.contentData.database.entities.content.GameChooseAnswerRelations;

public class ChooseAnswer_Options_RecyclerViewAdapter extends RecyclerView.Adapter<ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder> {

    public static List<GameChooseAnswerOption> options;
    private static int resultImageId;
    private static String resultText;
    private final GameChooseAnswerRelations game;
    private final Context context;
    private final Tree tree;
    private final Tree.GameCategories category;
    private final OptionClickInterface mOnClickListener;

    public ChooseAnswer_Options_RecyclerViewAdapter(OptionClickInterface mOnClickListener, GameChooseAnswerRelations _game, Context _context, Tree _tree, Tree.GameCategories _category) {
        this.mOnClickListener = mOnClickListener;
        options = _game.getOptions();
        Collections.shuffle(options);
        this.game = _game;
        this.context = _context;
        this.tree = _tree;
        this.category = _category;
    }

    public static GameChooseAnswerOption findResult(List<GameChooseAnswerOption> options) {
        for (GameChooseAnswerOption option : options) {
            if (option.isRight) {
                return option;
            }
        }
        return null;
    }

    public static boolean isImage(List<GameChooseAnswerOption> options) {
        GameChooseAnswerOption option = options.get(0);
        return option.optionType == GameChooseAnswerOption.OptionTypes.IMAGE;
    }

    public void add(int position, GameChooseAnswerOption item) {
        options.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        options.remove(position);
        notifyItemRemoved(position);
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
        final GameChooseAnswerOption option = options.get(position);

        if (option.optionType == GameChooseAnswerOption.OptionTypes.TEXT) {
            holder.button_txt.setVisibility(View.VISIBLE);
            holder.button_txt.setText(option.content);
            if (option.isRight) {
                resultText = holder.button_txt.getText().toString();
                GameActivity_ChooseAnswer.resultText = resultText;
            }
        } else if (option.optionType == GameChooseAnswerOption.OptionTypes.IMAGE) {
            holder.button_img.setVisibility(View.VISIBLE);
            int imageId = context.getResources().getIdentifier(option.content, "drawable", context.getPackageName());

            if (option.isRight) {
                resultImageId = imageId;
                GameActivity_ChooseAnswer.resultImage = resultImageId;
            }
            holder.button_img.setBackgroundResource(imageId);
        }

        holder.button_img.setOnClickListener(arg0 -> mOnClickListener.optionClicked(option));

        holder.button_txt.setOnClickListener(arg0 -> mOnClickListener.optionClicked(option));
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public interface OptionClickInterface {
        void optionClicked(GameChooseAnswerOption option);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageButton button_img;
        public final Button button_txt;

        ViewHolder(View v) {
            super(v);
            button_img = v.findViewById(R.id.button_img);
            button_txt = v.findViewById(R.id.button_txt);
        }
    }
}
