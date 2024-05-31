package org.vvamp.ingenscheveer.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    @JsonProperty("PositionReport")
    public PositionReport positionReport;
}
