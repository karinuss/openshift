package ru.karinuss.posttrackparser;

/**
 *
 * @author karina
 * 
 */
public class PostCarrier {
    private String country="undefined", name = "ordinary post", webtrack = "", type;

    public PostCarrier() {
    }
    
    public PostCarrier(String type) {
        this.type = type;
    }
    
    public PostCarrier(String type, String name) {
        this.type = type;
        this.name = name;
    }
    
    public PostCarrier(String type, String name, String webtrack) {
        this.type = type;
        this.name = name;
        this.webtrack = webtrack;
    }

    public PostCarrier(String type, String name, String country, String webtrack) {
        this.type = type;
        this.name = name;
        this.name = country;
        this.webtrack = webtrack;
    }
    
    public String getName() {
        return name;
    }

    public String getWebtrack() {
        return webtrack;
    }
    
    public String getCountry() {
        return country;
    }

    public String getType() {
        return type;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWebtrack(String webtrack) {
        this.webtrack = webtrack;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ("from " + country + ", " + name + ", " + type.toUpperCase() + ". Tracking " + webtrack);
    }
}
