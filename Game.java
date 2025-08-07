//Javiolin
//
//Yishan Lin and Stanley Wang
//Period 5
//Java Game Project
//
//Game.java
//
//This is the main class of the game which should be run to start the game
//It also methods to read files, and getter setter methods to access field variables.

//Imports
import java.awt.CardLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException; 
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFrame;


//Class header 
public class Game
{
    //All field variables

    //Field variables for game statistics
	public int accuracyPercent;    //Stores the accuracy of how many notes the user got correctly
    public int highScore;
	public int notesCountTotal; //To track how many notes the user has played through the entire game session
	public int noteCorrectTotal;	//To track how many notes the user has played CORRECTLY through the entire game session
	public int lifetimeNotesTracker;	//Tracks how many notes have been played by the user in the game session, as a milestone marker
    public int currentScore;    //Saves the current score of the game session

    //Field variables for game configurations
    public int gameDuration;    //The duration of the game in seconds (adjustable)
    public String gameMode;   //The mode of the game, either "song" or "freeplay"
    public String songName;   //The name of the song that is currently being played for the game
    public boolean hint;	//Whether hints are toggled on or off

    //Field variables for game state
    public boolean gameInSession; //Boolean to track if the game is in session

    //Field variables for information
    public String[] noteNames;  //Array to store all playable notes in the game

    //Field variables for files
	public Image[] images;  //Array of all images used in the game
    public File[] audioFiles;   //Array of all audio files used in the game

    //Field variables for objects
    public GamePanelHolder gph;	//Used for all panels to be able to access GamePanelHolder and switch cards in the main card layout
    public Scanner musicScanner;	//Used to read through the music files to determine what notes to play and their durations

    //Field variables for layout
    public CardLayout cl;  //Main CardLayout to switch between the home, game, instructions, and settings

    public Game()   //Constructor to initialize variables and call methods which read files/initialize scanner
    {
        
        //Below is an array of all notes
        noteNames = new String[]{"error", "A3", "B3", "C4", "D4", //G string excluding note G3
            "E4", "F4", "G4","A4",  //D string
            "B4", "C5", "D5", "E5", //A string
            "F5", "G5", "A5", "B5", "C6", "D6", "E6", "F6"};    //E string
        cl = new CardLayout();	//The card layout to switch between main panels, e.g home page/settings
        
        //Calls Methods to read all files
        getMyImage();	
        getMyAudioFiles();
        getMyFiles();
        getMyMusicScanner();

        //Initialize game statistics
        gameMode = "song";	//Stores the gamemode selected by the user
        songName = ""; //Default song name
        
        //Reset all game session-based stats
        notesCountTotal = 0;
        noteCorrectTotal = 0;
        currentScore = 0;
        
        gameDuration = 30;	//Default game duration
        gameInSession = false; //Game is not in session at the start
        hint = true;	//Turn hints on by default

        gph = new GamePanelHolder(this); //Initialize GamePanelHolder instance

    }

    public static void main(String[] args)  //Main
    {
        Game g = new Game();    //New instance of Game
        g.start();  //Starts the game
    }

