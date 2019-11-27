package de.lmu.treeapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.chooseAnswer.GameActivity_ChooseAnswer;
import de.lmu.treeapp.activities.minigames.dragDrop.GameActivity_DragDrop;
import de.lmu.treeapp.activities.minigames.inputString.GameActivity_InputString;
import de.lmu.treeapp.activities.minigames.onlyDescription.GameActivity_OnlyDescription;
import de.lmu.treeapp.activities.minigames.takePicture.GameActivity_TakePicture;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentData.DataManager;

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
        final Minigame_Base game = DataManager.getInstance(context).GetMinigame(games.get(position));
        final Tree tree = DataManager.getInstance(context).GetTree(treeId);
        if (tree.changeable.IsGameCompleted(category, game.uid)){
            holder.gameIcon.setImageResource(R.drawable.ic_quiz_checked);
            holder.gameIcon.setBackgroundResource(R.drawable.white_background);
        }
        holder.gameName.setText(game.name);

        holder.gameIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                switch(game.type){
                    case ChooseAnswer:
                        Intent intent = new Intent(context, GameActivity_ChooseAnswer.class);
                        intent.putExtra("TreeId",treeId);
                        intent.putExtra("Category", category);
                        intent.putExtra("GameId",game.uid);
                        context.startActivity(intent);
                        break;
                    case InputString:
                        Intent intent2 = new Intent(context, GameActivity_InputString.class);
                        intent2.putExtra("TreeId",treeId);
                        intent2.putExtra("Category", category);
                        intent2.putExtra("GameId",game.uid);
                        context.startActivity(intent2);
                        break;
                    case TakePicture:
                        Intent intent3 = new Intent(context, GameActivity_TakePicture.class);
                        intent3.putExtra("TreeId",treeId);
                        intent3.putExtra("Category", category);
                        intent3.putExtra("GameId",game.uid);
                        context.startActivity(intent3);
                        break;
                    case DragDrop:
                        Intent intent4 = new Intent(context, GameActivity_DragDrop.class);
                        intent4.putExtra("TreeId",treeId);
                        intent4.putExtra("Category", category);
                        intent4.putExtra("GameId",game.uid);
                        context.startActivity(intent4);
                        break;
                    case OnlyDescription:
                        Intent intent5 = new Intent(context, GameActivity_OnlyDescription.class);
                        intent5.putExtra("TreeId",treeId);
                        intent5.putExtra("Category", category);
                        intent5.putExtra("GameId",game.uid);
                        context.startActivity(intent5);
                        break;
                    default:
                        break;
                }
            }

        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return games.size();
    }
}
