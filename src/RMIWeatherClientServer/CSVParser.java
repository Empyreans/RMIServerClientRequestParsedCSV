package RMIWeatherClientServer;

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

    ArrayList<Day> availableDays = new ArrayList<>();
    boolean needsUpdate = false;
    FileReader fileReader = null;
    String fileName;

    public CSVParser(String fileName) {

        this.fileName = fileName;
        parseCSVFile();

//        try {
//            fileReader = new FileReader(fileName);
//            updateCSVFile();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//
//        try (FileReader fileReader = new FileReader(fileName)) {
//            parseCSVFile(fileReader);
//            updateCSVFile(fileReader);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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

    // return status and toggle needsUpdate
    public boolean isUpdated(){
        if (needsUpdate){
            this.needsUpdate = !this.needsUpdate;
            return true;
        } else {
            return false;
        }
    }

    public void updateCSVFile() {

        try {
            fileReader = new FileReader(fileName);
        } catch (IOException e){
            e.printStackTrace();
        }

        CSVReader reader = new CSVReaderBuilder(fileReader).build();


//        try {
//            int i = 0;                                                                          // Zaehler dafuer dass die Zeilen weitergelesen werden koennen wenn ein neuer Tag in der CSV-Datei anfaengt, beginnt bei 0
//            for (Day day: availableDays){                                                       // fuer jeden Tag der verfügbaren Tage
//                while ((nextLine = reader.readNext())[0].equals(day.getDate())){                // traversiere Zeilen der CSV-Datei solange die erste Spalte (Datum) gleich dem jeweiligen Tag ist
//                    if (nextLine[2].equals(day.getWeatherDataList().get(i).getCelsius())){
//                        day.getWeatherDataList().get(i).setUpdated(false);
//                    } else {
//                        day.getWeatherDataList().get(i).setCelsius(nextLine[2]);
//                        day.getWeatherDataList().get(i).setUpdated(true);
//                        this.needsUpdate = true;
//                    }
//                    i++;
//                }
//            }
//
//        } catch (IOException e){
//            e.printStackTrace();
//        }


        List daysfromCSV = new ArrayList<String[]>();
        try {
            daysfromCSV = reader.readAll();
        } catch (IOException e){
            e.printStackTrace();
        }

        String[] tempString;
        ListIterator csvIterator = daysfromCSV.listIterator();
        while (csvIterator.hasNext()){
            tempString = (String[])csvIterator.next();
            compareWeatherData(tempString[0], tempString[1], tempString[2]);
        }

        for (Day day: availableDays) {
            day.resetWeatherData();
        }
    }

    private void compareWeatherData(String day, String time, String celsius){
        Day tempDay = dayAvailabe(day);
        if (tempDay != null){
            for (WeatherData weatherData:tempDay.getWeatherDataList()) {
                if (weatherData.getTime().equals(time) && weatherData.getCelsius().equals(celsius)){
                    System.out.println("no update necessary");
                    // placeholder
                } else if (weatherData.getTime().equals(time) && !(weatherData.getCelsius().equals(celsius)) && !(weatherData.isUpdated())){
                    System.out.println("update necessary");
                    weatherData.setCelsius(celsius);
                    weatherData.setUpdated(true);
                    this.needsUpdate = true;
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

}
