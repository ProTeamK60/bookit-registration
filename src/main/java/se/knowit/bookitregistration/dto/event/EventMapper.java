package se.knowit.bookitregistration.dto.event;

import se.knowit.bookitregistration.model.event.Event;

import java.util.UUID;

public class EventMapper {
    private TimeSupport timeSupport;

    public EventMapper() {
        this(new TimeSupportImpl());
    }

    public EventMapper(TimeSupport timeSupport) {
        this.timeSupport = timeSupport;
    }

    public Event fromDTO(EventDTO dto) {
        Event event = new Event();
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setOrganizer(dto.getOrganizer());
        if (notNullOrBlank(dto.getEventId())) {
            event.setEventId(UUID.fromString(dto.getEventId()));
        }
        event.setEventStart(timeSupport.getInstantFromEpochMilli(dto.getEventStart()));
        event.setEventEnd(timeSupport.getInstantFromEpochMilli(dto.getEventEnd()));
        event.setDeadlineRVSP(timeSupport.getInstantFromEpochMilli(dto.getDeadlineRVSP()));
        return event;
    }

    public EventDTO toDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setLocation(event.getLocation());
        dto.setOrganizer(event.getOrganizer());
        if (event.getEventId() != null) {
            dto.setEventId(event.getEventId().toString());
        }
        dto.setEventStart(timeSupport.getEpochMilliFromInstant(event.getEventStart()));
        dto.setEventEnd(timeSupport.getEpochMilliFromInstant(event.getEventEnd()));
        dto.setDeadlineRVSP(timeSupport.getEpochMilliFromInstant(event.getDeadlineRVSP()));
        return dto;
    }

    private boolean notNullOrBlank(String test) {
        return test != null && !test.isBlank();
    }

}
