package de.lmu.treeapp.licenses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LicenseSection {
    @JsonProperty
    public String nameResource;
    @JsonProperty
    public List<LicenseInfo> generated;
    @JsonProperty
    public List<LicenseInfo> custom;
}
