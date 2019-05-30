package by.andreisergeichyk.voting.service;

import by.andreisergeichyk.voting.entity.Topic;
import by.andreisergeichyk.voting.entity.Voting;
import by.andreisergeichyk.voting.repository.TopicRepository;
import by.andreisergeichyk.voting.repository.VotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class VotingService {

    private final VotingRepository votingRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public VotingService(VotingRepository votingRepository,
                         TopicRepository topicRepository) {
        this.votingRepository = votingRepository;
        this.topicRepository = topicRepository;
    }

    public Voting save(Voting voting) {
        Voting newVoting = votingRepository.save(voting);
        voting.getTopics().forEach(topic -> topic.setVoting(newVoting));
        Iterable<Topic> topics = topicRepository.saveAll(voting.getTopics());

        newVoting.setTopics(StreamSupport.stream(topics.spliterator(), false)
                .collect(Collectors.toList()));

        return newVoting;
    }

    public Optional<Voting> findById(long id) {
        return votingRepository.findById(id);
    }

    public Optional<Voting> findByTopicId(Long topicId) {
        return votingRepository.findByTopicId(topicId);
    }
}
