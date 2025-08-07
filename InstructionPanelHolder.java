//Javiolin
//
//Yishan Lin and Stanley Wang
//Period 5
//Java Game Project
//
//InstructionPanelHolder.java
//
//This class holds all the instruction panels of the game, that includes instructions on
//how to play the game. It uses a CardLayout to switch between panels.

//imports
import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//Class header
public class InstructionPanelHolder extends JPanel
{
    //All field variables
    //Field variables for objects
    private CardLayout infoPages;
    private GamePanelHolder gph;
    private Game game;
	private Image[] images;
	private InstructionPanelHolder iph;

    //Makes a card layout to hold all the instruction panels and adds panels to it
    public InstructionPanelHolder(Game gameIn, GamePanelHolder gphIn)
    {
		iph = this;
        game = gameIn;
        gph = gphIn;
        images = game.getImages();

        //Set layout to card layout
        infoPages = new CardLayout();
        setLayout(infoPages);

        //Create instances of all instruction panels and add them to the card layout
        firstInstructionPanel fip = new firstInstructionPanel(this);
        secondInstructionPanel sip = new secondInstructionPanel(this);
        thirdInstructionPanel tip = new thirdInstructionPanel(this);
        fourthInstructionPanel foip = new fourthInstructionPanel(this);

        //Add all the panels to the card layout
        add(fip, "firstInstructionPanel");
        add(sip, "secondInstructionPanel");
        add(tip, "thirdInstructionPanel");
        add(foip, "fourthInstructionPanel");

        //Show the first instruction panel by default
        infoPages.show(this, "firstInstructionPanel");  
    }

    //The first instruction panel
    class firstInstructionPanel extends JPanel
    {
        //Constructor to initialize the first instruction panel
        public firstInstructionPanel(InstructionPanelHolder iph)
        {
            setLayout(null);    //Set layout to null

            //Next button to go to second panel
            JButton nextButton = new JButton("");
            nextButton.setBounds(875, 600, 100, 100);
            nextButton.setContentAreaFilled(false); 
            nextButton.setBorderPainted(false); 
            nextButton.setFocusPainted(true); 
            nextButton.addActionListener(new NextButtonHandler(iph, "secondInstructionPanel"));

            //Home button to go back to home screen
            JButton homeButton = new JButton("");
            homeButton.setBounds(50, 600, 100, 100);
            homeButton.addActionListener(new HomeButtonHandler());
            homeButton.setContentAreaFilled(false); 
            homeButton.setBorderPainted(false); 
            homeButton.setFocusPainted(true); 

            //Add components to panel
            add(homeButton);
            add(nextButton);
        }

