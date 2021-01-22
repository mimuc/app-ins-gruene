package de.lmu.treeapp.activities.minigames.description;

import android.content.Context;

import de.lmu.treeapp.R;

public class DescriptionElement {

    private final String text;
    private final Context context;
    final Boolean isRight;
    final Boolean isUserCreated;
    boolean isSelected;
    private RecyclerAdapter.MyViewHolder holder;

    public DescriptionElement(String text, Boolean isRight, Context context, Boolean isUserCreated) {
        this.text = text;
        this.isRight = isRight;
        this.context = context;
        this.isUserCreated = isUserCreated;
    }


    public String getText() {
        return text;
    }


    public void setCorrect(Boolean isCorrect) {
        if (isCorrect) {
            this.holder.getContent().getBackground().setTint(context.getResources().getColor(R.color.darkForest));
        } else {
            this.holder.getContent().getBackground().setTint(context.getResources().getColor(R.color.red));
        }
    }

    public void setHolder(RecyclerAdapter.MyViewHolder holder) {
        this.holder = holder;
    }

    public void changeSelected() {
        this.isSelected = !this.isSelected;
        if (this.isSelected) {
            this.holder.getContent().getBackground().setTint(context.getResources().getColor(R.color.beton));
        } else {
            this.holder.getContent().getBackground().setTint(context.getResources().getColor(R.color.lightAsphalt));
        }
    }
}
