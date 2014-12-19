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
  float speed = 7.5;
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
  void collisionTest()
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
  void getInput()
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
  void move()
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
  void oneWakka()
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
