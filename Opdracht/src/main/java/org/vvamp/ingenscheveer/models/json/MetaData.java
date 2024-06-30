package org.vvamp.ingenscheveer.models.json;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"MMSI", "MMSI_String", "ShipName", "latitude", "longitude", "time_utc"})
public class MetaData {

    public int MMSI;
    public String MMSI_String;
    public String ShipName;
    public float latitude;
    public float longitude;
    public String time_utc;
}
