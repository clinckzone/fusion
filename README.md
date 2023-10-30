# Fusion
## About
Fusion is a smoke simulation developed on Processing 3 which makes use of particle systems and additive blending for simulating smoke-like behavior. In order to make the simulation more interesting, the user has the freedom to place attractors on the screen towards which the smoke particles will get attracted to in accordance with Newton’s Inverse Square Law.

<a href="https://www.youtube.com/watch?v=L3Q1xFAgtJQ">Watch the video on Youtube!</a>

![fusion](https://user-images.githubusercontent.com/28980632/49060156-41248880-f232-11e8-80b1-d2e3a0bd72eb.png)


## Usage
### Setup for local development
 - Download the entire repository on your system. 
 - Inside the folder named Fusion, there's a fusion.pde file. 
 - Open it using Processing 3. 
 - Tweak the source code as per your wish.
 - Click the play button to run the code.
 
### Program Controls
Pressing the right mouse button (RMB) places a particle system at that position while pressing the left mouse button (LMB) places an attractor. Particles that are being released from the system are attracted to these attractors in accordance with Newton's Inverse Square Law.

## Details
There are two kinds of objects here. One is simply a **particle** and another one is an **attractor**. An attractor attracts other particles in accordance with Newtown's Inverse Square Law.

A new particle system is created wherever user presses LMB. This particle system sequencially releases (i.e. one by one) a total of around 1000 particles. Once all 1000 particles have been released, the particle system dies out. Similar to a particle system, each particle also has a life span. A particle loses its visual clarity with time and completely vanishes after sometime signalling it's dead. Color of the particle also changes from red to green with time.

The default blend mode has been changed to **additive** so as to strengthen its visuals to appear more smoke like and also bring in a glowing fluid like effect. Plus, each particle has been assigned a skin which is an image with fading boundary to make it appear more smoke like.
