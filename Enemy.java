import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;


 
public class Enemy extends Player{
      
      boolean lightBruteForce = false; //TODO: Bruges til at få AI til hrutigt at finde et skud.
      int difficulty = 0;
      int currentFrame;

      public Enemy(int id, String name,int difficulty) {
            super(id,name);
            this.difficulty = difficulty;
      }
      public void shoot(double angle, double force) {
            
            double[] shot = enemyDifficulty(enemyShot(xPos+size/2, yPos+size/2), difficulty);  //Inner function finds shot by bruteforce, outer function adds variability to shot.
            angle = shot[0];
            force = shot[1];
            
            try {
                  TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
            }
            this.playerShot = new Shot(xPos+size/2, yPos+size/2, true, true, id);
            playerShot.applyForce(angle, force);
      }

      public double[] enemyShot(int x, int y) {

            int iteration = 2500; //Number of updateBall() iterations per brute force simulation

            int iterationAngle = 90; // Antal udførte kast
            int iterationForce = 20; // Antal udførte kast

            double[] angleMinMax = { 0, 180 };
            double[] forceMinMax = { 0, 40 };

            ArrayList<ArrayList<Double>> liste = new ArrayList<ArrayList<Double>>();

            for (int iA = 0; iA <= iterationAngle; iA++) {
                  
                  double AngleStep = ((angleMinMax[1] - angleMinMax[0]) / iterationAngle);                        // Calculate the step size
                  double angleNext;
                  if ( iA % 2 == 0 ){
                        angleNext = (angleMinMax[1]/2)+(iA/2)*AngleStep;
                  } else {
                        angleNext = (angleMinMax[1]/2)-((iA+1)/2)*AngleStep;                                      //Offset added
                  }

                  for (int iF = 0; iF <= iterationForce; iF++) {
                        double ForceStep = ((forceMinMax[1] - forceMinMax[0]) / iterationForce);                        // Calculate the step size
                        double forceNext = forceMinMax[0] + ForceStep * iF;

                        Shot simulation = new Shot(x, y, true, false, this.id);
                        simulation.applyForce(angleNext,forceNext);

                        int collisionQuantification = 0;


                        ArrayList<Double> data = new ArrayList<Double>(); // Initialize and reset arraylist for every iteration
                        for (int i = 0; i < iteration; i++) {

                              if (simulation.wallCollision(false)) { // Attempt to quantify the amount of collision.
                                    collisionQuantification++;
                              }

                              // Save infomation if balls hits the target.
                              if (simulation.playerCollision(false)) {

                                    data.add(angleNext);
                                    data.add(forceNext);
                                    data.add(Double.valueOf(collisionQuantification));
                                    data.add(Double.valueOf(i));
                                    data.add(simulation.ballXPos);
                                    data.add(simulation.ballYPos);

                                    liste.add(data);
                                    break;                                          //Break out of the respective simulation loop.
                              }
                              simulation.updateShot();                              // Simulate the ball according to applied forces on the map.
                        }
                        simulation = null;                                          //Remove pointer to shot-object.
                  
                  
                  
                  //Break the double for loop if lightBruteForce is on, and a hit is found.
                        if (liste.size() >= 1 && lightBruteForce){
                              break;
                        }
                  }
                  if (liste.size() >= 1 && lightBruteForce){
                        break;
                  }
            }

            double[] bestShot = new double[liste.get(0).size()];  //Initialize an empty array the size of the arraylist, nessecary for the following code not to throw errors.

            boolean cleanShot = false; // Flag if a clean hit upon the target is possible.

            for (int i = 0; i < liste.size(); i++) {
                  if (liste.get(i).get(5) <= App.height && liste.get(i).get(2) == 0) { // highest hit and no wall collision
                        bestShot = doubleObjectToPrimitive(liste.get(i));
                        cleanShot = true;
                  }     
            }


            if (!cleanShot) {
                  bestShot[2] = 10000;
                  for (int i = 0; i < liste.size(); i++) {
                        if (liste.get(i).get(2) < bestShot[2]) {              // Lowest amount of wall collision
                              bestShot = doubleObjectToPrimitive(liste.get(i));
                        }
                  }
            }
            System.out.println(bestShot[0] + " " +bestShot[1] + " " + bestShot[2]);
            return bestShot;
      }


      public double[] doubleObjectToPrimitive(ArrayList<Double> object){
            double[] primitive = new double[object.size()];
            for (int i = 0; i < object.size(); i++){
                  primitive[i] = object.get(i);
            }
            return primitive;
      }




    public double[] enemyDifficulty(double[] shot, int difficulty){
        Random rand = new Random();

        double deltaAngle;
        double deltaForce;
        
        switch (difficulty) {
            case 1:                 //Easy
                                    //These random variance numbers arbitary determented for the respective levels
                  deltaAngle = 3;   
                  deltaForce = 3;
                  break;
            case 2:                 //Medium
                  deltaAngle = 2;
                  deltaForce = 2;
                 break;
            case 3:                 //Hard
                  deltaAngle = 1;
                  deltaForce = 1;
                  break;
            default:                //Anything but 1,2,3 results in no variance.
                  deltaAngle = 0;
                  deltaForce = 0;
                  break;
        }
        deltaAngle = (rand.nextDouble()*2*deltaAngle)-deltaAngle;
        deltaForce = (rand.nextDouble()*2*deltaForce)-deltaForce;

        shot[0] += deltaAngle;
        shot[1] += deltaForce;

        return shot;
    }

}