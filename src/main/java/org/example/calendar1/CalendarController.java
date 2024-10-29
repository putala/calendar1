package org.example.calendar1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;



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

    @FXML
    private FlowPane scheduleL;

    @FXML
    private FlowPane scheduleR;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        date = ZonedDateTime.now();
        today = ZonedDateTime.now();
        calendar.getChildren().clear();
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

    public void backOneDay() {
        date = date.minusDays(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    public void nextOneDay() {
        date = date.plusDays(1);
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
        int dateOffset = ZonedDateTime.of(date.getYear(), date.getMonthValue(), 1, 0, 0, 0, 0, date.getZone()).getDayOfWeek().getValue();

        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(date);

        if (date.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.GRAY);
                double rectangleWidth = calendarWidth / 7 - 1;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = calendarHeight / 6 - 1;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = 7 * i + j + 2;
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text dateText = new Text(String.valueOf(currentDate));
                        stackPane.getChildren().add(dateText);
                    }
                    if (currentDate == date.getDayOfMonth()) {
                        rectangle.setStroke(Color.RED);
                        List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
                        if (calendarActivities != null) {
                            createCalendarSchedule(calendarActivities);
                        }
                    }
                    if (today.getYear() == date.getYear() && today.getMonth() == date.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setFill(Color.rgb(68, 136, 255));
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarSchedule(List<CalendarActivity> activities) {
        scheduleL.getChildren().clear();
        scheduleR.getChildren().clear();

        for (int hour = 0; hour < 16; hour++) { // Zakres od 00:00 do 15:00
            boolean hasEvent = false;
            for (CalendarActivity activity : activities) {
                if (activity.getDate().getHour() == hour) {
                    hasEvent = true;

                    StackPane activityPaneL = new StackPane();
                    StackPane activityPaneR = new StackPane();

                    Rectangle rectangleL = new Rectangle();
                    rectangleL.setFill(Color.GRAY);
                    rectangleL.setStroke(Color.BLACK);
                    rectangleL.setWidth(60);
                    rectangleL.setHeight(21);

                    Rectangle rectangleR = new Rectangle();
                    rectangleR.setFill(Color.LIGHTGRAY);
                    rectangleR.setStroke(Color.BLACK);
                    rectangleR.setWidth(140);
                    rectangleR.setHeight(21);

                    Text activityTextL = new Text(activity.getDate().toLocalTime().toString());
                    Text activityTextR = new Text(activity.getClientEvent());

                    activityPaneR.setOnMouseClicked(mouseEvent -> {
                        TextInputDialog dialog = new TextInputDialog(activity.getClientEvent());
                        dialog.setTitle("Edytuj wydarzenie");
                        dialog.setHeaderText("Edytuj opis wydarzenia:");
                        dialog.setContentText("Nowy opis:");

                        Optional<String> result = dialog.showAndWait();
                        result.ifPresent(newDescription -> {
                            activity.setClientEvent(newDescription);
                            activityTextR.setText(newDescription);
                            System.out.println("Zaktualizowano wydarzenie: " + newDescription);
                        });
                    });

                    activityPaneL.getChildren().addAll(rectangleL, activityTextL);
                    activityPaneR.getChildren().addAll(rectangleR, activityTextR);

                    scheduleL.getChildren().add(activityPaneL);
                    scheduleR.getChildren().add(activityPaneR);
                }
            }
            if (!hasEvent) {
                StackPane emptyActivityPaneL = new StackPane();
                StackPane emptyActivityPaneR = new StackPane();

                Rectangle emptyRectangleL = new Rectangle();
                emptyRectangleL.setFill(Color.GRAY);
                emptyRectangleL.setStroke(Color.BLACK);
                emptyRectangleL.setWidth(60);
                emptyRectangleL.setHeight(21);

                Rectangle emptyRectangleR = new Rectangle();
                emptyRectangleR.setFill(Color.LIGHTGRAY);
                emptyRectangleR.setStroke(Color.BLACK);
                emptyRectangleR.setWidth(140);
                emptyRectangleR.setHeight(21);

                Text emptyActivityTextL = new Text(String.format("%02d:00", hour));
                Text emptyActivityTextR = new Text("...");

                emptyActivityPaneL.getChildren().addAll(emptyRectangleL, emptyActivityTextL);
                emptyActivityPaneR.getChildren().addAll(emptyRectangleR, emptyActivityTextR);

                scheduleL.getChildren().add(emptyActivityPaneL);
                scheduleR.getChildren().add(emptyActivityPaneR);
            }
        }
    }

    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity : calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if (!calendarActivityMap.containsKey(activityDate)) {
                calendarActivityMap.put(activityDate, List.of(activity));
            } else {
                List<CalendarActivity> oldListByDate = calendarActivityMap.get(activityDate);
                List<CalendarActivity> newList = new ArrayList<>(oldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return calendarActivityMap;
    }

    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime date) {
        List<CalendarActivity> calendarActivities = new ArrayList<>();
        int year = date.getYear();
        int month = date.getMonth().getValue();
        int monthMaxDate = date.getMonth().maxLength();

        for (int i = 0; i < monthMaxDate; i++) {
            for (int j = 0; j < 15; j++) { // Godziny od 00:00 do 15:00
                ZonedDateTime time = ZonedDateTime.of(year, month, i + 1, j, 0, 0, 0, date.getZone());
                calendarActivities.add(new CalendarActivity(time, "..."));
            }
        }
        return createCalendarMap(calendarActivities);
    }
}




