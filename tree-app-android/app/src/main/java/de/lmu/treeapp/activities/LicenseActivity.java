package de.lmu.treeapp.activities;

import android.os.Bundle;
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
import de.lmu.treeapp.licenses.LicenseInfo;
import de.lmu.treeapp.licenses.LicenseRoot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LicenseActivity extends AppCompatActivity implements LicenseRecyclerViewAdapter.ItemClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.licenses_title);
        setContentView(R.layout.activity_license);

        LicenseRoot licenseInfos = readYaml("licenses.yml");
        List<LicenseInfo> completeList = new ArrayList<>();
        if (licenseInfos.generated != null) {
            completeList.addAll(licenseInfos.generated);
        }
        if (licenseInfos.custom != null) {
            completeList.addAll(licenseInfos.custom);
        }

        RecyclerView recyclerView = findViewById(R.id.licenses);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        LicenseRecyclerViewAdapter recyclerViewAdapter = new LicenseRecyclerViewAdapter(completeList, this);
        recyclerViewAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
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
