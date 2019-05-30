package by.andreisergeichyk.voting.repository;

import by.andreisergeichyk.voting.entity.UserTopic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTopicRepository extends CrudRepository<UserTopic, Long> {
}
