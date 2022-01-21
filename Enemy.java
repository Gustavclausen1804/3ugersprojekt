//Frederik Buk Henriksen s183643, The enemy class

import java.util.ArrayList;
import java.util.Random;
 
public class Enemy extends Player{
      
      int difficulty = 0;
      int currentFrame;

      public Enemy(int id, String name, int difficulty) {
            super(id,name);
            this.difficulty = difficulty;
      }
      public void shoot() {
            double[] shot = enemyDifficulty(enemyShot(xPos+size/2, yPos+size/2), difficulty);               //Inner function finds shot by bruteforce, outer function adds variability to shot.
            this.playerShot = new Shot(xPos+size/2, yPos+size/2, true, true, id);                           //Create new shot afterwards
            playerShot.applyForce(shot[0], shot[1]);                                                        //Apply the 
      }

      public double[] enemyShot(int x, int y) {

            int iteration = 1500;                                                                           //Number of max updateBall() iterations per brute force simulation

            int iterationAngle = 90;                                                                        //Number of angle iterations
            int iterationForce = 25;                                                                        //Number of force iterations

            double[] angleMinMax = { 0, 180 };
            double[] forceMinMax = { 0, App.maxForce};

            ArrayList<ArrayList<Double>> succesfulShots = new ArrayList<ArrayList<Double>>();               //arraylist for all the succesive shots to be saved in

            for (int iA = 0; iA <= iterationAngle; iA++) {                                                  //Loop through the number of angle iterations
                  
                  double AngleStep = ((angleMinMax[1] - angleMinMax[0]) / iterationAngle);                  // Calculate the step size
                  double angleNext;                                                                         //Variable to hold the respective angle

                  //Will start from the middle of AngleMinMax[0] and AngleMinMax[0], then move towards the two sides equally for each iteration
                  if ( iA % 2 == 0 ){
                        angleNext = (angleMinMax[1]/2)+(iA/2)*AngleStep;                                    //90,92,94,96...
                  } else {
                        angleNext = (angleMinMax[1]/2)-((iA+1)/2)*AngleStep;                                //88,86,84,82...
                  }

                  for (int iF = 0; iF <= iterationForce; iF++) {                                            //Loop through the number of force iterations
                        double forceStep = ((forceMinMax[1] - forceMinMax[0]) / iterationForce);            //Calculate the step size
                        double forceNext = forceMinMax[0] + forceStep * iF;                                 //Variable to hold the respective angle

                        Shot simulation = new Shot(x, y, true, false, this.id);                             //Construct simulation shot
                        simulation.applyForce(angleNext,forceNext);                                         //apply the respective force and angle to the shot

                        int collisionQuantification = 0;                                                    //Set wall collision quantification

                        ArrayList<Double> data = new ArrayList<Double>();                                   //Initialize arraylist for the respective shot
                        for (int i = 0; i < iteration; i++) {                                               //Loop through the iteration max number

                              if (simulation.wallCollision(false)) {                                        // Attempt to quantify the amount of collision.
                                    collisionQuantification++;
                              }

                              
                              if (simulation.playerCollision(false)) {                                      // If the simulation shot hits a player

                                    //Save relevant data from the simulation shot
                                    data.add(angleNext);
                                    data.add(forceNext);
                                    data.add(Double.valueOf(collisionQuantification));
                                    data.add(Double.valueOf(i));
                                    data.add(simulation.ballXPos);
                                    data.add(simulation.ballYPos);

                                    succesfulShots.add(data);
                                    break;                                                                  //Break out of the respective simulation loop, if hit found
                              }
                              simulation.updateShot();                                                      //Update the ball according to forces in the game
                        }
                        //Break the double for loop if lightBruteForce is on, and a hit is found.
                        if (succesfulShots.size() >= 1 && toggleSimpleAi.isSelected()){
                              return doubleObjectToPrimitive(succesfulShots.get(0));                        //Grab the one shot that hits and return it
                        }
                  }
            }


            double[] bestShot = new double[succesfulShots.get(0).size()];                                   //Initialize an empty array the size of the arraylist, nessecary for the following code not to throw errors.

            boolean cleanShot = false;                                                                      //Flag if a clean hit upon the target is possible.

            for (int i = 0; i < succesfulShots.size(); i++) {                                               //Run through all simulated shots that hits a player
                  if (succesfulShots.get(i).get(5) <= App.height && succesfulShots.get(i).get(2) == 0) {    // highest hit (y-axis) and no wall collision
                        bestShot = doubleObjectToPrimitive(succesfulShots.get(i));                          //Save the shot
                        cleanShot = true;
                  }     
            }


            if (!cleanShot) {                                                                               //If no clean shot is found, look for a one with
                  bestShot[2] = 10000;                                                                      //Set the collision to an unrealisic high collision quantification
                  for (int i = 0; i < succesfulShots.size(); i++) {                                         //Run through all simulated shots that hits a player
                        if (succesfulShots.get(i).get(2) < bestShot[2]) {                                   // Lowest amount of wall collision
                              bestShot = doubleObjectToPrimitive(succesfulShots.get(i));                    //save the shot
                        }
                  }
            }
            return bestShot;
      }
      //Convert from Double object to double primitive
      public double[] doubleObjectToPrimitive(ArrayList<Double> object){              
            double[] primitive = new double[object.size()];                                                 //Create primitive double array          
            for (int i = 0; i < object.size(); i++){                                                        //Loop through values from Double object
                  primitive[i] = object.get(i);
            }
            return primitive;
      }

    public double[] enemyDifficulty(double[] shot, int difficulty){                                         //Add random variance to shot made by enemy.
        Random rand = new Random();

        double deltaAngle;
        double deltaForce;
        
        switch (difficulty) {                                                                               //Switch to according diffuiculty level
            case 1:                                                                                         //Easy
                                                                                                            //These random variance numbers arbitary determented for the respective levels
                  deltaAngle = 3;   
                  deltaForce = 3;
                  break;
            case 2:                                                                                          //Medium
                  deltaAngle = 2;
                  deltaForce = 2;
                 break;
            case 3:                                                                                          //Hard
                  deltaAngle = 1;
                  deltaForce = 1;
                  break;
            default:                                                                                         //Anything but 1,2,3 results in no variance.
                  deltaAngle = 0;
                  deltaForce = 0;
                  break;
        }
        deltaAngle = (rand.nextDouble()*2*deltaAngle)-deltaAngle;                                            //Add the angle variance, can be positive and minus.
        deltaForce = (rand.nextDouble()*2*deltaForce)-deltaForce;                                            //Add the force variance, can be positive and minus.

        shot[0] += deltaAngle;
        shot[1] += deltaForce;

        return shot;
    }
}

