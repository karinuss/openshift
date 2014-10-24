package ru.karinuss.posttrackparser;

/**
 *
 * @author karina
 */
public class InvalidTrackNumberException extends Exception {

    private String number;

    public InvalidTrackNumberException(String number) {
        super("Invalid number " + number + "!");
        this.number = number;
        
    }
    
    @Override
    public String toString() {
        return "Invalid Track number " + number + "!";
    }
    
    
}
