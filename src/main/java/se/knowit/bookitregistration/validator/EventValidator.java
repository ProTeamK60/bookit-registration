package se.knowit.bookitregistration.validator;

import se.knowit.bookitregistration.model.event.Event;

import java.time.Instant;
import java.util.Objects;

public class EventValidator {

    private static final String RSVP_ERROR_TEMPLATE = "RVSP of Event at '%s' is set after start of Event at '%s";
    private static final String END_TIME_ERROR_TEMPLATE = "End of Event at '%s' is set before start of Event at '%s";

    public Event ensureEventIsValidOrThrowException(Event incomingEvent) {
        Event event = Objects.requireNonNull(incomingEvent, "Event argument must not be null");
        ensureNameIsSet(event);
        ensureStartTimeIsSet(event);
        ensureDateTimeSettingsAreCorrect(event);
        return event;
    }


    private void ensureNameIsSet(Event event) {
        if (event.getName() == null || event.getName().isBlank()) {
            throw new IllegalArgumentException("Event must have a name");
        }
    }

    private void ensureStartTimeIsSet(Event event) {
        if (event.getEventStart() == null) {
            throw new IllegalArgumentException("Event must have a start time");
        }
    }

    private void ensureDateTimeSettingsAreCorrect(Event event) {
        ensureEndIsAfterStart(event);
        ensureRsvpIsBeforeStart(event);
    }

    private void ensureEndIsAfterStart(Event event) {
        Instant eventStart = event.getEventStart();
        Instant eventEnd = event.getEventEnd();
        if (eventEnd != null && eventStart.isAfter(eventEnd)) {
            throw new IllegalArgumentException(String.format(END_TIME_ERROR_TEMPLATE, eventEnd, eventStart));
        }
    }

    private void ensureRsvpIsBeforeStart(Event event) {
        Instant eventStart = event.getEventStart();
        Instant deadlineRVSP = event.getDeadlineRVSP();
        if (deadlineRVSP != null && eventStart.isBefore(deadlineRVSP)) {
            throw new IllegalArgumentException(String.format(RSVP_ERROR_TEMPLATE, deadlineRVSP, eventStart));
        }
    }

}
