//Javiolin
//
//Yishan Lin and Stanley Wang
//Period 5
//Java Game Project
//
//SettingsPanel.java
//
//This class is called to play audio, utilizing the java sound class. It ensures that
//audio files, stored as preloaded clips, can be played overlapped on eachother, by creating a new
//instance of a preloaded clip everytime the clip needs to be played.

//Imports
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

//Handles playback of note audio clips from files
public class AudioPlayer 
{
    private String[] noteNames; //Names of notes (e.g., "A", "B", etc.)
    private Clip[] clips;       //Array of preloaded audio clips
    private File[] audioFiles;  //Corresponding audio files for each note
	
    //Constructor, initializes note names and files, and loads the clips
	public AudioPlayer(String[] noteNamesIn, File[] audioFilesIn)
	{
		//Initialize variables
		noteNames = noteNamesIn;
		audioFiles = audioFilesIn;
		clips = new Clip[audioFiles.length];
		
		loadAudioFiles();	//Read audio files
	}

    //Loads and stores audio clips from the given files
    public void loadAudioFiles() 
    {
		//Loops through all the audio files
        for (int i = 0; i < audioFiles.length; i++)
        {
			//Try catch to read each audio file and convert it to a clip
            try 
            {
                AudioInputStream stream = AudioSystem.getAudioInputStream(audioFiles[i]);	//Creates an audio input stream to read the audio file
                //Saves the clip as the read audio file
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                clips[i] = clip;                
            } 
            catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) //Catches errors in reading the audio file
            {
				//Prints error message
                System.err.println("Failed to load: " + audioFiles[i]);	
                e.printStackTrace();	
            }
        }
    }

    //Plays the specified note clip for a shortened duration
	public void play(String noteName, int durationIn)
	{
		int duration = (int)(durationIn * 0.8); //Clip plays for 80% of given duration to prevent clip replay error
		Timer clipTimer;	//Create new timer for the clip duraition

		int index = findNoteIndex(noteName); //Get index of note in array
		Clip clip = clips[index];	//D&I a new clip based on a preloaded clip of the same index at the note to play

        //Restart a clip if it is already playing
		if (clip.isRunning())
		{			
			//Resets the same clip and restarts it
			clip.stop();
			clip.setFramePosition(0);
			clip.start();
		}
        //Otherwise, just start it from the top
		else
		{
			//Starts the clip from the start
			clip.setFramePosition(0);
			clip.start();
		}

        //Timer to stop the clip after duration
		class clipTimerHandler implements ActionListener
		{
			//After the duration is reached, reset the clip
			public void actionPerformed(ActionEvent evt)
			{
				clip.stop();	//Stops the clip from playing
				clip.setFramePosition(0);	//Resets the clip back to frame one
			}
		}
		
		//Create a new instance of an action listener for the clip timer and adds it to the timer
		clipTimerHandler cth = new clipTimerHandler();
		clipTimer = new Timer(duration, cth);	//Initializes new timer
		
		clipTimer.setRepeats(false);	//Ensures each note clip is only played one time
		clipTimer.start();	//Starts the timer for the note clip
	}
	
    //Returns the index of a note name in the noteNames array
    private int findNoteIndex(String noteName)
    {
        for (int i = 0; i < noteNames.length; i++) //Loops through the note names array
        {
            if (noteNames[i].equalsIgnoreCase(noteName))	//Checks if the audio file's note to be played (in parameters)
			{												//is at the same index of a note in the note names array
				return i;	//Returns that index
			}
        }
        return -1;	//If the note is unfindable, return -1
    }
}
