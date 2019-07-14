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
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentData.DataManager;

public class GameselectionRecyclerViewAdapter extends RecyclerView.Adapter<GameselectionRecyclerViewAdapter.ViewHolder> {

    List<Integer> games;

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

    public GameselectionRecyclerViewAdapter(List<Integer> gameIds) {
        this.games = gameIds;
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
        holder.gameName.setText(game.name);


        holder.gameIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
               /* Intent intent = new Intent(context,WantedPosterDetailsActivity.class);
                intent.putExtra("GameId",game.uid);
                context.startActivity(intent);*/
            }

        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return games.size();
    }
}
