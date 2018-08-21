class ParticleSystem  {

  ArrayList<Particle> particles;
  Vec2 position;
  float num;
  
  ParticleSystem(float x, float y, float num)  {
    this.position = new Vec2(x, y);
    this.num = num;
    particles = new ArrayList<Particle>();
  }
  
  void applyForce(Attractor attractor)  {
    for(Particle particle : particles)  {
      Vec2 force = attractor.attract(particle);
      particle.applyForce(force);
    }
  }
  
  void addParticles()  {
    particles.add(new Particle(position.x, position.y, 0, 1.5, random(255)));
    num--;
  }
  
  boolean isDead()  {
    if(particles.size() == 0 && num <= 0)  return true;
    else return false;
  }
  
  void run()  {
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
