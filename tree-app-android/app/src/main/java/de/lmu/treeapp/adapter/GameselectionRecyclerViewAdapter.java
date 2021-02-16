package de.lmu.treeapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.baumory.GameActivity_Baumory;
import de.lmu.treeapp.activities.minigames.catchFruits.GameActivity_CatchFruits;
import de.lmu.treeapp.activities.minigames.chooseAnswer.GameActivity_ChooseAnswer;
import de.lmu.treeapp.activities.minigames.contour.GameActivity_Contour;
import de.lmu.treeapp.activities.minigames.description.GameActivity_Description;
import de.lmu.treeapp.activities.minigames.dragDrop.GameActivity_DragDrop;
import de.lmu.treeapp.activities.minigames.inputString.GameActivity_InputString;
import de.lmu.treeapp.activities.minigames.leafOrder.GameActivity_LeafOrder;
import de.lmu.treeapp.activities.minigames.onlyDescription.GameActivity_OnlyDescription;
import de.lmu.treeapp.activities.minigames.orderWords.GameActivity_OrderWords;
import de.lmu.treeapp.activities.minigames.slidePuzzle.GameActivity_SlidePuzzle;
import de.lmu.treeapp.activities.minigames.puzzle.GameActivity_Puzzle;
import de.lmu.treeapp.activities.minigames.takePicture.GameActivity_TakePicture;
import de.lmu.treeapp.contentClasses.minigames.IGameBase;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.utils.glide.BackgroundTarget;

public class GameselectionRecyclerViewAdapter extends RecyclerView.Adapter<GameselectionRecyclerViewAdapter.ViewHolder> {

    List<Integer> games;
    int treeId;
    Tree.GameCategories category;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView gameName;
        public final ImageButton gameIcon;

        ViewHolder(View v) {
            super(v);
            gameName = v.findViewById(R.id.gameselection_single_game_name);
            gameIcon = v.findViewById(R.id.gameselection_single_game_icon);
        }
    }

    public void add(int position, Integer item) {
        games.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        games.remove(position);
        notifyItemRemoved(position);
    }

    public GameselectionRecyclerViewAdapter(List<Integer> gameIds, int treeId, Tree.GameCategories category) {
        this.games = gameIds;
        this.treeId = treeId;
        this.category = category;
    }


    @Override
    public GameselectionRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                          int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());

        View v = inflater.inflate(R.layout.gameselection_single_game_layout, parent, false);
        return new GameselectionRecyclerViewAdapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(GameselectionRecyclerViewAdapter.ViewHolder holder, final int position) {
        final Context context = holder.gameName.getContext();
        final IGameBase game = DataManager.getInstance(context).getMinigame(games.get(position));
        final Tree tree = DataManager.getInstance(context).getTree(treeId);
        if (game == null) {
            return;
        }
        if (tree.appData.isGameCompleted(category, game.getId())) {
            Glide.with(context).load(R.drawable.ic_quiz_checked).into(holder.gameIcon);
            Glide.with(context).load(R.drawable.white_background).into(new BackgroundTarget(holder.gameIcon));
        }
        holder.gameName.setText(game.getName());

        holder.gameIcon.setOnClickListener(arg0 -> {

            Class<?> gameActivityClass = null;
            switch (game.getType()) {
                case ChooseAnswer:
                    gameActivityClass = GameActivity_ChooseAnswer.class;
                    break;
                case InputString:
                    gameActivityClass = GameActivity_InputString.class;
                    break;
                case TakePicture:
                    gameActivityClass = GameActivity_TakePicture.class;
                    break;
                case DragDrop:
                    gameActivityClass = GameActivity_DragDrop.class;
                    break;
                case OnlyDescription:
                    gameActivityClass = GameActivity_OnlyDescription.class;
                    break;
                case Baumory:
                    gameActivityClass = GameActivity_Baumory.class;
                    break;
                case OrderWords:
                    gameActivityClass = GameActivity_OrderWords.class;
                    break;
                case Puzzle:
                    gameActivityClass = GameActivity_Puzzle.class;
                    break;
                case Description:
                    gameActivityClass = GameActivity_Description.class;
                    break;
                case CatchFruits:
                    gameActivityClass = GameActivity_CatchFruits.class;
                    break;
                case Contour:
                    gameActivityClass = GameActivity_Contour.class;
                    break;
                case LeafOrder:
                    gameActivityClass = GameActivity_LeafOrder.class;
                    break;
                case SlidePuzzle:
                    gameActivityClass = GameActivity_SlidePuzzle.class;
                    break;
                default:
                    break;
            }

            if (gameActivityClass != null) {
                Intent intent = new Intent(context, gameActivityClass);
                intent.putExtra("TreeId", treeId);
                intent.putExtra("Category", category);
                intent.putExtra("GameId", game.getId());
                context.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return games.size();
    }
}
