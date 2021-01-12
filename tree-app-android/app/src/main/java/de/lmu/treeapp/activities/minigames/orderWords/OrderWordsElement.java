package de.lmu.treeapp.activities.minigames.orderWords;

import android.content.Context;

import de.lmu.treeapp.R;

public class OrderWordsElement {

    private final String text;
    private final Context context;
    private RecyclerAdapter.MyViewHolder holder;

    public OrderWordsElement(String text, Context context) {
        this.text = text;
        this.context = context;
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

}
