package se.knowit.bookitregistration.model.event;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private UUID eventId;
    private String name;
    private String description;
    private Instant eventStart;
    private Instant eventEnd;
    private Instant deadlineRVSP;
    private String location;
    private String organizer;

    public boolean haveEventId(UUID other) {
        return Objects.equals(eventId, other);
    }
}
