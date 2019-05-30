package by.andreisergeichyk.voting.controller;

import by.andreisergeichyk.voting.entity.Status;
import by.andreisergeichyk.voting.entity.Topic;
import by.andreisergeichyk.voting.entity.Voting;
import by.andreisergeichyk.voting.service.UserTopicService;
import by.andreisergeichyk.voting.service.VotingService;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(VotingController.class)
public class VotingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VotingService votingService;

    @MockBean
    private UserTopicService userTopicService;

    @Test
    public void startMethodTest() throws Exception {
        mvc.perform(get("/voting/1/start").accept(MediaTypes.HAL_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.statistic.href", CoreMatchers.is("http://localhost/voting/1/statistic")));
    }

    @Test
    public void findVotingTest() throws Exception {
        Voting voting = new Voting(1L, "School meeting", Status.ACTIVE, Arrays.asList(
                new Topic(1L, "I'm going to meeting"),
                new Topic(2L, "I'm not going to meeting")
        ));

        given(votingService.findById(1L)).willReturn(Optional.of(voting));

        mvc.perform(get("/voting/1").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", CoreMatchers.is("School meeting")))
                .andExpect(jsonPath("status", CoreMatchers.is("ACTIVE")))
                .andExpect(jsonPath("topics[0].name", CoreMatchers.is("I'm going to meeting")))
                .andExpect(jsonPath("topics[1].name", CoreMatchers.is("I'm not going to meeting")));
    }

    @Test
    public void newVotingTest() throws Exception {
        Voting voting = new Voting(1L, "New voting", Status.ACTIVE, Arrays.asList(
                new Topic(1L, "topic one"),
                new Topic(2L, "topic two")
        ));

        given(votingService.save(any(Voting.class))).willReturn(voting);

        mvc.perform(post("/voting")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"New voting\",\"topics\": [{\"name\": \"topic one\"},{\"name\": \"topic two\"}]}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name", CoreMatchers.is("New voting")))
                .andExpect(jsonPath("status", CoreMatchers.is("ACTIVE")))
                .andExpect(jsonPath("topics[0].name", CoreMatchers.is("topic one")))
                .andExpect(jsonPath("topics[1].name", CoreMatchers.is("topic two")));
    }

    @Test
    public void closeVotingTest() throws Exception {
        Voting voting = Voting.builder().status(Status.ACTIVE).build();
        Voting saved = new Voting(1L, "New voting", Status.CLOSED, Arrays.asList(
                new Topic(1L, "topic one"),
                new Topic(2L, "topic two")
        ));

        given(votingService.findById(anyLong())).willReturn(Optional.of(voting));
        given(votingService.save(any(Voting.class))).willReturn(saved);

        mvc.perform(put("/voting/1/close").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("status", CoreMatchers.is("CLOSED")));
    }

    @Test
    public void getStatisticTest() throws Exception {
        Voting voting = new Voting(1L, "School meeting", Status.ACTIVE, Arrays.asList(
                new Topic(1L, "I'm going to meeting"),
                new Topic(2L, "I'm not going to meeting")
        ));

        given(votingService.findById(1L)).willReturn(Optional.of(voting));

        mvc.perform(get("/voting/1/statistic")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", CoreMatchers.is("School meeting")))
                .andExpect(jsonPath("status", CoreMatchers.is("ACTIVE")))
                .andExpect(jsonPath("topics[0].numberOfVoices", CoreMatchers.is(0)))
                .andExpect(jsonPath("topics[1].numberOfVoices", CoreMatchers.is(0)));
    }

    @Test
    public void vote() throws Exception {
        Voting voting = new Voting(1L, "School meeting", Status.ACTIVE, Arrays.asList(
                new Topic(1L, "I'm going to meeting"),
                new Topic(2L, "I'm not going to meeting")
        ));

        given(votingService.findByTopicId(1L)).willReturn(Optional.of(voting));

        mvc.perform(post("/voting/vote").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"Petr\",\"topicId\":1}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", CoreMatchers.is("School meeting")))
                .andExpect(jsonPath("status", CoreMatchers.is("ACTIVE")))
                .andExpect(jsonPath("topics[0].name", CoreMatchers.is("I'm going to meeting")))
                .andExpect(jsonPath("topics[1].name", CoreMatchers.is("I'm not going to meeting")));
    }
}