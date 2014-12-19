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
*      ↑ = up arrow
*      ↓ = down arrow
*      ← = left arrow
*      → = right arrow
*    Colors:
*      WakkaWakka = Yellow ~ (255,255,0)
*      bakcground = Black ~ (0,0,0)
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
// currentInput: > Storage for the most recent user key stroke (String)
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
//                         appropriate variables once all inputs are recieved
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
void setup()
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
void draw()
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
    starttime = currenttime = int(second()); 
  }
  
  
  // **
  // GAME EXECUTION
  // **
  if(game == 2)
  {
    
    frame.setLocation(-8, -30);
    Wakka.Donut.collisionTest();
    
    // In game clock used for Power Pellet spawn
    currenttime = int(second());
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
// This method checks to see whether a key has been pressed.  Depending on the current 
// stage of the program, what the method does with the input will vary.
//
// game = 0 -----> Store user inputs to fake CMD pastInput array for later manipulation
// game = 1 -----> Doesn't access this method
// game = 2 -----> Doesn't access this method
// game = 3 -----> Doesn't access this method
// game = 4-500 -> Increments up waiting for user input, if Enter or Return is pressed
//                 a new game is executed.
void keyPressed()
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
    // made up of key strokes between the penultimate and the most recent Enter/Return 
    // key stroke.
    if(key == ENTER || key == RETURN)
    {
      if(currentInput != "")
      {
        if(entercount == 0)
        {
          // If default values are accepted, start the game
          if(currentInput.equals("y") || currentInput.equals("Y"))
          {
            entercount = 11; 
          }
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
    // Catches undesireable keystrokes and prevents them from altering string
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
// This method is used in establishing the coordinates of the cursor/next desired 
// user input and draws a flashing cursor at that location.
void CursorLocation()
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
  // entercount increase, so I got creatiev with my solution.(If you watch the cursor 
  // on the directions screen, you may notice that it's a bit sporadic.  You probably
  // wouldn't have if I hadn't mentioned it.  I thought about redesigning the rest of
  // the method to be less indexed, but I decided against it.  I believe it gives the
  // cursor a bit character.)
  if(flasher < 15-int(.75*entercount))
  {
    text("_", cursorx, cursory);
  }
  else if(flasher > 30-2*int(.75*entercount))
  {
    flasher = -1;
  }
  flasher++;  
}
//----------------------------------------------------------------------------------------//

//----------------------------------------------------------------------------------------//
// This method is used to generate and update a fake Command Prompt Screen, which
// is incorporated to introduce users to the look and feel of CMD/Terminal operations
// It's a poor attempt.
void CMDScreen()
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
// This method stores all the default or user specified arguments from the temporary array 
// for later reference.
void KeyAssign()
{
  Default = pastInput[0];
  WakR = int(pastInput[1]);
  WakG = int(pastInput[2]);
  WakB = int(pastInput[3]);
  BackR = int(pastInput[4]);
  BackG = int(pastInput[5]);
  BackB = int(pastInput[6]);
  up = pastInput[7];
  down = pastInput[8];
  left = pastInput[9];
  right = pastInput[10];
}
//----------------------------------------------------------------------------------------//

//----------------------------------------------------------------------------------------//
// This method is saves all the text used in the fake CMD for later reference as well as 
// saving default arguments to a temporary array
void Declare()
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
  prompt[27] = "    ↑ = UP Arrow";
  prompt[28] = "    → = RIGHT Arrow";
  prompt[29] = "    ↓ = DOWN Arrow";
  prompt[30] = "    ← = LEFT Arrow";
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
