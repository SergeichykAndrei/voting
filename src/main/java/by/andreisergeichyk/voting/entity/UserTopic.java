package by.andreisergeichyk.voting.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_topic", schema = "voting_storage")
public class UserTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    public Optional<Long> getId() {
        return Optional.ofNullable(this.id);
    }
}
