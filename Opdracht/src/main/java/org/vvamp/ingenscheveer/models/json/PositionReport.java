package org.vvamp.ingenscheveer.models.json;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"Cog", "CommunicationState", "Latitude", "Longitude", "MessageID", "NavigationalStatus", "PositionAccuracy", "Raim", "RateOfTurn", "RepeatIndiciator", "Sog", "Spare", "SpecialManoeuvreIndicator", "Timestamp", "UserID", "Valid"})
public class PositionReport {
    public PositionReport(double cog, int communicationState, double latitude, double longitude, int messageID, int navigationalStatus, boolean positionAccuracy, boolean raim, int rateOfTurn, int repeatIndicator, double sog, int spare, int specialManoeuvreIndicator, int timestamp, int trueHeading, int userID, boolean valid) {
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

    public double getCog() {
        return cog;
    }

    public void setCog(double cog) {
        this.cog = cog;
    }

    public int getCommunicationState() {
        return communicationState;
    }

    public void setCommunicationState(int communicationState) {
        this.communicationState = communicationState;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getNavigationalStatus() {
        return navigationalStatus;
    }

    public void setNavigationalStatus(int navigationalStatus) {
        this.navigationalStatus = navigationalStatus;
    }

    public boolean isPositionAccuracy() {
        return positionAccuracy;
    }

    public void setPositionAccuracy(boolean positionAccuracy) {
        this.positionAccuracy = positionAccuracy;
    }

    public boolean isRaim() {
        return raim;
    }

    public void setRaim(boolean raim) {
        this.raim = raim;
    }

    public int getRateOfTurn() {
        return rateOfTurn;
    }

    public void setRateOfTurn(int rateOfTurn) {
        this.rateOfTurn = rateOfTurn;
    }

    public int getRepeatIndicator() {
        return repeatIndicator;
    }

    public void setRepeatIndicator(int repeatIndicator) {
        this.repeatIndicator = repeatIndicator;
    }

    public double getSog() {
        return sog;
    }

    public void setSog(double sog) {
        this.sog = sog;
    }

    public int getSpare() {
        return spare;
    }

    public void setSpare(int spare) {
        this.spare = spare;
    }

    public int getSpecialManoeuvreIndicator() {
        return specialManoeuvreIndicator;
    }

    public void setSpecialManoeuvreIndicator(int specialManoeuvreIndicator) {
        this.specialManoeuvreIndicator = specialManoeuvreIndicator;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getTrueHeading() {
        return trueHeading;
    }

    public void setTrueHeading(int trueHeading) {
        this.trueHeading = trueHeading;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @JsonProperty("Cog")
    private double cog;

    @JsonProperty("CommunicationState")
    private int communicationState;

    @JsonProperty("Latitude")
    private double latitude;

    @JsonProperty("Longitude")
    private double longitude;

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
    private double sog;

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
