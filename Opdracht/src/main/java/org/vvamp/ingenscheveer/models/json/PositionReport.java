package org.vvamp.ingenscheveer.models;
import com.fasterxml.jackson.annotation.JsonProperty;
public class PositionReport {
    @JsonProperty("Cog")
    private float cog;

    @JsonProperty("CommunicationState")
    private int communicationState;

    @JsonProperty("Latitude")
    private float latitude;

    @JsonProperty("Longitude")
    private float longitude;

    @JsonProperty("MessageID")
    private int messageID;

    @JsonProperty("NavigationalStatus")
    private int navigationalStatus;

    @JsonProperty("PositionAccuracy")
    private boolean positionAccuracy;

    @JsonProperty("Raim")
    private boolean raim;

    @JsonProperty("RateOfTurn")
    private int rateOfTurn;

    @JsonProperty("RepeatIndicator")
    private int repeatIndicator;

    @JsonProperty("Sog")
    private float sog;

    @JsonProperty("Spare")
    private int spare;

    @JsonProperty("SpecialManoeuvreIndicator")
    private int specialManoeuvreIndicator;

    @JsonProperty("Timestamp")
    private int timestamp;

    @JsonProperty("TrueHeading")
    private int trueHeading;

    @JsonProperty("UserID")
    private int userID;

    @JsonProperty("Valid")
    private boolean valid;

}
