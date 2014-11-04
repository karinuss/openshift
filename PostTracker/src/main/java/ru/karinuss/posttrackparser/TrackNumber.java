package ru.karinuss.posttrackparser;

import java.util.Locale;

/**
 *
 * @author karina
 */
public class TrackNumber {

    private String number;
    private PostCarrier carrier;

    public TrackNumber() {
        this.number = "";
        this.carrier = new PostCarrier();
    }
    
    public TrackNumber(String number) {
        this();
        this.number = number.trim().toUpperCase(Locale.US);
    }

    public TrackNumber(String number, PostCarrier carrier) {
        this(number);
        this.carrier = carrier;
    }

    public PostCarrier getCarrier() {
        return carrier;
    }

    public String getNumber() {
        return number;
    }

    public String getCountry() {
        return carrier.getCountry();
    }

    public void parseNumber() throws InvalidTrackNumberException {
        PostTrackParser parser = PostTrackParser.getInstance();

        try {
            carrier = parser.parseNumber(number);
        } catch (InvalidTrackNumberException ex) {
            // System.out.println(ex.toString());
            throw new InvalidTrackNumberException(number);
        }
    }

    @Override
    public String toString() {
        
        if(number.isEmpty()) {
            return number;
        }
        
        String msg = "TrackNumber" + "[" + number + "]";
         
        if(carrier != null) {
            msg += " " + carrier;
        }
             
        return msg;
    }

    public static void main(String[] args) {
        //TrackNumber t = new TrackNumber(" fg769937382cN ");
        TrackNumber t = new TrackNumber("RC771445333CN");
     
        try {
            t.parseNumber();
            System.out.println(t);
        } catch(InvalidTrackNumberException ex) {
            System.out.println(ex.toString());
        }  
    }
}
