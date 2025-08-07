//Javiolin
//
//Yishan Lin and Stanley Wang
//Period 5
//Java Game Project
//
//FeedbackPanel.java
//
//This class holds the feedback panel, where the feedback, based on the user's
//performance and stats after a round of the game is finished, is displayed to the user,
//including most missed notes, the average accuracy of each note, and what to work on
//to improve their note-reading skills.

//Imports
import javax.swing.JPanel;
import java.awt.Image;
import java.io.File;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;

//Makes a panel which contains components that display feedback to the user
public class FeedbackPanel extends JPanel
{
	//All field variables
	
	//Classes
	private GamePanel game;
	
	//Game stats
	private int[] correctNotes;
	private int[] missedNotes;
	private int[] noteTotal;
	private Color[] noteAccuracy;
	
	//Game information
	private Image[] images;
	private String[] noteNames;
	
	//Components
	private JTextArea feedbackText;
	
	//Constructor
	public FeedbackPanel(GamePanel gameIn, Image[] imagesIn, String[] noteNamesIn)
	{
		//Initializes field variables
		game = gameIn;
		images = imagesIn;
		noteNames = noteNamesIn;
		correctNotes = null;
		missedNotes = null;
		noteTotal = null;
		noteAccuracy = null;
		
		setLayout(null);	//Set nayout to null
		
        //Makes the Back button
        JButton backButton = new JButton("");
        backButton.setBounds(50, 600, 100, 100);
        
        //Makes the back button transparent
        backButton.setContentAreaFilled(false); 
        backButton.setBorderPainted(false); 
        backButton.setFocusPainted(true); 
        
        //Makes the feedback text area, contained in a scroll pane
        feedbackText = new JTextArea("");
        feedbackText.setLineWrap(true);
        feedbackText.setWrapStyleWord(true);
        feedbackText.setEditable(false);
        JScrollPane feedbackPane = new JScrollPane(feedbackText);
        feedbackPane.setBounds(50, 150, 450, 400);
        
        //Nested handler for the back button      
        class BackButtonHandler implements ActionListener
		{
			//Called when the button is clicked
			public void actionPerformed(ActionEvent e)
			{
				game.changeScene("results");	//Changes scene to results
			}
		}
		
		BackButtonHandler bbh = new BackButtonHandler();	//Makes a new instance of the handler for the back button
		backButton.addActionListener(bbh);	//Adds the action listener to the back button
		
		//Add components to the panel
		add(backButton);
		add(feedbackPane);
	}
	
