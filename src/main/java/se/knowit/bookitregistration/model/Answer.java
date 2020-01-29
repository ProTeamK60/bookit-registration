package se.knowit.bookitregistration.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@AllArgsConstructor

public class Answer {
  
  @Id
  private Long id;
  private Integer optionId;
  private String value;
}
