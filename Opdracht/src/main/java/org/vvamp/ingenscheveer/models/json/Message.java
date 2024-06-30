package org.vvamp.ingenscheveer.models.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message extends PositionReport {
    @JsonProperty("PositionReport")
    public PositionReport positionReport;
}
