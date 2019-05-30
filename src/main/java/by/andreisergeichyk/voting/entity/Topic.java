package by.andreisergeichyk.voting.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "topic", schema = "voting_storage")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "voting_id")
    private Voting voting;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_topic", schema = "voting_storage",
            joinColumns = {@JoinColumn(name = "topic_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users = new HashSet<>();

    public Topic(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(this.id);
    }
}