    //Creates JPanel and GamePanelHolder, which starts the game
    public void start() 
    {

        //Creates the JFrame
        JFrame frame = new JFrame("Javiolin");
        frame.setSize(1000, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setResizable(false);

        //Adds the GamePanelHolder to the JFrame
        frame.getContentPane().add(gph);
        frame.setVisible(true); //Sets the JFrame to be visible
    }
    
    //Reads all images from the Images folder
   	public void getMyImage() 
	{
        //Array of all image file names
		String[] imageName = new String[]{"settings.png", "background1.png", "title.png", "next.png", "selection.png", "settings.png", 
            "back.png", "fingerboard.png", "instructionsHome.png", "pause.png", "home.png", 
            "timer.png", "note.png", "character1.png", "selectionscreen.png", "S.png",
            "A.png", "B.png", "C.png", "D.png", "F.png",
            "chatBox.png", "feedbackKey.png", "feedbackTitle.png", "songBox.png", "instructions.png",
            "feedbackButton.png", "instructions1.png", "instructions2.png", "instructions3.png", "instructionsIntro.png",
            "instructionsGamemode.png"
            };

        //Array to store all images
		images = new Image[imageName.length];

        //Loop to try and read all images from the Images folder and saves them in an images array
		for(int i = 0; i < imageName.length; i++)
		{
            //Try catch to read through all image files, catches any IOExceptions if the image file is not found
			try 
			{
				images[i] = ImageIO.read(new File("Images/" + imageName[i]));	//Reads the next image in the images names array
			}
			catch(IOException e)	//If the file is not accessible
			{
				//Print error message
				System.err.println("\n\n\n" + imageName[i] + " can't be found.\n\n");
				e.printStackTrace();
			}
		}
	}

    //Reads all audio files from the Audio folder
    public void getMyAudioFiles()
    {
        audioFiles = new File[noteNames.length];	//Creates an array of file names with the same length as note names
        for(int i = 0; i < noteNames.length; i++)	//Loops through all of the note names to find corresponding audio file names
        {
			//Try catch to read a new file with the same name as one of the note names (+ ".wav")
            try
            {
                audioFiles[i] = new File("Audio/" + noteNames[i] + ".wav");	//Reads the audio file
            }
            catch(Exception e)	//Catches error if there is something wrong with accessing the file
            {
				//Prints error message
                System.err.println("\n\n\n" + noteNames[i] + ".wav can't be found.\n\n");
                e.printStackTrace();	
            }
        }
    }

    //Reads all text files from the Data folder
    public void getMyFiles()
    {
        //Array of all text file names
        String[] fileNames = new String[]{"music.txt", "highscore.txt"};

        //Loops through all text files and tries to reads them
        for(int i = 0; i < fileNames.length; i++)
        {
            //TryCatches any IOExceptions if the text file is not found
            try 
            {
                File musicFile = new File("Data/" + fileNames[i]);	//Reads the next file in the fileNames array
            }
            catch(Exception e)	//Prints error message and exits window if an error occurs
            {
                System.err.println("\n\n file can't be found.\n\n");
                e.printStackTrace();
            }
        }
    }

    //Creates a scanner to read the music.txt file
    public void getMyMusicScanner()
    {
        //Try catch to create a scanner to read the music.txt file, catches any IOExceptions if the file is not found
        try
        {
            musicScanner = new Scanner(new File("Data/music.txt"));	//Makes a new scanner that scans music.txt
        }
        catch (IOException e)	//Catches error if the file can't be read
        {
			//Prints error message
            System.err.println("Error loading music.txt");
            e.printStackTrace();
            
            System.exit(1);	//Exits window
        }
    }

    //Resets the music scanner, used when the game is restarted
    public void resetMusicScanner()
    {
        //Try catch to close the existing scanner and reinitialize it to read the music.txt file again
        try
        {
            if (musicScanner != null)	//Sees if a music scanner already exists
            {
                musicScanner.close(); // Close the existing Scanner
            }
            musicScanner = new Scanner(new File("Data/music.txt")); //Reinitialize the Scanner
        }
        catch (IOException e)	//Catches error if the file can't be read
        {
			//Prints error message
            System.err.println("Error resetting music.txt");
            e.printStackTrace();
            
            System.exit(1);	//Exits window
        }
    }	


    //Below I wrote many getter setter methods for all the field variables in the class,
    //so that I can access all of them from other classes. 

    //All the field variables usually have a get method, which returns the variable,
    //and a setter method, which accepts a parameter of the same data type as its corresponding
    //field variable and sets the field variable to that parameter.

    // Getters and Setters for statistics
    public int getAccuracyPercent()
    {
        return accuracyPercent;
    }

    public int getHighScore()
    {
        return highScore;
    }

    public void setHighScore(int highScore)
    {
        this.highScore = highScore;
    }

    public int getNotesCountTotal()
    {
        return notesCountTotal;
    }

    public void addNotesCountTotal(int notesCountTotalIn)
    {
        notesCountTotal += notesCountTotalIn;
    }
    
    public void resetNotesCountTotal()
    {
		notesCountTotal = 0;
	}
    
    public int getNoteCorrectTotal()
    {
		return noteCorrectTotal;
	}
	
	public void addNoteCorrectTotal(int noteCorrectTotalIn)
	{
		noteCorrectTotal += noteCorrectTotalIn;
		lifetimeNotesTracker += noteCorrectTotalIn;
	}
	
	public int getLifetimeNotes()
	{
		return lifetimeNotesTracker;
	}
	
	public void resetLifetimeNotes()
	{
		lifetimeNotesTracker = 0;
	}
	
    public void resetNotesCorrectTotal()
    {
		noteCorrectTotal = 0;
	}

    public int getCurrentScore()
    {
        return currentScore;
    }

    public void setCurrentScore(int currentScore)
    {
        this.currentScore = currentScore;
    }

    // Getters and Setters for game configurations
    public int getGameDuration()
    {
        return gameDuration;
    }

    public void setGameDuration(int gameDuration)
    {
        this.gameDuration = gameDuration;
    }

    public String getGameMode()
    {
        return gameMode;
    }

    public void setGameMode(String gameMode)
    {
        this.gameMode = gameMode;
    }

    public String getSongName()
    {
        return songName;
    }

    public void setSongName(String songName)
    {
        this.songName = songName;
    }
    
    public boolean getHint()
    {
		return hint;
	}
	
	public void setHint(boolean hintIn)
	{
		hint = hintIn;
	}

    // Getters and Setters for game state
    public boolean isGameInSession()
    {
        return gameInSession;
    }

    public void setGameInSession(boolean gameInSession)
    {
        this.gameInSession = gameInSession;
    }

    // Getters for information
    public String[] getNoteNames()
    {
        return noteNames;
    }

    // Getters for files
    public Image[] getImages()
    {
        return images;
    }

    public File[] getAudioFiles()
    {
        return audioFiles;
    }

    // Getters and Setters for classes
    public GamePanelHolder getGamePanelHolder()
    {
        return gph;
    }

    public void setGamePanelHolder(GamePanelHolder gph)
    {
        this.gph = gph;
    }

    public Scanner getMusicScanner()
    {
        return musicScanner;
    }

    public void setMusicScanner(Scanner musicScanner)
    {
        this.musicScanner = musicScanner;
    }

    // Getters for layout
    public CardLayout getCardLayout()
    {
        return cl;
    }

}
