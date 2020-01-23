package se.knowit.bookitregistration.dto.event;

import java.time.Instant;
import java.util.Optional;

public class TimeSupportImpl implements TimeSupport {
    @Override
    public Instant getInstantFromEpochMilli(Long epochMilli) {
        return Optional.ofNullable(epochMilli).map(Instant::ofEpochMilli).orElse(null);
    }

    @Override
    public Long getEpochMilliFromInstant(Instant instant) {
        return Optional.ofNullable(instant).map(Instant::toEpochMilli).orElse(null);
    }
}
