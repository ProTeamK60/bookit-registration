package se.knowit.bookitregistration.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OptionDTO {
    private Integer optionId;
    private String optionType;
    private String title;
    private String queryString;
}
