package de.lmu.treeapp.licenses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LicenseInfo {
    @JsonProperty
    public String artifact;

    @JsonProperty
    public String name;

    @JsonProperty
    public String copyrightHolder;

    @JsonProperty
    public String license;

    @JsonProperty
    public String licenseUrl;

    @JsonProperty
    public String url;
}
