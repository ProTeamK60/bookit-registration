package se.knowit.bookitregistration.dto.event;

import se.knowit.bookitregistration.model.Option;
import se.knowit.bookitregistration.model.event.Event;

import java.util.UUID;
import java.util.stream.Collectors;

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
        event.setMaxNumberOfApplicants(dto.getMaxNumberOfApplicants());
        if (notNullOrBlank(dto.getEventId())) {
            event.setEventId(UUID.fromString(dto.getEventId()));
        }
        event.setEventStart(timeSupport.getInstantFromEpochMilli(dto.getEventStart()));
        event.setEventEnd(timeSupport.getInstantFromEpochMilli(dto.getEventEnd()));
        event.setDeadlineRVSP(timeSupport.getInstantFromEpochMilli(dto.getDeadlineRVSP()));
        if (null != dto.getOptions()) {
          event.setOptions(dto.getOptions().stream().map(e -> {return new Option(event.getId(), e.getOptionId(), e.getOptionType(), e.getTitle(), e.getQueryString());}).collect(Collectors.toList()));
        }
        return event;
    }

    public EventDTO toDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setLocation(event.getLocation());
        dto.setOrganizer(event.getOrganizer());
        dto.setMaxNumberOfApplicants(event.getMaxNumberOfApplicants());
        if (event.getEventId() != null) {
            dto.setEventId(event.getEventId().toString());
        }
        dto.setEventStart(timeSupport.getEpochMilliFromInstant(event.getEventStart()));
        dto.setEventEnd(timeSupport.getEpochMilliFromInstant(event.getEventEnd()));
        dto.setDeadlineRVSP(timeSupport.getEpochMilliFromInstant(event.getDeadlineRVSP()));
        if (null != event.getOptions()) {
          dto.setOptions(event.getOptions().stream().map(e -> {return new OptionDTO(e.getOptionId(), e.getOptionType(), e.getTitle(), e.getQueryString());}).collect(Collectors.toList()));
        }
        return dto;
    }

    private boolean notNullOrBlank(String test) {
        return test != null && !test.isBlank();
    }

}
