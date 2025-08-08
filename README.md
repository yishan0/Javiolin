# Javiolin

Javiolin is an educational Java-based game designed to help high school violinists learn and master high positions on the violin fingerboard while improving their sight-reading skills. The game combines note recognition, timing, and interactive feedback to create a fun and effective learning experience.

---

## Overview

### Goal of the Game
The main goal is to help players grasp a solid understanding of violin notes in higher positions and enhance their ability to sight-read music accurately and quickly.

### Gameplay

<img width="1000" height="717" alt="Screenshot 2025-08-07 at 5 09 55 PM" src="https://github.com/user-attachments/assets/d530aa5f-61a0-4ac1-9577-59edfaeda743" />

- The game features a realistic violin fingerboard divided into sections that glow to represent different notes.
- Notes randomly generate and fall down the fingerboard.
- Players must type the correct note name on their keyboard as the note reaches the glowing section.
- Correct and timely inputs earn points; incorrect or mistimed notes deduct points.
- Each typed note plays the corresponding sound to reinforce audio recognition.
- The game is timed, either by a chosen song or a freeplay mode consisting of rounds customizable between 30 seconds and 2 minutes.

### Teaching & Feedback
- After each round, fingerboard sections glow in colors from green to red, indicating how often each note was played correctly or missed.
- A list of the most missed notes is displayed to help players focus their practice.
- Scores are accompanied by a letter grade from A to F based on accuracy.

---

## Features

- Realistic violin fingerboard layout.
- Dynamic note generation with audio playback.
- Interactive feedback to track strengths and weaknesses.
- Adjustable round timer.
- Simple and intuitive UI components (buttons, sliders, menus).

---

## Components

- Java Swing components such as `JButton`, `JLabel`, `JTextArea`, `JTextField`, `JRadioButton`, `JSlider`, and `JMenuBar`.
- File I/O for storing missed notes and high scores (`missedNotes.txt`, `highScores.txt`).
- Images for UI enhancement, including fingerboard visuals and character animations.
- Audio files for each violin note (planned for future implementation).

---

## Installation & Running the Game

1. Clone or download the repository.
2. Open the project in your Java IDE (e.g., IntelliJ IDEA, Eclipse).
3. Compile and run the `Main` class to start the game.
`javac Game.java`
`java Game`
5. Use the menus and settings to configure game duration and preferences.
6. Enjoy practicing your violin note recognition!


---

## Future Improvements

- Add sharps and flats for advanced learning.
- Enhance UI animations and sound effects.
- Introduce difficulty levels and leaderboards.


---

## Author

Yishan Lin 
Date: April 6, 2025

---

## License

This project is for educational purposes. Please cite sources appropriately if you reuse any part of the code or assets.

---
