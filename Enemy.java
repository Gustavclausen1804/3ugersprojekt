import java.util.ArrayList;
import java.util.Random;


 
public class Enemy extends Player{
      
      int difficulty = 0;

      public Enemy(int id, String name,int difficulty) {
            super(id,name);
            this.difficulty = difficulty;
            System.out.println("ENEMY OPRETTET");
      }
      public void shoot(double angle, double force) {
            double[] shot = enemyDifficulty(enemyShot(xPos+size/2,      yPos+size/2), difficulty);  //Inner function finds shot by bruteforce, outer function adds variability to shot.
            angle = shot[0];
            force = shot[1];
            
            this.playerShot = new Shot(xPos+size/2, yPos+size/2, true, true, id);
            playerShot.applyForce(angle, force);
    }

      public double[] enemyShot(int x, int y) {
            System.out.println("Enemy, shoot()");

            int iteration = 2500; // 1000/30FPS = 30sec simulering, but devided by refinement factor

            int iterationAngle = 90; // Antal udførte kast
            int iterationForce = 20; // Antal udførte kast

            double[] angleMinMax = { 0, 180 };
            double[] forceMinMax = { 10, 40 };

            ArrayList<ArrayList<Double>> liste = new ArrayList<ArrayList<Double>>();

            for (int iA = 0; iA < iterationAngle; iA++) {
                  for (int iF = 0; iF < iterationForce; iF++) {

                        // Calculate the step size.
                        double AngleStep = ((angleMinMax[1] - angleMinMax[0]) / iterationAngle);
                        double ForceStep = ((forceMinMax[1] - forceMinMax[0]) / iterationForce);

                        // Set-up for the variables for the next shot in line.
                        double angleNext = angleMinMax[0] + AngleStep * iA;
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

                                    liste.add(data);
                                    break;

                              }
                              // Simulate the ball according to applied forces on the map.


                              simulation.updateShot();
                        }
                        simulation = null;  //Remove pointer to shot-object.
                  }
            }


            double[] bestShot = { 0, forceMinMax[1]+1, 0 };

            boolean cleanShot = false; // Flag if a clean hit upon the target is possible.

            for (int i = 0; i < liste.size(); i++) {
                  if (liste.get(i).get(1) < bestShot[1] && liste.get(i).get(2) == 0) { // Lowest force and no
                                                                                       // wall collision
                        bestShot[0] = liste.get(i).get(0);
                        bestShot[1] = liste.get(i).get(1);
                        bestShot[2] = liste.get(i).get(2);
                        cleanShot = true;
                  }
            }

            System.out.println("Antal mulige skud: " +liste.size() + ", cleanShot:" + cleanShot);


            if (!cleanShot) {
                  for (int i = 0; i < liste.size(); i++) {
                        if (liste.get(i).get(2) < bestShot[2]) { // Lowest amount of wall collision
                              bestShot[0] = liste.get(i).get(0);
                              bestShot[1] = liste.get(i).get(1);
                              bestShot[2] = liste.get(i).get(2);
                        }
                  }
            }
            System.out.println("Modstander: " + bestShot[0] + " " + bestShot[1]);
            return bestShot;

      }
    public double[] enemyDifficulty(double[] shot, int difficulty){
        Random rand = new Random();

        double deltaAngle;
        double deltaForce;
        
        switch (difficulty) {
            case 1:     //Easy
            deltaAngle = 3;
            deltaForce = 3;
                break;
            case 2:     //Medium
            deltaAngle = 2;
            deltaForce = 2;
                break;
            case 3:     //Hard
            deltaAngle = 1;
            deltaForce = 1;
                break;
            default:
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