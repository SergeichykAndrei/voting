package by.andreisergeichyk.voting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TopicDto {

    private Long id;
    private String name;
    private Integer numberOfVoices;
}
