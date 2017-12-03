import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Empyreans on 29.10.2017.
 */
public class CSVParser {

    private ArrayList<Day> availableDays = new ArrayList<>();
    ArrayList<Day> updatedDays = new ArrayList<>();

    private FileReader fileReader = null;
    private String fileName;

    public CSVParser(String fileName) {

        this.fileName = fileName;
        parseCSVFile();

    }

    public void parseCSVFile() {

        try {
            fileReader = new FileReader(fileName);
        } catch (IOException e){
            e.printStackTrace();
        }

        CSVReader reader = new CSVReaderBuilder(fileReader).build();
        Day tempDay;
        String[] nextLine;

        try {

            while ((nextLine = reader.readNext()) != null) { // holt sich die nächste Zeile der .csv

                // Tag bereits vorhanden
                if ((tempDay = dayAvailabe(nextLine[0])) != null){
                    tempDay.addWeatherData(nextLine[1], nextLine[2]);
                }

                // Tag nicht vorhanden
                if (dayAvailabe(nextLine[0]) == null){
                    tempDay = new Day(nextLine[0], nextLine[1], nextLine[2]);
                    availableDays.add(tempDay);
                }

            }

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    // To-Do: Streamline
    public void updateCSVFile() {

        // To-Do: Warum muss ich den FileReader neu initialisieren?
        try {
            fileReader = new FileReader(fileName);
        } catch (IOException e){
            e.printStackTrace();
        }

        CSVReader reader = new CSVReaderBuilder(fileReader).build();
        String[] nextLine;

        try {
            while ((nextLine = reader.readNext()) != null) {
                compareWeatherData(nextLine[0], nextLine[1], nextLine[2]);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

//        // ruft fuer jede Zeile der CSV compareWeatherData() auf
//        List daysfromCSV = new ArrayList<String[]>();
//        try {
//            daysfromCSV = reader.readAll();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//        String[] tempString;
//        ListIterator csvIterator = daysfromCSV.listIterator();
//        while (csvIterator.hasNext()){
//            tempString = (String[])csvIterator.next();
//            compareWeatherData(tempString[0], tempString[1], tempString[2]);
//        }

    }

    private void compareWeatherData(String day, String time, String celsius){
        Day tempDay = dayAvailabe(day);
        if (tempDay != null){
            for (WeatherData weatherData:tempDay.getWeatherDataList()) {
                if (weatherData.getTime().equals(time) && weatherData.getCelsius().equals(celsius)){
                    System.out.println("no update necessary");
                } else if (weatherData.getTime().equals(time) && !(weatherData.getCelsius().equals(celsius)) && !(weatherData.isUpdated())){
                    System.out.println("update necessary");
                    weatherData.setCelsius(celsius);
                    weatherData.setUpdated(true);
                    updatedDays.add(tempDay);
                }
            }
        }
    }

    private Day dayAvailabe(String day){
        for (Day d:availableDays){
            if (d.getDate().equals(day)){
                return d;
            }
        }
        return null;
    }

    public String printDayWeatherData(String dayString) throws RemoteException {
        Day day = dayAvailabe(dayString);
        if (day != null){
            return day.getDayWeatherData();
        }
        return null;
    }

    public void resetUpdateStatus(){
        updatedDays = new ArrayList<>();
        for (Day day: availableDays) {
            day.resetWeatherDataUpdateStatus();
        }
    }
}
