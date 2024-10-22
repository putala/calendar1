package org.example.calendar1;

import java.time.ZonedDateTime;

public class CalendarActivity {
    private ZonedDateTime date;
    private String name;
    private Integer number;

    public CalendarActivity(ZonedDateTime date, String name, Integer number) {
        this.date = date;
        this.name = name;
        this.number = number;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "CalenderActivity{" +
                "date = " + date +
                ", name = " + name +
                ", number = " + number +
                "}";
    }

}
