/*---------------WeatherData--------------
 * Author: Phi Nguyen
 * Created: 4/6/20
 * Last edited: 5/29/20
 * Purpose: Class that accesses openweathermap.org
 * api to retrieve weather data from selected location
 * ----------------------------------------
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;
import com.google.gson.reflect.*;


public class WeatherData 
{
	private String API_KEY = "2fad0b0bf686178d55b373c4b9859bd3";
	private double temp = 0;
	private double windSpeed = 0;
	
	/*--------jsonToMap(data to sort)-----------
	 * Takes in a string of json data and sorts
	 * it into a hashmap using google gson dependency.
	 * helper method for getWeatherInfo().
	 * 
	 * returns a hashmap 
	 * -----------------------------------------
	 */
	public Map<String, Object> jsonToMap(String string)
	{
		Map<String, Object> map = new Gson().fromJson(string, new TypeToken<HashMap<String, Object>>() {}.getType());
		return map;
	}
	
	public WeatherData()
	{
		temp = 0;
		windSpeed = 0;
	}
	
	/*----------------getWeatherInfo(city, state)-----------
	 * Method that utilizes openweathermap.org api to retrieve
	 * weather from specified city and state. sets weatherData object
	 * variables.
	 * -----------------------------------------------------
	 */
	public void getWeatherInfo(String city, String state)
	{
		String LOCATION = city + "," + state;
		String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + LOCATION + "&appid=" + API_KEY + "&units=imperial";
		try
		{
			StringBuilder result = new StringBuilder();
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			while((line = rd.readLine()) != null)
			{
				result.append(line);
			}
			rd.close();
			System.out.println(result);
			
			Map<String, Object> respMap = jsonToMap(result.toString());
			Map<String, Object> mainMap = jsonToMap(respMap.get("main").toString());
			Map<String, Object> windMap = jsonToMap(respMap.get("wind").toString());

			temp = (double) mainMap.get("temp");
			windSpeed = (double) windMap.get("speed");
		}
		catch(IOException e)
		{
			System.out.println("an error has occured");
			e.printStackTrace();
		}
	}
	
	public void printWeatherInfo()
	{
		System.out.println(temp);
		System.out.println(windSpeed);
	}
	
	public double getTemp()
	{
		return temp;
	}
	
	public double getWindSpeed()
	{
		return windSpeed;
	}
}
