//Javiolin
//
//Yishan Lin and Stanley Wang
//Period 5
//Java Game Project
//
//GamePanel.java
//
//This class is the main game panel of the game. It contains all the game logic, including the game timer, 
//note dropping, note guessing, score tracking, and results panel.

//Imports
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException; 
import java.io.FileNotFoundException; 
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.Timer;

//Class header
public class GamePanel extends JPanel
{
    //All field variables

    //Field variables for objects/panels
    private Game game;
    private GamePanelHolder gph;
    private loadingPanel loading;
    private mainGamePanel mainGame;
    private resultsPanel results;
    private FeedbackPanel feedback;
    private Scanner musicScanner;

    //Field variables for files
    private Image[] images;

    //Field variables for game stats
    private int currentScore;	
    private double currentAccuracy;	
    private int totalNoteCount;
    private int correctNoteCount;
    private String currentNote;
    private int[] missedNotes;
    private int[] correctNotes;

    //Field variables for game configuration
    private String songName;
    private int gameDuration;
    
    //Field variables for game state
	private boolean freeplayModeInSession;	

    //Field variables for information
    private String[] noteNames;

    //Field variables for layout
    private CardLayout gamePages;


    //Constructor to initialize the GamePanel and add all the panels to the card layout
    public GamePanel(Game gameIn, GamePanelHolder gphIn)
    {
        //Initialize field variables
        game = gameIn;
        gph = gphIn;
        songName = game.getSongName();
        gameDuration = game.getGameDuration();
        currentScore = 0;
        musicScanner = game.getMusicScanner();
        noteNames = game.getNoteNames();
        images = game.getImages();
        currentNote = "N/A"; //Initialize currentNote to a default value
        freeplayModeInSession = false;
        
        gamePages = new CardLayout();	//Make a new card layout to switch between scenes of the game

        //Set layout to card layout
        setLayout(gamePages);

        //Create instances of all panels and adds them to the card layout
        loading = new loadingPanel();
        mainGame = new mainGamePanel(this);
        results = new resultsPanel(this);
        feedback = new FeedbackPanel(this, images, noteNames);
        
        add(loading, "loading");
        add(mainGame, "main game");
        add(results, "results");
        add(feedback, "feedback");


        gamePages.show(this, "loading");    //Show the loading panel by default

        setFocusable(true); //Ensure the panel is focusable
        requestFocusInWindow(); //Request focus for the panel
        
    }

    //Shows the loading screen
    public void showLoadingScreen()
    {
        gamePages.show(this, "loading");    //Show the loading panel
        loading.selection();  //Start the selection screen
    }

    //Switches to a different scene (card)
    public void changeScene(String scene)
    {
        gamePages.show(this, scene);    //Switch to the specified card
    }

    //Loading panel with starts a transition to the main game
    class loadingPanel extends JPanel
    {
        private Timer timer;    //Timer for loading transition

        //Constructor
        public loadingPanel()
        {
            setLayout(null); //Set layout to null
            setBackground(new Color(30, 30, 30));   //Set background color to approximately black
        }

        //Waits for a second before switching to the main game
        public void startTransition()   
        {			
            //Resets timer
            if (timer != null && timer.isRunning())
            {
                timer.stop();   //Stop the timer if it is already running
            }

            //Create a new timer to wait for 1 second
            timer = new Timer(1000, new LoadingTransitionHandler());
            game.setGameInSession(true); //Set gameInSession to true when loading the game
            timer.setRepeats(false);    //Ensure the timer only triggers once
            timer.start();  //Start the timer
        }

        //ActionListener for the loading transition
        class LoadingTransitionHandler implements ActionListener
        {
            //When the timer triggers, switch to the main game
            public void actionPerformed(ActionEvent e)
            {
                boolean gameInSession = game.isGameInSession(); //Check if the game is in session
                timer.stop();
                if (gameInSession) //Ensure the game is in session before proceeding
                {
                    changeScene("main game");   //Switch to the main game panel
                    mainGame.startGame(); //Start the game (timer and notes)
                }
            }
        }

	//Makes a JList and JButton to select songs
    public void selection()
    {

		//Make an array of selectable song names to put on the JList
        String[] songOptions = {
          "[Demo] Twinkle Twinkle Little Star - Easy",
          "[Demo] New World Symphony Largo - Normal",
          "Mary Had a Little Lamb - Easy",
          "Twinkle Twinkle Little Star - Normal",
          "Twinkle Twinkle Little Star - Difficult",
          "Ode To Joy - Difficult",
          "Mozart Serenade Opening - Extreme (Contains Double Stops)",
          "New World Symphony Largo - Extreme (Contains Double Stops)",
          "freeplay (practice with random notes!)"
        };

		//Makes a new JList with all the song options
        JList<String> songList = new JList<>(songOptions);
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	//Ensures only one song is selectable
        songList.setVisibleRowCount(6);	//Allow better formatting

		//Makes a scroll pane and adds the JList to it so the user can scroll through all song options
        JScrollPane scrollPane = new JScrollPane(songList);
        scrollPane.setBounds(310, 220, 400, 250); //Positioning and size
        
        //Makes a new button for the user to confirm selecting a song, allowing the game to start
        JButton songSelectButton = new JButton("Select this song");
        songSelectButton.setBounds(360, 500, 300, 50);
        
        //Makes a home button to return back home if needed
        JButton homeButton = new JButton("");
        homeButton.setBounds(50, 600, 100, 100);
        homeButton.setContentAreaFilled(false); 
        homeButton.setBorderPainted(false); 
        homeButton.setFocusPainted(true); 
        
        //Nested handler for the home button, to return back home
		class HomeButtonHandler implements ActionListener
		{
			//When the button is clicked
			public void actionPerformed(ActionEvent e)
			{
				gph.showCard("home");	//Show the home panel
			}
		}
  
		//Nested handler for the song list, to select a song
        class SongListHandler implements ListSelectionListener
        {
			//When a different item is selected		
			public void valueChanged(ListSelectionEvent e)
			{
				//Checks if the user is not constantly clicking random songs to make sure the selected song is accurate
				if (!e.getValueIsAdjusting())
				{
					String selected = songList.getSelectedValue();	//Gets the selected item (song) from the list
					
					game.setGameMode("song");	//On default, the gamemode is song

					//For the following, the selected item is checked, and the song name (when formatted music is read on music.txt) is
					//set as the user's selected song on the JList
					if(selected.equals("[Demo] Twinkle Twinkle Little Star - Easy"))	//Checks what the selected song is
					{
						songName = "demo";		//Sets the song name to play as the selected song
					}
					else if(selected.equals("[Demo] New World Symphony Largo - Normal"))
					{
						songName = "new_worldDEMO";
					}						
					else if(selected.equals("Mary Had a Little Lamb - Easy"))
					{
						songName = "mary";
					}					
					else if (selected.equals("Twinkle Twinkle Little Star - Normal"))
					{
						songName = "twinkle_easy";
					}
					else if(selected.equals("Twinkle Twinkle Little Star - Difficult"))
					{
						songName = "twinkle";
					} 	
					else if(selected.equals("Ode To Joy - Difficult"))
					{
						songName = "Ode";
					}				
					else if (selected.equals("Mozart Serenade Opening - Extreme (Contains Double Stops)"))
					{
						songName = "serenade";
					}
					else if(selected.equals("New World Symphony Largo - Extreme"))
					{
						songName = "new_world";
					} 						
					else if(selected.contains("freeplay"))	//Activates freeplay mode (random notes)
					{
						songName = "freeplay";	//Sets the song name to something not on music.txt, as freeplay mode notes are randomly generated
						game.setGameMode("freeplay");	//Sets the gamemode to freeplay to allow random notes to drop
					}
				}
			}
        }
        
        //Nested handler for selection button to start the game when the button is pressed
        class SongSelectHandler implements ActionListener
        {
			//When the button is pressed
			public void actionPerformed(ActionEvent evt)
			{
				//Ensures that a song has been selected (the song name is not blank)
				if(!songName.equals(""))
				{	
					game.setSongName(songName);	//Sets the song name
					startTransition();	//Start the transition to the main game
				}
			}
		}
		
		//Makes an instance of each handler and adds them to the corresponding component
		HomeButtonHandler hbh = new HomeButtonHandler();
        homeButton.addActionListener(hbh);
        
		SongListHandler lsl = new SongListHandler();
        songList.addListSelectionListener(lsl);
        
        SongSelectHandler ssh = new SongSelectHandler();
        songSelectButton.addActionListener(ssh);

        //Adds all components to the panel
        add(scrollPane);      
        add(songSelectButton);
        add(homeButton);      
    }
        
