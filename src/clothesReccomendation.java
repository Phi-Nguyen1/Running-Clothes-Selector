/*----------clothesReccomendation-------
 * Author: Phi Nguyen
 * Created: 4/15/20
 * Last edited: 5/29/20
 * Purpose: Class that takes in temperature
 * and windspeed and selects outfit based on 
 * given temp and windspeed
 * ----------------------------------------
 */

public class clothesReccomendation 
{
	public clothesReccomendation()
	{
		
	}
	
	/*-----------------outfitChooser(temp, windSpeed, amount to vary temp by)-------------
	 * method that selects an outfit based on temp, windspeed, and user pref temp.
	 * user pref temp changes temperature used to figure out which if statement
	 * should be chosen.
	 * 
	 * returns a string containing outfit reccomendation.
	 * -----------------------------------------------------------------------------------
	 */
	public String outfitChooser(double temp, double windSpeed, int userPref)
	{
		//sources used to obtain running outfit choices
		//https://www.nbcnews.com/better/health/6-tips-stay-fit-warm-winter-ncna944061
		//https://www.runnersworld.com/beginner/a20834718/the-newbie-guide-to-running-when-its-cold/
		
		String initial = "With a temp of " + temp + " degrees farenheit, a wind speed of " + windSpeed +
				 " mph, and your user preferences in mind...";
		String outfitString = "";
		//figuring out what temperature to use to choose outfit
		double tempCalc = windChillCalc(temp, windSpeed) + userPref;
		
		if(tempCalc >= 60)
		{
			outfitString = "You should wear a tank top and shorts!";
		}
		else if(tempCalc  >= 50 && tempCalc <= 59)
		{
			outfitString = "You should wear a short sleeve shirt and shorts";
		}
		else if(tempCalc >= 40 && tempCalc <= 49)
		{
			outfitString = "You should wear a long sleeve shirt, shorts or tights!"
					+ " Also consider gloves and a headband to cover your ears.";
		}
		else if(tempCalc >= 30 && tempCalc <= 39)
		{
			outfitString = "You should wear a long sleeve tech shirt; shorts, tights, or pants;"
					+ " gloves, and headband or hat to cover ears";
		}
		else if(tempCalc >= 20 && tempCalc <= 29)
		{
			outfitString = "You should wear two shirts layered;a long sleeve tech shirt and a short sleeve tech shirt;"
					+ " or long sleeve shirt and jacket; tights or pants, gloves, and headband or hat to cover ears";
		}
		else if(tempCalc >= 10 && tempCalc <= 19)
		{
			outfitString = "You should wear two shirts layered, tights, gloves or mittens, headband or hat, and "
					+ "warm windbreaker jacket/pants";
		}
		else if(tempCalc >= 0 && tempCalc <= 9)
		{
			outfitString = "You should wear two shirts layered, tights, thermal/windbreaker jacket/pants, mittens,"
					+ " headband or hat, ski mask to cover face. Add layers as necessary";
		}
		else if(tempCalc <= 0)
		{
			outfitString = "You should wear a thermal layer below your thermal pants, a	a warm vest over your jacket. "
					+ "Add a second buff (either on your neck or wrapped around your arm) to switch out when the first one gets frosty. "
					+ "Use hand warmers, and minimize any exposed skin.";
		}
		
		return initial + outfitString;
	}
	
	public double windChillCalc(double temp, double windSpeed)
	{
		//calculating temperature based off windchill. information came from 
		//http://www.skyviewweather.com/learning/wind-chill-questions-and-answers/
		if(temp < 50 && windSpeed > 3)
		{
			return 35.74+0.621*temp-35.75*(Math.pow(windSpeed, 0.16))+0.4275*temp*(Math.pow(windSpeed, 0.16));
		}
		//if windchill conditions aren't met, return location's temp.
		else
		{
			return temp;
		}
	}

}
