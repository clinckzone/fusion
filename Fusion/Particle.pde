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
  
  void makeParticle(float x, float y, float radius, float angle)  {
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
  
  void applyForce(Vec2 force)  {
    Vec2 position = body.getWorldCenter();    
    body.applyForce(force, position);
  }
  
  void update()  {
    prevPosition = position.clone();
    position = box2d.getBodyPixelCoord(body);
    angle = body.getAngle();
    lifespan = lifespan - 0.5;
    
    //Release this statement just in case you want to change the display method :)
    hue = lifespan;
  }
  
  boolean isDead()  {
    if(lifespan < 0)  return true;
    else return false;
  }
  
  void destroy()  {
    box2d.destroyBody(body);
  }
  
  void display()  {
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
