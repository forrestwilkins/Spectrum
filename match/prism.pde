// increase number of cubes with levels, using ArrayList
// collisions caused by user could cause explosion, producing more cubes

class Prism {
  int mode = 0;
  int bestMatch;
  color sColor;
  boolean used, touched, fading;
  float red, green, blue,
    xSpeed, ySpeed, zSpeed;
  float alpha = 220;
  float spinDirect = random(4);
  float spin = random(4);
  float stroke = 0;
  boolean fatR = false,
    fatG = false,
    fatB = false,
    fatS = false;
  float x, y, z, w, h, d;
  float choiceX, choiceY;
  
  Prism () {
    colorPalette();
    xSpeed = random(-1, 1);
    ySpeed = random(-1, 1);
    w = h = d = 100;
    touched = false;
    fading = false;
    used = false;
  }
  
  void render(Prism[] p) {
    if (!used && !paused) {
      fadeAway();
      strokeWeight(3);
      stroke(sColor);
      move(p);
      cube();
    }
  }

  void move(Prism[] p) {
    if (touched) {
      x = mouseX;
      y = mouseY;
    } else {
      x += xSpeed;
      y += ySpeed;
      z = 0;
    } collision(p);
    bounce();
  }
  
  void collision(Prism[] p) {
    for (int i=0; i < p.length; i++) {
      float xDist = abs(x - p[i].x);
      float yDist = abs(y - p[i].y);
      if ((xDist < w+5) && (yDist < h+5) &&
      p[i] != this && !p[i].used) {
        xSpeed = -xSpeed;
        ySpeed = -ySpeed;
      }
    }
  }
  
  void bounce() {
    if (x < width*0.1 || x > width-(width*0.1)) {
      xSpeed = -xSpeed;
    } if (y < height*0.15 || y > height-(height*0.3)) {
        ySpeed = -ySpeed;
    }
  }
  
  boolean othersTouched(Prism[] p) {
    boolean others = false;
    for (int i=0; i < p.length; i++) {
      if (p[i].touched && p[i] != this) {
        others = true;
      } else others = false;
    } if (others) return true;
    else return false;
  }
  
  void bestMatch(Choices[] c) {
    float redDiff, greenDiff, blueDiff;
    float smallestDiff = 0;
    for (int i=0; i < c.length; i++) {
      redDiff = abs(red - c[i].red);
      greenDiff = abs(green - c[i].green);
      blueDiff = abs(blue - c[i].blue);
      if (redDiff+greenDiff+blueDiff < smallestDiff ||
        smallestDiff == 0) {
          smallestDiff = redDiff+greenDiff+blueDiff;
          bestMatch = i;
      }
    }
  }
  
  void fadeAway() {
    float easing = 0.03;
    if (fading) {
      x += (choiceX-x)*easing;
      y += (choiceY-y)*easing;
      if (w > 0) {
        w -= 2;
      } if (alpha > 0) {
        alpha -= 2;
      } if (w < 1) {
        used = true;
      }
    }
  }
  
  void fadeTo(float toX, float toY) {
    choiceX = toX;
    choiceY = toY;
  }

  void overCube(Prism[] p) {
    float disX = x - mouseX;
    float disY = y - mouseY;
    if (sqrt(sq(disX) + sq(disY)) < (w+50)/2 &&
      !othersTouched(p)) {
        touched = true;
    }
  }
  
  void cube() {
    if (mode == 0) {
      fill(sColor, alpha);
      pushMatrix();
      // move/change here
      translate(x, y, z);
      spin();
      box(w);
      popMatrix();
    }
  }

  void spin() {
    spin += 0.005;
    switch (int(spinDirect)) {
      case 0:
        rotateX(PI/5 - spin);
        rotateY(PI/5 - spin);
        break;
      case 1:
        rotateX(PI/5 + spin);
        rotateY(PI/5 + spin);
        break;
      case 2:
        rotateX(PI/5 + spin);
        rotateY(PI/5 - spin);
        break;
      case 3:
        rotateX(PI/5 - spin);
        rotateY(PI/5 + spin);
        break;
      case 4:
        rotateX(PI/5);
        rotateY(PI/5 + spin);
        break;
      case 5:
        rotateX(PI/5);
        rotateY(PI/5 - spin);
        break;
    }
  }
  
  void colorPalette() {
    // greater ranges of colors in higher levels
    if (level <= 2) {
      red = random(100, 256);
      green = random(100, 256);
      blue = random(100, 256);
    } else if (level <= 4) {
      red = random(50, 256);
      green = random(50, 256);
      blue = random(50, 256);
    } else {
      red = random(0, 256);
      green = random(0, 256);
      blue = random(0, 256);
    } sColor = color(red, green, blue, alpha);
  }

  void colorMorph() {
    float colorCR = 1;
    float min = 100;
    float max = 254;
    if (red < min) {
      fatR = false;
    } else if (red > max) {
        fatR = true;
    } if (fatR) {
        red -= colorCR;
    } else red += colorCR;
    // Green
    if (green < min) {
      fatG = false;
    } else if (green > max) {
        fatG = true;
    } if (fatG) {
        green -= colorCR;
    } else green += colorCR;
    // Blue
    if (blue < min) {
      fatB = false;
    } else if (blue > max) {
        fatB = true;
    } if (fatB) {
        blue -= colorCR;
    } else blue += colorCR;
    sColor = color(red, green, blue);
  }
}
