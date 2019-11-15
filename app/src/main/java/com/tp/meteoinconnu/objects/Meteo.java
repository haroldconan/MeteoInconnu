package com.tp.meteoinconnu.objects;

import java.text.DecimalFormat;

public class Meteo {
    private double temp;
    private int humidity;
    private String main;

    public Meteo(Double temp, int humidity, String main) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        Double d = temp - 273.15;
        this.temp = Double.parseDouble(df.format(d).replace(",","."));
        this.humidity = humidity;
        this.main = main;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }
}
