package de.lmu.treeapp.licenses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LicenseRoot {
    @JsonProperty
    public LicenseSection libraries;
    @JsonProperty
    public LicenseSection media;
    @JsonProperty
    public LicenseSection fonts;
}
