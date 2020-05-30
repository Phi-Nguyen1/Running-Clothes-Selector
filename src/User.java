/*-----------------User-----------------
 * Author: Phi Nguyen
 * Created: 4/4/20
 * Last edited: 5/29/20
 * Purpose: Class that stores info for each
 * user
 * -------------------------------------
 */
public class User 
{
	private String name;
	private int tempPref;
	
	public User()
	{
		name = " ";
		tempPref = 0;
	}
	
	public User(String nameSet)
	{
		name = nameSet;
		tempPref = 0;	
	}
	
	public User(String nameSet, int tempSet)
	{
		name = nameSet;
		tempPref = tempSet;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTempChange() {
		return tempPref;
	}

	public void setTempChange(int tempChange) {
		this.tempPref = tempChange;
	}
	

}
