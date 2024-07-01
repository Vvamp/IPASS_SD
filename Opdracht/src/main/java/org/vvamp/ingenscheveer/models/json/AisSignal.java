package org.vvamp.ingenscheveer.models.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@JsonPropertyOrder({"Message", "MessageType", "MetaData"})
public class AisSignal {
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    @JsonProperty("Message")
    private Message message;

    @JsonProperty("MessageType")
    private MessageType messageType = MessageType.Unknown;

    @JsonProperty("MetaData")
    private MetaData metaData;

    @JsonIgnore
    public Date getUtcDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnnnnn Z z");
        try {
            ZonedDateTime dateTime = ZonedDateTime.parse(metaData.getTime_utc(), formatter);
            return Date.from(dateTime.toInstant());
        } catch (Exception e) {
            return Date.from(Instant.EPOCH);
        }
    }

    @JsonIgnore
    public int getUtcTimestamp() {
        String unclean = metaData.getTime_utc();
        String clean = unclean.replaceAll("\\s\\+[0-9]+\\sUTC", "").replace(" ", "T");
        LocalDateTime ltd = LocalDateTime.parse(clean);
        return (int) ltd.toInstant(ZoneOffset.UTC).getEpochSecond();

    }
}
