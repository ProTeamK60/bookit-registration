package se.knowit.bookitregistration.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;

import lombok.Data;

@Data
@Entity
@AllArgsConstructor
public class Option {
  
  @Id
  private Long id;
  private Integer optionId;
  private String optionType;
  private String title;
  private String queryString;
}
