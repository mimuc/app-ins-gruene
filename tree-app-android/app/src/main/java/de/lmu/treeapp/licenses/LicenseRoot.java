package de.lmu.treeapp.licenses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LicenseRoot {
    @JsonProperty
    public List<LicenseInfo> generated;
    public List<LicenseInfo> custom;
}
