package de.lmu.treeapp.adapter.grouped;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class AbstractViewHolder extends RecyclerView.ViewHolder {
    public AbstractViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    abstract public int getType();
}
