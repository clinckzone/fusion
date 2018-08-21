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
  
  void makeAttractor(float x, float y, float angle, float radius)  {
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
  
  Vec2 attract(Particle particle)  {
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
  
  void display()  {
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
