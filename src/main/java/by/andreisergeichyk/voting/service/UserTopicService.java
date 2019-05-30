package by.andreisergeichyk.voting.service;

import by.andreisergeichyk.voting.dto.UserTopicDto;
import by.andreisergeichyk.voting.entity.Topic;
import by.andreisergeichyk.voting.entity.User;
import by.andreisergeichyk.voting.entity.UserTopic;
import by.andreisergeichyk.voting.repository.TopicRepository;
import by.andreisergeichyk.voting.repository.UserRepository;
import by.andreisergeichyk.voting.repository.UserTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserTopicService {

    private final UserTopicRepository userTopicRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public UserTopicService(UserTopicRepository userTopicRepository, UserRepository userRepository,
                            TopicRepository topicRepository) {
        this.userTopicRepository = userTopicRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }

    public Optional<UserTopic> save(UserTopicDto userTopicDto) {
        Optional<User> user = userRepository.findByUsername(userTopicDto.getUsername());
        Optional<Topic> topic = topicRepository.findById(userTopicDto.getTopicId());

        if (user.isPresent() && topic.isPresent()) {
            return Optional.of(userTopicRepository.save(UserTopic.builder()
                    .topic(topic.get())
                    .user(user.get())
                    .build()));
        }

        return Optional.empty();
    }
}