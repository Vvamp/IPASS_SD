package org.vvamp.ingenscheveer.models.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    @JsonProperty("PositionReport")
    public PositionReport positionReport;
}
