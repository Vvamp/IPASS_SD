package org.vvamp.ingenscheveer.models.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class AisSignal {
    @JsonProperty("Message")
    public Message message;

    @JsonProperty("MessageType")
    public MessageType messageType = MessageType.Unknown;

    @JsonProperty("MetaData")
    public MetaData metaData;

    @JsonIgnore
    public Date getUtcDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnnnnn Z z");
        try {
            ZonedDateTime dateTime = ZonedDateTime.parse(metaData.time_utc, formatter);
            return Date.from(dateTime.toInstant());
        } catch (Exception e) {
            return Date.from(Instant.EPOCH);
        }
    }

    @JsonIgnore
    public int getUtcTimestamp(){
        String unclean = metaData.time_utc;
        String clean = unclean.replaceAll("\\s\\+[0-9]+\\sUTC", "").replace(" ", "T");
        LocalDateTime ltd = LocalDateTime.parse(clean);
        return (int) ltd.toInstant(ZoneOffset.UTC).getEpochSecond();

    }
}
