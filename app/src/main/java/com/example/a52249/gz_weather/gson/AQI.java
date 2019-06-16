package com.example.a52249.gz_weather.gson;

/**
 * Created by 52249 on 2019/6/15.
 * 解析json解析中的当前城市的aqi，pm2.5的值
 */

public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;

    }
}
