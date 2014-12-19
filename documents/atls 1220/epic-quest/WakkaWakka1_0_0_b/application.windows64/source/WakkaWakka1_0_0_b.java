import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class WakkaWakka1_0_0_b extends PApplet {

/*
*                        Wakka Wakka - Educational Tool Extraodinaire
*
*  This program utilizes a Command Prompt(cmd.exe) like interface (we'll call it fake CMD) 
*  to familiarize inexperienced users with a priliminary feel for using cmd or Terminal 
*  (the Mac equivalent).  The user is exposed to a few Computer Science principles during
*  the fake CMD portion, after which the user can play a game I created which is a mix of
*  Pac-Man and Snake.  
*
*  The default settings are as follows:
*    Controls:
*      \u2191 = up arrow
*      \u2193 = down arrow
*      \u2190 = left arrow
*      \u2192 = right arrow
*    Colors:
*      WakkaWakka = Yellow ~ (255,255,0)
*      background = Black ~ (0,0,0)
*
*
*
*  Added Features:
*      - Pellet size and worth decrease the longer they are on the field
*
*
*  Requires:
*    - Game v.1.0.0.a
*    - Ghost v.1.0.0.b
*    - WakkaWakkaDude v.1.0.0.a
*
*
* Hugh Dowdy
* December 12, 2014
*
* Special Credit: Bri Beemer - First tester ("This is fun")
* Basis for my text input code - https://www.processing.org/discourse/beta/num_1223627482.html
*/

//*--------------------------------------------------------------------------------------*//
//*--------------------------------------------------------------------------------------*//
//*--------------------------------------------------------------------------------------*//
// 
// GLOBAL Variables:
//----------------------------------------------------------------------------------------//
//*Name: --------> Description (Data Type)*
//
// BackB: -------> Blue intensity of background (int)
// BackG: -------> Green intensity of background (int)
// BackR: -------> Red intensity of background (int)
// currentInput: > Storage for the most recent user keystroke (String)
// currenttime: -> Used in unison with starttime and temp to determine whether to spawn a 
//                 Power Pellet.  Functions as a clock. (float)
// cursorx: -----> Used in establishing the x coordinate of the cursor/next input (int)
// cursory: -----> Used in establishing the y coordinate of the cursor/next input (int)
// Default: -----> User defined input to determine whether the default arguments will be 
//                 used (String)
// down: --------> User defined input to change Wakka Wakka direction to down (String)
// entercount: --> Iterator used to log the number of times the enter key has been pressed 
//                 during GAME SETUP.  The execution of each action is contingent on the 
//                 current value of the entercount. (int) 
// flasher: -----> Iterator used to make the cursor flash in fake CMD
// game: --------> Iterator to determine which stage the program is in [0 = GAME SETUP, 
//                 1 = GAME INITIATION, 2 = GAME EXECUTION, 3 = WAITING FOR PLAYER, 
//                 4-50 waiting for for user after end of game] (int)
// left: --------> User defined input to change Wakka Wakka direction to left (String)
// myFont: ------> Storage for desired font arguments (PFont)
// pastInput: ---> During the I/O portion(GAME SETUP) to store past user inputs & later 
//                 pass them to appropriate variables (String array) 
// prompt: ------> Set of outputs for fake CMD, the prompt index accessed is determined
//                 by entercount. (String array)
// right: -------> User defined input to change Wakka Wakka direction to right (String)
// starttime: ---> Used in unison with currenttime and temp to determine whether to spawn a 
//                 Power Pellet.  Functions as a clock. (float)
// temp: --------> Used with starttime and currenttime to determine whether to spawn a Power 
//                 Pellet.  Functions as a clock. (float)
// up: ----------> User defined input to change Wakka Wakka direction to up (String)
// WakR: --------> Red intensity of background (int)
// WakG: --------> Green intensity of background (int)
// WakB: --------> Blue intensity of background (int)
//
//
// OBJECTS:
// Name: --------> Purpose (class)
//
// Wakka: -------> Executes game functioning (Game)

//----------------------------------------------------------------------------------------//

// Methods:
//----------------------------------------------------------------------------------------//
// void CMDScreen() -----> Generates a Command Prompt like interface for user input
// void CursorLocation() > Updates base cursor/text entry location
// void draw() ----------> Runs the setup and game
// void Declare() ----> Sets defaults and creates text for fake CMD
// void KeyAssign() -----> Sets the user inputs or default values to the corresponding 
//                         variables
// void keyPressed() ----> Save the user inputs as a string which are save to the 
//                         appropriate variables once all inputs are received
// void setup() ---------> Declares
//---------------------------------------------------------------------------------------//

//---------------------------------------------------------------------------------------//
// GLOBAL Variables

float starttime, currenttime;
float temp = 1;
int entercount = 0;
int BackR, BackG, BackB, WakR, WakG, WakB, cursorx, cursory;
int flasher = 0;
int game = 0;
PFont myFont;
String currentInput = new String();
String up, down, left, right, Default;
String[] pastInput = new String[12];
String[] prompt = new String[32];

// Objects:
Game Wakka;
//----------------------------------------------------------------------------------------//

//----------------------------------------------------------------------------------------//
// Declaration of a few arguments and variables used in GAME SETUP
public void setup()
{
  size(800, 600);
  smooth();
  fill(200);
  myFont = createFont("Consolas", 16);
  textFont(myFont);
  textAlign(LEFT);
  BackR = BackG = BackB = WakB = 0;
  WakR = WakG = 255;
  frame.setResizable(true);
  Declare();
}
//----------------------------------------------------------------------------------------//

//----------------------------------------------------------------------------------------//
// Loop used to run the various stages of program
public void draw()
{
  
  
  // **
  // GAME SETUP
  // **
  if(game == 0)
  {
    frame.setLocation(0, 0);
    background(0);
    CMDScreen();
    CursorLocation();
    text(currentInput, cursorx-currentInput.length()*10, cursory);//length(), 5+15*entercount);
  }
  
  
  // **
  // GAME INITIATION
  // **
  if(game == 1)
  {
    KeyAssign();
    game = 3;
    Wakka = new Game(Default, WakR, WakG, WakB, BackR, BackG, BackB, up, down, left, right);
    frame.setSize(displayWidth+16, displayHeight+8);
    frame.setLocation(-8, -30);
    starttime = currenttime = PApplet.parseInt(second()); 
  }
  
  
  // **
  // GAME EXECUTION
  // **
  if(game == 2)
  {
    
    frame.setLocation(-8, -30);
    Wakka.Donut.collisionTest();
    
    // In game clock used for Power Pellet spawn
    currenttime = PApplet.parseInt(second());
    temp = abs(currenttime - starttime);
    if(temp >= 5 )
    {
      Wakka.powerPellet();
      starttime = currenttime;
    }
    Wakka.nextScreen();
  }
  
  //*
  // WAITING FOR PLAYER
  //*
  if(game == 3)
  {
    background(BackR,BackG,BackB);
    Wakka.Donut.oneWakka();
    Wakka.Oney.ghost();
    Wakka.Twoey.ghost();
    Wakka.Threeey.ghost();
    if(keyPressed == true)
    {
      game = 2;
    }
  }
  
  // **
  // NON-RESPONSE TIMER
  // **
  if(game >= 4)
  {
    game++;
    
    // If the user hasn't responded, close the program
    if(game == 500)
    {
      exit();
    }
  } 
}
//----------------------------------------------------------------------------------------//


// Methods:
//----------------------------------------------------------------------------------------//
// This method is used to generate and update a fake Command Prompt Screen, which
// is incorporated to introduce users to the look and feel of CMD/Terminal operations
// It's a poor attempt.
public void CMDScreen()
{ 
  // First text block
  text(prompt[1], 5, 16);
  text(prompt[2], 5, 34);
  text(prompt[3], 5, 78);
  text(prompt[4], 5, 96);
  text(prompt[5], 5, 114);
  text(prompt[6], 5, 132);
  text(prompt[7], 5, 150);
  text(prompt[8], 5, 186);
  
  // User I/O portion
  if(entercount > 0)
  {
    text(pastInput[0], 680, 186);
    text(prompt[9], 5, 222);
    text(prompt[10], 5, 240);
    text(prompt[11], 5, 258);
    text(prompt[12], 5, 294);
    if(entercount > 1)
    {
      text(pastInput[1], 690, 294);
      text(prompt[13], 5, 312);
      if(entercount > 2)
      {
        text(pastInput[2], 710, 312);
        text(prompt[14], 5, 330);
        if(entercount > 3)
        {
          text(pastInput[3], 700, 330);
          text(prompt[15], 5, 348);
          if(entercount > 4)
          {
            text(pastInput[4], 740, 348);
            text(prompt[16], 5, 366);
            if(entercount > 5)
            {
              text(pastInput[5], 760, 366);
              text(prompt[17], 5, 384);
              if(entercount > 6)
              {
                text(pastInput[6], 750, 384);
                text(prompt[18], 5, 402);
                if(entercount > 7)
                {
                  text(pastInput[7], 630, 402);
                  text(prompt[19], 5, 420);
                  if(entercount > 8)
                  {
                    text(pastInput[8], 650, 420);
                    text(prompt[20], 5, 438);
                    if(entercount > 9)
                    {
                      text(pastInput[9], 650, 438);
                      text(prompt[21], 5, 456);
                      if(entercount > 10)
                      {
                        text(pastInput[10], 660, 456);
                        text(prompt[31], 5, 474);
                        if(entercount >11)
                        {
                          background(0);
                          text(prompt[22], 5, 16);
                          text(prompt[23], 37, 52);
                          text(prompt[24], 37, 70);
                          text(prompt[25], 37, 88);
                          text(prompt[26], 5, 142);
                          text(prompt[27], 37, 160);
                          // If the user entered values, they are displayed
                          if(pastInput[0].charAt(0) != 'y' && pastInput[0].charAt(0) != 'Y')
                          {
                            text(" or ", 212, 160);
                            text(" or ", 212, 178);
                            text(" or ", 212, 196);
                            text(" or ", 212, 214);
                            text(pastInput[7], 257, 160);
                            text(pastInput[10], 257, 178);
                            text(pastInput[9], 257, 214);
                            text(pastInput[8], 5+28*9, 196);
                          }
                          text(prompt[28], 37, 178);
                          text(prompt[29], 37, 196);
                          text(prompt[30], 37, 214);
                          text(prompt[31], 5, 250);
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  } 
} 
//----------------------------------------------------------------------------------------//

//----------------------------------------------------------------------------------------//
// This method is used in establishing the coordinates of the cursor/next desired 
// user input and draws a flashing cursor at that location.
public void CursorLocation()
{
  // Set cursor location
  if(entercount == 0)
  {
    cursorx = 680+currentInput.length()*10;
    cursory = 186;
  }
  if(entercount == 1)
  {
    cursorx = 690+currentInput.length()*10;
    cursory = 294;
  }
  if(entercount == 2)
  {
    cursorx = 710+currentInput.length()*10;
    cursory = 312;
  }
  if(entercount == 3)
  {
    cursorx = 700+currentInput.length()*10;
    cursory = 330;
  }
  if(entercount == 4)
  {
    cursorx = 740+currentInput.length()*10;
    cursory = 348;
  }
  if(entercount == 5)
  {
    cursorx = 760+currentInput.length()*10;
    cursory = 366;
  }
  if(entercount == 6)
  {
    cursorx = 750+currentInput.length()*10;
    cursory = 384;
  }  
  if(entercount == 7)
  {
    cursorx = 630+currentInput.length()*10;
    cursory = 402;
  }
  if(entercount == 8)
  {
    cursorx = 650+currentInput.length()*10;
    cursory = 420;
  }
  if(entercount == 9)
  {
    cursorx = 650+currentInput.length()*10;
    cursory = 438;
  }
  if(entercount == 10)
  {
    cursorx = 660+currentInput.length()*10;
    cursory = 456;
  }
  if(entercount == 11)
  {
    cursorx = 300+currentInput.length()*10;
    cursory = 474;
  }
  if(entercount == 12)
  {
    cursorx = 295+currentInput.length()*10;
    cursory = 250;
  }
  
  // Determines whether/draws cursor at appropriate location
  // Due to the indexed nature of this method, the cursor began to flash slower as 
  // entercount increase, so I got creative with my solution.(If you watch the cursor 
  // on the directions screen, you may notice that it's a bit sporadic.  You probably
  // wouldn't have if I hadn't mentioned it.  I thought about redesigning the rest of
  // the method to be less indexed, but I decided against it.  I believe it gives the
  // cursor a bit character.)
  if(flasher < 15-PApplet.parseInt(.75f*entercount))
  {
    text("_", cursorx, cursory);
  }
  else if(flasher > 30-2*PApplet.parseInt(.75f*entercount))
  {
    flasher = -1;
  }
  flasher++;  
}
//----------------------------------------------------------------------------------------//

//----------------------------------------------------------------------------------------//
// This method is saves all the text used in the fake CMD for later reference as well as 
// saving default arguments to a temporary array
public void Declare()
{
  // Fake CMD prompts for interacting with the user
  prompt[1] = "Microsoft Windows [Version 6.1.7600]";
  prompt[2] = "Copyright (c) 2009 Microsoft Corporation.  All rights reserved.";
  prompt[3] = "Welcome to your first experience with Command Prompt(CMD)";
  prompt[4] = "This while this is not actually CMD, this program will introduce you to the feel of CMD";
  prompt[5] = "and some basic Computing Principles.  After supplying a few inputs, you will be able to";
  prompt[6] = "play a modified version of Pac-Man and Snake.  The values you choose will affect your";
  prompt[7] = "gaming experience!";
  prompt[8] = "Would you like to skip setup and play the game with default settings? (Y/N)";
  prompt[9] = "In Computer Science, colors are represented by the intensity of three hues: Red,";
  prompt[10] = "Green and Blue (RGB). Each hue is assigned a value from 0(no emission) - 255 (highest";
  prompt[11] = "intensity).";
  prompt[12] = "What would you like to set the Red intensity for WakkaWakka Dude to(0-255)?";
  prompt[13] = "What would you like to set the Green intensity for WakkaWakka Dude to(0-255)?";
  prompt[14] = "What would you like to set the Blue intensity for WakkaWakka Dude to(0-255)?";
  prompt[15] = "What would you like to set the Red intensity for the game's background to(0-255)?";
  prompt[16] = "What would you like to set the Green intensity for the game's background to(0-255)?";
  prompt[17] = "What would you like to set the Blue intensity for the game's background to(0-255)?";
  prompt[18] = "Which button would you like to bind to the change the direction to up?";
  prompt[19] = "Which button would you like to bind to the change the direction to down?";
  prompt[20] = "Which button would you like to bind to the change the direction to left?";
  prompt[21] = "Which button would you like to bind to the change the direction to right?";
  prompt[22] = "DIRECTIONS:";
  prompt[23] = "    Your goal is to consume as many Power Pellets as possible, without being eaten";
  prompt[24] = "    yourself!  Don't be afraid, because though they all look the same, some Power";
  prompt[25] = "    Pellets give you the strength to make your ghoulish enemies cower in fear!";
  prompt[26] = "CONTROLS:";
  prompt[27] = "    \u2191 = UP Arrow";
  prompt[28] = "    \u2192 = RIGHT Arrow";
  prompt[29] = "    \u2193 = DOWN Arrow";
  prompt[30] = "    \u2190 = LEFT Arrow";
  prompt[31] = "Press ENTER or RETURN when ready";


  // Default Settings
  pastInput[0] = "Y";
  pastInput[1] = "255";
  pastInput[2] = "255";
  pastInput[3] = "0";
  pastInput[4] = "0";
  pastInput[5] = "0";
  pastInput[6] = "0";
  pastInput[7] = "UP";
  pastInput[8] = "DOWN";
  pastInput[9] = "LEFT";
  pastInput[10] = "RIGHT";
}
//----------------------------------------------------------------------------------------//

//----------------------------------------------------------------------------------------//
// This method stores all the default or user specified arguments from the temporary array 
// for later reference.
public void KeyAssign()
{
  Default = pastInput[0];
  WakR = PApplet.parseInt(pastInput[1]);
  WakG = PApplet.parseInt(pastInput[2]);
  WakB = PApplet.parseInt(pastInput[3]);
  BackR = PApplet.parseInt(pastInput[4]);
  BackG = PApplet.parseInt(pastInput[5]);
  BackB = PApplet.parseInt(pastInput[6]);
  up = pastInput[7];
  down = pastInput[8];
  left = pastInput[9];
  right = pastInput[10];
}
//----------------------------------------------------------------------------------------//

//----------------------------------------------------------------------------------------//
// This method checks to see whether a key has been pressed.  Depending on the current 
// stage of the program, what the method does with the input will vary.
//
// game = 0 -----> Store user inputs to fake CMD pastInput array for later manipulation
// game = 1 -----> Doesn't access this method
// game = 2 -----> Doesn't access this method
// game = 3 -----> Doesn't access this method
// game = 4-500 -> Increments up waiting for user input, if Enter or Return is pressed
//                 a new game is executed.
public void keyPressed()
{
  //  Though it's unconventional to have higher iterations before lower, this section
  //  functions as a timer and doesn't need to check whether it satisfies the boolean 
  //  for the other portion of this method. If the user presses Enter/Return, the game 
  //  stage is returned to GAME INTIATION.
  if(game >= 4)
  {
    if(key == ENTER || key == RETURN)
    {
      game = 1;
      return;
    }
    return;
  }
  
  // This portion is used during the GAME SETUP stage to store the user inputs to 
  // appropriate temporary locations.
  if(game == 0)
  {
    // Stores the most recent string to variable currently being edited.  The string is
    // made up of keystrokes between the penultimate and the most recent Enter/Return 
    // keystroke.
    if(key == ENTER || key == RETURN)
    {
      if(currentInput.isEmpty() == false)
      {
        if(entercount == 0)
        {
          // If default values are accepted, start the game
          if(currentInput.equals("y") || currentInput.equals("Y"))
          {
            entercount = 11; 
          }
          //else if(currentInput.equals("IMPOSSIBLE MODE"))
          //{
          //  pastInput[] = {"IMPOSSIBLE MODE",0,0,0,0,0,0,UP,DOWN,LEFT,RIGHT};
          //}
          else
          {
          }
        }
        if(entercount == 12)
        {
          game = 1;
          keyPressed = false;
          return;
        }
        
        // Stores the most recent string to the pastInput array, clears the current string
        // indexed to the previous entercount iterator value, then increments the 
        // entercount iterator by 1.
        pastInput[entercount] = currentInput;
        currentInput = "";
        entercount++;
      }
      else if(entercount == 0)
      {
        pastInput[0] = "n";
        return;
      }
      // Ends the GAME SETUP stage and advances to the GAME INITATION stage
      else if(entercount == 11)
      {
        entercount++;
        return;
      }
      else if(entercount == 12)
      {
        game = 1;
        keyPressed = false;
        return;
      }
    }
    
    // Delete most recent character in fake CMD
    else if(key == BACKSPACE && currentInput.length() > 0)
    {
      currentInput = currentInput.substring(0, currentInput.length() - 1);
    }
    // Catches undesirable keystrokes and prevents them from altering string
    else if(key == BACKSPACE || keyCode == SHIFT)
    {
      return;
    }
    // catches CODED keys such as arrow keys and binds them to most recent fake CMD 
    // user input request.  (you cannot alter the functionality of the arrow keys however)
    else if(key == CODED) 
    {
      if(entercount > 6)
      {
        if(entercount >= 11)
        {
          return;
        }
        // Stores the most recent string to the pastInput array, clears the current string
        // indexed to the previous entercount iterator value, then increments the 
        // entercount iterator by 1.
        pastInput[entercount] = currentInput = currentInput + keyCode;
        currentInput = "";
        entercount++;
      }
    }
    
    // Updates the current string, assuming specific keys have not been struck
    else
    {
      currentInput = currentInput + key;
    }
  }
}
//----------------------------------------------------------------------------------------//

//----------------------------------------------------------------------------------------//
// class Game
// This class is responsible for drawing and calculations in Wakka Wakka
//
//
// Author(Hugh Dowdy)
// Date(December 12, 2014)
// v.1.0.0.a
//
//
// Local Variables:
//----------------------------------------------------------------------------------------//
//*Name: --------> Description (Data Type)*
//
// backgroundB: -> Blue intensity of background (int)
// backgroundG: -> Green intensity of background (int)
// BackgroundR:--> Red intensity of background (int)
// doubled: -----> User defined input to determine whether the default arguments will be 
//                 used (String)
// open: --------> Variable used to determine whether Wakka Wakka is opening his mouth or
//                 closing it. [0 = closing, 1 = opening] (int)
// pellet: ------> Iterator used to keep track of number of Power Pellets on screen (int)
// score: -------> Player's score, incremented by 12.5 for each Power Pellet consumed. 
//                 This variable is primarily manipulated in Ghost.powerPellet(int). 
//                 ^(double)
// textscore: ---> score converted to a string for display (String)
// theta: -------> Half the angle WakkaWakkaDude's mouth makes(float)
// WakX: --------> WakkaWakkaDude's x coordinate(float)
// WakY: --------> WakkaWakkaDude's y coordinate(float)
//
//
// OBJECTS:
// Name: ---------> Purpose (class)
//
// Oney: --------> Generates Ghost 1 (Ghost)
// Donut: -------> Generates the Wakka Wakka Dude (WakkaWakkaDude)
// Twoey: -------> Generates Ghost 2 (Ghost)
// Threeey: -----> Generates Ghost 3 (Ghost)
//----------------------------------------------------------------------------------------//

// Methods:
//----------------------------------------------------------------------------------------//
// void endgame() -------> Displays end game screen and offers to restart or end game
// void nextScreen() ----> Draws the next frame of the game
// void powerPellet() ---> Increases the number of power pellets on the board
//----------------------------------------------------------------------------------------//



//----------------------------------------------------------------------------------------//
//----------------------------------------------------------------------------------------//
public class Game
{
  //--------------------------------------------------------------------------------------//
  // Initialization of Local Variables
  float WakX, WakY, theta;
  int open;
  int pellet = 0;
  double score = 0;
  String textscore = new String("0");
  String doubled = new String("");
  int backgroundR, backgroundG, backgroundB;
  
  // Objects:
  Ghost Oney, Twoey, Threeey;
  WakkaWakkaDude Donut;
  //--------------------------------------------------------------------------------------//
  
  //--------------------------------------------------------------------------------------//
  // Initializes game using variables passed from GAME SETUP in WakkaWakka
  Game(String Default, int PR, int PG, int PB, int BR, int BG, int BB, String u, 
              String d, String L, String R)
  {
    // Establishing a few arguments
    size(displayWidth,displayHeight);
    noStroke();
    myFont = createFont("Helvetica Bold", 50);
    textFont(myFont);
    textAlign(CENTER);
    
    
    // Declaration of Objects
    Oney = new Ghost(PApplet.parseInt(random(1, displayWidth/2-200)), PApplet.parseInt(random(60, displayHeight-100)), 3, 255, 0, 0);
    Twoey = new Ghost(PApplet.parseInt(random(1, displayWidth/2-200)), PApplet.parseInt(random(60, displayHeight-100)), 2, 0, 255, 0);
    Threeey = new Ghost(PApplet.parseInt(random(displayWidth/2+71,displayWidth-129)), PApplet.parseInt(random(61, displayHeight-99)), 1, 255, 0, 255);
    
    
    Donut = new WakkaWakkaDude(PR,PG,PB,u,d,L,R);
    
    // Declaration and passing variables
    open = 0;
    doubled = Default;
    theta = PI/12;
    backgroundR = BR;
    backgroundG = BG;
    backgroundB = BB;
  }
  //--------------------------------------------------------------------------------------//
  
  
  // Methods:
  //--------------------------------------------------------------------------------------//
  // This method is responsible for drawing the final screen after the game is over.  It 
  // displays the score, asks the user whether they would like to play again and changes
  // the game stage from GAME EXECUTION to NON-RESPONSE TIMER.
  public void endgame()
  {
    // Clears the screen, setting it to red
    background(255,0,0);
    backgroundR = 255;
    backgroundG = 0;
    backgroundB = 0;
    
    // Draws the last location of Wakka Wakka, Power Pellets, and the Ghosts
    Donut.oneWakka();
    Oney.ghost();
    Twoey.ghost();
    Threeey.ghost();
    Oney.powerPellet(pellet);
    
    // End of game interaction, including final score and prompting the user for input
    // to start a new game or close program.  The latter is carried out in WakkaWakka
    fill(0);
    text("You bit the big one.",displayWidth/2, displayHeight/2);
    text("Your score was", displayWidth/2, displayHeight/2 + 52);
    textAlign(LEFT);
    text(textscore, displayWidth/2 + 7*27.5f , displayHeight/2 + 54);
    textAlign(CENTER);
    text("Press Enter to play again, or Esc to quit",displayWidth/2, displayHeight/2 + 104);
    
    // Changes game state from GAME EXECUTION to NON-RESPONSE TIMER
    game = 4;
  }
  //--------------------------------------------------------------------------------------//
  
  //--------------------------------------------------------------------------------------//
  // This method is the backbone of the game.  It is responsible for calling other methods
  // as well as presenting a GUI for user visual feedback.
  public void nextScreen()
  {
    // Clears the screen
    background(backgroundR, backgroundG, backgroundB);
    
    // Determines the angle at which Wakka Wakka's mouth is open
    // Opening
    if(theta <= .707f/2.7f*PI && open == 0)
    {
      theta += .02f;
      
      // Alternative for determining direction (more/less jagged?)
      // Oney.lookeyloo();
      // Twoey.lookeyloo();
      // Threeey.lookeyloo();
      
      if(theta >= .707f/2.7f*PI-.02f)
      {
        open = 1;
      }
    }
    // Closing
    if(theta >=0 && open == 1)
    {
      theta += -.02f;
      if(theta <= 0+.02f)
      {
        open = 0;
      }
    }
    
    // Draw Wakka Wakka and calculate location using user input
    Donut.getInput();
    Donut.move();
    Donut.oneWakka();
    
    // Waits for the first pellet to initialize Power Pellet locations/rendering
    if(pellet > 0)
    {
      
      Oney.powerPellet(pellet);

    } 
    
    // Calculate Ghost direction and shortest distance 50% of the time
    // This helps with jaggedness
    if(millis()%2 == 0)
    {
      Oney.lookeyloo();
      Twoey.lookeyloo();
      Threeey.lookeyloo();
    }
    
    
    // Draw ghosts and calculate location
    Oney.ghost();
    Oney.move();
    Twoey.ghost();
    Twoey.move();
    Threeey.ghost();
    Threeey.move();    
    
        
    // Display the user's score
    if(score > 0)
    {
      fill(255,0,0);
      textscore = Double.toString(score);
      text(textscore, displayWidth/2, displayHeight/2);
    } 
    
    // Test for collision between Wakka Wakka and ghosts
    Donut.collisionTest();
    
    //This section is for debugging using the click function (physically click the mouse)
    //if(mousePressed == true)
    //{
    //  Oney.mousePressed();
    //  Twoey.mousePressed();
    //  Threeey.mousePressed();
    //  mousePressed = false;
    //}
    
  }
  //--------------------------------------------------------------------------------------//
  
  //--------------------------------------------------------------------------------------//
  // This method simply increments the number of pellets on the screen every five seconds
  // according to the game clock.  Refer to Ghost.powerPellet for more in depth explanation
  public void powerPellet()
  {
    /*if(pellet < 5)
    {
      pellet++ ;
    }
    else
    {
      pellet = 6;
    }*/
    pellet ++;
  }
  //--------------------------------------------------------------------------------------//
  
// class Ghost
// This class is responsible for initializing and manipulating ghosts
//
//
// Author(Hugh Dowdy)
// Date(December 12, 2014)
// v.1.0.0.b
//
//
// Local Variables:
//----------------------------------------------------------------------------------------//
//*Name: ---------> Description (Data Type)*
//
// a: ------------> 2 Dimensional Array used to log location of Power Pellets and score 
//                  associated with corresponding pellets(int array)
// b: ------------> Array used to log location of Power Pellets(int array)
// c: ------------> Array responsible for housing the characteristics of the ghost. 
//                  (indices: 0-2 body color, 3-5 eye color, 6-8 pupil color, 9 speed, 
//                  10 direction, 11-21 are the same as 0-10 for Ghoul state) refer to 
//                  click (float array)
// click: --------> Determines blinky's state (0 = normal, 1 = ghoul)(int) 
// directionx: ---> Determines whether blinky is going right (1) or left (-1)(int)
// directiony: ---> Determines whether blinky is going up (-1) or down(1)(int)
// i: ------------> Iterator used in powerPellet(int). (int)
// j: ------------> Iterator used in powerPellet(int). (int)
// k: ------------> Iterator used in powerPellet(int). (int)
// l: ------------> Iterator used in powerPellet(int). (int)
// lookx: --------> Direction ghost is looking horizontally (0 - left, 1 - straight, 2 - 
//                  right)(int)
// looky: --------> Direction ghost is looking vertically (-1 - up, 0 straight, 1 - down)
//                  ^(int)
// thomasthetrain:> Determines whether Wakka Wakka has been in contact with a Power Pellet
//                  ^(boolean)
// x: ------------> Ghost's x coordinate(float)
// y: ------------> Ghost's y coordinate(float)
//----------------------------------------------------------------------------------------//

// Methods:
//----------------------------------------------------------------------------------------//
// void ghost() -----------> Draws the ghost at a specified location (x,y)
// void lookyloo() --------> Determines the direction the ghost will head and the direction
//                           it will look
// void mousePressed() ----> On mousePressed, alternates between ghost states, normal and 
//                           ghoul
// void move() ------------> Calculates the new position of ghost and ensures it stays on
//                           screen
// void powerPellet() -----> Calculates location, and number of Power Pellets on the board 
//                           and checks to see whether Wakka Wakka ate it
//----------------------------------------------------------------------------------------//



//----------------------------------------------------------------------------------------//
class Ghost
{
  //--------------------------------------------------------------------------------------//
  // Declaration and Initialization of Local Variables
  int lookx = 0;      
  int looky = 0;
  float x = 0;         
  float y = 70;        
  int directionx = 1; 
  int directiony = 0;
  float[] c = {255, 0, 0, 255, 255, 255, 0, 0, 255, 2, 1, 0, 0, 255, 0, 0, 255, 255, 255, 255, 4, -1};
  int click = 0;     
  //--------------------------------------------------------------------------------------//
  
  //--------------------------------------------------------------------------------------//
  // Initializes a ghost using variables passed from class Game in WakkaWakka
  Ghost(int initialx, int initialy, float speed, int cr, int cg, int cb)
  {
    // Setting Ghost colors, speed, and location
    c[0] = cr;
    c[1] = cg;
    c[2] = cb;
    c[9] = speed;
    c[20] = speed*2;
    x = initialx;
    y = initialy;
  }
  //--------------------------------------------------------------------------------------//
  
  // Methods:
  //--------------------------------------------------------------------------------------//
  // This method draws the ghost at a specified location, (x,y).  The method will pull 
  // different values from c[] depending on which state the ghost is in.
  public void ghost()
  {
    int w = 10; // Sets Blinky's width
    
    // Draw Body
    fill( c[0+11*click], c[1+11*click], c[2+11*click]); // Set fill to (n - red, g - blue)
    rect( x, y, w, 90); //From left, bar 1
    rect( x+10, y-30, w, 130); //From left, bar 2
    rect( x+20, y-40, w, 140); //From left, bar 3
    rect( x+30, y-50, w, 140); //From left, bar 4
    rect( x+40, y-50, w, 130); //From left, bar 5
    rect( x+50, y-60, w, 150); //From left, bar 6
    rect( x+60, y-60, w, 160); //From left, bar 7
    rect( x+70, y-60, w, 160); //From left, bar 8
    rect( x+80, y-60, w, 150); //From left, bar 9
    rect( x+90, y-50, w, 130); //From left, bar 10
    rect( x+100, y-50, w, 140); //From left, bar 11
    rect( x+110, y-40, w, 140); //From left, bar 12
    rect( x+120, y-30, w, 130); //From left, bar 13
    rect( x+130, y, w, 90); //From left, bar 14
  
    // Draw eyes
    fill( c[3+11*click], c[4+11*click], c[5+11*click]); //set fill to (n - white, g - blue)
    rect( x+10+lookx*10, y, w, 30); //From left, bar 1 in left eye
    rect( x+20+lookx*10, y-10, w, 50); //From left, bar 2 in left eye
    rect( x+30+lookx*10, y-10, w, 50); //From left, bar 3 in left eye
    rect( x+40+lookx*10, y, w, 30); //From left, bar 4 in left eye
    rect( x+70+lookx*10, y, w, 30); //From left, bar 1 in left eye
    rect( x+80+lookx*10, y-10, w, 50); //From left, bar 2 in right eye
    rect( x+90+lookx*10, y-10, w, 50); //From left, bar 3 in right eye
    rect( x+100+lookx*10, y, w, 30); //From left, bar 4 in left eye
  
    if(click == 0)
    {    
      // Draw pupils
      fill( c[6+11*click], c[7+11*click], c[8+11*click]); //set fill to (n - blue, g - white)
      rect( x+10+2*lookx*10, y+10+looky*10, 20, 20); //Left pupil
      rect( x+70+2*lookx*10, y+10+looky*10, 20, 20); //Right pupil
    }
  
    // Draw's ghost Blinky's mouth and pupils
    if(click == 1)
    {
      // Draw pupils
      fill(255); //set fill to white
      rect( x+40, y, 20, 20); //Left pupil
      rect( x+80, y, 20, 20); //Right pupil
  
      // Draw mouth
      rect(x+10, y+50, 10, 10);
      rect(x+20, y+40, 20, 10);
      rect(x+40, y+50, 20, 10);
      rect(x+60, y+40, 20, 10);
      rect(x+80, y+50, 20, 10);
      rect(x+100, y+40, 20, 10);
      rect(x+120, y+50, 10, 10);
    }
  }
  //--------------------------------------------------------------------------------------//
  
  //--------------------------------------------------------------------------------------//
  // This method calculates the quickest way to reach Wakka Wakka.  It will determine 
  // which quadrant Wakka Wakka is in with respect to the ghost and whether Wakka Wakka
  // is farther horizontally or vertically.  Depending on the ghost's state, the ghost's 
  // direction and look direction will be adjusted.  If the ghost is normal, it will pursue 
  // Wakka Wakka, and if it is Ghoul mode, it will evade.
  public void lookeyloo()
  {
    // Wakka Wakka is to the far to the right
    if(((WakX - (x+70)) > ((x+70) - WakX)) && ((WakX - (x+70)) > (WakY - (y+20))) &&
    ((WakX - (x+70)) > ((y+20) - WakY)))
    {
      // If ghost is normal, decrease horizontal distance
      if(click == 0)
      {
        lookx = 2;
        looky = 0;
        directionx = 1;
        directiony = 0;
      }  
      // If ghost is in ghoul state, increase vertical distance
      else
      {
        if((WakY-(y+20)) > ((y+20) - WakY))
        {
          lookx = 1;
          looky = 0;
          directionx = 0;
          directiony = -1;
        }
        else
        {
          lookx = 1;
          looky = 1;
          directionx = 0;
          directiony = 1;
        }
      }
    }
    
    // Wakka Wakka is far to the Left
    if((((x+70) - (WakX)) > (WakX - (x+70))) && (((x+70) - WakX) > (WakY - (y+20))) &&
    (((x+70) - WakX) > ((y+20) - WakY)))
    {
      // If ghost is normal, decrease horizontal distance
      if(click == 0)
      {
        lookx = 0;
        looky = 0;
        directionx = -1;
        directiony = 0;
      }
      // If ghost is in ghoul state, increase vertical distance
      else
      {
        if((WakY-(y+20)) > ((y+20) - WakY))
        {
          lookx = 1;
          looky = 0;
          directionx = 0;
          directiony = -1;
        }
        else
        {
          lookx = 1;
          looky = 1;
          directionx = 0;
          directiony = 1;
        }
      }
    }
    
    // Wakka Wakka is far below
    if(((WakY - (y+20)) > (WakX - (x+70))) && ((WakY - (y+20)) > ((x+70) - WakX)) &&
    ((WakY - (y+20)) > ((y+20) - WakY)))
    {
      // If ghost is normal, decrease vertical distance
      if(click == 0)
      {
        lookx = 1;
        looky = 0;
        directionx = 0;
        directiony = 1;
      }
      // If ghost is in ghoul state, increase horizontal distance
      else
      {
        if((WakX - (x+50)) > ((x+20) - WakX))
        {
          lookx = 1;
          looky = 1;
          directionx = -1;
          directiony = 0;
        }
        else
        {
          lookx = 1;
          looky = 1;
          directionx = 1;
          directiony = 0;
        }
      }
    }
    
    // Wakka Wakka is far below
    if((((y+20) - WakY) > (WakX - (x+70))) && (((y+20) - WakY) > ((x+70) - WakX)) &&
    (((x+20) - WakY) > (WakY - (x+20))))
    {
      // If ghost is normal, decrease vertical distance
      if(click == 0)
      {
        lookx = 1;
        looky = -1;
        directionx = 0;
        directiony = -1;
      }
      // If ghost is in ghoul state, increase horizontal distance
      else
      {
        if((WakX - (x+50)) > ((x+20) - WakX))
        {
          lookx = 1;
          looky = 1;
          directionx = -1;
          directiony = 0;
        }
        else
        {
          lookx = 1;
          looky = 1;
          directionx = 1;
          directiony = 0;
        }
      }   
    }
  }
  //--------------------------------------------------------------------------------------//

  //--------------------------------------------------------------------------------------//
  // This method determines which state the ghost is in (0 - normal or 1 - ghoul)
  public void mousePressed()
  {
    if(click == 0)
    {
      click = 1;
      mousePressed = false;
    }
    if(mousePressed == true && click == 1)
    {
      click = 0;
    }
  }
  //--------------------------------------------------------------------------------------//

  //--------------------------------------------------------------------------------------//
  // This method is responsible for calculating the ghost's new position.  It also prevents
  // the ghost from leaving the screen
  public void move()
  {
    //Potential variable for determining how successfully normal ghosts pursue
    float competence = random(0,5);
    
    // Is the ghost at the edge of the screen horizontally?
    if(x+130 >= displayWidth || x <= 0)
    {
      // Is it at the left edge?
      if(x <= 0)
      {
        // Move away from the wall, change direction and calculate next position
        x =  1;
        directionx = -directionx;
        x += directionx*c[9+11*click];
      }
      // Then it must be at the right edge.
      else
      {
        // Move away from the wall, change direction and calculate next position
        x = displayWidth-131;
        directionx = -directionx;
        x += directionx*c[9+11*click];
      }
    }
    // Is the ghost at the edge of the screen vertically?
    else if(y+100 >= displayHeight || y-60 <= 0)
    {
      // Is it at the top edge?
      if(y-60 <= 0)
      {
        // Move away from the wall, change direction and calculate next position
        y =  61;
        directiony = -directiony;
        y += directiony*c[9+11*click];
      }
      // Then it must be at the bottom edge
      else
      {
        // Move away from the wall, change direction and calculate next position
        y = displayHeight-101;
        directiony = -directiony;
      y += directiony*c[9+11*click];
      }
    }
    // Since it's not a the edge, calculate new position normally
    else if(competence < 5)
    {
      x += directionx*c[9+11*click];
      y += directiony*c[9+11*click];
    }
  }
  //--------------------------------------------------------------------------------------//

  //--------------------------------------------------------------------------------------//
  //  This method is used to calculate the existence and position of Power Pellets.  It 
  //  then draws the existing Power Pellets.  It gets the number of Power Pellets in 
  //  existence from Game.powerPellets, which is iterated during Wakka Wakka.  x and y are
  //  already in use in this class so I used a and b respectively. Thomas the Train 
  //  determines whether Wakka Wakka Dude has been in contact with a Power Pellet. Because 
  //  I wanted to, that's why. 
  
  // Super Local Variables - like in the your neighborhood, like inside the house, 
  // like in your room, like in your closet
  int[][] a;
  int[]b;
  boolean thomasthetrain = false;
  
  public void powerPellet(int p)
  {
    
    // if the ghost is in ghoul state, return them all to normal
    if(click == 1 && pellet <= 1)
    {
      Oney.click = 0;
      Twoey.click = 0;  
      Threeey.click = 0;
    }
    
    // initializing the first 5 pellet locations
    if(a == null)
    {
      a = new int[2][6];
      b = new int[5];
      a[0][5] = a[1][5] = 0;
      for(int j = 0; j <= 4; j++)
      {
        a[0][j] = PApplet.parseInt(random(25, displayWidth - 25));
        a[1][j] = 0;
        b[j] = PApplet.parseInt(random(25, displayHeight - 25));
      }  
    }
    
    // If the pellet count exceeds 5, the first pellet will be removed and a new pellet 
    // will go at the end of the array.  Each pellet that remains will move down one 
    // position, the good old-fashioned way.
    if ( pellet == 6 || a[1][0] > 5)
    {
      if(pellet == 6)
      {
        pellet = 5;
      }
      if (a[1][0] > 5)
      {
        pellet = pellet - 1;
      }
      for(int k = 0; k <= pellet-1; k++)
      {
        a[0][k] = a[0][k+1];
        a[1][k] = a[1][k+1];
        b[k] = b[k+1];
      }
      a[0][pellet-1] = PApplet.parseInt(random(25, displayWidth - 25));
      a[1][pellet] = 0;
      b[pellet-1] = PApplet.parseInt(random(25, displayHeight - 25));
    }
    
    // Loop to check whether WakkaWakka Dude has eaten a pellet
    for(int l = 0; l <= pellet-1; l++)
    {
      // Update pellet score timer
      if( a[1][pellet-1] == 0 )
      {
          a[1][l] += 1;
      }

      // Contact? Each is a circle, so calculating the distance and comparing that
      // to the sum of their radii. 95 = radius of pellet + radius of WakkaWakka Dude
      if(sqrt(sq(abs(WakX-a[0][l]))+sq(abs(WakY-b[l]))) <= 95) 
      {
        thomasthetrain = true;
        
        // Update player score
        score += 60-(10*a[1][l])-temp*2;
      }
      // Move each pellet down in the array, starting with the pellet after the one eaten
      if(thomasthetrain == true && l < (pellet - 1))
      {
        a[0][l] = a[0][l+1];
        a[1][l] = a[1][l+1];
        b[l] = b[l+1];
      }
      // add a new Power Pellet to the top of the array, though it won't yet be drawn
      else if(thomasthetrain == true && l == (pellet - 1))
      {
        a[0][l] = PApplet.parseInt(random(25, displayWidth - 25));
        a[1][l] = 0;
        b[l] = PApplet.parseInt(random(25, displayHeight - 25));
        thomasthetrain = false;
        pellet = pellet - 1;
        
        // There's also a 20% chance the Power Pellet will contain acid and you will start
        // seeing blue ghosts
        if(PApplet.parseInt(random(1,5)) == 3)
        {
          mousePressed = true;
          Oney.mousePressed();
          Twoey.mousePressed();
          Threeey.mousePressed();
        }
      }
    }
   
    //
    // Interface for Debugging Power Pellet
    //fill(255);
    //score = temp;
    //text(Double.toString(a[1][0]), displayWidth/2, 200);  
    //text(Double.toString(a[1][1]), displayWidth/2, 236);  
    //text(Double.toString(a[1][2]), displayWidth/2, 272);  
    //text(Double.toString(a[1][3]), displayWidth/2, 308); 
    //text(Double.toString(a[1][4]), displayWidth/2, 344);   
    //text(pellet, displayWidth/2, 420);
    //
   
    //  Draw current number of visible pellets
    for(int i = 0; i <= pellet-1; i++)
    {  
      if (temp != 5)
      {
        fill(255);
        ellipse(a[0][i], b[i], 60-(10*a[1][i])-temp*2, 60-(10*a[1][i])-temp*2);   
      }
      else
      {
        fill(255);
        ellipse(a[0][i], b[i], 60-(10*a[1][i]), 60-(10*a[1][i]));
      }
    }  
  }
  //--------------------------------------------------------------------------------------//
//// NEED TO FIX COUNTER TO REFLECT A 0 during first second. The issue lies in the UPDATE 
//// pellet score timer. Find problem with only one pellet that ages past 25 seconds old.
//// updates two pellet timers when it should only update one.
}

//----------------------------------------------------------------------------------------//

// class WakkaWakkaDude
// This class is responsible for initializing and manipulating Wakka Wakka
//
//
// Author(Hugh Dowdy)
// Date(December 12, 2014)
// v.1.0.0.a
//
//
// Local Variables:
//----------------------------------------------------------------------------------------//
//*Name: --------> Description (Data Type)*
//
// Fill: --------> color(R,G,B) value for Wakka Wakka(int)

// diameter: ----> Sets the size of Wakka Wakka by his diameter(int)
// directionx: --> Determines whether blinky is going right (1) or left (-1)(int)
// directiony: --> Determines whether blinky is going up (-1) or down(1)(int)
// speed: -------> Iterator used in powerPellet(int). (float)
//----------------------------------------------------------------------------------------//

// Methods:
//----------------------------------------------------------------------------------------//
// void collisionTest() ---> Determines whether Wakka Wakka has collided with a ghost
// void getInput() --------> Receives an input from the user and determines the direction
//                           Wakka Wakka will go
// void move() ------------> Calculates the new position of Wakka Wakka and ensures he 
//                           stays on screen
// void oneWakka() --------> Draws Wakka Wakka at a specified location (x,y) with mouth 
//                           angle (2*theta)
//----------------------------------------------------------------------------------------//



//----------------------------------------------------------------------------------------//
class WakkaWakkaDude
{
  //--------------------------------------------------------------------------------------//
  // Declaration and Initialization of Local Variables
  int diameter, directionx, directiony, Fill;
  float speed = 7.5f;
  //--------------------------------------------------------------------------------------//
  
  //--------------------------------------------------------------------------------------//
  // Initializes a ghost using variables passed from class Game in WakkaWakka
  WakkaWakkaDude(int R, int G, int B, String up, String down, String left, String right)
  { 
    // Declaration of variables
    WakX = displayWidth/2;
    WakY = displayHeight/2;
    diameter = 140;
    Fill = color(R, G, B);
    directionx = directiony = 0;
  }
  //--------------------------------------------------------------------------------------//
  
  // Methods:
  //--------------------------------------------------------------------------------------//
  //  This method determines whether Wakka Wakka has collided with a ghost.  If he has, and
  //  the ghost is in a normal state, the game is over.
  public void collisionTest()
  {
  // Loop meant to check whether WakkaWakka Dude has met with a ghost
    for(int l = 0; l <= pellet-1; l++)
    {
      if(Oney.click == 0)
      {
        if(sqrt(sq(abs(WakX-(Oney.x+65)))+sq(abs(WakY-(Oney.y+20)))) <= 135) //95 = approx. radius of ghost + radius of WakkaWakka Dude
        {
          Wakka.endgame();       
        }
        if(sqrt(sq(abs(WakX-(Twoey.x+65)))+sq(abs(WakY-(Twoey.y+20)))) <= 135) //95 = approx. radius of ghost + radius of WakkaWakka Dude
        {
         Wakka.endgame();
        }
        if(sqrt(sq(abs(WakX-(Threeey.x+65)))+sq(abs(WakY-(Threeey.y+20)))) <= 135) //95 = approx. radius of ghost + radius of WakkaWakka Dude
        {
         Wakka.endgame();
        }
      }
    }
  }
  //--------------------------------------------------------------------------------------//
  
  //--------------------------------------------------------------------------------------//
  // This method determines Wakka Wakka's direction. It uses either the default settings or
  // user defined directional keys.
  public void getInput()
  {
    if(keyCode == UP || key == up.charAt(0))
    {
      directiony = -1;
      directionx = 0;
    }
    if(keyCode == DOWN || key == down.charAt(0))
    {
      directiony = 1;
      directionx = 0;
    }
    if(keyCode == LEFT  || key == left.charAt(0))
    {
      directionx = -1;
      directiony = 0;
    }
    if(keyCode == RIGHT  || key == right.charAt(0))
    {
      directionx = 1;
      directiony = 0;
    }
  }
  //--------------------------------------------------------------------------------------//

  //--------------------------------------------------------------------------------------//
  // This method is responsible for calculating the Wakka Wakka's new position.  It also 
  // prevents him from leaving the screen (though you can break the barrier if you really
  // push).  I know half of Wakka can leave the screen, but I feel the extra wiggle room
  // makes the game more playable.
  public void move()
  {
    WakX += directionx*speed;
    WakY += directiony*speed;
    if(WakX >= displayWidth || WakX <= 0)
    {
      directionx = 0;
      if(WakX <= 0)
      {
        WakX = 0;
      }
      else
      {
        WakX = displayWidth;
      }
    }
    if(WakY >= displayHeight || WakY <= 0)
    {
      directiony = 0;
      if(WakY <= 0)
      {
        WakY = 0;
      }
      else
      {
        WakY = displayHeight;
      }
    }
  }
  //--------------------------------------------------------------------------------------//
  
  //--------------------------------------------------------------------------------------//
  // This method draws Wakka Wakka at a specified location, (Wakx,Waky).  It will draw his 
  // mouth opening in the direction he is going, at an angle of 2*theta.
  // This section is the most time intensive section of the code, it takes ~4x longer than
  // any other section in the game rendering
  public void oneWakka()
  {
    fill(Fill);
    ellipse(WakX, WakY, diameter, diameter);
    fill(backgroundR, backgroundG, backgroundB);
    if(directionx == -1)
    {
      triangle(WakX-diameter/.75f*cos(theta),WakY+diameter/.75f*sin(theta),WakX-diameter/.75f*cos(theta),WakY-diameter/.75f*sin(theta),WakX,WakY);
    }
    else if(directiony == 1)
    {
      triangle(WakX+diameter/.75f*sin(theta),WakY+diameter/.75f*cos(theta),WakX-diameter/.75f*sin(theta),WakY+diameter/.75f*cos(theta),WakX,WakY);
    }
    else if(directiony == -1)
    {
      triangle(WakX+diameter/.75f*sin(theta),WakY-diameter/.75f*cos(theta),WakX-diameter/.75f*sin(theta),WakY-diameter/.75f*cos(theta),WakX,WakY);
    }
    else
    {
      triangle(WakX+diameter/.75f*cos(theta),WakY+diameter/.75f*sin(theta),WakX+diameter/.75f*cos(theta),WakY-diameter/.75f*sin(theta),WakX,WakY);
    }
  }
  //--------------------------------------------------------------------------------------//

}
//----------------------------------------------------------------------------------------//
//----------------------------------------------------------------------------------------//

}
//*--------------------------------------------------------------------------------------*//
//*--------------------------------------------------------------------------------------*//
//*--------------------------------------------------------------------------------------*//
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "WakkaWakka1_0_0_b" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
