package org.vvamp.ingenscheveer.models.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"MMSI", "MMSI_String", "ShipName", "latitude", "longitude", "time_utc"})
public class MetaData {
    @JsonProperty("MMSI")
    private int MMSI;
    @JsonProperty("MMSI_String")
    private String MMSI_String;
    @JsonProperty("ShipName")
    private String ShipName;
    @JsonProperty("latitude")
    private float latitude;
    @JsonProperty("longitude")
    private float longitude;
    @JsonProperty("time_utc")
    private String time_utc;

    public int getMMSI() {
        return MMSI;
    }

    public void setMMSI(int MMSI) {
        this.MMSI = MMSI;
    }

    public String getMMSI_String() {
        return MMSI_String;
    }

    public void setMMSI_String(String MMSI_String) {
        this.MMSI_String = MMSI_String;
    }

    public String getShipName() {
        return ShipName;
    }

    public void setShipName(String shipName) {
        ShipName = shipName;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getTime_utc() {
        return time_utc;
    }

    public void setTime_utc(String time_utc) {
        this.time_utc = time_utc;
    }
}
