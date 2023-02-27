package com.Weather;

import com.google.gson.JsonObject;

import org.json.JSONObject;

public class WeatherData {

    private String mTemperature , micon , mcity , mWeatherTye;
    private int mCondition;


    public static WeatherData fromJson(JSONObject jsonObject){

        try {

            WeatherData weatherData = new WeatherData();
            weatherData.mcity=jsonObject.getString("name");
            weatherData.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherData.mWeatherTye = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
          //  weatherData.micon = updateWeatherIcon(weatherData.mCondition);
            double tempresult = jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue = (int)Math.rint(tempresult);
            weatherData.mTemperature = Integer.toString(roundedValue);
            return weatherData;




        } catch (Exception e) {
            e.printStackTrace();

            return null;

        }
    }


//    private static String updateWeatherIcon(int condition){
//
//        if (condition>=0 && condition<=300){
//
//            return "thunder.json";
//
//        }
//        else if (condition>=300 && condition<=500){
//
//            return "lightrain.json";
//
//        }
//        else if (condition>=500 && condition<=600){
//
//            return "lightrain.json";
//
//        }
//        else  if (condition>=600 && condition<=700){
//
//            return "snowfall.json";
//
//        }
//        else if (condition>=701 && condition<=771){
//
//            return "fog.json";
//
//        }
//       else if (condition>=772 && condition<=800){
//
//            return "overcast.json";
//
//        }
//
//        else if (condition == 800){
//
//            return "sunney.json";
//
//        }
//
//
//        else if (condition>=0 && condition<=300){
//
//            return "cloudy.json";
//
//        }
//
//        return "dunno";
//
//    }


    public String getmTemperature() {
        return mTemperature+"°C";
    }

    public String getMicon() {
        return micon;
    }

    public String getMcity() {
        return mcity;
    }

    public int getmCondition() {
        return mCondition;
    }

    public String getmWeatherTye() {
        return mWeatherTye;


    }
}
