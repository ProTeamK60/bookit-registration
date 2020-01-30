package se.knowit.bookitregistration.dto.event;

import java.util.List;

import lombok.Data;


@Data
public class EventDTO {
    private String eventId;
    private String name;
    private String description;
    private Long eventStart;
    private Long eventEnd;
    private Long deadlineRVSP;
    private String location;
    private String organizer;
    private List<OptionDTO> options;
}
