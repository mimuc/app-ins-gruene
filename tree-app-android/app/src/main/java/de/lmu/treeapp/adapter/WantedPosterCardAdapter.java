package de.lmu.treeapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.fragments.WantedPosterCard;

public class WantedPosterCardAdapter extends RecyclerView.Adapter<WantedPosterCardAdapter.WantedPosterCardViewHolder> {

    private final List<WantedPosterCard> wantedPosterCards;
    private int expandedPosition = -1;

    public WantedPosterCardAdapter(List<WantedPosterCard> wantedPosterCards) {
        this.wantedPosterCards = wantedPosterCards;
    }

    class WantedPosterCardViewHolder extends RecyclerView.ViewHolder {
        TextView header;
        TextView subHeader;
        ImageView picture;
        TextView infoText;
        ImageView collapseArrow;
        LinearLayout expander;

        public WantedPosterCardViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.card_wanted_poster_header);
            subHeader = itemView.findViewById(R.id.card_wanted_poster_sub_header);
            picture = itemView.findViewById(R.id.card_wanted_poster_circle_image);
            infoText  = itemView.findViewById(R.id.card_wanted_poster_description_text);
            collapseArrow = itemView.findViewById(R.id.card_wanted_poster_collapse_arrow);
            expander = itemView.findViewById(R.id.card_wanted_poster_expander);


        }
    }
    @NonNull
    @Override
    public WantedPosterCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_wanted_poster, parent, false);
        return new WantedPosterCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final WantedPosterCardViewHolder holder, final int position) {

        final WantedPosterCard card = wantedPosterCards.get(position);
        if(!card.getHeader().isEmpty()){
            holder.header.setText(card.getHeader());
        }

        if(!card.getSubHeader().isEmpty()){
            holder.subHeader.setText(card.getSubHeader());
        }

        if (!card.getInfotext().isEmpty()){
            holder.infoText.setText(card.getInfotext());
        }

        if(card.getPicture() != null){
            holder.picture.setImageDrawable(card.getPicture());
        }

        final boolean isExpanded = position==expandedPosition;
        holder.expander.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.collapseArrow.setImageDrawable(card.getCollapse());
        if(card.isUnlocked()){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.collapseArrow.getRotation() == 90.0f){
                        holder.collapseArrow.setRotation(0);
                    } else {
                        holder.collapseArrow.setRotation(-90.0f);
                    }
                    expandedPosition = isExpanded ? -1 : position;
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.collapseArrow.setRotation(0);
        }
    }

    @Override
    public int getItemCount() {
        return wantedPosterCards.size();
    }
}
