package de.lmu.treeapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import de.lmu.treeapp.R;
import de.lmu.treeapp.licenses.LicenseInfo;

import java.util.List;

public class LicenseRecyclerViewAdapter extends RecyclerView.Adapter<LicenseRecyclerViewAdapter.ViewHolder> {

    private final List<LicenseInfo> localDataSet;
    private ItemClickListener mClickListener;
    private Context context;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView icon;
        private final TextView name;
        private final TextView copyrightHolder;
        private final TextView license;
        private final TextView url;

        public ViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);

            icon = view.findViewById(R.id.icon);
            name = view.findViewById(R.id.name);
            copyrightHolder = view.findViewById(R.id.copyrightHolder);
            license = view.findViewById(R.id.license);
            url = view.findViewById(R.id.url);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                int adapterPosition = getAdapterPosition();
                LicenseInfo licenseInfo = localDataSet.get(adapterPosition);
                if (licenseInfo.url != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(licenseInfo.url));
                    context.startActivity(browserIntent);
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
    public LicenseRecyclerViewAdapter(List<LicenseInfo> dataSet, Context context) {
        localDataSet = dataSet;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.license_info_item, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        LicenseInfo licenseInfo = localDataSet.get(position);
        viewHolder.name.setText(licenseInfo.name);
        if (licenseInfo.url != null) {
            if (licenseInfo.url.contains("github.com")) {
                Glide.with(context).load(R.drawable.ic_baseline_github).into(viewHolder.icon);
                if (licenseInfo.copyrightHolder == null) {
                    licenseInfo.copyrightHolder = licenseInfo.url.split("/")[3];
                }
            } else if (licenseInfo.url.contains("kotlin")) {
                Glide.with(context).load(R.drawable.ic_kotlin).into(viewHolder.icon);
            } else if (licenseInfo.url.contains("android.com")) {
                Glide.with(context).load(R.drawable.ic_baseline_android).into(viewHolder.icon);
            } else if (licenseInfo.url.contains("jetbrains")) {
                Glide.with(context).load(R.drawable.ic_jetbrains).into(viewHolder.icon);
            } else {
                viewHolder.icon.setImageDrawable(null);
            }
        }
        if (licenseInfo.copyrightHolder != null) {
            viewHolder.copyrightHolder.setVisibility(View.VISIBLE);
            viewHolder.copyrightHolder.setText("By: " + licenseInfo.copyrightHolder);
        } else {
            viewHolder.copyrightHolder.setVisibility(View.GONE);
        }
        if (licenseInfo.url != null) {
            viewHolder.url.setText(licenseInfo.url);
        }
        if (licenseInfo.license != null) {
            if (licenseInfo.licenseUrl != null) {
                String licenseText = "<a href=\"" + licenseInfo.licenseUrl + "\">" + licenseInfo.license + "</a>";
                viewHolder.license.setText(HtmlCompat.fromHtml(licenseText, HtmlCompat.FROM_HTML_MODE_LEGACY));
                viewHolder.license.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                viewHolder.license.setText(licenseInfo.license);
            }
        }
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
