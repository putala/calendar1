package org.example.calendar1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;


public class CalendarController implements Initializable {

    ZonedDateTime date;
    ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    public Text day;

    @FXML
    private FlowPane calendar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        date = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    public void backOneMonth() {
        date = date.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    public void nextOneMonth() {
        date = date.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }



    private void drawCalendar() {
        year.setText(String.valueOf(date.getYear()));
        month.setText(String.valueOf(date.getMonth()));
        day.setText(String.valueOf(date.getDayOfMonth()));
        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        int monthMaxDate = date.getMonth().maxLength();
        int dateOffset = ZonedDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),0,0,0,0,date.getZone()).getDayOfWeek().getValue();

        if(date.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.GRAY);
                double rectangleWidth = calendarWidth/7 - 1;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = calendarHeight/6 - 1;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = 7*i+j+2;
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        stackPane.getChildren().add(date);
                    }
                    if (currentDate == date.getDayOfMonth()) {
                        rectangle.setStroke(Color.RED);
                    }
                    if (today.getYear() == date.getYear() && today.getMonth() == date.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setFill(Color.rgb(68,136,255));
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }
}

