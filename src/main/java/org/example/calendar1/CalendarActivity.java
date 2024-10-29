package org.example.calendar1;

import java.time.ZonedDateTime;

public class CalendarActivity {
    private Integer eventID;
    private ZonedDateTime date;
    private String clientEvent;


    public CalendarActivity(ZonedDateTime date, String clientEvent) {
        this.date = date;
        this.clientEvent = clientEvent;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getClientEvent() {
        return clientEvent;
    }

    public void setClientEvent(String clientName) {
        this.clientEvent = clientName;
    }

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer serviceNo) {
        this.eventID = serviceNo;
    }

    @Override
    public String toString() {
        return "CalenderActivity{" +
                ", event=" + eventID +
                "date=" + date +
                ", clientEvent='" + clientEvent + '\'' +
                '}';
    }

}
