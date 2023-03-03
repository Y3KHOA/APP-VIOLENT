package com.example.appviolent;

import java.io.Serializable;

public class NotificationClass implements Serializable {
    //name-time
    String content , time_notification, link_video, time_video ;

    public NotificationClass(String content, String time_notification, String link_video, String time_video) {
        this.content = content;
        this.time_notification = time_notification;
        this.link_video = link_video;
        this.time_video = time_video;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime_notification() {
        return time_notification;
    }

    public void setTime_notification(String time_notification) {
        this.time_notification = time_notification;
    }

    public String getLink_video() {
        return link_video;
    }

    public void setLink_video(String link_video) {
        this.link_video = link_video;
    }

    public String getTime_video() {
        return time_video;
    }

    public void setTime_video(String time_video) {
        this.time_video = time_video;
    }


}
