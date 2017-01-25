public class Planet {
  // all of the numbers for this project will be doubles
  double xxPos; // its current x position
  double yyPos; // its current y position
  double xxVel; // its current velocity in the x direction
  double yyVel; // its current velocity in the y direction
  double mass;  // its mass
  String imgFileName; // The name of an image in the images directory that depicts the planet

  public Planet(double xP, double yP, double xV,
              double yV, double m, String img){
    this.xxPos = xP;
    this.yyPos = yP;
    this.xxVel = xV;
    this.yyVel = yV;
    this.mass = m;
    this.imgFileName = img;

  }
  public Planet(Planet p){
    this.xxPos = p.xxPos;
    this.yyPos = p.yyPos;
    this.xxVel = p.xxVel;
    this.yyVel = p.yyVel;
    this.mass = p.mass;
    this.imgFileName = p.imgFileName; 
  }

  public double calcDistance(Planet p){
    double dx = p.xxPos - this.xxPos;
    double dy = p.yyPos - this.yyPos;
    double r2 = dx*dx + dy*dy;
    double r = Math.sqrt(r2);
    return r;

  }

  public double calcForceExertedBy(Planet p){
    double r = this.calcDistance(p);
    double G = 6.67 * Math.pow(10, -11);
    double F = G * this.mass * p.mass / (r*r);
    return F;
  }

  public double calcForceExertedByX(Planet p){
    double F = this.calcForceExertedBy(p);
    double dx = p.xxPos - this.xxPos;
    double r = this.calcDistance(p);
    double Fx = F * dx/r;
    return Fx;
  }

  public double calcForceExertedByY(Planet p){
    double F = this.calcForceExertedBy(p);
    double dy = p.yyPos - this.yyPos;
    double r = this.calcDistance(p);
    double Fy = F * dy/r;
    return Fy;

  }

  public double calcNetForceExertedByX(Planet[] ps){
    double netFX = 0;
    for (int i = 0; i < ps.length; i++){
      if (!(this.equals(ps[i]))) {
        netFX += this.calcForceExertedByX(ps[i]);
      }
    }
    return netFX;
  }

  public double calcNetForceExertedByY(Planet[] ps){
    double netFY = 0;
    for (int i = 0; i < ps.length; i++){
      if (this.equals(ps[i])) {
      }
      else {
        netFY += this.calcForceExertedByY(ps[i]);
      }
    }
    return netFY;
  }

  public void update(double dt, double fX, double fY){
    double net_ax = fX/this.mass;
    double net_ay = fY/this.mass;
    this.xxVel = this.xxVel + dt*net_ax;
    this.yyVel = this.yyVel + dt*net_ay;
    this.xxPos = this.xxPos + dt*xxVel;
    this.yyPos = this.yyPos + dt*yyVel;
  }

  public void draw(){
    StdDraw.picture(this.xxPos, this.yyPos, "images/" + imgFileName);
  }

}
