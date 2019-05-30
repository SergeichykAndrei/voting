package by.andreisergeichyk.voting.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTopicDto {

    private Long topicId;
    private String username;
}
