import java.util.ArrayList;

 
public class Enemy extends Player{
      

      public Enemy(int id, String name) {
            super(id,name);
            System.out.println("ENEMY OPRETTET");
      }

      // public void leggo() {
      //       System.out.println("Enemy, shoot()");

      //       int iteration = 1000; // 1000/30FPS = 30sek simulering

      //       int iterationAngle = 180; // Antal udførte kast
      //       int iterationForce = 50; // Antal udførte kast

      //       double[] angleMinMax = { 0, 180 };
      //       double[] forceMinMax = { 0, 100 };

      //       ArrayList<ArrayList<Double>> liste = new ArrayList<ArrayList<Double>>();

      //       for (int iA = 0; iA < ite  rationAngle; iA++) {
      //             for (int iF = 0; iF < iterationForce; iF++) {

      //                   // Calculate the step size.
      //                   double AngleStep = ((angleMinMax[1] - angleMinMax[0]) / iterationAngle);
      //                   double ForceStep = ((forceMinMax[1] - forceMinMax[0]) / iterationForce);

      //                   // Set-up for the variables for the next shot in line.
      //                   double angleNext = angleMinMax[0] + AngleStep * iA;
      //                   double forceNext = forceMinMax[0] + ForceStep * iF;

      //                   Shot simulation = new Shot(xPos, yPos, true, false,0);

      //                   simulation.applyForce(angleNext,forceNext);

      //                   int collisionQuantification = 0;

      //                   ArrayList<Double> data = new ArrayList<Double>(); // Initialize and reset arraylist for every iteration
      //                   for (int i = 0; i < iteration; i++) {

      //                         if (simulation.wallCollisionTEST()) { // Attempt to quantify the amount of collision.
      //                               collisionQuantification++;
      //                         }

      //                         // Save infomation if balls hits the target.
      //                         if ((simulation.getPosition()[0] <= 50) && (simulation.getPosition()[1] <= 50)) { // TODO: Collision mellem bold og player skal være her
      //                               data.add(angleNext);
      //                               data.add(forceNext);
      //                               data.add(Double.valueOf(collisionQuantification));

      //                               liste.add(data);
      //                               break;
      //                         }
      //                         // Simulate the ball according to applied forces on the map.
      //                         simulation.updateShot();
      //                   }
      //                   simulation = null;
      //                   // System.gc(); 
      //             }
      //       }

      //       double[] bestShot = { 0, 0, 0 };

      //       boolean cleanShot = false; // Flag if a clean hit upon the target is possible.

      //       for (int i = 0; i < liste.size(); i++) {
      //             if (liste.get(i).get(1) > bestShot[1] && liste.get(i).get(2) == 0) { // Lowest force and no
      //                                                                                  // wall collision
      //                   bestShot[0] = liste.get(i).get(0);
      //                   bestShot[1] = liste.get(i).get(1);
      //                   bestShot[2] = liste.get(i).get(2);
      //                   cleanShot = true;
      //             }
      //       }
      //       if (!cleanShot) {
      //             for (int i = 0; i < liste.size(); i++) {
      //                   if (liste.get(i).get(2) < bestShot[2]) { // Lowest amount of wall collision
      //                         bestShot[0] = liste.get(i).get(0);
      //                         bestShot[1] = liste.get(i).get(1);
      //                         bestShot[2] = liste.get(i).get(2);
      //                   }
      //             }
      //       }
      //       System.out.println("Modstander: " + bestShot[0] + " " + bestShot[1]);
      //       Shot shot = new Shot(xPos, yPos, true, true);
      //       // shot.applyForce(bestShot[0],bestShot[1]);

      // }

}