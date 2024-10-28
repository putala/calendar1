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
import java.util.*;


public class CalendarController implements Initializable {

    ZonedDateTime date;
    ZonedDateTime today;

    @FXML
    FlowPane schedule;

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
        int dateOffset = ZonedDateTime.of(date.getYear(), date.getMonthValue(), 1,0,0,0,0,date.getZone()).getDayOfWeek().getValue();

        //List of activities for a given month
        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(date);

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
                        List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
                        if(calendarActivities != null){
                            createCalendarSchedule(calendarActivities);
                        }
                    }
                    if (today.getYear() == date.getYear() && today.getMonth() == date.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setFill(Color.rgb(68,136,255));
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }


    private void createCalendarSchedule(List<CalendarActivity> activities) {
        schedule.getChildren().clear();

        for (CalendarActivity activity : activities) {
            StackPane activityPane = new StackPane();

            Rectangle rectangle = new Rectangle();
            rectangle.setFill(Color.LIGHTGRAY);
            rectangle.setStroke(Color.BLACK);
            rectangle.setWidth(200);
            rectangle.setHeight(20);

            Text activityText = new Text(
                    activity.getDate().toLocalTime() + "  " + activity.getClientName()
            );

            activityPane.getChildren().addAll(rectangle, activityText);

            // Dodanie reakcji na kliknięcie
            activityPane.setOnMouseClicked(mouseEvent -> {
                System.out.println("Wybrana aktywność: " + activity.getClientName());
                // Kod do dodatkowych działań przy kliknięciu
            });

            schedule.getChildren().add(activityPane);
        }
    }



//    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
//        VBox calendarActivityBox = new VBox();
//        for (int k = 0; k < calendarActivities.size(); k++) {
//            if(k >= 2) {
//                Text moreActivities = new Text("...");
//                calendarActivityBox.getChildren().add(moreActivities);
//                moreActivities.setOnMouseClicked(mouseEvent -> {
//                    //On ... click print all activities for given date
//                    System.out.println(calendarActivities);
//                });
//                break;
//            }
//            Text text = new Text(calendarActivities.get(k).getClientName() + ", " + calendarActivities.get(k).getDate().toLocalTime());
//            calendarActivityBox.getChildren().add(text);
//            text.setOnMouseClicked(mouseEvent -> {
//                //On Text clicked
//                System.out.println(text.getText());
//            });
//        }
//        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
//        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
//        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
//        calendarActivityBox.setStyle("-fx-background-color:GRAY");
//        stackPane.getChildren().add(calendarActivityBox);
//    }


    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity: calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if(!calendarActivityMap.containsKey(activityDate)){
                calendarActivityMap.put(activityDate, List.of(activity));
            } else {
                List<CalendarActivity> OldListByDate = calendarActivityMap.get(activityDate);

                List<CalendarActivity> newList = new ArrayList<>(OldListByDate);
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

        Random random = new Random();
        for (int i = 0; i < monthMaxDate-1; i++) {
            for (int j = 0; j < 15; j++) {
                ZonedDateTime time = ZonedDateTime.of(year, month, i+1, j,0,0,0,date.getZone());
                calendarActivities.add(new CalendarActivity(time, "Spotkanie_" + (random.nextInt(9000)+999)));
            }
        }
        return createCalendarMap(calendarActivities);
    }
}