	//Called in MainGamePanel.java when stats are updated, to update the feedback of the round
	public void updateFeedback(int[] correctNotesIn, int[] missedNotesIn, char gradeIn)
	{
		//Initialize local variables to store incoming new stats
		int accuracyAverage;
		int mostMissedNoteAccuracy;
		String mostMissedNote;
		String notesToWorkOn;
		String notesCommonlyMissed;
		
		//Initialize variables
		accuracyAverage = 0;
		mostMissedNoteAccuracy = 100;
		mostMissedNote = "";
		notesToWorkOn = "";
		notesCommonlyMissed = "";
		
		//Set the variables as the new incoming stats
		correctNotes = correctNotesIn;
		missedNotes = missedNotesIn;
		
		//Initializes arrays to store the data after the stats are processed
		noteTotal = new int[correctNotes.length];	//Stores the amount of times each note was played
		noteAccuracy = new Color[noteTotal.length];	//This stores a color representing accuracy in an index the same as
													//the corresponding note's accuracy in the array noteNames, it is a color
													//because when drawing out each note's accuracy, it can easily be determined
													//which color to use
		//Loops through all notes to check how the average accuracy of each note the user guessed correctly during the round
		for(int i = 0; i < correctNotes.length; i++)
		{
			//Sees how many times the note was played
			noteTotal[i] = correctNotes[i] + missedNotes[i];
			
			//Prevents math error (division by 0), or else calculates the accuracy of the single note
			if(noteTotal[i] != 0)
				accuracyAverage = (int)(((double)correctNotes[i] / noteTotal[i]) * 100);
				
			if(correctNotes[i] == 0 && missedNotes[i] == 0)	//Checks if the note has not been played in this round
				noteAccuracy[i] = Color.GRAY;	//Sets accuracy color as a default color which will not be drawn
			else    //Else the note has been played
			{
				//Based on the accuracy of that note, set the corresponding color as a color ranging from teal (100% accuracy)
				//to red (<60% accuracy)
				if(accuracyAverage == 100)
					noteAccuracy[i] = new Color(0, 172, 144);	//Teal 
				else if(accuracyAverage >= 90)
					noteAccuracy[i] = new Color(0, 172, 73);	//Dark green
				else if(accuracyAverage >= 80)
					noteAccuracy[i] = new Color(179, 206, 146);	//Yellow green
				else if(accuracyAverage >= 70)
					noteAccuracy[i] = new Color(255, 218, 50);	//Yellow
				else if(accuracyAverage >= 60)
					noteAccuracy[i] = new Color(255, 143, 50);	//Orange
				else
					noteAccuracy[i] = new Color(255, 70, 50);	//Red
					
				//Check if current note's accuracy is lower than the lowest accuracy recorded so far
				if(accuracyAverage < mostMissedNoteAccuracy)
				{
					//Update most missed note accuracy
					mostMissedNoteAccuracy = accuracyAverage;
					
					//Record the note name that was most missed
					mostMissedNote = noteNames[i];
				}
				
				//If the note’s accuracy is less than 60%, mark it as commonly missed
				if(accuracyAverage < 60)
					notesCommonlyMissed += noteNames[i] + ", ";	//Add to list of commonly missed notes
				else if(accuracyAverage < 80)	//If the note is not that commonly missed, but not great either (between 60% and 80%)
					notesToWorkOn += noteNames[i] + ", ";	//Add to list of notes that can use improvement
			}
		}

		//Repaint panel to update visual changes
		repaint();

		//Generate written feedback using the stats just calculated
		feedbackWriter(gradeIn, mostMissedNoteAccuracy, mostMissedNote, notesToWorkOn, notesCommonlyMissed);
	}
	
