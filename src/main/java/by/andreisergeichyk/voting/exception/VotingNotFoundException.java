package by.andreisergeichyk.voting.exception;

public class VotingNotFoundException extends RuntimeException {

    public VotingNotFoundException() {
        super("Could not find voting");
    }
}
