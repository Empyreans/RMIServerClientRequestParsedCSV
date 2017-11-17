package RMIWeatherClientServer;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Empyreans on 29.10.2017.
 */
public class CSVParser {

    ArrayList<Day> availableDays = new ArrayList<>();
    boolean isUpdated = false;
    FileReader fileReader = null;

    public CSVParser(String fileName) {

        try {
            fileReader = new FileReader(fileName);
            parseCSVFile();
        } catch (IOException e){
            e.printStackTrace();
        }

        try {
            fileReader = new FileReader(fileName);
            updateCSVFile();
        } catch (IOException e){
            e.printStackTrace();
        }

//
//        try (FileReader fileReader = new FileReader(fileName)) {
//            parseCSVFile(fileReader);
//            updateCSVFile(fileReader);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public void parseCSVFile() {

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

    public void updateCSVFile() {

        CSVReader reader = new CSVReaderBuilder(fileReader).build();
        String[] nextLine;

//        try{
//            nextLine = reader.readNext();
//            System.out.println(nextLine[0]);
//        } catch (IOException e){
//            e.printStackTrace();
//        }


        int i = 0;

//        for (Day day:availableDays) {
//            try {
//                while((nextLine = reader.readNext()) != null){
//                    if (nextLine[0].equals(day.getDate())){
//                        if (nextLine[2].equals(day.getWeatherDataList().get(i++).getCelsius())){
//                            System.out.println("sup");
//                        }
//                    }
//                }
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//        }


        for (Day day:availableDays) {
            try{
                while(( nextLine = reader.readNext()) != null && nextLine[0].equals(day.getDate())){
                    System.out.println(nextLine[2]);
                }

            } catch (IOException e){
                e.printStackTrace();
            }
            System.out.println("soweitsogut");
        }

//        try {
//            int i = 0;                                                                          // Zaehler dafuer dass die Zeilen weitergelesen werden koennen wenn ein neuer Tag in der CSV-Datei anfaengt, beginnt bei 0
//            for (Day day: availableDays){                                                       // fuer jeden Tag der verfügbaren Tage
//                while ((nextLine = reader.readNext())[0].equals(day.getDate())){                // traversiere Zeilen der CSV-Datei solange die erste Spalte (Datum) gleich dem jeweiligen Tag ist
//                    if (nextLine[2].equals(day.getWeatherDataList().get(i).getCelsius())){
//                        day.getWeatherDataList().get(i).setUpdated(false);
//                    } else {
//                        day.getWeatherDataList().get(i).setCelsius(nextLine[2]);
//                        day.getWeatherDataList().get(i).setUpdated(true);
//                        this.isUpdated = true;
//                    }
//                    i++;
//                }
//            }
//
//        } catch (IOException e){
//            e.printStackTrace();
//        }
    }

    public String printDayWeatherData(String dayString) throws RemoteException {
        Day day = dayAvailabe(dayString);
        if (day != null){
            return day.getDayWeatherData();
        }
        return null;
    }

    public Day dayAvailabe(String day){
        for (Day d:availableDays){
            if (d.getDate().equals(day)){
                return d;
            }
        }
        return null;
    }

    // return and toggle
    public boolean checkUpdated(){
        boolean tempUpdate = this.isUpdated;
        this.isUpdated = !this.isUpdated;
        return tempUpdate;
    }

//    public boolean checkConsistency(){
//        boolean fileHasSameContent = false;
//        try (FileReader fileReader = new FileReader(fileName)) {
//            ArrayList<Day> availableDaysCurrent = parseCSVFile(fileReader);
//            if (availableDaysCurrent.containsAll(availableDays)){
//                System.out.println("The consistency of the .csv file has not changed");
//                fileHasSameContent = true;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return fileHasSameContent;
//    }
}
