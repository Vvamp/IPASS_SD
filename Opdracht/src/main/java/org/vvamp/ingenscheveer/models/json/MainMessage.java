package org.vvamp.ingenscheveer.models.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MainMessage {
    @JsonProperty("Message")
    public Message message;

    @JsonProperty("MessageType")
    public MessageType messageType = MessageType.Unknown;

    @JsonProperty("MetaData")
    public MetaData metaData;
}
