Zain Rashid 20172929

# Compile code
 1. Create new **Gradle** project in IntelliJ using listed Java Version
 2. Import GitLab files/source code to project ***(Don't import module-info as with Gradle 6.6+ it causes errors)***
 3. On the right hand side select the tab "*Gradle*" then navigate to "*Tasks/build/*" and press "*build*" to run the builder
 4. Once built go to "*Tasks/application/*" and press "*run*"
 5. The game window should now run
 6. **JavaDocs** is located in the "*docs*" folder within the root project directory
# Implemented Features
 - Music player which seamlessly plays from program launch, can be toggled in the menu or game and implements singleton pattern 
 - Main Menu with play game, load game and settings branch
 - Settings contains toggle music button, help screen, image and colour picker
 - - Load game function added to main menu, automatically goes to map directory
 - Image and colour picker for every entity in the game, currently selected image or colour displayed for user
 - Help box in game which pauses timer
 - Reset level function which resets move and time counter 
 - Dynamic move, level and timer counter which update while game is played
 - Save game function which saves current position, time, moves of the game,total game moves as well as remaining levels in map set
 - Username screen with restricted inputs so no invalid characters
 - Output at the end of each level with total move count, time taken, level, etc and if the level index is greater than one also the total game moves so far. Tells user whether they were in the top 10 scores for the level and/or were the high score.
 - Scoreboard which outputs top 10 results for level based on time then move count as a second criteria
 - Keeper animation: users feet alternates when moving and when stopped for one second will reset to a standing position
 - Iterator inside of Level function which removes excess walls around game grid to make game run smoother
 - Gradle Project
# Implemented features not working perfectly
 - Undo function implements stack which pops and pushes new user key codes, doesn't function as intended as will skip multiple positions and leave the grid array
 ## Features Not Implemented
 - Custom map sets with unique rules such as certain coloured crates to certain coloured diamonds:
  Would have had to redo my image picker to consolidate this feature which would have taken too long
## New Java Classes
 - Implemented controllers for each of my FXML views which call functions when a certain action is taken by the user
 -  Movement class holding all movement related functions
 - Created a Dialog class which acts as a view and controller for multiple scenes such as level complete and the help box
 - Timer class with all time related functions
 - Save Game class which handles writing current game state to file
 - Leaderboard view and model which outputs the scoreboard
## Modified Classes
  - Broke up StartMeUp and Main by moving load game functions to a seperate class
  - Moved Main menu bar to FXML
  - Fixed GameLogger class as it would output multiple log files instead of just one

  
  
