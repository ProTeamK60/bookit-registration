package se.knowit.bookitnotification.dto;

import lombok.Data;
import se.knowit.bookitregistration.dto.ParticipantDTO;

@Data
public class NotificationDTO {
    String eventId;
    ParticipantDTO participant;
}
