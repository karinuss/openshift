/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.karinuss.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import ru.karinuss.posttrackparser.InvalidTrackNumberException;
import ru.karinuss.posttrackparser.TrackNumber;

/**
 *
 * @author karina
 */
@ManagedBean(name = "trackBean")
//@RequestScoped 
@SessionScoped
public class PostTrackBean implements Serializable {

    private TrackNumber track;
    private boolean checked;
   
    public PostTrackBean() {

        this.track = new TrackNumber();
        this.checked = false;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public String getTrack() {
        if(track.getNumber().isEmpty()) {
           // return "TrackNumber is empty";
            return "";
        }
        
        return track.getNumber();
    }

    public void setTrack(String number) {

     //   synchronized(this) {
            this.checked = false;
            this.track = new TrackNumber(number);

            if (track.getNumber().isEmpty()) {
                return;
            }
            try {
                this.track.parseNumber();
                this.checked = true;
            } catch (InvalidTrackNumberException ex) {
            }
    //    }
    }
//
//    public String trackInfo() {
//
//        if(track.getNumber().isEmpty()) {
//            return "";
//        }
//        
//        try {
//           this.track.parseNumber();
//        } catch(InvalidTrackNumberException ex) {
//            return ex.toString();
//        }  
//        
//        return track.toString();
//    } 
    
    public String getTrackCountry() {

        return (!checked) ? "" : this.track.getCountry();   
    }
    
    public String getTrackNumber() {
        return (!checked) ? "" : this.track.getNumber();   
    }
    
    public String getTrackType() {
        return (!checked) ? "" : this.track.getCarrier().getType();   
    }
    
    public String getTrackCarrier() {
        return (!checked) ? "" : this.track.getCarrier().getName();   
    }
    
    public String getTrackWeblink() {
        return (!checked) ? "" : this.track.getCarrier().getWebtrack();   
    }
}
