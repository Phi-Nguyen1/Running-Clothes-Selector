/*------------------MainGUI--------------------
 * Author: Phi Nguyen
 * Created: 4/3/20
 * Last edited: 5/29/20
 * Purpose: Class that initializes the gui
 * ---------------------------------------------
 */
import java.awt.event.*; 
import java.awt.*; 
import javax.swing.*;
import java.io.*;

import java.util.Scanner;
import java.util.jar.Attributes.Name;
import java.util.List;
import java.util.ArrayList;

public class MainGUI implements ActionListener, ItemListener
{
	private JFrame frame;
	private JComboBox <String> comboBox;
	private List <User> users = new ArrayList<User>();
	private clothesReccomendation clothesReccomend = new clothesReccomendation();
	private JTextField zipCodeInput;
	private JTextField countryInitInput;
	private WeatherData data = new WeatherData();
	private JLabel results;
	private JTextArea reccomendation;
	private JPanel userPrefAdjustPanel;
	private JPanel changesNotedPanel;
	
	public static void main(String[] args) throws IOException 
	{
		createFile();
		new MainGUI();
	}
	
	public MainGUI()
	{
		loadUsers();
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame = new JFrame("Running Clothes Selector");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(400,400));
		JPanel mainPanel = new JPanel();
		
		//making program pop up in middle-ish of screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JPanel userSelectPanel = new JPanel();
		userSelectPanel.setLayout(new GridLayout(2,1));
		JPanel weatherInfoPanel = new JPanel();
		weatherInfoPanel.setLayout(new GridLayout(3,1));
		userPrefAdjustPanel = new JPanel();
		userPrefAdjustPanel.setLayout(new GridLayout(3,1));
		
		mainPanel.add(userSelectPanel);
		mainPanel.add(weatherInfoPanel);
		mainPanel.add(userPrefAdjustPanel);
		
		//userSelectPanel initialization
		JPanel chooseUserLabel = new JPanel();
		chooseUserLabel.setLayout(new FlowLayout());
		JLabel chooseUser = new JLabel("Choose User");
		chooseUserLabel.add(chooseUser);
		userSelectPanel.add(chooseUserLabel);
		
		JPanel userSelectandRemove = new JPanel();
		userSelectandRemove.setLayout(new FlowLayout());
		String [] usernames = retrieveUsers().toArray(new String[retrieveUsers().size()]);
		comboBox = new JComboBox<>(usernames);
		comboBox.addItemListener(this);
		JButton removeUser = new JButton("Remove User");
		removeUser.addActionListener(this);
		JButton addUser = new JButton("Add User");
		addUser.addActionListener(this);
		
		userSelectandRemove.add(comboBox);
		userSelectandRemove.add(removeUser);
		userSelectandRemove.add(addUser);
		userSelectPanel.add(userSelectandRemove);
		
		//weatherInfoPanel initialization
		JPanel selectLocation = new JPanel();
		selectLocation.setLayout(new GridLayout(2,2));
		JLabel chooseZip = new JLabel("Enter ZipCode");
		zipCodeInput = new JTextField(16);
		JLabel chooseCountryInit = new JLabel("Enter Country's Initials");
		countryInitInput = new JTextField(16);
		JPanel reccomendButtonPanel = new JPanel();
		reccomendButtonPanel.setLayout(new FlowLayout());
		JButton reccomendButton = new JButton("Reccomend");
		reccomendButton.addActionListener(this);
		JPanel reccomendationPanel = new JPanel();
		reccomendation = new JTextArea(2,2);
		reccomendation.setWrapStyleWord(true);
		reccomendation.setLineWrap(true);
		reccomendation.setEditable(false);
		JScrollPane	reccomendationScrollPane = new JScrollPane(reccomendation);
		reccomendationPanel.setLayout(new GridLayout(1,1));
		reccomendationPanel.add(reccomendationScrollPane);
		
		selectLocation.add(chooseZip);
		selectLocation.add(zipCodeInput);
		selectLocation.add(chooseCountryInit);
		selectLocation.add(countryInitInput);
		reccomendButtonPanel.add(reccomendButton);
		
		weatherInfoPanel.add(selectLocation);
		weatherInfoPanel.add(reccomendButtonPanel);
		weatherInfoPanel.add(reccomendationPanel);
		
		//userPrefAdjustPanel initialization
		JPanel howWasRunPanel = new JPanel();
		howWasRunPanel.setLayout(new FlowLayout());
		JLabel howWasRunLabel = new JLabel("How was your run?");
		howWasRunPanel.add(howWasRunLabel);
		
		JPanel adjustTemp = new JPanel();
		adjustTemp.setLayout(new GridLayout(1,2));
		JButton addTemp = new JButton("I prefer feeling warmer");
		JButton minusTemp = new JButton("I prefer feeling cooler");
		addTemp.addActionListener(this);
		minusTemp.addActionListener(this);
		adjustTemp.add(addTemp);
		adjustTemp.add(minusTemp);
	
