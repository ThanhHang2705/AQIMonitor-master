package fimo.uet.fairapp.MyNotification;

/**
 * Created by HP on 4/22/2017.
 */
public class IndexInfo{
    double PM, Temperature, Humidity;
    public IndexInfo(double PM, double Temperature, double Humidity){
        this.PM = PM;
        this.Temperature = Temperature;
        this.Humidity = Humidity;
    }

    public double getPM() {
        return PM;
    }

    public double getHumidity() {
        return Humidity;
    }

    public double getTemperature() {
        return Temperature;
    }
}
