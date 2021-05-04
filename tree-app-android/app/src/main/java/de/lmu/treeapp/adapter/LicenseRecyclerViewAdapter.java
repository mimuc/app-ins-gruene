package de.lmu.treeapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import de.lmu.treeapp.R;
import de.lmu.treeapp.adapter.grouped.AbstractListItem;
import de.lmu.treeapp.adapter.grouped.AbstractViewHolder;
import de.lmu.treeapp.adapter.grouped.HeaderViewHolder;
import de.lmu.treeapp.licenses.LicenseInfo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class LicenseRecyclerViewAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private final List<AbstractListItem> localDataSet;
    private final Context context;
    private ItemClickListener mClickListener;

    public static class LicenseListItem extends AbstractListItem {
        public LicenseListItem(LicenseInfo info) {
            this.obj = info;
        }

        @Override
        public int getType() {
            return AbstractListItem.TYPE_ELEMENT;
        }
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ElementViewHolder extends AbstractViewHolder implements View.OnClickListener {
        private final ImageView icon;
        private final TextView name;
        private final TextView copyrightHolder;
        private final TextView license;
        private final TextView url;

        public ElementViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);

            icon = view.findViewById(R.id.icon);
            name = view.findViewById(R.id.name);
            copyrightHolder = view.findViewById(R.id.copyrightHolder);
            license = view.findViewById(R.id.license);
            url = view.findViewById(R.id.url);
        }

        @Override
        public int getType() {
            return AbstractListItem.TYPE_ELEMENT;
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                int adapterPosition = getAdapterPosition();
                AbstractListItem abstractListItem = localDataSet.get(adapterPosition);
                if (abstractListItem.getType() == AbstractListItem.TYPE_ELEMENT) {
                    LicenseInfo licenseInfo = (LicenseInfo) abstractListItem.obj;
                    if (licenseInfo.url != null) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(licenseInfo.url));
                        context.startActivity(browserIntent);
                    }
                }
                mClickListener.onItemClick(view, adapterPosition);
            }
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public LicenseRecyclerViewAdapter(List<AbstractListItem> dataSet, Context context) {
        localDataSet = dataSet;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        if (viewType == AbstractListItem.TYPE_ELEMENT) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.license_info_item, viewGroup, false);
            return new ElementViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recyclerview_header_item, viewGroup, false);
            return new HeaderViewHolder(view);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AbstractViewHolder viewHolder, final int position) {
        if (viewHolder.getType() == AbstractListItem.TYPE_ELEMENT) {
            ElementViewHolder elementViewHolder = (ElementViewHolder) viewHolder;
            LicenseInfo licenseInfo = (LicenseInfo) localDataSet.get(position).obj;
            elementViewHolder.name.setText(licenseInfo.name);
            if (licenseInfo.url != null) {
                try {
                    URI uri = new URI(licenseInfo.url);
                    String domain = uri.getHost();
                    if (domain.contains("github.com")) {
                        Glide.with(context).load(R.drawable.ic_baseline_github).into(elementViewHolder.icon);
                        if (licenseInfo.copyrightHolder == null) {
                            // Use Github user name as holder name
                            licenseInfo.copyrightHolder = licenseInfo.url.split("/")[3];
                        }
                    } else {
                        if (licenseInfo.copyrightHolder == null) {
                            // Use domain as holder name
                            licenseInfo.copyrightHolder = domain;
                        }
                        if (domain.contains("kotlin")) {
                            Glide.with(context).load(R.drawable.ic_kotlin).into(elementViewHolder.icon);
                        } else if (domain.contains("android.com")) {
                            Glide.with(context).load(R.drawable.ic_baseline_android).into(elementViewHolder.icon);
                        } else if (domain.contains("jetbrains")) {
                            Glide.with(context).load(R.drawable.ic_jetbrains).into(elementViewHolder.icon);
                        } else if (domain.contains("freepik")) {
                            Glide.with(context).load(R.drawable.ic_freepik_company).into(elementViewHolder.icon);
                        } else {
                            elementViewHolder.icon.setImageDrawable(null);
                        }
                    }
                } catch (URISyntaxException e) {
                    Log.w("URISyntaxException", e.getMessage());
                }
            }
            if (licenseInfo.copyrightHolder != null) {
                elementViewHolder.copyrightHolder.setVisibility(View.VISIBLE);
                elementViewHolder.copyrightHolder.setText("By: " + licenseInfo.copyrightHolder);
                if (licenseInfo.url != null) {
                    elementViewHolder.url.setVisibility(View.GONE);
                }
            } else {
                elementViewHolder.copyrightHolder.setVisibility(View.GONE);
                if (licenseInfo.url != null) {
                    elementViewHolder.url.setVisibility(View.VISIBLE);
                    elementViewHolder.url.setText(licenseInfo.url);
                }
            }
            if (licenseInfo.license != null) {
                if (licenseInfo.licenseUrl != null) {
                    String licenseText = "<a href=\"" + licenseInfo.licenseUrl + "\">" + licenseInfo.license + "</a>";
                    elementViewHolder.license.setText(HtmlCompat.fromHtml(licenseText, HtmlCompat.FROM_HTML_MODE_LEGACY));
                    elementViewHolder.license.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    elementViewHolder.license.setText(licenseInfo.license);
                }
            }
        } else {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            String title = (String) localDataSet.get(position).obj;
            headerViewHolder.title.setText(title);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return localDataSet.get(position).getType();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
