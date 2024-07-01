package org.vvamp.ingenscheveer.models.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message extends PositionReport {
    @JsonProperty("PositionReport")
    private PositionReport positionReport;

    public PositionReport getPositionReport() {
        return positionReport;
    }

    public void setPositionReport(PositionReport positionReport) {
        this.positionReport = positionReport;
    }
}
