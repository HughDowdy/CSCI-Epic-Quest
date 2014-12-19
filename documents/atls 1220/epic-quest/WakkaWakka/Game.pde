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
    Oney = new Ghost(int(random(1, displayWidth/2-200)), int(random(60, displayHeight-100)), 3, 255, 0, 0);
    Twoey = new Ghost(int(random(1, displayWidth/2-200)), int(random(60, displayHeight-100)), 2, 0, 255, 0);
    Threeey = new Ghost(int(random(displayWidth/2+71,displayWidth-129)), int(random(61, displayHeight-99)), 1, 255, 0, 255);
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
  void endgame()
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
    text(textscore, displayWidth/2 + 7*27.5 , displayHeight/2 + 54);
    textAlign(CENTER);
    text("Press Enter to play again, or Esc to quit",displayWidth/2, displayHeight/2 + 104);
    
    // Changes game state from GAME EXECUTION to NON-RESPONSE TIMER
    game = 4;
  }
  //--------------------------------------------------------------------------------------//
  
  //--------------------------------------------------------------------------------------//
  // This method is the backbone of the game.  It is responsible for calling other methods
  // as well as presenting a GUI for user visual feedback.
  void nextScreen()
  {
    // Clears the screen
    background(backgroundR, backgroundG, backgroundB);
    
    // Determines the angle at which Wakka Wakka's mouth is open
    // Opening
    if(theta <= .707f/2.7f*PI && open == 0)
    {
      theta += .02;
      
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
      theta += -.02;
      if(theta <= 0+.02)
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
    if(score >0)
    {
      fill(255,0,0);
      textscore = Double.toString(score);
      text(textscore, displayWidth/2, displayHeight/2);
    }
    
    // Test for collision between Wakka Wakka and ghosts
    Donut.collisionTest();
    
    //This section is for debugging using the click function (physically click the mouse)
    //
    //if(mousePressed == true)
    //{
    //  Oney.mousePressed();
    //  Twoey.mousePressed();
    //  Threeey.mousePressed();
    //  mousePressed = false;
    //}
    //
    //
    
    
  }
  //--------------------------------------------------------------------------------------//
  
  //--------------------------------------------------------------------------------------//
  // This method simply increments the number of pellets on the screen every five seconds
  // according to the game clock.  Refer to Ghost.powerPellet for more in depth explanation
  void powerPellet()
  {
    if(pellet < 5)
    {
      pellet++ ;
    }
    else
    {
      pellet = 6;
    }
    
  }
  //--------------------------------------------------------------------------------------//
  
