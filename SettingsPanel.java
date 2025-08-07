//Javiolin
//
//Yishan Lin and Stanley Wang
//Period 5
//Java Game Project
//
//SettingsPanel.java
//
//This class is the settings screen of the game. It contains buttons to go to the instructions, 
//change Freeplay mode game duration, reset stats, toggle hints on, and go back to home.

//Imports
import java.awt.Color;
import java.awt.Image;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.PrintWriter;
import java.io.IOException;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

//Makes panels that show the settings of the game, which the use can adjust
public class SettingsPanel extends JPanel
{
	//All field variables
	
	//Classes
    private Game game;
    private GamePanelHolder gph;
    	
    //Game information
    private Image[] images;

	//Constructor
    public SettingsPanel(Game gameIn, GamePanelHolder gphIn)
    {
		//Initialize variables 
        game = gameIn;
        gph = gphIn;
        images = game.getImages();
        
        setLayout(null);	//Set panel layout
        setOpaque(false);	//Make the panel not opaque (to allow transparency to occur for the background)

		//Adds a new instance of a panel, which shows the setting options, to the null layout
        SettingsPaneldown spd = new SettingsPaneldown();	
        spd.setBounds(100, 150, 800, 500);
        add(spd);
    }
    
    //Draws the background and label
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);	//Clears the panel
        g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);	//Draws the background image
        g.drawImage(images[5], 300, 30, 400, 100, this);	//Draws the Settings label
    }
	
	//Clears all high scores when called during stats reset
	public void clearHighScoreFile()
	{
		//Try catch to make a print writer to clear a file
		try
		{
			PrintWriter fileClearer = new PrintWriter("Data/highscore.txt");	//New PrintWriter in highscore.txt
			fileClearer.print("");  //Clears file contents
			fileClearer.close();	//Flushes the buffer
		} 
		catch (IOException e)	//Catches errors in accessing the file and prints an error message
		{
			System.err.println("Cannot access highscore.txt");	//Prints an error message
			System.exit(1);	//Exits window
		}
	}    

    //Holds all settings options
    class SettingsPaneldown extends JPanel
    {
		//Constructor
        public SettingsPaneldown()
        {
            setLayout(new GridLayout(1, 2, 40, 0)); //Set new grid layout
            
            //Makes background translucent
            setOpaque(true);	
            setBackground(new Color(0, 0, 0, 150));	//Translucent greyishcolor
            
            //Adds panels to the holder
            add(new SettingsLeftPanel());
            add(new SettingsRightPanel());
        }

		//Makes labels for the setting options
        class SettingsLeftPanel extends JPanel
        {
			//Constructor
            public SettingsLeftPanel()
            {
                setOpaque(false);	//Allows transparency to occur for the background
                setLayout(new GridLayout(6, 1, 0, 20));	//Sets the layout

                Font labelFont = new Font("Arial", Font.BOLD, 20); 	//Makes a default font
                
                //Stores an array of all settings option label text
                String[] labels = new String[]
                {
                    "Instructions", "Game Length (Freeplay Mode)", "Reset One Stat",
                    "Reset All Stats", "Hints", "Home"
                };

				//Loops through all label names and makes them into a JLabel, and adds it to the panel
                for (int i = 0; i < labels.length; i++)
                {
                    JLabel label = new JLabel(labels[i]);	//New JLabel of the next String in the array
                    label.setFont(labelFont);	//Sets the font
                    label.setHorizontalAlignment(JLabel.CENTER);	//Aligns the label in the middle
                    label.setForeground(Color.WHITE);  //Makes the text white text for better contrast
                    add(label);	//Adds the new label to the panel
                }
            }
        }

		//A panel to display all adjustable components
        class SettingsRightPanel extends JPanel
        {
            private JSlider slider1;

            public SettingsRightPanel()
            {
                setLayout(new GridLayout(6, 1, 0, 20)); //Added vertical gap 20 in grid layout
                setOpaque(false);	//Set opaque to false for transparency in the background

				//Adds all components to the panel
                add(new InstructionsButton());	//Add instruction button
                makeSlider();	//Create slider
                add(slider1);	//Add slider
                add(new ResetOne());	//Add panel holding Jmenubar and checkboxes to reset one stat
                add(new Reset());	//Add panel holding checkboxes to reset all stats
                add(new Hints());	//Add radio button to toggle hints on/off
                add(new HomeButton());	//Add home button to return back home
            }

			//Creates and centers a button that directs the user to home panel
            class HomeButton extends JPanel
            {
				//Constructor
                public HomeButton()
                {
                    setOpaque(false);//Makes background have transparency 
                    setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));  //Centers button with spacing
                    JButton homeButton = new JButton("Home");	//Creates the button
                    homeButton.setFont(new Font("Arial", Font.PLAIN, 18));	//Sets the button font
                    
                    //Nested handler for the home button to bring the user back to the home panel
                    class HomeButtonHandler implements ActionListener
                    {
						//When clicked, goes back to home
						public void actionPerformed(ActionEvent evt)
						{
							gph.showCard("home");	//Shows the home panel
						}
					}
										
					homeButton.addActionListener(new HomeButtonHandler());	//Adds an action listener to the home button 
                    add(homeButton);	//Adds home button to panel
                }
            }

			//A button that directs the user to the first instruction panel
            class InstructionsButton extends JPanel
            {
				//Constructor
                public InstructionsButton()
                {
                    setOpaque(false);	//Sets opaque to false to ensure transparency in the background
                    setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));	//Centers the button
                    JButton instructionsButton = new JButton("Go To Instructions");	//Creates the button
                    instructionsButton.setFont(new Font("Arial", Font.PLAIN, 18));	//Sets the button's font

					//Nested handler for the instructions button to bring the user to the instructions panel
                    class InstructionsButtonHandler implements ActionListener
                    {
						//When clicked, goes to instructions panel
						public void actionPerformed(ActionEvent evt)
						{
							gph.showCard("instructions");	//Shows the instructions panel
						}
					}

                    instructionsButton.addActionListener(new InstructionsButtonHandler());	//Creates a new listener for the instructions button and adds it to the buttn
                    add(instructionsButton);	//Adds the instructions button to the panel
                }
            }     
            
			//Reset a specific statistic that is chosen from a JMenuBar
            class ResetOne extends JPanel
            {
				//Field variables for components so all methods can access the checkboxes
                private JCheckBox checkBoxTF;
                private JCheckBox checkBoxNevermind;
                
                private String command;//String that determines the check box selections

				//Constructor, creates check boxes and menus and adds them to the panel
                public ResetOne()
                {
                    setOpaque(false);	//Turns opaque off to allow transparency for the background
                    setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));	//Centers the buttons	
                    JMenuBar menu = makeStatsMenuBar();//Calls a method that creates the menu bar
                    menu.setOpaque(false);	//Allows the JMenuBar's backgrond to have transparency
                    add(menu);	//Adds the stats menu

					//Make two confirmation checkboxes that only show up fter a stat has been selected  in the JMenu
					
					//Creates checkbox for agreement and make it invisible, will turn visible once a JMenuItem is selected from the stat selection JMenuBar
                    checkBoxTF = new JCheckBox("Are you sure?");
                    
                    //Makes the background of it invisible 
                    checkBoxTF.setContentAreaFilled(false);
					checkBoxTF.setBorderPainted(false); 
					checkBoxTF.setFocusPainted(false); 
                    checkBoxTF.setVisible(false);
                    checkBoxTF.setOpaque(false);
                    
                    //Adjust styling
                    checkBoxTF.setFont(new Font("Arial", Font.PLAIN, 14));
                    checkBoxTF.setForeground(Color.WHITE);
                    
                    checkBoxTF.addActionListener(new CheckBoxListener());//Adds new instance of an action listener to the button
                    
                    add(checkBoxTF);	//Adds checkbox to the panel
                    
                    //Creates checkbox for diagreement and make it invisible, will turn visible once a JMenuItem is selected from the stat selection JMenuBar
                    checkBoxNevermind = new JCheckBox("Nevermind");
                    
                    //Makes the background of it invisible 
                    checkBoxNevermind.setContentAreaFilled(false); 
					checkBoxNevermind.setBorderPainted(false); 
					checkBoxNevermind.setFocusPainted(false); 
                    checkBoxNevermind.setVisible(false);
                    checkBoxNevermind.setOpaque(false);
                    
                    //Adjust styling
                    checkBoxNevermind.setForeground(Color.WHITE);
                    checkBoxNevermind.setFont(new Font("Arial", Font.PLAIN, 14));
                    
                    checkBoxNevermind.addActionListener(new CheckBoxListener());//Adds new instance of an action listener to the button
                    
                    add(checkBoxNevermind);	//Adds checkbox to the panel
                }

				//Nested handler for the confirmation checkbox to determine whether to reset the stat or not
                class CheckBoxListener implements ActionListener
                {
                    public void actionPerformed(ActionEvent evt)//When a checkbox is clicked, listens to the check boxes and then decides what to do
                    {
                        if (checkBoxTF.isSelected())//Checks if confirmation textboxt is selected  and resets the stat chosen by the Jmenu 
                        {
                            if (command.equals("Overall Accuracy"))//If the stat choosen is accuracy, calls methods to update the data stored in game
                            {
								//Basically changes the variables used to calculate overall accuracy back to zero, so overall accuracy is also zero
                                game.resetNotesCountTotal();//Reset the notes count total variable in game
                                game.resetNotesCorrectTotal();//Reset the note correct total in game
                                
                                gph.updateStatsInHome();	//Change stats shown in home
                            }
                            else if (command.equals("Total Note Count"))//Else if total note count is chosen
                            {
                                game.resetLifetimeNotes();	//Variable in game is changed
                                gph.updateStatsInHome();	//Change stats shown in home
                            }
                            else if (command.equals("Highscore"))	//Same idea as before but as highscore is a txt file, a method is called to create a new txt file, therefore resetting it
                            {
                                clearHighScoreFile();	//Calls method to reset high score file
                                gph.updateStatsInHome();	//Updates stats shown in home
                            }

                        }
                        
                        //After the action is completed or checkbox nevermind is selected
                        //The checkboxes are unselected and turn invisible again
                        checkBoxTF.setSelected(false);	
						checkBoxTF.setVisible(false);	                       		
                        checkBoxNevermind.setSelected(false);
                        checkBoxNevermind.setVisible(false);				
							
                    }
                }

				//This method creates the stats JMenuBar to select a stat to reset
                public JMenuBar makeStatsMenuBar()
                {
                    JMenuBar statsSelectionBar = new JMenuBar();//Creates Jmenubar
             
					//create Jmenu that is added to the JMenubar
                    JMenu statsMenu = new JMenu("Click To Choose Stats To Reset");
                    statsMenu.setFont(new Font("Arial", Font.PLAIN, 14));	//Adds a font to it
                    
                    //Sets opaque to off to allow the backgrounds to be transparent
                    statsSelectionBar.setOpaque(false);
                    statsMenu.setOpaque(false);
                    
                    //Create the JMenuItems that are added to the JMenu
                    JMenuItem accuracy = new JMenuItem("Overall Accuracy");
                    JMenuItem notes = new JMenuItem("Total Note Count");
                    JMenuItem highscore = new JMenuItem("Highscore");

                    ActionListener rsmh = new resetStatMenuHandler();//Creates an instance of the action listener that handles menubar
                    
                    //Adds the action listeners to the JMenuItems
                    accuracy.addActionListener(rsmh);
                    notes.addActionListener(rsmh);
                    highscore.addActionListener(rsmh);

					//Adds the JMenuItems to the menu, and the JMenu to the JMenuBar
                    statsMenu.add(accuracy);
                    statsMenu.add(notes);
                    statsMenu.add(highscore);
                    statsSelectionBar.add(statsMenu);

                    return statsSelectionBar;	//Returns the created JMenuBar
                }

				//Nested handler for the JMenuItems to make the check boxes visible once the JMenuItem is selected, to allow the user to confirm or cancel
                class resetStatMenuHandler implements ActionListener
                {
					//When a JMenuItem is selected
                    public void actionPerformed(ActionEvent evt)
                    {
						//Make sure the checkboxes are not selected and deselects them
                        checkBoxTF.setSelected(false); 
                        checkBoxNevermind.setSelected(false);
                        
                        command = evt.getActionCommand();//Stores the JMenuItem selected in the field variable command
                        
                        //Make the check boxes visible (Allows the user to confirm or deny the change)
                        checkBoxTF.setVisible(true);
                        checkBoxNevermind.setVisible(true);
                    }
                }
            }

			//This method has 3 check boxes and only when both the reset checkbox and confirm checkbox are clicked are all your stats reset to prevent misclick
			//Connfirmation check boxes only visible once the reset checkbox is clicked
            class Reset extends JPanel	
            {
				//Field variables for all check boxes
                private JCheckBox checkBoxAll;
                private JCheckBox checkBoxTFAll;
                private JCheckBox checkBoxAllNevermind;

				//Constructor creates and adds checkboxes to the panel
                public Reset()	
                {
                    setOpaque(false);	//Turns opaque of to allow the background to be transparent
                    setForeground(Color.WHITE);	//Sets all the text as white
                    setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));	//Centers checkboxes

					//Creates a checkbox to reset stats 
                    checkBoxAll = new JCheckBox("Click To Reset All Stats");
                    
                    //Makes the background of it transparent
                    checkBoxAll.setOpaque(false);
                    checkBoxAll.setContentAreaFilled(false); 
					checkBoxAll.setBorderPainted(false); 
					checkBoxAll.setFocusPainted(false); 
					
					//Sets style of checkbox
                    checkBoxAll.setForeground(Color.WHITE);                  
                    checkBoxAll.setFont(new Font("Arial", Font.PLAIN, 14));
                    
                    checkBoxAll.addActionListener(new CheckBoxListener());	//Adds an instance of an action listener to the button

					//Confirmation checkboxes
					//Same as before, creates checkbox and make it not opaque to have a transparent background
                    checkBoxTFAll = new JCheckBox("Are you sure?");	//For confirmation
                    checkBoxTFAll.setOpaque(false);
                    checkBoxTFAll.setContentAreaFilled(false); 
					checkBoxTFAll.setBorderPainted(false); 
					checkBoxTFAll.setFocusPainted(false); 
					
					//Sets style
                    checkBoxTFAll.setForeground(Color.WHITE);                    
                    checkBoxTFAll.setFont(new Font("Arial", Font.PLAIN, 14));
                    
                    checkBoxTFAll.setVisible(false);	//Makes it not visible at the start
                    checkBoxTFAll.addActionListener(new CheckBoxListener());	//Adds an instance of an action listener to the button
                    
                    //Same as before, creates checkbox and make it not opaque to have a transparent background
                    checkBoxAllNevermind = new JCheckBox("Nevermind");	//For disagreement
                    checkBoxAllNevermind.setOpaque(false);
                    checkBoxAllNevermind.setContentAreaFilled(false); 
					checkBoxAllNevermind.setBorderPainted(false); 
					checkBoxAllNevermind.setFocusPainted(false); 
					
					//Sets style of checkbox
                    checkBoxAllNevermind.setForeground(Color.WHITE);
                    checkBoxAllNevermind.setFont(new Font("Arial", Font.PLAIN, 14));
                    
                    checkBoxAllNevermind.setVisible(false);	//Makes it not visible at the start
                    checkBoxAllNevermind.addActionListener(new CheckBoxListener());	//Adds an instance of an action listener to the button                 

					//Adds all checkboxes to the panel
                    add(checkBoxAll);
                    add(checkBoxTFAll);
                    add(checkBoxAllNevermind);
                }
				
				//Nested listener for the checkboxes
                class CheckBoxListener implements ActionListener
                {
					//When a checkbox is selected, determines if the stats should be reset or not
                    public void actionPerformed(ActionEvent evt)
                    {						
                        if (checkBoxAll.isSelected())	//Checks if reset all stats checkbox is selected to turn the confirmation checkboxes visible
                        {
							//If it is, the make the other checkbox visible (For the user to either confirm or deny changes)
                            checkBoxTFAll.setVisible(true);
                            checkBoxAllNevermind.setVisible(true);
                        }
                        else    //If the reset all stats checkbox is deselected
                        {
							//Make the other checkboxes invisible and deselected
                            checkBoxTFAll.setVisible(false);
                            checkBoxTFAll.setSelected(false);
                            checkBoxAllNevermind.setVisible(false);
                            checkBoxAllNevermind.setSelected(false);
                        }
                        
                        if (checkBoxAll.isSelected() && checkBoxTFAll.isSelected())	//Only if both reset all stats and confirmation checkbox are BOTH selected are all stats reset
                        {															//Returns the checkboxes back to their original state	
							//Calls methods to reset the stats in game and home panel
                            game.resetNotesCountTotal();
                            game.resetNotesCorrectTotal();
                            game.resetLifetimeNotes();
                            clearHighScoreFile();
                            gph.updateStatsInHome();  //Resets stats displayed on the home panel
                                                    
                            checkBoxAll.setSelected(false);	//Deselects reset all stats checkbox for further use
                            
                            //Makes confirmation checkboxes deselected and invisible for further use
                            checkBoxTFAll.setSelected(false);
                            checkBoxTFAll.setVisible(false);
                            checkBoxAllNevermind.setSelected(false);
                            checkBoxAllNevermind.setVisible(false);                                                       
                        }
                        
                        if(checkBoxAllNevermind.isSelected())//If the disagreement checkbox is selected, do not reset stats, and change checkboxes back to their original state
                        {
							checkBoxAll.setSelected(false);	//Deselects reset all stats checkbox for further use
							
							//Makes confirmation checkboxes deselected and invisible for further use
                            checkBoxTFAll.setVisible(false);
                            checkBoxTFAll.setSelected(false);
                            checkBoxAllNevermind.setVisible(false);
                            checkBoxAllNevermind.setSelected(false);                                                        
                        }                        
                    }
                }
            }
			
			//A JRadioButton to allow the user to turn hints on or off
            class Hints extends JPanel
            {
                private JRadioButton radioButtonHint;	//Field variable for the hint button
	
				//Constructor, creates and adds the checkbox
                public Hints()
                {
                    setOpaque(false);	//Set opaque to false for transparency in the background
                    setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));	//Center button

					//Makes a new radio button to allow toggling hints on or off
                    radioButtonHint = new JRadioButton("Turn Hints Off (Difficult)");
                    
                    //Make hint button have a transparent background
                    radioButtonHint.setContentAreaFilled(false); 
					radioButtonHint.setBorderPainted(false); 
					radioButtonHint.setFocusPainted(false); 
                    radioButtonHint.setOpaque(false);
					
					//Set style of the button
                    radioButtonHint.setForeground(Color.WHITE);                    
                    radioButtonHint.setFont(new Font("Arial", Font.PLAIN, 14));
                    
                    //Makes a new action listener for the hint button and adds it to the button
                    radioButtonHint.addActionListener(new HintListener());
                    add(radioButtonHint);	//Adds the button to the panel
                }

                class HintListener implements ActionListener//Nested handler for Checkbox to toggle hints on/off
                {
					//When the check box is clicked
                    public void actionPerformed(ActionEvent evt)
                    {
						if(radioButtonHint.isSelected())	//If the checkbox is selected
							game.setHint(false);	//Set hints to false
						else   //The checkbox is not selected
							game.setHint(true);	//Turn hints back on
                    }
                }
            }

            class SliderListener implements ChangeListener	//Nested handler for slider changes
            {
				//When the value of the slider is changed
                public void stateChanged(ChangeEvent evt)
                {
                    game.setGameDuration(slider1.getValue());	//Changes game duration based on slider adjustments
                }
            }

            public void makeSlider()	//Makes a slider thaat has a transparent background with specific increments and color
            {
                slider1 = new JSlider(30, 180, 30);	//Makes the slider
                
                //Makes the background of the slider transparent
                slider1.setOpaque(false);
	
				//Set the incremements and ticks
                slider1.setMajorTickSpacing(15);
                slider1.setPaintTicks(true);
                slider1.setLabelTable(slider1.createStandardLabels(15));
                slider1.setPaintLabels(true);                
                
                //Sets style of slider
                slider1.setOrientation(JSlider.HORIZONTAL);
                slider1.setForeground(Color.WHITE);
                slider1.setFont(new Font("Arial", Font.PLAIN, 12));
                
                slider1.addChangeListener(new SliderListener());	//Adds new change listener to the slider
            }
        }
    }
}
