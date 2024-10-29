package org.example.calendar1;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarDataManager {

    private static final String FILE_PATH = "calendar_data.txt";

    public static void saveActivities(Map<ZonedDateTime, List<CalendarActivity>> activities) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(activities);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Błąd podczas zapisu danych do pliku.");
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<ZonedDateTime, List<CalendarActivity>> loadActivities() {
        Path path = Paths.get(FILE_PATH);
        if (Files.exists(path)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
                return (Map<ZonedDateTime, List<CalendarActivity>>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Błąd podczas odczytu danych z pliku.");
            }
        }
        return new HashMap<>();  // Zwraca pustą mapę, jeśli plik nie istnieje
    }
}