        //Draws the background and all relevant images
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);	//Clears panel drawings
            g.drawImage(images[1], 0, 0, getWidth(), getHeight(), this);    //Background
            g.drawImage(images[30], 100, 200, 300, 300, this);              //Instructions
            g.drawImage(images[31], 400, 200, 450, 450, this);              //Instructions
            g.drawImage(images[8], 50, 600, 100, 100, this);                //Home button icon
            g.drawImage(images[25], 175, 50, 650, 100, this);               //Instructions title text
            g.drawImage(images[3], 875, 600, 100, 100, this);               //Next button icon
        }
    }

    //The second instruction panel
    class secondInstructionPanel extends JPanel
    {
        //Constructor to initialize the second instruction panel
        public secondInstructionPanel(InstructionPanelHolder iph)
        {
            setLayout(null);    //Set layout to null

            //Back button to go back to previous panel
            JButton backButton = new JButton("");
            backButton.setBounds(750, 600, 100, 100);
            backButton.setContentAreaFilled(false); 
            backButton.setBorderPainted(false); 
            backButton.setFocusPainted(true); 
            backButton.addActionListener(new NextButtonHandler(iph, "firstInstructionPanel"));

            //Next button to go to next panel
            JButton nextButton = new JButton("");
            nextButton.setBounds(875, 600, 100, 100);
            nextButton.setContentAreaFilled(false); 
            nextButton.setBorderPainted(false); 
            nextButton.setFocusPainted(true); 
            nextButton.addActionListener(new BackButtonHandler(iph, "thirdInstructionPanel"));

            //Home button to go to home screen
            JButton homeButton = new JButton("");
            homeButton.setBounds(50, 600, 100, 100);
            homeButton.setContentAreaFilled(false); 
            homeButton.setBorderPainted(false); 
            homeButton.setFocusPainted(true); 
            homeButton.addActionListener(new HomeButtonHandler());

            //Add components to panel
            add(backButton);
            add(nextButton);
            add(homeButton);
        }

        //Draws the background and navigation icons
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);	//Clears panel drawings
            g.drawImage(images[27], 0, 0, getWidth(), getHeight(), this);   //Instructions
            g.drawImage(images[3], 875, 600, 100, 100, this);               //Next button icon
            g.drawImage(images[6], 750, 600, 100, 100, this);               //Back button icon
            g.drawImage(images[8], 50, 600, 100, 100, this);                //Home button icon
        }
    }

    //The third instruction panel
    class thirdInstructionPanel extends JPanel
    {
        //Constructor to initialize the third instruction panel
        public thirdInstructionPanel(InstructionPanelHolder iph)
        {
            setLayout(null);    //Set layout to null

            //Next button to go to fourth panel
            JButton nextButton = new JButton("");
            nextButton.setBounds(875, 600, 100, 100);
            nextButton.setContentAreaFilled(false); 
            nextButton.setBorderPainted(false); 
            nextButton.setFocusPainted(true); 
            nextButton.addActionListener(new NextButtonHandler(iph, "fourthInstructionPanel"));
            
            //Back button to go back to second panel
            JButton backButton = new JButton("");
            backButton.setBounds(750, 600, 100, 100);
            backButton.setContentAreaFilled(false); 
            backButton.setBorderPainted(false); 
            backButton.setFocusPainted(true); 
            backButton.addActionListener(new BackButtonHandler(iph, "secondInstructionPanel"));

            //Home button to go back to home screen
            JButton homeButton = new JButton("");
            homeButton.setBounds(50, 600, 100, 100);
            homeButton.setContentAreaFilled(false); 
            homeButton.setBorderPainted(false); 
            homeButton.setFocusPainted(true); 
            homeButton.addActionListener(new HomeButtonHandler());

            //Add components to panel
            add(nextButton);
            add(backButton);
            add(homeButton);
        }

        //Draws background and navigation icons
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);	//Clears panel drawings
            g.drawImage(images[28], 0, 0, getWidth(), getHeight(), this);   //Instructions
            g.drawImage(images[3], 875, 600, 100, 100, this);               //Next button icon
            g.drawImage(images[8], 50, 600, 100, 100, this);                //Home button icon
            g.drawImage(images[6], 750, 600, 100, 100, this);               //Back button icon
        }
    }

    //The fourth instruction panel
    class fourthInstructionPanel extends JPanel
    {
        //Constructor to initialize the fourth instruction panel
        public fourthInstructionPanel(InstructionPanelHolder iph)
        {
            setLayout(null);    //Set layout to null
            
            //Back button to go back to third panel
            JButton backButton = new JButton("");
            backButton.setBounds(875, 600, 100, 100);
            backButton.setContentAreaFilled(false); 
            backButton.setBorderPainted(false); 
            backButton.setFocusPainted(true); 
            backButton.addActionListener(new BackButtonHandler(iph, "thirdInstructionPanel"));

            //Home button to go to home screen
            JButton homeButton = new JButton("");
            homeButton.setBounds(50, 600, 100, 100);
            homeButton.setContentAreaFilled(false); 
            homeButton.setBorderPainted(false); 
            homeButton.setFocusPainted(true); 
            homeButton.addActionListener(new HomeButtonHandler());
            
            //Add components to panel
            add(backButton);
            add(homeButton);
        }

        //Draws background and icons
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);	//Clears panel drawings
            g.drawImage(images[29], 0, 0, getWidth(), getHeight(), this);   //Instructions
            g.drawImage(images[8], 50, 600, 100, 100, this);                //Home button icon
            g.drawImage(images[6], 875, 600, 100, 100, this);               //Back button icon
        }
    }

    //Action listeners

    //Handles switching to the next panel in the instructions card layout
    class NextButtonHandler implements ActionListener
    {
		//Variables used to call the card switching method in InstructionPanelHolder 
        private InstructionPanelHolder iph;
        private String targetPanel;

		//Constructor
        public NextButtonHandler(InstructionPanelHolder iphIn, String targetPanelIn)
        {
			//Set variables as the parameters given
            iph = iphIn;
            targetPanel = targetPanelIn;
        }

        //When clicked, switch to target panel
        public void actionPerformed(ActionEvent e)
        {
            infoPages.show(iph, targetPanel);	//Shows the specific panel
        }
    }

    //Nested handler that allows switching to the previous panel in the instructions card layout
    class BackButtonHandler implements ActionListener
    {
		//Variables used to call the card switching method in InstructionPanelHolder 
        private InstructionPanelHolder iph;
        private String targetPanel;

		//Constructor
        public BackButtonHandler(InstructionPanelHolder iphIn, String targetPanelIn)
        {
			//Set variables as the parameters given
            iph = iphIn;
            targetPanel = targetPanelIn;
        }

        //When clicked, switch to target panel
        public void actionPerformed(ActionEvent e)
        {
            infoPages.show(iph, targetPanel);	//Shows the specific panel
        }
    }

    //Nested handler for going back to the home screen
    class HomeButtonHandler implements ActionListener
    {
        //When clicked Go back to first instruction panel and show home screen
        public void actionPerformed(ActionEvent e)
        {
            infoPages.show(iph, "firstInstructionPanel");	//Shows the first instruction panel (So the first panel is shows first when the user comes back to instructions again)
            gph.showCard("home");	//Goes back home
        }
    }
}
