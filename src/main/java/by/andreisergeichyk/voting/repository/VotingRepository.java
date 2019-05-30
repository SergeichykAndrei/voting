package by.andreisergeichyk.voting.repository;

import by.andreisergeichyk.voting.entity.Voting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotingRepository extends CrudRepository<Voting, Long> {

    @Query("select vb from Voting vb join vb.topics t where t.id = :topicId")
    Optional<Voting> findByTopicId(@Param("topicId") Long topicId);
}
