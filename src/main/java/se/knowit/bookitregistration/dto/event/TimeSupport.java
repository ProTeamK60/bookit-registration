package se.knowit.bookitregistration.dto.event;

import java.time.Instant;

public interface TimeSupport {
    Instant getInstantFromEpochMilli(Long epochMilli);
    Long getEpochMilliFromInstant(Instant instant);
}
