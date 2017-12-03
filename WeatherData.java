
/**
 * Created by Empyreans on 29.10.2017.
 */
public class WeatherData {
    private String time;
    private String celsius;
    private boolean updated = false; // benoetigen Updatestatus fuer den Printbefehl

    public WeatherData(String time, String celsius){
        this.time = time;
        this.celsius = celsius;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean b){
        updated = b;
    }

    public void setCelsius(String celsius) {
        this.celsius = celsius;
    }

    public String getTime() {
        return time;
    }

    public String getCelsius() {
        return celsius;
    }
}
