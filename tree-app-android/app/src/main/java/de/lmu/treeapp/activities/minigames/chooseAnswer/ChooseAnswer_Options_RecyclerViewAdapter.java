package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.app.AutomaticZenRule;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.minigames.Minigame_ChooseAnswer;
import de.lmu.treeapp.contentClasses.minigames.components.AnswerOption;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;

public class ChooseAnswer_Options_RecyclerViewAdapter extends RecyclerView.Adapter<ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder>  {

    public static List<AnswerOption> options;
    private Minigame_ChooseAnswer game;
    private Context context;
    private Tree tree;
    private Tree.GameCategories category;

    public static int current=1;
    //protected static int columnNumber;
    private static int resultImageId;
    private static String resultText;

    public interface OptionClickInterface
    {
        void optionClicked(AnswerOption option);
    }
    private final OptionClickInterface mOnClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public static boolean imgButton;
        public final Button button;
        public final Button textButton;
        ViewHolder(View v) {
            super(v);
            button = v.findViewById(R.id.game_chooseAnswer_option_button);
            textButton = v.findViewById(R.id.game_chooseAnswer_option_textButton);
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

    public static AnswerOption findResult(List<AnswerOption> options){
        for (AnswerOption option : options) {
            if (option.right) {
                return option;
            }
        }
        return null;
    }

    public ChooseAnswer_Options_RecyclerViewAdapter(OptionClickInterface mOnClickListener, Minigame_ChooseAnswer _game, Context _context, Tree _tree, Tree.GameCategories _category) {
        this.mOnClickListener = mOnClickListener;
        this.options = _game.options;
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
        System.out.println(holder.button);
        System.out.println(holder.textButton);

        if(option.type == AnswerOption.OptionTypes.text){
            //GameActivity_ChooseAnswer.columnNumber = 1;
            holder.button.setVisibility(View.INVISIBLE);
            holder.textButton.setText(option.content);
            if(option.right){
                resultText = holder.textButton.getText().toString();
            }
        }else if(option.type == AnswerOption.OptionTypes.image){
            GameActivity_ChooseAnswer.columnNumber = 2;
            holder.textButton.setVisibility(View.INVISIBLE);
            int imageId = context.getResources().getIdentifier(option.content, "drawable", context.getPackageName());
            if(option.right){
                resultImageId = imageId;
            }
            holder.button.setBackgroundResource(imageId);
            holder.button.setText("");
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(option.right){
                    current=current+1;
                }else{
                    GameActivity_ChooseAnswer.resultImage = resultImageId;
                }
                mOnClickListener.optionClicked(option);
            }
        });

        holder.textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(option.right){
                    current=current+1;
                }else{
                    GameActivity_ChooseAnswer.resultText = resultText;
                }
                mOnClickListener.optionClicked(option);
            }
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }
}
