package com.example.appviolent;

import java.io.Serializable;

public class ItemVideoMedia implements Serializable {

    String link, time , name, urlImage;
    public ItemVideoMedia (){}
    public ItemVideoMedia(String link, String name, String time, String urlImage) {
        this.link = link;
        this.time = time;
        this.name = name;
        this.urlImage = urlImage;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}

