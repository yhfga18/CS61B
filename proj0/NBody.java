public class NBody {
  public static double readRadius(String file){
    In in = new In(file);
    in.readInt();
    return in.readDouble();
  }
  public static Planet[] readPlanets(String file){
    In in = new In(file);
    int n = in.readInt();
    double radius = in.readDouble();
    Planet[] aPlanets = new Planet[n];
    double xCoordinate;
    double yCoordinate;
    double xVelocity;
    double yVelocity;
    double mass;
    String name;
    for(int i = 0; i < n; i++){
      xCoordinate = in.readDouble();
      yCoordinate = in.readDouble();
      xVelocity = in.readDouble();
      yVelocity = in.readDouble();
      mass = in.readDouble();
      name = in.readString();
      aPlanets[i] = new Planet(xCoordinate, yCoordinate, xVelocity, yVelocity, mass, name);
    }
  return aPlanets;
  }

  public static void main(String[] args){

    double T = Double.parseDouble(args[0]);
    double dt = Double.parseDouble(args[1]);
    String filename = args[2];
    Double radius = readRadius(filename);
    Planet[] planets = readPlanets(filename);
    String imageToDraw = "images/starfield.jpg";
    StdDraw.setScale(-radius, radius);
    StdDraw.picture(0, 0, imageToDraw);
    StdAudio.play("audio/2001.mid");
    for (int i = 0; i < planets.length; i++){
      planets[i].draw();
    }

    double time = 0;
    while (time < T){
      Double[] xForces = new Double[planets.length];
      Double[] yForces = new Double[planets.length];
      for (int i=0; i < planets.length; i++){
        xForces[i] = planets[i].calcNetForceExertedByX(planets);
        yForces[i] = planets[i].calcNetForceExertedByY(planets);
      }
      for (int i=0; i < planets.length; i++){
        planets[i].update(dt, xForces[i], yForces[i]);
      }
      StdDraw.picture(0, 0, imageToDraw);
      for (int i = 0; i < planets.length; i++){
        planets[i].draw();
      }
      StdDraw.show(10);
      time += dt;
    }
    StdOut.printf("%d\n", planets.length);
    StdOut.printf("%.2e\n", radius);
    for (int i = 0; i < planets.length; i++) {
    StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
      planets[i].xxPos, planets[i].yyPos, planets[i].xxVel, planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
}

  }


}




