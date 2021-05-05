package de.lmu.treeapp.adapter.grouped;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import de.lmu.treeapp.R;

public class HeaderViewHolder extends AbstractViewHolder {
    public final TextView title;

    public HeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
    }

    @Override
    public int getType() {
        return AbstractListItem.TYPE_HEADER;
    }
}
