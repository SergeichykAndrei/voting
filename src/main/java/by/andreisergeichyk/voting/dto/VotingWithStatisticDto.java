package by.andreisergeichyk.voting.dto;

import by.andreisergeichyk.voting.entity.Status;
import by.andreisergeichyk.voting.entity.Voting;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
@JsonPropertyOrder({"id", "name", "status", "topics"})
public class VotingWithStatisticDto {

    @JsonIgnore
    private final Voting voting;

    public Long getId() {
        return this.voting.getId()
                .orElseThrow(() -> new RuntimeException("Couldn't find anything."));
    }

    public String getName() {
        return this.voting.getName();
    }

    public Status getStatus() {
        return this.voting.getStatus();
    }

    public List<TopicDto> getTopics() {
        return this.voting.getTopics().stream()
                .map(it -> TopicDto.builder()
                        .id(it.getId().get())
                        .name(it.getName())
                        .numberOfVoices(it.getUsers().size())
                        .build())
                .collect(Collectors.toList());
    }
}
