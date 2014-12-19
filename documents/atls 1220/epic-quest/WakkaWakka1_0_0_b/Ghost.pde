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