		//Draws the background image
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);  //Clears the panel
            g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this); //Draw the background image
            g.drawImage(images[8], 50, 600, 100, 100, this); //Draws home button

        } 
    }

    //Main game panel that contains the game logic and graphics
    class mainGamePanel extends JPanel
    {
        //All field variables

        //Field variables for objects/components
        private AudioPlayer audio;
        private Timer gameTimer;
        private Timer noteTimer;
        private mainGamePanel mainGame;
        private noteDestination[] noteDest;
        private noteFall[] notes;
        private infoBarPanel infoBar;
        private statsBarPanel statsBar;
        private JLabel timerLabel; // Label to display the time
        private GamePanel gp;
        private NoteDropperHandler dropNextNote;

        //Field variables for game state
        private int timeLeft = game.getGameDuration(); // Time left in seconds

        //Field variables for files
        private Image noteImage; // Image for the note

        //Field variables for info
        private int x;
        private int arrayIndex;
        private int y;
        private int noteLength;
        private int noteX;
        private int noteY;
        private int rng;
        private int freeplayNoteID;
        private String previousNote;
        private boolean gamePaused;   
        private boolean gameContinued;   

        //Constructor
        public mainGamePanel(GamePanel gpIn)
        {
			//Initialize variables
			audio = new AudioPlayer(game.getNoteNames(), game.getAudioFiles());	//Makes a new instance of an audio player
			gp = gpIn;
            mainGame = this; 
            
            //Set game states
            gamePaused = false;
            gameContinued = false;

            setLayout(new BorderLayout());  //Set layout to border layout
            
            setOpaque(false);	//Makes sure transparent/translucent components can show up

            //Make an instance of the stats bar and info bar, sets their size, and adds them to the panel
            statsBar = new statsBarPanel();
            statsBar.setPreferredSize(new Dimension(getWidth(), 50));
            infoBar = new infoBarPanel();
            infoBar.setPreferredSize(new Dimension(getWidth(), 50));
            add(infoBar, BorderLayout.NORTH);
            add(statsBar, BorderLayout.SOUTH);

            noteImage = images[12]; //Ensures efficient access to images
            
            //Initializes arrays for all the note objects
            noteDest = new noteDestination[100];
            notes = new noteFall[100];
            

            setFocusable(true); // Make the panel focusable to receive key events
        }

        //Method to starts the game (note dropping/note readin/note generating process) depending on the user's choice and gamemode
        public void startGame()
        {
			//Initialize variables to start a game, refreshing the single game stats of the previous game
            String gameMode = game.getGameMode(); //Get the game mode to determine whether a song is selected or freeplay is selected
            gameTimer = null;
            missedNotes =  new int[noteNames.length];
            correctNotes = new int[noteNames.length];
            gamePaused = false;
            correctNoteCount = 0;
            totalNoteCount = 0;
            currentNote = "";
            
            //Resets the text on the info bar and stats bar
            infoBar.repaint();
            statsBar.repaint();
            
            //Initialize the arrays of all note drop and note destination objects to be drawn
            noteDest = new noteDestination[100];
            notes = new noteFall[100];
             
            if(!gameMode.equals("song"))    //Check if the gamemode is not song, and is Freeplay mode
            {
				freeplayModeInSession = true;	//Sets game state 
				freeplayNoteID = 0; //Reset note ID for freeplay mode
                gameDuration = game.getGameDuration(); //Set the game duration to the intended duration adjustable in settings
                startGameTimer(); //Start the timer to track time left for freeplay mode
                
                game.setGameMode("freeplay");	//Sets gamemode to freeplay                
                generateRandomNotesForFreeplay();	//Starts the random notes generating process
            }
            else    //Else the game is in song mode
            {
				freeplayModeInSession = false;	//Sets the game state
				readMusicFileAndDropNotes(mainGame); //Start dropping notes based on the user's selected song
			}

        }

        //Starts the game timer in freeplay mode
        public void startGameTimer()
        {
            timeLeft = gameDuration; // Reset the timer to the intended game duration in seconds
            
            //Stops timer if there is something wrong with it/ the timer was not stopped after the previous round
            if (gameTimer != null && gameTimer.isRunning())
            {
                gameTimer.stop();   //Stop the timer if it is already running
            }

            //Create a new timer to count down the time
            class timerHandler implements ActionListener
            {
                //When the timer triggers, update the time left and repaint the panel
                public void actionPerformed(ActionEvent e)
                {
                    timeLeft--; //Subtracts a second from the time display

                    //Checks if the time is over, and updates results
                    if (timeLeft <= 0)
                    {
						freeplayModeInSession = false;	//Changes game state
						
						//Updates the results panel information
						results.updateResults();

                        gameTimer.stop();	//Stops the game timer
                        changeScene("results"); //Switch to results page after timer ends
                    }
                    statsBar.repaint(); //Updates the timer display
                }
            }

            timerHandler th = new timerHandler(); //Create a new timer handler
            gameTimer = new Timer(1000,th); //Create a new timer to count down the time, repainting every 1 second
            gameTimer.setRepeats(true);   //Ensure the timer repeats (To allow multiple games to happen)
            gameTimer.start();  //Start the timer
        }

        //Stops the game timer
        public void stopGameTimer()
        {
            //Stops timer if the timer exists
            if (gameTimer != null && gameTimer.isRunning())
            {
                gameTimer.stop();   //Stop the timer if it is already running
            }
        }
        
        //Pauses timer when the game is paused
        public void pauseTimer()
        {
			noteTimer.stop();	//Pauses the note dropping timer so notes are frozen in place
				
			//Pauses the game timer if in freeplay mode, so that the game length stays the same
			if(gameTimer != null)
				gameTimer.stop();
		}
		
		//Unpauses the timer when the game is unpaused
		public void continueTimer()
		{
			noteTimer.start();	//Unpauses the note dropping timer so notes fall and update again
				
			//Unpauses the game timer if in freeplay mode, so that the time left of the game continues to drop 
			if(gameTimer != null)
				gameTimer.start();
		}

		//Method used in freeplay mode to generate random notes instead of reading from a file
		public void generateRandomNotesForFreeplay()
		{
			//Initializes a new note timer to track the time between spawning notes
			noteTimer = new Timer(1000, null);
			noteTimer.setRepeats(false);

			//Nested handler for the note timer
			class FreeplayNoteHandler implements ActionListener
			{
				//When the next note is ready to be dropped
				public void actionPerformed(ActionEvent e)
				{
					if (timeLeft > 0 && game.isGameInSession()) // Ensure the game is still running
					{
						//D&I int to determine what random index corresponding to a random note should be selected
						int randomIndex = 0;
						
						while(randomIndex == 0)	//While loop to avoid math error (division by 0 when randomIndex is 0)
							randomIndex = (int) (Math.random() * noteNames.length); // Generate a random note index 
							
						String randomNote = noteNames[randomIndex];	//Sets the random note as a random note from the noteNames array based on the randomly generated note index
						   
						int duration = 2;	//Sets the duration of the note as the default duration
						int randomWaitTime = 1000 + (int) (Math.random() * 2000); //Generates random note generation gap time between 1 and 3 seconds (in ms)

						//Checks if game is not almost over 
						if(timeLeft > 5)	//When time left reaches 5 seconds, no more notes are generated to prevent notes dropping right before the game ends (therefore unplayable)
						{
							currentNote = randomNote;	//Sets the current note as the newly generated note in order for hints to update
							dropNoteAcrossScreen(mainGame, randomNote, duration, freeplayNoteID); //Drops the random note
						
							freeplayNoteID++;	//Increments note ID for the next note to have a new index
						
							noteTimer.setInitialDelay(randomWaitTime); //Set next wait time
							noteTimer.restart(); //Wait before next drop
						}
						
						//Repaints all bars and the results screen to update stats  
						infoBar.repaint();
						statsBar.repaint();
						results.updateResults();
						
					}
					else    //Else when the game ends
					{				
						gp.changeScene("results");	//Go to results screen
						noteTimer.stop(); // Stop note waiting/generatint timer
					}
				}
			}

			//Makes a new instance of the action listener for the note timer and adds it to the timer
			FreeplayNoteHandler fnh = new FreeplayNoteHandler();
			noteTimer.addActionListener(fnh);
			
			noteTimer.start(); //Start the timer to start the note dropping process
		}
		
        //Reads the music file and drops notes in song mode
        public void readMusicFileAndDropNotes(mainGamePanel mainGameIn)
        {
            game.resetMusicScanner(); //Ensure the Scanner is reset before use

            musicScanner = game.getMusicScanner(); //Get the Scanner for reading music files

            String name;	//Stores the name of the next word in music.txt in order to find the user's selected song name

            mainGamePanel mainGameInstance = mainGameIn;    //Initializes the main game instance

            for (int i = 0; i < noteDest.length; i++) //When game starts, all the note objects are null
            {
                noteDest[i] = null; //Sets all note objects to null
            }
			
            statsBar.resetScore();	//Resets the stats bar

            noteTimer = new Timer(0, null); //Timer for dropping notes
            noteTimer.setRepeats(false); //Ensure the timer only triggers once per note

			//Do--while loop to read the next word in music.txt until the specified song is reached
            do   
            {
                name = musicScanner.next(); // Read the next word
            } while (!name.equals(songName)); //Checks if the scanner has reached the specified song

            // Create an ActionListener for dropping notes
            dropNextNote = new NoteDropperHandler(mainGameInstance, noteTimer, musicScanner, infoBar);
            noteTimer.addActionListener(dropNextNote);
            noteTimer.start(); // Start the note dropping process
        }

        //Calls another method that drops a note across the screen, called by both freeplay and song mode to drop notes
        public void dropNoteAcrossScreen(mainGamePanel gamePanelIn, String note, int durationIn, int noteDestIDIn) 
        {
            drawDestination(gamePanelIn, note, durationIn, noteDestIDIn); //Decide the note destination rectangle location

        }

        //Decides the location of the note destination object, based on the note passed in
        public void drawDestination(mainGamePanel gamePanelIn, String note, int durationIn, int noteDestIDIn)
        {
            //Declare local variables
            String savedNote;
            int duration;
            int section;	//Finds which block location the destination rectangle is at to help calculate note dropping speed
            
            //Initialize variables
            duration = durationIn;
            x = 525;	//Default x position for the destination rectangle
            y = 211; //Default y position for the destination rectangle
            noteLength = 48;	//Length of the note (pixels)
            section = -1;
            
            //Prevents same back-to-back notes from switching strings, to improve the note fingering logic.
            if(!note.equalsIgnoreCase(previousNote))
				rng = (int)(Math.random() * 2 );    //To solve confusion between notes available on both strings and improve variation
            
            //The rng value (between 0 and 1), is used to to decide whether the note should be placed on what string in case it is playable on two strings,
            //and you can see that in the for loop below
            
            //Loops through the length of an array of all note names to decide where to place the note on the fingerboard
            for(int i = 0; i < noteNames.length; i++)
            {
                if(note.equals(noteNames[i]))// Checks if the name of the current note scanned equals a note in the note names array
                {
                    if(i <= 4)	//The note can only be on the G string
                    {
                        x = 749; //G String X location

                        y += (i - 1) * 52;  //Moves down string to find correct Y value
                        section = i;	//Determines which section the note is played in, adjusting to the string it is placed on
                    }
                    else if(i <= 8 && i > 4) //Technically playable on G and D string
                    {
                        if(rng == 0)
                        {
                            x = 749;    //G string X location
                            y += (i - 1) * 52;  //Moves down string to find correct Y value
                            section = i;	//Determines which section the note is played in, adjusting to the string it is placed on
                        }
                        else
                        {
                            x = 763;    //D string  X location
                            y += (i - 5) * 52;  //Moves down string to find correct Y value
                            section = i - 4;	//Determines which section the note is played in, adjusting to the string it is placed on
                        }

                    }
                    else if(i <= 12 && i > 8)   //Technically playable on D and A string
                    {
                        if(rng == 0)
                        {
                            x = 763;    //D string X location
                            y += (i - 5) * 52;  //Moves down string to find correct Y value
                            section = i - 4;	//Determines which section the note is played in, adjusting to the string it is placed on
                        }
                        else
                        {
                            x = 776;    //A string X location
                            y += (i - 9) * 52;  //Moves down string to find correct Y value
                            section = i - 8;	//Determines which section the note is played in, adjusting to the string it is placed on
                        }
                    }
                    else if(i <= 16 && i > 12)  //Technically playable on A and E string
                    {
                        if(rng == 0)
                        {
                            x = 776;    //A string location X location
                            y += (i - 9) * 52;  //Moves down string to find correct Y value
                            section = i - 8;	//Determines which section the note is played in, adjusting to the string it is placed on
                        }
                        else
                        {
                            x = 789;    //E string location X location
                            y += (i - 13) * 52;  //Moves down string to find correct Y value
                            section = i - 12;	//Determines which section the note is played in, adjusting to the string it is placed on
                        }
                    }
                    else //Only E string is possible
                    {
                        y += (i - 13) * 52;  //Moves down string
                        x = 789;	//E string X location
                        section = i - 12;	//Determines which section the note is played in, adjusting to the string it is placed on
                    }
                }
            }

            //Creates new noteDestination object
            previousNote = note;	//Keeps track of previous note to prevent the next same note hopping strings (Which messes up the fingering logic)
            noteDest[noteDestIDIn] = new noteDestination(gamePanelIn, note, noteDestIDIn, x, y, duration, section);	//Create a new not destination object
            repaint();  //Repaint the panel to update the destination rectangle
        }

        //Separate handler class for the timer for dropping notes
        class NoteDropperHandler implements ActionListener
        {
            //Field variables
            private mainGamePanel gamePanel;
            private Timer noteTimer;
            private Scanner musicScanner;
            private int delay;
            private int duration;
            private int noteDestID;
            private infoBarPanel infoBar;
            private int rest;
            private String noteInfo;

            //Constructor
            public NoteDropperHandler(mainGamePanel gamePanelIn, Timer noteTimer, Scanner musicScanner, infoBarPanel infoBarIn)
            {
                //Initialize field variables
                gamePanel = gamePanelIn;
                this.noteTimer = noteTimer;
                this.musicScanner = musicScanner;
                noteDestID = 0;
                infoBar = infoBarIn;
                
            }

            //Action performed when the timer triggers
            public void actionPerformed(ActionEvent e) 
            {
                //Check if the scanner has more notes to read, and the game session has not ended
                if (musicScanner.hasNext() && game.isGameInSession()) 
                {
					
                    String noteInfo = musicScanner.next(); //Read the next note
                    
                    //Ensures the note is in correct format and the song has not ended
                    if (noteInfo.length() >= 2 && !noteInfo.equals("end")) 
                    {
                        String note = noteInfo.substring(0, 2); //Store the current note
                        duration = Integer.parseInt(noteInfo.substring(noteInfo.indexOf("(") + 1, noteInfo.indexOf(")"))); //Read the duration

                        currentNote = note; //Update the current note
                        infoBar.repaint(); //Update the hint label with the current note
                        noteDestID++;   //Increment the note destination ID

                        //Resets the note destination ID if it larger than the array length, so ID starts back at 0
                        if(noteDestID >= noteDest.length)
                        {
                            noteDestID = 0; //Reset the note destination ID
                        }

                        //Check if the scanner has a duration between two notes
                        if (musicScanner.hasNextInt())
                        {
                            rest = musicScanner.nextInt(); //Read the rest
                            delay = rest * 1000; //Convert seconds to milliseconds

                            gamePanel.dropNoteAcrossScreen(gamePanel, note, duration, noteDestID);  //Drop the note across the screen


                            //Schedule the next note drop, and restarts the timer to start the next note drop
                            noteTimer.setInitialDelay(delay);
                            noteTimer.restart();

                        } 
                        else 
                        {
                            //Drop the note immediately if no duration is specified
                            gamePanel.dropNoteAcrossScreen(gamePanel, note, delay, noteDestID);
                            noteTimer.setInitialDelay(0);   //No delay
                            noteTimer.restart();	//Restarts timer to drop the next note
                        }
                    }
                    else  //Ends the game if the note format is incorrect
                    {
						noteTimer.stop();	//Stops timer to stop dropping notes
						gp.changeScene("results");	//Go to results screen
						results.updateResults();	//Updates all the labels in the results panel to show the correct stats of the user
					}
                }
                else  //Ends the game if there are no notes left
                {
                    noteTimer.stop(); //Stop the timer when all notes are processed
                    gp.changeScene("results");	//Go to results screen
                    results.updateResults();	//Updates all the labels in the results panel to show the correct stats of the user
                }
            }
        }

        //Paints the main game panel
        public void paintComponent(Graphics g) 
        {
            super.paintComponent(g);    //Clear the panel
            
            //Draw the background images
            g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);
            g.drawImage(images[7], 550, 50, 450, 675, this);
            g.drawImage(images[13], 100, 210, 340, 400, this);
            
            g.setColor(Color.GREEN);    //Defaultly set color to green
        
            //Loops through the note destination array and draws the notes
            for (int noteID = 0; noteID < noteDest.length; noteID++) 
            {
                noteDestination noteDestObject = noteDest[noteID]; //Get the destination note object from the array
                noteFall noteFallObject = notes[noteID]; //Get the falling note object from the array
        
                if (noteDestObject != null) 
                { //Only checks and updates note if noteDestObject is not null to prevent errors
                    boolean noteEnded = noteDestObject.getState();  //Check if the note has ended

                    //Initializes all the variables for the note
                    int noteFallX = 0;
                    int noteFallY = 0; 
                    char guess = '?';
                    char note = '!';
                    
                    //Stores information of the note destination	
                    int noteX = noteDestObject.getX();
                    int noteY = noteDestObject.getY();
                    int duration = noteDestObject.getDuration();
        
					if(noteDestObject.noteIsCorrect())	//If note has been guessed correctly already
						g.setColor(Color.CYAN);	//Set note color to cyan to signify a correctly guessed note
					else
						g.setColor(Color.GREEN);	//Defaultly color is green (to signify note is not played yet)
					
                    if (noteFallObject != null) 	//Check if noteFallObject is not null
                    { 
						if(gamePaused)	//Checks if the game is paused
						{
							//Pauses the timer of the note destination and note fall objects so their durations pause and their positions don't update
							//Creating a frozen effect of the notes
							noteDestObject.pauseDestTimer();	
							noteFallObject.pauseFall();						
						}
						
						if(gameContinued)	//Checks if the game was continued (unpaused)
						{
							//Allows the note destination and note fall objects duration timers to continue and their positions to continue updating
							//Basically unfreezing the notes and resuming the game
							noteDestObject.resumeTimer();
							noteFallObject.resumeFall();						
						}

                        //Initialize note fall variables to store the location of the falling note
                        noteFallX = noteFallObject.getX();
                        noteFallY = noteFallObject.getY();

                        if(noteFallY >= noteY - noteLength && noteFallY <= noteY + noteLength)	//Only checks note accuracy if note falling is within range of note destination
                        {
							noteFallObject.setGuessOn();	//Allows the user to start guessing (Key inputs are enabled)
							guess = noteFallObject.getGuess();	//Gets what the user last input
							note = noteFallObject.getNote();	//Gets what the note should be
                        
                        
							if(guess == note && !(noteFallY > (noteLength + noteY)))	//Checks if note is guessed correctly and in range 
							{
									noteDest[noteID].noteCorrectlyGuessed();	//Marks note as correctly guessed
							}
                        
							if(noteDest[noteID].noteIsCorrect())	//Checks if the note is marked as correctly guessed
							{							
							
								if(!noteFallObject.getScoreDone())  //Check if the score has not been updated already
								{
									//Updates game statistics to help calculate accuracy in StatsBarPanel
									totalNoteCount++;
									correctNoteCount++;
									
									statsBar.updateScore(50);  //Update the score if note correctly guessed
									
									audio.play(noteDestObject.getNote() ,duration);	//Plays the note sound if it is correct
									
									//Loops through the array of note names to find which one was correctly guessed, to help calculate inidividual note accuracy in feedback
									for(int y = 0; y < noteNames.length; y++)
									{
										if(noteDestObject.getNote().equals(noteNames[y]))	//Checks if the current note destination's note is equal to the note in note names
										{
											System.out.println(noteNames[y] + "CORRECTLY GUESSED");	//Testing purposes
											correctNotes[y]++;	//Increments by one in the correctNotes array at the same index as the note in noteNames that was correctly guessed
										}
									}
									noteFallObject.setScoreDone();	//Set scoreDone to true to avoid duplicate scoring
								}
							
								g.setColor(Color.CYAN);	//Sets color to cyan to mark a correct note
							}
						}
						else if(noteFallY > (noteLength + noteY) && !noteDest[noteID].noteIsCorrect())    //Checks if note falling is guessed incorrectly and bypasses the note destination and updates score
						{
								if(!noteFallObject.getScoreDone())  //Check if the score has not been updated already
								{
									totalNoteCount++;	//Updates game statistics to help calculate accuracy in StatsBarPanel
									statsBar.updateScore(-10);  //Update the score if the note is missed
									audio.play("error", 2000);	//Plays error sound if wrong
									audio.play(noteDestObject.getNote() ,duration);	//Plays the note sound to remind the user of the note
									
									//Loops through all note names to find which note was not guessed correctly to help calculate individual note accuracy in feedback
									for(int y = 0; y < noteNames.length; y++)
									{
										if(noteDestObject.getNote().equals(noteNames[y]))	//Checks if the current note destination's note is equal to the note in note names
										{
											System.out.println(noteNames[y] + "WAS NOT CORRECTLY GUESSED");	//Testing purposes
											missedNotes[y]++;	//Increments by one in the missedNotes array at the same index as the wrongly guessed note in the note names array
										}
									}									
								}

								g.setColor(Color.RED);   //Set color to red to paint the destination rectangle if it is missed
								noteFallObject.setScoreDone(); //Set scoreDone to true to avoid duplicate scoring
						}

                        //Checks if the note exists or ended to draw the note destination block
                        if(!noteEnded && noteDestObject != null)
                            g.fillRect(noteX, noteY, 12, noteLength); //Draw the destination rectangle
                        
                        g.drawImage(noteImage, noteFallX, noteFallY, 12, noteLength, this); //Draws the falling note with newly updated coordinates
                        
                        //After the note ends, the index in the array is cleared up for further notes to take its place
                        if(noteEnded)
							noteDest[noteID] = null;
					}
                } 
            }
            
            if(gameContinued == true)
				gameContinued = false;
        }

        class noteDestination	//Object for note destinations, a new object is created each time a new note is read form the scanner
        {						//All notes are reset to null after the round ends

            //Field variables
            //Field variable for note information
            private int noteID;
            private int noteX;
            private int noteY;
            private int duration;
            private String note;
            
            //Field variables for note state
            private boolean noteEnd;
            private boolean noteCorrect;
            
            //Field variables for objects
            private Timer noteDuration;
            
            //Constructor
            public noteDestination(mainGamePanel mgp, String noteIn, int noteDestIDIn, int noteXIn, int noteYIn, int durationIn, int sectionIn)
            {
                //Initialize field variables
                //Set game state
                noteEnd = false;                
                noteCorrect = false;
                noteID = noteDestIDIn;
                noteX = noteXIn;
                noteY = noteYIn;
                note = noteIn;
                duration = durationIn * 1000; //Convert duration seconds to milliseconds

                notes[noteID] = new noteFall(note, noteX, noteY, duration, sectionIn); //Create a new falling note object
                
                //Nested handler to check when the note ends			/////MAke it so that the notefall reaches the bottom noteend is true, not reliant on timer
                class TimerHandler implements ActionListener
                {
					//Updates once the duration ends
                    public void actionPerformed(ActionEvent evt)
                    {
                        noteEnd = true;	//Sets note ended as true
                        mgp.repaint();	//Repaints to clear the current note destination object from the screen
                    }
                }
                
                TimerHandler th = new TimerHandler();	//Makes a handler for the timer
                noteDuration = new Timer((int)(duration * 2.5), th);	//Makes the timer that a while after the duration of the note is reached
                noteDuration.setRepeats(false); //Ensure the timer only triggers once
                noteDuration.start(); //Start the timer
            }
            
            public void pauseDestTimer()
            {
				noteDuration.stop();
			}
			
			public void resumeTimer()
			{
				noteDuration.start();
			}
            
            //Getter methods to access note destination information
            
            public int getID()
            {
                return noteID;
            }
            
            public int getX()
            {
                return noteX;
            }
            
            public int getY()
            {
                return noteY;
            }
            
            public int getDuration()
            {
                return duration;
            }
            
            public String getNote()
            {
				return note;
			}
            
            //Getter setter methods to access note destination state
            public boolean noteIsCorrect()
            {
				return noteCorrect;
			}
			
			public void noteCorrectlyGuessed()
			{
				noteCorrect = true;
			}
			
			public void noteEnded()
			{
				noteEnd = true;
			}
            
            public boolean getState()
            {
                return noteEnd;
            }
        } 

        class noteFall  //Object for falling notes
        {
            //D&I variables
            private int x; //Image x coordinate
            private int y;   //Image y coordinate
            private int speed; //Speed of note fall in pixels per tick
            private char guess;	//Stores the user's guess
            private Timer noteTimer;	//Timer to update the note's location every 30 miliseconds
            private String note;   //Note name of this note fall instance
            private boolean scoreDone; //Used to check if score has been updated already to avoid duplicated score counts
            private boolean guessOn;	//Whether the note is available to guess

            public noteFall(String noteIn, int xIn, int yIn, int durationIn, int sectionIn) 
            {
				int section; //The location of the note destination, helps ensure proportional falling speed in speed calculation
				int duration;	//Duration of the note, helps calculate falling speed
				note = noteIn;
				note = note.substring(0, 1).toLowerCase();	//Gets note name 
				guess = '?';
                x = xIn; //Set the x coordinate
                y = 50; //Set the y coordinate
                section = sectionIn; //Set the section of the violin the note destination is at
                duration = durationIn;
                guessOn = false;	//Cannot guess until note reaches note dest bounds
                
                //A formula used to calculate the pixels per second of the note dropping. It ensures that the falling note will reach the note
                //destination block in the specified note's duration
                speed = (int)(((section + 3) * (700 / 13.0) * 30.0) / duration);

                scoreDone = false; //Initialize scoreDone to false

                //Set up the Timer to update the position every 30ms
                noteTimer = new Timer(30, null); //Timer for falling notes
                noteTimer.setRepeats(true); //Ensure the timer repeats

				//Nested handler for the timer to update the Y location, which is basically an animation that shows the note falling
                class timerHandler implements ActionListener
                {
					//Updates y value for every period the timer ends
                    public void actionPerformed(ActionEvent e)
                    {
                        y += speed; // Move the rectangle down
                        
                        if (y > getHeight()) 	//Checks if the note has fell off screen
                        { 
                            noteTimer.stop(); // Stop the timer when the note goes off the screen
                        }
                        mainGame.repaint(); // Repaint the panel to update the rectangle's position
                    }
                }
                timerHandler th = new timerHandler(); //Create a new timer handler
                noteTimer.addActionListener(th); //Add the action listener to the timer
                startFalling();	//Calls a method to start the timer
                
				//Make a new handler to detect key presses and adds it to the object
                noteFallHandler nfh = new noteFallHandler();
                addKeyListener(nfh);
                
                //Gets focus to the window
                setFocusable(true);
                requestFocusInWindow();                
            }
            
            public void pauseFall()
            {
				noteTimer.stop();
			}
			
			public void resumeFall()
			{
				noteTimer.start();
			}

			//Getter methods to access note fall information
			
            public int getX()
            {
                return x;
            }
            public int getY()
            {
                return y;
            }
            
            public char getNote()
            {
				return note.charAt(0);
			}
            
            public char getGuess()
            {
				return guess;
			}

			//Getter setter methods to access note fall state
			
            public boolean getScoreDone()
            {
                return scoreDone;
            }

            public void setScoreDone()
            {
                scoreDone = true;
            }

			public void setGuessOn()
			{
				guessOn = true;
			}
			
			
			//Starts the timer
            public void startFalling() 
            {
                noteTimer.start(); // Start the Timer manually
            }
            
            //Nested class to deal with key presses (Which are basically the user's guesses)
            class noteFallHandler implements KeyListener
            {
				//Gets the key the user pressed and stores it in a field variable called "guess"
				public void keyPressed(KeyEvent evt)
				{
					if(guessOn)	//Guesses only count once noteFall reaches note dest bounds
					{
						requestFocusInWindow();	//Requests focus once a key is pressed
						guess = evt.getKeyChar();	//Sets guess as the user's key press
						
						if(guess >= 'A' && guess <= 'Z')	//Checks if the user's input is uppercase
							guess += 32;	//Converts the uppercase character to lowercase
					}
				}
				
				//Unused methods part of the key listener interface
				public void keyReleased(KeyEvent evt){}				
				public void keyTyped(KeyEvent evt){}
			}
        }

		//Shows information about the current game session
        class infoBarPanel extends JPanel
        {
			//Field variables for components
            private JButton home;
            private JButton pause;
            private JLabel timerLabel;
            private JLabel hintLabel;
            private JLabel pauseLabel;

            public infoBarPanel()	//Constructor
            {	
                setBackground(Color.BLACK);	//Sets background to black
                setLayout(null);	//Sets layout to null

				//Makes a JButton to direct user back home 
                home = new JButton();
                home.setContentAreaFilled(false); 	//Make it transparent (So that a button image can be drawn over)
                home.setBorderPainted(false); 
                home.setFocusPainted(true); 
                home.setBounds(15, 5, 40, 40);
            
				//Make a JButton to be able to pause the game
                pause = new JButton();
                pause.setContentAreaFilled(false); 	//Make it transparent (So that a button image can be drawn over)
                pause.setBorderPainted(false); 
                pause.setFocusPainted(true); 
                pause.setBounds(940, 5, 40, 40);

				//Make a JLabel that displays the time left in the game (if the game is in freeplay mode)
                timerLabel = new JLabel("Time Left: " + timeLeft + "s");
                timerLabel.setForeground(Color.WHITE);
                timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
                timerLabel.setBounds(100, 5, 200, 40);

				//Make a JLabel that displays the current note as a hint (if hints are toggled on)
                hintLabel = new JLabel("Hint: " + currentNote);
                hintLabel.setForeground(Color.WHITE);
                hintLabel.setFont(new Font("Arial", Font.BOLD, 16));
                hintLabel.setBounds(300, 5, 400, 40);
                

				//Make a JLabel that tells the user what to press to pause the game
                pauseLabel = new JLabel("Pause");
                pauseLabel.setForeground(Color.WHITE);
                pauseLabel.setFont(new Font("Arial", Font.BOLD, 16));
                pauseLabel.setBounds(890, 5, 400, 40);
                
                //Add all components to the panel
                add(pause);
                add(home);                
                add(timerLabel);
                add(hintLabel);        
                add(pauseLabel);       

				//Make instances of action listeners and add them to the components
                homeButtonHandler hb = new homeButtonHandler();
                pauseButtonListener pbl = new pauseButtonListener();
                home.addActionListener(hb);
                pause.addActionListener(pbl);
            }

			//Updates labels and draws images
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);	//Clears the panel
                g.drawImage(images[9], 950, 10, 30, 30, this); //Draw pause.png on the pause button
                g.drawImage(images[10], 20, 10, 30, 30, this); //Draw home.png on the home button
                g.drawImage(images[11], 65, 10, 30, 30, this); //Draw timer.png next to the timer label
                
                //Checks if the gamemode is freeplay
                if(game.getGameMode().equals("freeplay"))	//Updates game timer label if in freeplay mode
					timerLabel.setText("Time Left: " + timeLeft + "s"); //Update the timer label
				else    //Otherwise the timer should be off
					timerLabel.setText("Timer Off for Song Mode"); //Update the timer label
                if(game.getHint())	//Checks if hints are configured on in settings
                {
					if(currentNote.equals(""))	//Checks if no notes are played yet, also avoids index out of bounds error
						hintLabel.setText("Hint: "); //Update the hint label
					else   //Else update the hint with the current note
						hintLabel.setText("Hint: " + currentNote.substring(0, 1)); //Update the hint label
				}
				else    //Else hints are off
					hintLabel.setText("Hints: Off, Can Turn On in Settings");	//Update the hint label
            }
            
            //Nested handler class for the pause button to freeze the screen, utilizing a JOptionPane 
            class pauseButtonListener implements ActionListener
            {
				//Ends game session and returns user back home once pressed
                public void actionPerformed(ActionEvent e)
                {
					mainGame.pauseTimer();	//pauses the note timer to prevent further note drops
					gamePaused = true;	//Sets game state to stop the note animation in paintComponent on the next repaint
					
					//Makes a JOptionPane with options to resume game
					int choice = JOptionPane.showConfirmDialog(null, "Resume Game? (Please give the game time to load)",
                                                   "Game Paused", JOptionPane.DEFAULT_OPTION,
													JOptionPane.INFORMATION_MESSAGE);
					//If the user clicks okay or closes the panel, the game will continue  
					if (choice == JOptionPane.OK_OPTION || choice == JOptionPane.CLOSED_OPTION)
					{
						mainGame.continueTimer();	//Continues note dropping timer
						
						//Sets game state to allow changes in the paintComponent method to occur, continuing note animation
						gamePaused = false;	
						gameContinued = true;					
					}
                }
            }
           

			//Nested handler class for the home button
            class homeButtonHandler implements ActionListener
            {
				//Can end game session and return user back home once pressed
                public void actionPerformed(ActionEvent e)
                {
					mainGame.pauseTimer();	//When clicked, pauses timer to stop game
					gamePaused = true;	//Updates game state
					
					//Makes a new JOptionPane to show the option to quit the game or not
					int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit the game? Stats won't save!",
                                                   "Game Paused", JOptionPane.OK_CANCEL_OPTION);  
                    //Checks the choice to determine what action happens
					if (choice == JOptionPane.OK_OPTION)	//If "OK" is selected
					{
						stopGameTimer(); //Stop the timer when exiting the main game page
						game.setGameInSession(false); //Set gameInSession to false when returning to home                    
						gph.showCard("home"); //Navigate to the home page						          
						JOptionPane.showMessageDialog(null, "Round Over!");	//Makes new JOptionPane which says "Round Over"					
					}
					else    //When anything else (Cancel/Close Panel) is clicked
					{
						mainGame.continueTimer();	//Resumes timer
						//Updates game state
						gamePaused = false;
						gameContinued = true;
					}
                }
            }
        }
    }

	//Displays the user's stats in the main game panel
    class statsBarPanel extends JPanel
    {

		//Declare field variable for JLabels
        private JLabel scoreLabel;
        private JLabel accuracyLabel;
        
        public statsBarPanel()	//Constructor
        {
			//Sets background color and layout 
            setBackground(Color.BLACK);
            setLayout(null);

			//Initializes score label
            scoreLabel = new JLabel("Score:" + currentScore);
            scoreLabel.setForeground(Color.WHITE);
            scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
            scoreLabel.setBounds(100, 5, 200, 40);
            
            //Initializes accuracy label
            accuracyLabel = new JLabel("Accuracy: " + "N/A");
            accuracyLabel.setForeground(Color.WHITE);
            accuracyLabel.setFont(new Font("Arial", Font.BOLD, 16));
            accuracyLabel.setBounds(300, 5, 200, 40);

			//Adds label to the panel
            add(scoreLabel);
            add(accuracyLabel);
        }
        
        //Resets all user stats (As game restarts)
        public void resetScore()
        {
			//Sets the current game stats to 0 by default
			currentScore = 0;
			currentAccuracy = 0.0;
			totalNoteCount = 0;
			correctNoteCount = 0;
			
			//Resets JLabels to default
			scoreLabel.setText("Score: " + currentScore);
			accuracyLabel.setText("Accuracy: N/A");
		}

		//Updates the current score and score JLabel
        public void updateScore(int score)
        {
            currentScore += score;	//Adds the score in parameters to the score variable
            scoreLabel.setText("Score: " + currentScore);	//Updates score label to show current score
            updateAccuracy();	//Calls updateAccuracy to make sure it is directly updated along with the score
        }
        
        //Updates the accuracy score and the accuracy JLabel
        public void updateAccuracy()
        {
			if(totalNoteCount != 0)	//Checks if there are more than 0 total notes to avoid math error (cannot divide by 0)
			{
				currentAccuracy = ((double)correctNoteCount/totalNoteCount) * 100;	//Basic formula to calculate accuracy, updates as game goes on
				accuracyLabel.setText("Accuracy: " + String.format("%.2f", currentAccuracy) + " %");	//Updates accuracy label to show current accuracy
			}																							//Utilizes format to display up to two decimal places of the accuracy
		}
    }

	//A panel which shows up after the main game ends, displaying the user's stats and results for the game
    class resultsPanel extends JPanel	
    {
		//All field variables
		
		//Field variables for fonts
		private Font title;
		private Font buttonText;   
		private Font subtitle;  
		
		//Field variables for components
		private JLabel scoreLabel;
		private JLabel accuracyLabelResults;
		private JLabel notesPlayedLabel;
		
		//Field variables for classes
		private gradePanel gp;
		private GamePanel gameP;
		
		//Field variables for objects 
		private PrintWriter output;
		private Scanner lineReader;
		
		//Field variables for game information
		private File highscoreFile;
		private String savedLines;
		
		
        public resultsPanel(GamePanel gamePIn) // Constructor
        {
                highscoreFile = new File("Data/highscore.txt");	//Makes an instance of the highscore.txt tile
                
                savedLines = "";	//Resets the saved lines of the saved highscore.txt

                gameP = gamePIn;	//Initialize instance of game panel

				//Try catch to initialize the scanner
                try 
                {
                        lineReader = new Scanner(highscoreFile);	//New instance of a Scanner to read through the highscore.txt
                } 	
                catch(FileNotFoundException e) //Catches error if highscore.txt can't be found
                {
                        System.err.println("Error, line highscore.txt not found");	//Prints error message in terminal
                        System.exit(1);	//Exits window
                }

				//Initialize fonts
                title = new Font("Arial", Font.BOLD, 100);
                buttonText = new Font("Arial", Font.PLAIN, 30);
                subtitle = new Font("Arial", Font.BOLD, 24); 

                setLayout(null);	//Sets layout
                setOpaque(false);	//Makes panel not opaque to allow translucent backgrounds

				//Makes a JPanel to show all results and adds it to the panel
                JPanel results = new JPanel();
                results.setLayout(new GridLayout(2, 2));
                results.setOpaque(false);	//Makes the panel not opaque to allow translucent backgrounds
                results.setBounds(50, 50, 900, 625);
                add(results);

				//Makes an instance of a panel which shows the player's grade and adds it to results
                gp = new gradePanel();
                results.add(gp);

				//Make an instance of a panel which shows all the number stats of the user and adds it to results
                JPanel allStatsPanel = new JPanel();
                results.add(allStatsPanel);
                
                allStatsPanel.setBackground(new Color(0, 0, 0, 150));	//Set all stats panel to a translucent gray color
                allStatsPanel.setLayout(new GridLayout(4, 1, 10, 10));	//Sets layout of all stats panel to a grid layout

				//Makes Jlabels that show the user's stats and sets them with a specific font and text color
				//Shows score
                scoreLabel = new JLabel("Score: " + currentScore);             
                scoreLabel.setFont(subtitle);
                scoreLabel.setForeground(Color.WHITE);
				
				//Shows accuracy
                accuracyLabelResults = new JLabel("Accuracy: " + currentAccuracy);
                accuracyLabelResults.setFont(subtitle);
                accuracyLabelResults.setForeground(Color.WHITE);

				//Shows how many notes the user got correct out of the total notes dropped
                notesPlayedLabel = new JLabel(correctNoteCount + " out of " + totalNoteCount + " correct!");
                notesPlayedLabel.setFont(subtitle);
                notesPlayedLabel.setForeground(Color.WHITE);
				
				//Adds all labels to the all stats panel
                allStatsPanel.add(scoreLabel);
                allStatsPanel.add(accuracyLabelResults);
                allStatsPanel.add(notesPlayedLabel);

				//Makes an instance of a return home panel and adds it to results
                JPanel rhp = new returnHomePanel();
                results.add(rhp);

				//Makes an instance of a feedback panel and adds it to results
                JPanel feedbackP = new feedbackPanel();
                results.add(feedbackP);
        }

		//A JPanel that shows the user's grade
        class gradePanel extends JPanel
        {
                private int gradeImageIndex;	//Stores the index of an image in images array corresponding to the grade the user got which should be printed

                public gradePanel()	//Constructor
                {
						//Sets layout to null and background to a translucent gray color
                        setLayout(null);
                        setBackground(new Color(0, 0, 0, 150));
                        
                        gradeImageIndex = 0;	//Initialize gradeImageIndex
                }

				//Draws the grade
                public void paintComponent(Graphics g)
                {
                        super.paintComponent(g);	//Clears the panel's drawings
                        g.drawImage(images[gradeImageIndex], 125, 50, 200, 200, this);	//Draws the user's earned grade
                }

				//Called to update the index of the grade's image to draw
                public void updateGrade(int score)
                {
                        gradeImageIndex = score;	//Stores the index of the grade image
                        repaint();	//Repaints the panel to show the new grade
                }
        }

		//A panel that contains a button to direct the user to a panel which has feedback for the user based on their performance for the round
        class feedbackPanel extends JPanel
        {
			//Constructor
			public feedbackPanel()
            {
				//Sets layout to null and background to a translucent gray
                setLayout(null);
                setBackground(new Color(0, 0, 0, 150));
                        
                //Makes and scales an image icon with an image of a feedback button
                ImageIcon originalIcon = new ImageIcon(images[26]);
                Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

				//Makes a button with an image icon to direct the user to the feedback page, and adds it to the panel
                JButton feedbackButton = new JButton(scaledIcon);
                feedbackButton.setFont(buttonText);
                feedbackButton.setContentAreaFilled(false);
                feedbackButton.setFocusPainted(false);
                feedbackButton.setBounds(100, 60, 200, 200);
                add(feedbackButton);

				//Makes a new instance of an action listener for feedback button and adds it to feedback button
                feedbackButtonHandler fbh = new feedbackButtonHandler();
                feedbackButton.addActionListener(fbh);
            }

			//Nested handler for the feedback button to bring the user to the feedback page
            class feedbackButtonHandler implements ActionListener
            {
				//When clicked, changes card to the feedback page
				public void actionPerformed(ActionEvent e)
                {
					gameP.changeScene("feedback");	//Calls a method in game panel to change the card to the feedback page
                }
            }
        }

		//A panel that contains a button to bring the user back home
        class returnHomePanel extends JPanel
        {
			//Constructor
			public returnHomePanel()
            {
				//Sets layout to null and sets the background to a translucent gray
				setLayout(null);
                setBackground(new Color(0, 0, 0, 150));                        

				//Makes and scales an image icon with an image of a return home button
                ImageIcon originalIcon = new ImageIcon(images[8]);
                Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

				//Makes a button with an image icon and adds it to the panel
                JButton homeButton = new JButton(scaledIcon);
                homeButton.setFont(buttonText);
                homeButton.setContentAreaFilled(false);
                homeButton.setFocusPainted(false);
                homeButton.setBounds(100, 60, 200, 200);
                add(homeButton);

				//Makes a new instance of a action listener for the home button and adds it to the home button
                homeButtonHandler hbl = new homeButtonHandler();
                homeButton.addActionListener(hbl);
           }

		   //Nested handler for the home button which brings the user to the home page
           class homeButtonHandler implements ActionListener
           {
			    //When the button is clicked, brings the user back to home page and updates game state and stats to display in home
				public void actionPerformed(ActionEvent e)
                {
					game.setGameInSession(false);	//Updates game state
					gph.showCard("home");	//Shows the home page
                    gph.updateStatsInHome();	//Updates the stats that are shown on the home page
                }
           }
        }

		//Updates results displayed in the results panel
        public void updateResults()
        {
			//D&I char to store what grade the user got
			char grade;
			grade = '?';
			
			savedLines = "";	//Resets the lines saved to print out in highscore.txt
			
			//Updates all stats label with the current stats
			scoreLabel.setText("Score: " + currentScore);
            accuracyLabelResults.setText("Accuracy: " + String.format("%.2f", currentAccuracy) + " %");
            notesPlayedLabel.setText(correctNoteCount + " out of " + totalNoteCount + " correct!");
                
            //Checks if freeplay mode is in session (round has not ended yet), to avoid duplicate note count increments and highscore printings(freeplay mode refreshes stats often)
            if(!freeplayModeInSession)
            {
				//Adds the individual round stats to the accumulate stats
				game.addNotesCountTotal(totalNoteCount);
				game.addNoteCorrectTotal(correctNoteCount);           
            
				//Try catch to make a scanner that reads through highscore.txt file and copies all the score down to save them as new PrintWriters clear all previous lines of code
				try 
				{
					lineReader = new Scanner(highscoreFile);	//New scanner to read through highscore.txt
					while (lineReader.hasNextLine()) 	//Continuosly adds score in highscore.txt to a saved string of text until no lines are left
					{
						savedLines += lineReader.nextLine() + "\n";	//Adds the next score in highscore.txt to the saved string
					}
					lineReader.close();	//Closes scanner                      
				} 
				catch (FileNotFoundException e)	//Catches error in accessing highscore.txt
				{
					System.err.println("Error: highscore.txt not found.");	//Prints error message in terminal
					System.exit(1);	//Exits window
				}

				//Try catch to make a PrintWriter which adds all previous lines of scores to highscore.txt while keeping the old ones by printing the previously saved lines
				try 
				{
					highscoreFile = new File("Data/highscore.txt");	//Reset highscore file to allow previous scores and the current score to be put in
					PrintWriter output = new PrintWriter(highscoreFile);	//New PrintWriter that writes to highscore.txt
					output.print(savedLines);	//Prints out all previously saved scores onto highscore.txt
					output.println(currentScore);	//Prints the latest score to highscore.txt
					output.close();	//Closes PrintWriter to flush the buffer
				} 
				catch (IOException e)	//Catches error if there is any problem writing to highscore.txt
				{
					System.err.println("Error: Cannot write to highscore.txt");	//Prints error message in terminal
					System.exit(2);	//Exits window
				}
			}
			//Determines what grade the user gets and what grade image should be drawn according to the grade received
            if(currentAccuracy >= 95)	//Checks the accuracy percentage
			{
				grade = 'S';	//Saves the grade the user received
				gp.updateGrade(15);	//Updates the grade to be drawn with the index of the corresponding grade image in the images array
            }
            //The following else if/else follow the same format to determine what grade the user gets
            else if(currentAccuracy >= 90)
            {
				grade = 'A';
                gp.updateGrade(16);
            }
            else if(currentAccuracy >= 80)
			{
				grade = 'B';
                gp.updateGrade(17);
            }
            else if(currentAccuracy >= 70)
            {
				grade = 'C';
				gp.updateGrade(18);
			}
            else if(currentAccuracy >= 60)
			{
				grade = 'D';					
				gp.updateGrade(19);
			}
			else
			{
				grade = 'F';
				gp.updateGrade(20);
			}         
            feedback.updateFeedback(correctNotes, missedNotes, grade);	//Updates the feedback page to structure and show the latest feedback
        }

		//Clears the panel and draws the background image
        public void paintComponent(Graphics g)
        {
                super.paintComponent(g);	//Clears the drawings on the panel
                g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);	//Draws the background image
        }

    }
}
