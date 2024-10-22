package org.example.calendar1;

import javafx.event.ActionEvent;
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
    private FlowPane calendar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        date = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    public void backOneMonth(ActionEvent event) {
        date = date.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    public void nextOneMonth(ActionEvent event) {
        date = date.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar() {
        year.setText(String.valueOf(date.getYear()));
        month.setText(String.valueOf(date.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double strokeHeight = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        int monthMaxDate = date.getMonth().maxLength();

        if(date.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }

        int dateOffset = ZonedDateTime.of(date.getYear(), date.getMonthValue(), 1,0,0,0,0,date.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeHeight);
                double rectangleWidth = calendarWidth/7 - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = calendarHeight/6 - strokeHeight - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j+2)+(7*i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        stackPane.getChildren().add(date);
                    }
                    if (today.getYear() == date.getYear() && today.getMonth() == date.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }
}

