/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.karinuss.beans;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ru.karinuss.posttrackparser.PostTrackParser;

@FacesValidator("ru.karinuss.beans.TrackNumberValidator")
public class TrackNumberValidator implements Validator {

    private static final String patternTrack = "^[a-zA-Z]{2}\\d{9}[a-zA-Z]{2}$";
    private static final Pattern pattern = Pattern.compile(patternTrack);
    private static Matcher matcher;
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        
        String number = value.toString();
        
        if(number.trim().isEmpty()) {
            FacesMessage msg = new FacesMessage("Track number is empty!");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        
        matcher = pattern.matcher(number);
        
        if(!matcher.matches()) {
            FacesMessage msg = new FacesMessage("Invalid track number format!");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        
        PostTrackParser parser = PostTrackParser.getInstance();
        if(!parser.validDigits(number)) {    
            FacesMessage msg = new FacesMessage("Invalid CRC code!");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
    
}