	//Writes a personalized paragraph of feedback based on the user’s performance
	public void feedbackWriter(char gradeIn, int mostMissedNoteAccuracy, String mostMissedNote, String notesToWorkOn, String notesCommonlyMissed)
	{
		//Declare the string that will hold the entire paragraph
		String feedbackParagraph;
		feedbackParagraph = "";	//Start with an empty paragraph
		
		//Add different feedback opening messages based on the grade received

		if(gradeIn == 'S')
		{
			feedbackParagraph += "Bravo! Your performance was great, and you were able to get the highest possible grade, S (Superior)!! "
				+ "Here is some personalized feedback based on your performance to make your mastery of violin notes even stronger! ";
		}
		else if(gradeIn == 'A')
		{
			feedbackParagraph += "Good job! You played well, and got an A. You're already doing great, have a solid understanding of violin notes,"
				+ " but you still have the potential to aim for the best and try to get an S! Here is some personalized feedback based on your"
				+ " performance to help push you to the top! ";
		}
		else if(gradeIn == 'B')
		{
			feedbackParagraph += "Not bad, you did pretty decently, and demonstrated that you are comfortable with a lot of notes. However, you could still improve"
				+ " and become even better. Here is some personalized feedback based on your performance to help you become EVEN better! ";
		}
		else if(gradeIn == 'C')
		{
			feedbackParagraph += "You did okay, but there is certainly still more to improve. Nice try though, as you got most of the notes correct still!"
				+ " Here is some personalized feedback based on your performance that can help you become more comfortable with violin notes. ";
		}
		else if(gradeIn == 'D')
		{
			feedbackParagraph += "Well, you did not mess up too badly, but you could definetly get some more practice with notes so that you can more easily"
				+ " identify and play notes on the violin. Here is some personalized feedback based on your performance that will help you learn notes more efficiently. ";
		}
		else if(gradeIn == 'F')
		{
			feedbackParagraph += "Nice try, but you may not be that familiar with violin notes yet. There is still more to practice for you to become"
			+ " comfortable with notes. Here is some personalized feedback based on your performance that will help you learn notes more efficiently. ";
		}
		
		//Give a formatted feedback on notes that were okay but not consistent
		if(notesToWorkOn.length() > 2)	//Only if there are any
		{
			feedbackParagraph += "You could take a second look at the following: " + notesToWorkOn.substring(0, notesToWorkOn.length() - 2) + "."
				+ " For these notes above, you scored above 60% but under 80% correctly, so you probably know the notes, but cannot always immediately recognize them in time."
				+ " You can look at higher positions and shifts up to these notes more, which will help you become more familiar with the difficult positions of these notes. ";
		}
		
		//Give a formatted feedback on notes that were mostly missed
		if(notesCommonlyMissed.length() > 2)	//Only if there are any
		{
			feedbackParagraph += "You could spend more time on: " + notesCommonlyMissed.substring(0, notesCommonlyMissed.length() - 2) + "."
				+ " For these notes above, you scored below 60% correctly, so you might not be familiar with them. You can try to spend time and work on these notes, "
				+ "and it would be really helpful too, if you play more rounds and try to remember these locations more and slowly get better at recognizing these notes."
				+ " You can start from working on the first position of these notes first, and then look at the higher positions and shifts to other locations of these notes,"
				+ " so that you can efficiently get practice done and improve your memorization starting from something you may be more comfortable at. ";
		}
		
		//Give a formatted specific comment about the single most missed note
		if(mostMissedNoteAccuracy != 100)
		{
			feedbackParagraph += "Your most missed note, or one of your most missed notes, was " + mostMissedNote + ", with an accuracy of around " + mostMissedNoteAccuracy + "%. Remember to take note of this," 
				+ " take a close look at where this note is, and learn how to shift up to this note when you are comfortable with its location. ";
		}
		else   //Else nothing was missed
		{
			//If nothing was missed, then there is nothing to say
			feedbackParagraph += "Wait... Feedback? You got everything correct, so there is nothing much to say here. Congratulations on a successful performance,"
				+ " you did REALLY good. ";
		}
		
		//Final message to encourage playing again and continuing practice
		feedbackParagraph += "Remember: Practice makes perfect, and no matter if you are comfortable with violin notes or not, spending time on learning notes will help with your journey as a musician."
			+ " Definetly play again if you are interested, and see how you perform next time!!";
			
		//Set the feedback text area with the generated paragraph
		feedbackText.setText(feedbackParagraph);		
	}
	
	//Draws the feedback screen and note rating visuals
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);	//Clears panel drawings

		int x;	//X position for rating blocks
		int y;	//Y position for rating blocks
				
		//Draw feedback screen background and title elements
		g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);	//Background
		g.drawImage(images[7], 550, 50, 450, 675, this);	//Fingerboard
        g.drawImage(images[6], 50, 600, 100, 100, this); //Back button image
        g.drawImage(images[22], 550, 0, 450, 50, this);	//Accuracy key
        g.drawImage(images[23], 50, 50, 450, 80, this);	//Feedback title image

		//Draw note accuracy rating bars only if there were notes played
		if(noteTotal != null)
		{
			for(int i = 1; i < noteTotal.length; i++)	//Loops through all notes
			{
				y = 211;	//Reset y before adjusting
				
				//Testing purposes
				System.out.println(noteNames[i] + ": Rating- " + noteTotal[i]);

				//Adjust x and y based on note index to correctly draw out the note locationss
				if(i <= 4)	//G string
				{
					x = 749;	//G string X location
					y += (i - 1) * 52;	//Moves down to next column based on G string
				}
				else if(i <= 8)	//D string
				{
					x = 763;	//D string X location
					y += (i - 5) * 52;  //Moves down to next column based on D string
				}
				else if(i <= 12)	//A string
				{
					x = 776;	//A string X location
					y += (i - 9) * 52;	//Moves down to next column based on A string
				}	
				else  //E string
				{
					x = 789;	//E string X location
					y += (i - 13) * 52;	//Moves down to next column based on E string
				}
					
				//Draw rating bar in color only if the color is valid (not gray, which means no data)
				if(!noteAccuracy[i].equals(Color.GRAY))
				{	
					g.setColor(noteAccuracy[i]);	//Set the bar color to current note’s rating color
					g.fillRect(x, y, 12, 48);	//Draw the rating bar
				}	
			}
		}
	}
}
