package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.minigames.MediaType;
import de.lmu.treeapp.contentData.database.entities.content.GameChooseAnswerOption;
import de.lmu.treeapp.contentData.database.entities.content.GameChooseAnswerRelations;
import de.lmu.treeapp.utils.glide.BackgroundTarget;

public class ChooseAnswer_Options_RecyclerViewAdapter extends
        RecyclerView.Adapter<ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder> {

    public List<GameChooseAnswerOption> options;
    private final Context context;
    private final OptionClickInterface mOnClickListener;

    public ChooseAnswer_Options_RecyclerViewAdapter(OptionClickInterface mOnClickListener,
                                                    GameChooseAnswerRelations _game,
                                                    Context _context) {
        this.mOnClickListener = mOnClickListener;
        options = _game.getOptions();
        Collections.shuffle(options);
        this.context = _context;
    }

    @NonNull
    @Override
    public ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                  int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());

        View v = inflater.inflate(R.layout.activity_game__choose_answer__option, parent, false);

        return new ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder holder,
                                 final int position) {
        final GameChooseAnswerOption option = options.get(position);

        if (option.optionType == MediaType.TEXT) {
            holder.button_txt.setVisibility(View.VISIBLE);
            holder.button_txt.setText(option.content);
        } else if (option.optionType == MediaType.IMAGE) {
            holder.button_img.setVisibility(View.VISIBLE);
            int imageId = context.getResources().getIdentifier(option.content,
                    "drawable", context.getPackageName());

            Glide.with(context).load(imageId).into(new BackgroundTarget(holder.button_img));
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
