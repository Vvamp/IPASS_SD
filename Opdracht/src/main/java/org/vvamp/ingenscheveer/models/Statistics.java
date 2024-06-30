package org.vvamp.ingenscheveer.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.vvamp.ingenscheveer.models.json.AisSignal;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Statistics {
    private final List<FerryCrossing> crossings;
    private final int current_time;

    public Statistics() {
        crossings = Ferry.getFerry().getFerryCrossings(1440); //1440 24h limit
        current_time = (int) Instant.now().getEpochSecond();
    }

    @JsonProperty("CurrentSpeed")
    public double getCurrentSpeed() {
        FerryCrossing mostRecentFC = crossings.stream().findFirst().orElse(null);
        if (mostRecentFC == null) {
            return -1;
        }

        AisSignal mostRecentSignal = crossings.stream().flatMap(c -> c.getAisSignals().stream()).sorted((s1, s2) -> Long.compare(s2.getUtcTimestamp(), s1.getUtcTimestamp())).findFirst().orElse(null);
        if (mostRecentSignal == null) {
            return -1;
        }

        return mostRecentSignal.message.positionReport.sog;
    }

    @JsonProperty("AverageSpeed")
    public double getAverageSpeed() {
        int limit = current_time - (60 * 60 * 24);
        // Get all ais signals from all crossings where timestamp > now-24h
        List<AisSignal> signals = crossings.stream().flatMap(c -> c.getAisSignals().stream()).filter(signal -> signal.getUtcTimestamp() > limit && signal.message.positionReport.sog
        > 0).distinct().collect(Collectors.toList());

        if(signals.size() <= 0){
            return -1;
        }

        double sum = 0;
        for(AisSignal signal : signals){
            sum += signal.message.positionReport.sog;
        }
        double avg = sum/ signals.size();
        return avg;
    }

    @JsonProperty("CrossingCount")
    public int getCrossingCount() {
        int limit = current_time - (60 * 60); // 1 hour
        List<FerryCrossing> lastCrossings = crossings.stream().filter(crossing -> crossing.getDeparture().getEpochSeconds() > limit).collect(Collectors.toList());
        return lastCrossings.size();
    }

    @JsonProperty("LastUpdate")
    public Date getLatestUpdate() {
        AisSignal mostRecentSignal = crossings.stream().flatMap(c -> c.getAisSignals().stream()).sorted((s1, s2) -> Long.compare(s2.getUtcTimestamp(), s1.getUtcTimestamp())).findFirst().orElse(null);
        if (mostRecentSignal == null) {
            return Date.from(Instant.EPOCH);
        }
        return Date.from(Instant.ofEpochSecond(mostRecentSignal.getUtcTimestamp()));
    }

}
