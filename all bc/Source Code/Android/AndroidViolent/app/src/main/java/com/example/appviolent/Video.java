package com.example.appviolent;

import java.io.Serializable;

public class Video implements Serializable {
    String link, time ;
    public Video (){}
    public Video(String link, String time) {
        this.link = link;
        this.time = time;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
