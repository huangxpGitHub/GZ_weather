package com.example.a52249.gz_weather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 52249 on 2019/6/15.
 *
 * 把之前的五个实体类，聚在一起，应用创建的实体类
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
