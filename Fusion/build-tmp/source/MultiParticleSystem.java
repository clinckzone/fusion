import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import shiffman.box2d.*; 
import java.util.Iterator; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.joints.*; 
import org.jbox2d.collision.shapes.*; 
import org.jbox2d.collision.shapes.Shape; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.*; 
import org.jbox2d.dynamics.contacts.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MultiParticleSystem extends PApplet {











float G = 1000.0f;
ArrayList<ParticleSystem> systems;
ArrayList<Attractor> attractors;
Box2DProcessing box2d;
PImage image;

public void setup()  {
  
  background(0);
  
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  box2d.setGravity(0, 0);
  
  image = loadImage("particle1.png");
  systems = new ArrayList<ParticleSystem>();
  attractors = new ArrayList<Attractor>();
}

public void draw()  {
  blendMode(ADD);
  background(0);   
  box2d.step();
  
  for(Attractor attractor : attractors)  {
    attractor.display();
  }
  
  Iterator<ParticleSystem> iterator = systems.iterator();
  while(iterator.hasNext())  {
    ParticleSystem particleSystem = iterator.next();
    if(particleSystem.isDead())  iterator.remove();
    else  {
      for(Attractor attractor : attractors)  {
        particleSystem.applyForce(attractor);     
      }
      particleSystem.run();
    }
  }
}

public void mousePressed()  {
  if(mouseButton == LEFT)  {
    systems.add(new ParticleSystem(mouseX, mouseY, 1000));
  }
  else if(mouseButton == RIGHT)  {
    attractors.add(new Attractor(mouseX, mouseY, 0, 20, random(255)));
  }
  else if(mouseButton == CENTER)  {
    background(255);
  }
}
class Attractor  {

  Body body;
  float hue;
  float angle;
  float radius;
  Vec2 position;
  
  Attractor(float x, float y, float angle, float radius, float hue)  {
    this.hue = hue;
    this.angle = angle;
    this.radius = radius;
    this.position = new Vec2(x, y);    
    makeAttractor(x, y, angle, radius);
  }
  
  public void makeAttractor(float x, float y, float angle, float radius)  {
    BodyDef bodyDef = new BodyDef();
    bodyDef.setType(BodyType.STATIC);
    bodyDef.setAngle(angle);
    bodyDef.position.set(box2d.coordPixelsToWorld(x,y));
    body = box2d.createBody(bodyDef);  
    
    CircleShape circleShape = new CircleShape();
    circleShape.m_radius = box2d.scalarPixelsToWorld(radius);
    
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = circleShape;
    fixtureDef.density = 5;
    fixtureDef.friction = 0;
    fixtureDef.restitution = 0;
    
    body.createFixture(fixtureDef);
  }
  
  public Vec2 attract(Particle particle)  {
    Vec2 particlePos = particle.body.getWorldCenter();
    Vec2 attractorPos = body.getWorldCenter();
    Vec2 force = attractorPos.sub(particlePos);
    
    float distance = force.length();
    distance = constrain(distance, 1, 5);
    float strength = (G)*(particle.body.m_mass)/sq(distance); //<>//
    
    force.normalize();
    force.mulLocal(strength);
    
    return force;
  }
  
  public void display()  {
    pushStyle();
      colorMode(HSB);
      noStroke();
      fill(hue, 255, 255, 200);
      pushMatrix();
        translate(position.x, position.y);
        rotate(-angle);
        ellipse(0, 0, 2*radius, 2*radius);
      popMatrix();
    popStyle();
  }

}
class Particle  {
  
  Body body;
  float hue;
  float angle;
  float radius;
  float lifespan;
  Vec2  position;
  Vec2 prevPosition;
  
  Particle(float x, float y, float angle, float radius, float hue)  {
    this.position = new Vec2(x, y);
    this.prevPosition = null;
    
    this.hue = hue;
    this.angle = angle;
    this.radius = radius;
    this.lifespan = 255;
    
    makeParticle(x, y, radius, angle);
  }
  
  public void makeParticle(float x, float y, float radius, float angle)  {
    BodyDef bodyDef = new BodyDef();
    bodyDef.setType(BodyType.DYNAMIC);
    bodyDef.setAngle(angle);
    bodyDef.position.set(box2d.coordPixelsToWorld(x,y));
    body = box2d.createBody(bodyDef);  
    
    CircleShape circleShape = new CircleShape();
    circleShape.m_radius = box2d.scalarPixelsToWorld(radius);
    
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = circleShape;
    fixtureDef.density = 5;
    fixtureDef.friction = 0;
    fixtureDef.restitution = 0;
    
    body.createFixture(fixtureDef);
    body.setLinearVelocity(new Vec2(random(-1, 1), random(-1, 1)));
  }
  
  public void applyForce(Vec2 force)  {
    Vec2 position = body.getWorldCenter();    
    body.applyForce(force, position);
  }
  
  public void update()  {
    prevPosition = position.clone();
    position = box2d.getBodyPixelCoord(body);
    angle = body.getAngle();
    lifespan = lifespan - 0.5f;
    
    //Release this statement just in case you want to change the display method :)
    hue = lifespan;
  }
  
  public boolean isDead()  {
    if(lifespan < 0)  return true;
    else return false;
  }
  
  public void destroy()  {
    box2d.destroyBody(body);
  }
  
  public void display()  {
    pushStyle();
      noStroke();
      colorMode(HSB);
      imageMode(CENTER);
      //fill(hue, 255, 255, lifespan);
      tint(hue, 255, 255, lifespan/2);
      pushMatrix();
        translate(position.x, position.y);
        rotate(-angle);
        image(image, 0, 0);
        //ellipse(0, 0, 6*radius, 6*radius);
      popMatrix();
      //stroke(hue, 255, 255, lifespan);
      //line(prevPosition.x, prevPosition.y, position.x, position.y);
    popStyle();
  }
  
}
class ParticleSystem  {

  ArrayList<Particle> particles;
  Vec2 position;
  float num;
  
  ParticleSystem(float x, float y, float num)  {
    this.position = new Vec2(x, y);
    this.num = num;
    particles = new ArrayList<Particle>();
  }
  
  public void applyForce(Attractor attractor)  {
    for(Particle particle : particles)  {
      Vec2 force = attractor.attract(particle);
      particle.applyForce(force);
    }
  }
  
  public void addParticles()  {
    particles.add(new Particle(position.x, position.y, 0, 1.5f, random(255)));
    num--;
  }
  
  public boolean isDead()  {
    if(particles.size() == 0 && num <= 0)  return true;
    else return false;
  }
  
  public void run()  {
    if(num > 0)  addParticles();        
    Iterator<Particle> iterator = particles.iterator();
    while(iterator.hasNext())  {
      Particle particle = iterator.next();
      if(particle.isDead())  {
        particle.destroy();
        iterator.remove();
      }
      else  {  
        particle.update();        
        particle.display(); 
      }
    }
  }
  
}
  public void settings() {  fullScreen(P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "MultiParticleSystem" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
