package de.lmu.treeapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.lmu.treeapp.R;
import de.lmu.treeapp.adapter.LicenseRecyclerViewAdapter;
import de.lmu.treeapp.adapter.grouped.AbstractListItem;
import de.lmu.treeapp.adapter.grouped.HeaderListItem;
import de.lmu.treeapp.licenses.LicenseInfo;
import de.lmu.treeapp.licenses.LicenseRoot;
import de.lmu.treeapp.licenses.LicenseSection;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LicenseActivity extends AppCompatActivity implements LicenseRecyclerViewAdapter.ItemClickListener {
    private List<AbstractListItem> licensesAdapterList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.licenses_title);
        setContentView(R.layout.activity_license);

        LicenseRoot licenseRoot = readYaml("licenses.yml");
        if (licenseRoot == null) {
            Log.e("license", "License file (licenses.yml) missing, wrong formatted or empty, please fix!");
        } else {
            addLicenseSection(licenseRoot.libraries);
            addLicenseSection(licenseRoot.media);
        }

        RecyclerView recyclerView = findViewById(R.id.libraries);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        LicenseRecyclerViewAdapter recyclerViewAdapter = new LicenseRecyclerViewAdapter(licensesAdapterList, this);
        recyclerViewAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void addLicenseSection(LicenseSection section) {
        if (section != null) {
            if (section.nameResource != null) {
                licensesAdapterList.add(new HeaderListItem(getString(getResources().getIdentifier(section.nameResource, "string", getPackageName()))));
            }
            if (section.generated != null) {
                for (LicenseInfo licenseInfo : section.generated) {
                    licensesAdapterList.add(new LicenseRecyclerViewAdapter.LicenseListItem(licenseInfo));
                }
            }
            if (section.custom != null) {
                for (LicenseInfo licenseInfo : section.custom) {
                    licensesAdapterList.add(new LicenseRecyclerViewAdapter.LicenseListItem(licenseInfo));
                }
            }
        }
    }

    public LicenseRoot readYaml(final String name) {
        try {
            InputStream stream = getResources().getAssets().open(name);
            final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(stream, LicenseRoot.class);
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
