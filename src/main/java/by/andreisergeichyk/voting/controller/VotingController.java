package by.andreisergeichyk.voting.controller;

import by.andreisergeichyk.voting.dto.UserTopicDto;
import by.andreisergeichyk.voting.dto.VotingWithStatisticDto;
import by.andreisergeichyk.voting.entity.Status;
import by.andreisergeichyk.voting.entity.Voting;
import by.andreisergeichyk.voting.exception.VotingNotFoundException;
import by.andreisergeichyk.voting.service.UserTopicService;
import by.andreisergeichyk.voting.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class VotingController {

    private VotingService votingService;
    private UserTopicService userTopicService;

    @Autowired
    public VotingController(VotingService votingService,
                            UserTopicService userTopicService) {
        this.votingService = votingService;
        this.userTopicService = userTopicService;
    }

    @GetMapping("/voting/{id}/start")
    public ResponseEntity<ResourceSupport> startVote(@PathVariable Long id) {
        ResourceSupport resourceSupport = new ResourceSupport();
        resourceSupport.add(linkTo(methodOn(VotingController.class).getStatistic(id)).withRel("statistic"));

        return ResponseEntity.ok(resourceSupport);
    }

    @PostMapping("/voting")
    public ResponseEntity<Voting> newVotingBulletin(@RequestBody Voting voting) {
        voting.setStatus(Status.ACTIVE);
        Voting newVoting = votingService.save(voting);

        return newVoting.getId()
                .map(id -> ResponseEntity.created(
                        linkTo(methodOn(VotingController.class).startVote(id)).toUri()).body(newVoting))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/voting/{id}/close")
    public ResponseEntity<?> closeVote(@PathVariable("id") Long id) {
        Voting voting = votingService.findById(id).orElseThrow(VotingNotFoundException::new);

        if (voting.getStatus() == Status.ACTIVE) {
            voting.setStatus(Status.CLOSED);

            return ResponseEntity.ok(votingService.save(voting));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed",
                        "You can't close an voting that is in the " + voting.getStatus() + " status"));
    }

    @PostMapping("/voting/vote")
    public ResponseEntity<?> registrationVote(@RequestBody UserTopicDto userTopicDto) {
        Voting voting = votingService.findByTopicId(userTopicDto.getTopicId())
                .orElseThrow(VotingNotFoundException::new);

        if (voting.getStatus() == Status.ACTIVE) {
            userTopicService.save(userTopicDto);

            return votingService.findByTopicId(userTopicDto.getTopicId())
                    .map(VotingWithStatisticDto::new)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed",
                        "You can't close an voting bulletin that is in the " + voting.getStatus() + " status"));
    }

    @GetMapping("/voting/{id}/statistic")
    public ResponseEntity<VotingWithStatisticDto> getStatistic(@PathVariable Long id) {
        return votingService.findById(id)
                .map(VotingWithStatisticDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/voting/{id}")
    public ResponseEntity<Voting> findVoting(@PathVariable Long id) {
        return votingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
