package org.vvamp.ingenscheveer.models.json;
import com.fasterxml.jackson.annotation.JsonProperty;
public class PositionReport {
    public PositionReport(float cog, int communicationState, float latitude, float longitude, int messageID, int navigationalStatus, boolean positionAccuracy, boolean raim, int rateOfTurn, int repeatIndicator, float sog, int spare, int specialManoeuvreIndicator, int timestamp, int trueHeading, int userID, boolean valid) {
        this.cog = cog;
        this.communicationState = communicationState;
        this.latitude = latitude;
        this.longitude = longitude;
        this.messageID = messageID;
        this.navigationalStatus = navigationalStatus;
        this.positionAccuracy = positionAccuracy;
        this.raim = raim;
        this.rateOfTurn = rateOfTurn;
        this.repeatIndicator = repeatIndicator;
        this.sog = sog;
        this.spare = spare;
        this.specialManoeuvreIndicator = specialManoeuvreIndicator;
        this.timestamp = timestamp;
        this.trueHeading = trueHeading;
        this.userID = userID;
        this.valid = valid;
    }
    public PositionReport(){}

    @JsonProperty("Cog")
    public float cog;

    @JsonProperty("CommunicationState")
    public int communicationState;

    @JsonProperty("Latitude")
    public float latitude;

    @JsonProperty("Longitude")
    public float longitude;

    @JsonProperty("MessageID")
    public int messageID;

    @JsonProperty("NavigationalStatus")
    public int navigationalStatus;

    @JsonProperty("PositionAccuracy")
    public boolean positionAccuracy;

    @JsonProperty("Raim")
    public boolean raim;

    @JsonProperty("RateOfTurn")
    public int rateOfTurn;

    @JsonProperty("RepeatIndicator")
    public int repeatIndicator;

    @JsonProperty("Sog")
    public float sog;

    @JsonProperty("Spare")
    public int spare;

    @JsonProperty("SpecialManoeuvreIndicator")
    public int specialManoeuvreIndicator;

    @JsonProperty("Timestamp")
    public int timestamp;

    @JsonProperty("TrueHeading")
    public int trueHeading;

    @JsonProperty("UserID")
    public int userID;

    @JsonProperty("Valid")
    public boolean valid;


}