		changesNotedPanel = new JPanel();
		changesNotedPanel.setLayout(new FlowLayout());
		JLabel changesNotedLabel = new JLabel("Your choice has been noted!");
		changesNotedPanel.add(changesNotedLabel);
		changesNotedPanel.setVisible(false);
		
		userPrefAdjustPanel.add(howWasRunPanel);
		userPrefAdjustPanel.add(adjustTemp);
		userPrefAdjustPanel.add(changesNotedPanel);
		userPrefAdjustPanel.setVisible(false);
		
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);
	}
	

	/*
	 * Button actions
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		String command = arg0.getActionCommand();
		/*
		 * Add User button
		 */
		if(command.equals("Add User"))
		{
			String input = JOptionPane.showInputDialog("Please input name of user to add.");
			if(this.addUser(input, 0));
			{
				comboBox.addItem(input);
			}	
		}
		/*
		 * Remove User button
		 */
		else if(command.equals("Remove User"))
		{
			try 
			{
				removeUser(comboBox.getSelectedItem().toString());
			    comboBox.removeItemAt(comboBox.getSelectedIndex());
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		/*
		 * Reccomend button
		 */
		else if(command.equals("Reccomend"))
		{
			if(comboBox.getSelectedItem() == null)
			{
				JOptionPane.showMessageDialog(frame,"Please select a user."); 
				return;
			}
			else if(zipCodeInput.getText().isEmpty() && countryInitInput.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(frame,"Please enter zip code and country's initials."); 
				return;
			}
			else if(zipCodeInput.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(frame,"Please enter zip code."); 
				return;
			}
			else if(countryInitInput.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(frame,"Please enter country's initials."); 
				return;
			}
			
			int indexOfUsers = 0;
			for(int i = 0; i < users.size(); i++)
			{
				if(comboBox.getSelectedItem().equals(users.get(i).getName()))
				{
					indexOfUsers = i;
					break;
				}
			}
			
    		data.getWeatherInfo(zipCodeInput.getText(), countryInitInput.getText());
		    results = new JLabel("Temp = " + data.getTemp() + " Wind = " + data.getWindSpeed());
		    reccomendation.setText(clothesReccomend.outfitChooser(data.getTemp(), data.getWindSpeed(), 
		    		users.get(indexOfUsers).getTempChange()));
		    userPrefAdjustPanel.setVisible(true);
		}
		/*
		 * warmer button
		 */
		else if(command.equals("I prefer feeling warmer"))
		{
			//finding user in list
			int indexOfUsers = 0;
			for(int i = 0; i < users.size(); i++)
			{
				if(comboBox.getSelectedItem().equals(users.get(i).getName()))
				{
					indexOfUsers = i;
					break;
				}
			}
			//adjusting found user's temp preference by increasing by 2 degrees f
			users.get(indexOfUsers).setTempChange(users.get(indexOfUsers).getTempChange() + 2);
			reloadUsers();
			changesNotedPanel.setVisible(true);
		}
		/*
		 * cooler button
		 */
		else if(command.equals("I prefer feeling cooler"))
		{
			//finding user in list
			int indexOfUsers = 0;
			for(int i = 0; i < users.size(); i++)
			{
				if(comboBox.getSelectedItem().equals(users.get(i).getName()))
				{
					indexOfUsers = i;
					break;
				}
			}
			//adjusting found user's temp preference by increasing by 2 degrees f
			users.get(indexOfUsers).setTempChange(users.get(indexOfUsers).getTempChange() - 2);
			reloadUsers();
			changesNotedPanel.setVisible(true);
		}
		
	}

	/*
	 * ComboBox actions
	 */
	@Override
	public void itemStateChanged(ItemEvent arg0) 
	{
		if(arg0.getSource() == comboBox)
		{
			resetGUI();
		}
	}
	/*------------resetGUI()-------------------
	 * Resets visibility of panels and resets
	 * reccomendation text so as to prevent the user from 
	 * seeing another users data
	 * -----------------------------------------
	 */
	public void resetGUI()
	{
		userPrefAdjustPanel.setVisible(false);
		changesNotedPanel.setVisible(false);
		reccomendation.setText("");
	}
	
	/*------------------loadUsers()------------
	 * Initializes all the users so preferences
	 * can be accessed.
	 * ----------------------------------------
	 */
	public void loadUsers()
	{
		File userFiles = new File("Users.txt");
		Scanner fileParse;
		try
		{
			fileParse = new Scanner(userFiles);
			//initializing new users and adding to users arraylist
			while(fileParse.hasNextLine())
			{
				String username = fileParse.nextLine();
				int tempPref = Integer.parseInt(fileParse.nextLine());
				User userToAdd = new User(username, tempPref);
				users.add(userToAdd);
			}
			fileParse.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("An error occurred loading the users.");
			e.printStackTrace();
		}
	}
	
	/*--------------reloadUsers()--------------
	 * Adjusts the users preference file (Users.txt)
	 * so that any adjustments to their user preferences
	 * , done in the GUI, are adjusted accordingly
	 */
	public void reloadUsers()
	{
		try
		{
			//checking if any users exist to reload
			if(users.isEmpty() == true)
			{
				return;
			}
			
			File inputFile = new File("Users.txt");
			File tempFile = new File("tempUsers.txt");
			tempFile.createNewFile();

			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			//adding users to new user save file with new data
			for(int i = 0; i < users.size(); i++)
			{
				writer.write(users.get(i).getName());
				writer.newLine();
				writer.write(String.valueOf(users.get(i).getTempChange()));  
				writer.newLine();
			}
			
			//deleting old save file and renaming new save file
			writer.close(); 
			inputFile.delete();
			tempFile.renameTo(inputFile);	
		}
		catch(IOException e)
		{
			System.out.println("error reloading users");
		}
		
	}
	
	/*-----------retrieveUsers()--------------
	 * Returns a list of all users.
	 * Used to initialize combo box.
	 *-----------------------------------------*/
	public List <String> retrieveUsers()
	{
		File userFiles = new File("Users.txt");
		Scanner fileParse;
		List <String> usernames = new ArrayList<String>();
		try
		{
			fileParse = new Scanner(userFiles);
			while(fileParse.hasNextLine())
			{
				usernames.add(fileParse.nextLine());
				fileParse.nextLine();
			}
			fileParse.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("An error occurred retrieving users.");
			e.printStackTrace();
		}
		return usernames;
	}
	
	/*------------------createFile()--------------------------
	 *Creates the initial file that stores user information. If file
	 *already exists, nothing happens.
	 *--------------------------------------------------------*/
	private static void createFile() throws IOException
	{
		try 
		{
			File userFile = new File("Users.txt");
			if (userFile.createNewFile()) 
		    {
				System.out.println("File created: " + userFile.getName());
		    } 
			else 
			{
		        System.out.println("File already exists.");
		    }
		} 
		catch (IOException e) 
		{
			System.out.println("An error occurred creating the Users.txt file.");
		    e.printStackTrace();
		}
		  
	}
	
	/*-------------UserAlreadyExists(name of user)------------
	 *Checks if the inputted username exists in the save File.
	 *If exists, returns true. Else returns false.
	 *Helper function for addUser() to prevent usage of the same
	 *name
	 *-------------------------------------------------------*/
	public boolean userAlreadyExists(String name)
	{
		File users = new File("Users.txt");
		Scanner fileParse;
		boolean found = false;
		try 
		{
			fileParse = new Scanner(users);
			//searching Users.txt for a matching name returns true
			//if found
			while (fileParse.hasNextLine())
			{
				String currentName = fileParse.nextLine();
				if(currentName.equals(name))
				{	
					found = true;
				}
			}
			if(found == true)
			{
				fileParse.close();
				return true;
			}
			else
			{
				fileParse.close();
				return false;
			}
			
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("File not found: Users.txt");
			e.printStackTrace();
		}
		
		return false;
	}
	
	/*-----------------addUser()------------------------------
	 * Adds a new user to the user file. If user already exists
	 * returns false.
	 * ------------------------------------------------------- */
	public boolean addUser(String name, int tempChange)
	{
		try 
		{
			FileWriter fWriter = new FileWriter("Users.txt", true);
			BufferedWriter bWriter = new BufferedWriter(fWriter);
			PrintWriter pWriter = new PrintWriter(bWriter);
			//checking if user exists. if no users exists
			//writes new user to file and add to users arraylist.
			if(!userAlreadyExists(name))
			{
				pWriter.println(name);
			    pWriter.println(tempChange);
			    System.out.println("Successfully wrote to the file.");
			    pWriter.close();
			    User userToAdd = new User(name, tempChange);
			    users.add(userToAdd);
			    
			    return true;
			}
			else
			{
				System.out.println("User already exists");
				pWriter.close();
				return false;
			}
		} 
		catch (IOException e) 
		{
			System.out.println("An error occurred while adding a new user.");
			e.printStackTrace();
		}
		  
		return false;
	}
	
	/*------removeUser(name of user to remove)------------
	 * removes a user from the user save file (Users.txt).
	 * creates a new file, adds all users except the one to delete,
	 * deletes the old save file, and renames the new file.
	 * throws exception upon error.
	 * -----------------------------------------------------
	 */
	public void removeUser(String name) throws IOException, FileNotFoundException
	{
		//checking if any users exist to delete
		if(users.isEmpty() == true)
		{
			return;
		}
		
		File inputFile = new File("Users.txt");
		File tempFile = new File("tempUsers.txt");
		tempFile.createNewFile();

		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		
		//removing user from user list
		for(int i = 0; i < users.size(); i++)
		{
			if((users.get(i).getName()).equals(name))
			{
				users.remove(i);
				break;
			}
		}
		
		//writing users to new save file
		for(int i = 0; i < users.size(); i++)
		{
			writer.write(users.get(i).getName());
			writer.newLine();
			writer.write(String.valueOf(users.get(i).getTempChange()));  
			writer.newLine();
		}
		
		//deleting old file and renaming new file
		writer.close(); 
		inputFile.delete();
		tempFile.renameTo(inputFile);	
	}
	

}
