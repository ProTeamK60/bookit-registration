package se.knowit.bookitregistration.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Participant {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private final String email;
	
}
