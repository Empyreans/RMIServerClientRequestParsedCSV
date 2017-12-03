import com.opencsv.bean.CsvBindByName;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Empyreans on 29.10.2017.
 */
public class Day {

    @CsvBindByName
    private String date;
    private ArrayList<WeatherData> weatherDataList = new ArrayList<>();

    public Day(String date, String time, String celsius){
        this.date = date;
        weatherDataList.add(new WeatherData(time, celsius));
    }

    public void addWeatherData(String time, String celsius){
        weatherDataList.add(new WeatherData(time,celsius));
    }

    public void resetWeatherDataUpdateStatus(){
        for (WeatherData weatherData:weatherDataList) {
            weatherData.setUpdated(false);
        }
    }

    private String getAvgTemp(){
        float avgTempFloat = 0.0F;
        for (WeatherData weatherData : weatherDataList){
            float celsiusFloat = Float.parseFloat(weatherData.getCelsius());
            avgTempFloat = avgTempFloat + celsiusFloat;
        }
        if (weatherDataList.size() > 1){
            avgTempFloat = avgTempFloat / weatherDataList.size();
        }
        return Float.toString(avgTempFloat);
    }

    private String getMaxTemp(){
        float maxTempFloat = 0.0F;
        for (WeatherData weatherData : weatherDataList){
            float celsiusFloat = Float.parseFloat(weatherData.getCelsius());
            if (celsiusFloat > maxTempFloat){
                maxTempFloat = celsiusFloat;
            }
        }
        return Float.toString(maxTempFloat);
    }

    private String getLowestTemp(){
        float lowTempFloat = Float.parseFloat(weatherDataList.get(0).getCelsius());
        for (WeatherData weatherData : weatherDataList){
            float celsiusFloat = Float.parseFloat(weatherData.getCelsius());
            if (celsiusFloat < lowTempFloat){
                lowTempFloat = celsiusFloat;
            }
        }
        return Float.toString(lowTempFloat);
    }

    public String getDate(){
        return date;
    }

    public String getDayWeatherData(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(date+"\n");
        stringBuilder.append("Durchschnittstemperatur: " + getAvgTemp()+"\n");
        stringBuilder.append("Maximaltemperatur: " + getMaxTemp()+"\n");
        stringBuilder.append("Minimaltemperatur: " + getLowestTemp()+"\n");
        for (WeatherData weatherData : weatherDataList){
            stringBuilder.append(String.format("%s %10s°" ,weatherData.getTime(), weatherData.getCelsius()));
            if (weatherData.isUpdated()){
                stringBuilder.append(" neu\n");
            } else {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public ArrayList<WeatherData> getWeatherDataList() {
        return weatherDataList;
    }

}
