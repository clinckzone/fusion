import shiffman.box2d.*;
import java.util.Iterator;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.joints.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

float G = 1000.0;
ArrayList<ParticleSystem> systems;
ArrayList<Attractor> attractors;
Box2DProcessing box2d;
PImage image;

void setup()  {
  fullScreen(P2D);
  background(0);
  
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  box2d.setGravity(0, 0);
  
  image = loadImage("particle1.png");
  systems = new ArrayList<ParticleSystem>();
  attractors = new ArrayList<Attractor>();
}

void draw()  {
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

void mousePressed()  {
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
