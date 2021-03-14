package de.lmu.treeapp.activities.minigames.takePicture.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentData.database.AppDatabase;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureImage;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureRelations;
import de.lmu.treeapp.utils.glide.BackgroundTarget;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TakePictureRecyclerViewAdapter extends RecyclerView.Adapter<TakePictureRecyclerViewAdapter.ViewHolder> {

    View.OnClickListener listener;
    GameStateTakePictureRelations gameStateRelations;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageButton imageItem;
        public final ImageView badgeButton;

        ViewHolder(View v) {
            super(v);
            imageItem = v.findViewById(R.id.image);
            badgeButton = v.findViewById(R.id.badge);
        }
    }

    public TakePictureRecyclerViewAdapter(GameStateTakePictureRelations gameStateRelations, View.OnClickListener listener) {
        this.listener = listener;
        this.gameStateRelations = gameStateRelations;
    }

    public void add(GameStateTakePictureImage item) {
        int position = getItemCount();
        add(position, item);
    }

    public void add(int position, GameStateTakePictureImage item) {
        GameStateTakePictureImage selectedImage = gameStateRelations.getSelectedImage();
        if (selectedImage != null) {
            notifyItemChanged(gameStateRelations.images.indexOf(selectedImage));
        }
        gameStateRelations.images.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if (gameStateRelations.gameState.selectedImageId != null && gameStateRelations.gameState.selectedImageId == gameStateRelations.images.get(position).id) {
            // Unselect when delete
            gameStateRelations.gameState.selectedImageId = null;
        }
        gameStateRelations.images.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public TakePictureRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.take_picture_image_item_layout, parent, false);
        return new TakePictureRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TakePictureRecyclerViewAdapter.ViewHolder holder, final int position) {
        final Context context = holder.imageItem.getContext();
        GameStateTakePictureImage gameStateTakePictureImage = gameStateRelations.images.get(position);

        Glide.with(context).load(gameStateTakePictureImage.imagePath).into(new BackgroundTarget(holder.imageItem));
        if (gameStateTakePictureImage.id == gameStateRelations.getSelectedImageId()) {
            holder.imageItem.setAlpha(0.5f);
            holder.imageItem.setPadding(32, 32, 32, 32);
            Glide.with(context).load(R.drawable.ic_completed).into(holder.imageItem);
        } else {
            holder.imageItem.setAlpha(1f);
            Glide.with(context).clear(holder.imageItem);
        }

        holder.imageItem.setOnClickListener(view -> {
            GameStateTakePictureImage selectedImage = gameStateRelations.getSelectedImage();
            if (selectedImage != null) {
                notifyItemChanged(gameStateRelations.images.indexOf(selectedImage));
            }
            // Select / Unselect image
            if (gameStateRelations.gameState.selectedImageId != null && gameStateRelations.gameState.selectedImageId == gameStateTakePictureImage.id) {
                gameStateRelations.gameState.selectedImageId = null;
            } else {
                gameStateRelations.gameState.selectedImageId = gameStateTakePictureImage.id;
            }
            listener.onClick(view);
            notifyItemChanged(position);
        });

        holder.badgeButton.setOnClickListener(view -> {
            remove(position);
            File file = new File(gameStateTakePictureImage.imagePath);
            boolean deleted = file.delete();
            AppDatabase.getInstance(context).gameStateTakePictureImageDao().deleteOne(gameStateTakePictureImage).subscribeOn(Schedulers.io()).subscribe();
            listener.onClick(view);
        });
    }

    @Override
    public int getItemCount() {
        return gameStateRelations.images.size();
    }
}
