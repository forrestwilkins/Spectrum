package processing.test.match;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class match extends PApplet {

Prism[] ps = new Prism[5];
Choices[] cs = new Choices[4];
Engine ei;

int level = 1, highestLvl = 5, totalCorrect = 0, correct, current;
float score, levelScore, shownTotal, totalScore = 0;
boolean intro = true, paused, helping = false;

public void setup() {
 
  orientation(PORTRAIT);
  ei = new Engine();
  ei.bootStrap();
}

public void draw() {
  lights();
  background(0);
  ei.display();
}

public void mousePressed() {
  for (int i=0; i < ps.length; i++) {
    ps[i].overCube(ps);
  }
}

public void mouseReleased() {
  ei.checkButtons();
  cs[ei.which()].chosen(ps);
  for (int i=0; i < ps.length; i++) {
    ps[i].touched = false;
  } mouseX = 0;
  mouseY = 0;
}
// take color off as arg for display

class Button {
  int guiColor;
  float x, y, w, h;
  
  Button (float xLoc, float yLoc) {
    x = xLoc;
    y = yLoc;
    w = 200;
    h = 100;
  }
  
  public void display(String label, int gColor) {
    guiColor = gColor;
    noFill();
    if (overButton()) {
      stroke(255);
      strokeWeight(6);
      textSize(45);
    } else {
      stroke(guiColor);
      strokeWeight(4.5f);
      textSize(40);
    } rectMode(CENTER);
    rect(x, y, w, h, 15);
    if (overButton()) {
      fill(255);
    } else fill(guiColor);
    text(label, x, y+15);
  }
  
  public void goBack() {
    noStroke();
    triangle(x, y, x+50, y-25, x+50, y+25);
  }
  
  public void up() {
    noStroke();
    triangle(/*left*/x, y,
      /*right*/x+60, y,
      /*top*/x+30, y-65);
  }
  
  public void down() {
    noStroke();
    triangle(x, y-20, x+60, y-20, x+30, y+50);
  }

  public boolean overButton() {
    float disX = x - mouseX;
    float disY = y - mouseY;
    if(sqrt(sq(disX) + sq(disY)) < w/2) {
      return true;
    } else return false;
  }
}
// number of correct coupled with time for overall score
// colorMorph() for level 5, more variety of colors in higher levels
// a certain number of correct to get to the next level
// more prisms and less time in higher levels
// needs more animations

class Choices extends Prism {
  int numberID;
  
  Choices () {
    colorPalette();
    w = h = d = 75;
  }
  
  public void render() {
    if (!paused) {
      stroke(sColor);
      strokeWeight(3);
      fill(sColor, alpha);
      cube();
    }
  }
  
  public void chosen(Prism[] p) {
    if (mouseY > height-(height*0.3f)) {
      for (int i=0; i < p.length; i++) {
        if (ps[i].touched) {
          ps[i].fadeTo(x, y);
          ps[i].fading = true;
        }
      } shapeCompare(p);
    }
  }
  
  public void shapeCompare(Prism[] p) {
    float redDiff, greenDiff, blueDiff;
    for (int i = 0; i < p.length; i++) {
      if (p[i].touched) {
        redDiff = abs(red - p[i].red);
        greenDiff = abs(green - p[i].green);
        blueDiff = abs(blue - p[i].blue);
        score = map(redDiff + greenDiff +
          blueDiff, 0, 300, 100, 0);
        if (numberID == p[i].bestMatch) {
          totalScore += score;
          levelScore += score;
          totalCorrect++;
          correct++;
        } else levelScore += score/3;
      }
    }
  }
  
  public void cube() {
    if (mode == 0) {
      pushMatrix();
      // move/change here
      translate(x, y, z);
      box(w);
      popMatrix();
    }
  }
  
  public void ball() {
    if (mode == 1) {
      fill(sColor, alpha);
      pushMatrix();
      // move/change here
      translate(x, y, z);
      spin();
      sphere(w/2);
      popMatrix();
    }
  }
}
class Engine {
  Gui gui = new Gui();
  
  float backRed, backGreen, backBlue;
  int backColor;
  boolean fatR = false,
    fatG = false,
    fatB = false;
  
  Engine () {
    backRed = random(0, 256);
    backGreen = random(0, 256);
    backBlue = random(0, 256);
    backColor = color(backRed,
      backGreen, backBlue);
  }
  
  public void display() {
    if (!levelFinished() && !intro) {
      for (int i=0; i < cs.length; i++) {
        if (level == highestLvl) {
          cs[i].colorMorph();
        }
        cs[i].render();
      } for (int i=0; i < ps.length; i++) {
          if (level == highestLvl) {
            ps[i].bestMatch(cs);
          }
          ps[i].render(ps);
      }
    } gameFace();
  }
  
  public void gameFace() {
    if (intro) {
      backMorph();
      background(backColor);
      gui.intro();
    } else {
      if (levelFinished()) {
        gui.nextLevel();
        gui.tryAgain();
        gui.playAgain();
      } else {
        gui.total();
        gui.clock();
        gui.pauseButton();
      } gui.pauseScreen();
      gui.help();
    }
  }
  
  public void checkButtons() {
    if (levelFinished()) {
      gui.checkNext();
      gui.checkYesNo();
    } gui.checkIntro();
    gui.checkPause();
    gui.checkMenu();
    gui.checkHelp();
  }
    
  public boolean levelFinished() {
    boolean allUsed = true;
    for (int i=0; i < ps.length; i++) {
      if (!ps[i].used) {
        allUsed = false;
      }
    } if (allUsed || current < 1) return true;
    else return false;
  }
  
  public int which() {
    if (mouseX < cs[0].x + (width*0.1f)) {
      return 0;
    } else if (mouseX < cs[1].x + (width*0.1f)) {
      return 1;
    } else if (mouseX < cs[2].x + (width*0.1f)) {
      return 2;
    } else {
      return 3;
    }
  }
  
  public void bootStrap() {
    float x = width*0.2f;
    gui = new Gui();
    // reset states
    paused = false;
    levelScore = 0;
    shownTotal = 0;
    correct = 0;
    score = 0;
    // time for each level
    if (level == 1) {
      current = 60;
    } else if (level <= 3) {
      current = 45;
    } else if (level == 4) {
      current = 30;
    } else if (level == 5) {
      current = 20;
    } // boot up choices
    for (int i=0; i < cs.length; i++) {
      cs[i] = new Choices();
      cs[i].numberID = i;
      cs[i].x = x;
      cs[i].y = height*0.9f;
      x += width*0.2f;
    } // boot up prisms
    for (int i=0; i < ps.length; i++) {
      ps[i] = new Prism();
      ps[i].bestMatch(cs);
    } grid();
  }
  
  public void grid() {
    int i = 0;
    for (float y = height*0.2f; y < height*0.7f; y += height*0.2f) {
      for (float x = width*0.2f; x < width*0.8f; x += height*0.2f) {
        if (i < ps.length) {
          ps[i].x = x;
          ps[i].y = y;
        } i++;
      }
    }
  }
  
  public void backMorph() {
    float colorCR = 0.5f;
    float min = 1;
    float max = 200;
    backColor = color(backRed,
      backGreen, backBlue);
    if (backRed < min) {
      fatR = false;
    } else if (backRed > max) {
        fatR = true;
    } if (fatR) {
        backRed -= colorCR;
    } else backRed += colorCR;
    // Green
    if (backGreen < min) {
      fatG = false;
    } else if (backGreen > max) {
        fatG = true;
    } if (fatG) {
        backGreen -= colorCR;
    } else backGreen += colorCR;
    // Blue
    if (backBlue < min) {
      fatB = false;
    } else if (backBlue > max) {
        fatB = true;
    } if (fatB) {
        backBlue -= colorCR;
    } else backBlue += colorCR;
  }
}
// different animations in finish screen, showing time/number correct, circles filling up
// an intro screen to greet users, animations when transitioning to game
// help screen from pause menu for rgb info on colors, user could adjust values -
// interactively to create colors to learn, with triangle buttons
// explain why level failed

class Gui {  
  Button introStart, introExit, nextLevel,
    tryYes, tryNo, playYes, playNo,
    resume, reset, help, exit, back,
    redUp, redDown, greenUp, greenDown,
      blueUp, blueDown;

  float pX, pY, pW, pH;
  float red, green, blue,
    redV, greenV, blueV,
    currentMillis;
  int guiColor, helpColor;
  boolean fatR = false,
    fatG = false,
    fatB = false;
  
  Gui () {
    currentMillis = millis();
    // colors
    red = random(0, 200);
    green = random(0, 200);
    blue = random(0, 200);
    guiColor = color(red,
      green, blue);
    // help colors
    redV = greenV = blueV = 255;
    // main buttons
    introStart = new Button(width*0.3f, height*0.6f);
    introExit = new Button(width*0.7f, height*0.6f);
    nextLevel = new Button(width/2, height*0.8f);
    tryYes = new Button(width*0.3f, height*0.7f);
    tryNo = new Button(width*0.7f, height*0.7f);
    playYes = new Button(width*0.35f, height*0.75f);
    playNo = new Button(width*0.65f, height*0.75f);
    playYes.w = playNo.w = 150;
    playYes.h = playNo.h = 100;
    // pause button
    pX = width-60;
    pY = 50;
    pW = 20;
    pH = 40;
    // pause menu
    resume = new Button(width/2, height*0.2f);
    reset = new Button(width/2, height*0.4f);
    exit = new Button(width/2, height*0.6f);
    help = new Button(width/2, height*0.8f);
    back = new Button(50, 75);
    // help buttons
    redUp = new Button(width*0.17f, height*0.4f);
    greenUp = new Button(width*0.47f, height*0.4f);
    blueUp = new Button(width*0.77f, height*0.4f);
    redDown = new Button(width*0.17f, height*0.6f);
    greenDown = new Button(width*0.47f, height*0.6f);
    blueDown = new Button(width*0.77f, height*0.6f);
  }
  
  public void intro() {
    colorMorph();
    fill(guiColor);
    textSize(100);
    textAlign(CENTER);
    text("Spectrum", width/2, height*0.4f);
    introStart.display("Start", guiColor);
    introExit.display("Exit", guiColor);
  }
  
  public void nextLevel() {
    if (correct >= 3 && level < highestLvl) {
      colorMorph();
      fill(guiColor);
      textAlign(CENTER);
      textSize(50);
      text("Time Left: " + current + " sec.",
        width/2, height*0.2f);
      text("Score: " + PApplet.parseInt(levelScore),
        width/2, height*0.4f);
      text("Correct: " + correct,
        width/2, height*0.6f);
      nextLevel.display("Level " + (level+1), guiColor);
    }
  }
  
  public void tryAgain() {
    if (correct <= 2) {
      colorMorph();
      fill(guiColor);
      textAlign(CENTER);
      textSize(80);
      text("Level Failed", width/2, height*0.2f);
      textSize(40);
      text(failReason(), width/2, height*0.4f);
      textSize(70);
      text("Try again?", width/2, height*0.6f);
      tryYes.display("Yes", guiColor);
      tryNo.display("No", guiColor);
    }
  }

  public void playAgain() {
    if (level >= highestLvl && correct >= 3) {
      colorMorph();
      fill(guiColor);
      textAlign(CENTER);
      // Total score result
      textSize(75);
      text("Total Score: " + PApplet.parseInt(totalScore),
        width/2, height*0.2f);
      // message for user
      textSize(40);
      text(message(), width/2, height*0.4f);
      // Play again question
      textSize(60);
      text("Play again?",
        width/2, height*0.65f);
      playYes.display("Yes", guiColor);
      playNo.display("No", guiColor);
    }
  }
  
  public void pauseScreen() {
    if (paused && !helping) {
      colorMorph();
      resume.display("Resume", guiColor);
      reset.display("Reset", guiColor);
      exit.display("Exit", guiColor);
      help.display("Help", guiColor);
    }
  }
  
  public void help() {
    if (paused && helping) {
      if (redV+greenV+blueV <= 15) {
        redV=greenV=blueV=255;
      } helpColor = color(redV,
      greenV, blueV);
      fill(helpColor);
      textAlign(CENTER);
      textSize(55);
      // back
      back.goBack();
      // Red
      redUp.up();
      text("R:"+PApplet.parseInt(redV), width*0.2f, height/2);
      redDown.down();
      // Green
      greenUp.up();
      text("G:"+PApplet.parseInt(greenV), width/2, height/2);
      greenDown.down();
      // Blue
      blueUp.up();
      text("B:"+PApplet.parseInt(blueV), width*0.8f, height/2);
      blueDown.down();
    }
  }
  
  public void total() {
    if (!paused) {
      colorMorph();
      fill(guiColor);
      textSize(55);
      textAlign(CENTER);
      text(PApplet.parseInt(shownTotal), 50, 70);
      if (shownTotal < levelScore) {
        shownTotal += 1;
      }
    }
  }
  
  public void clock() {
    if (!paused) {
      if (millis() > currentMillis+1000) {
        currentMillis = millis();
        current--;
      } colorMorph();
      fill(guiColor);
      textSize(35);
      textAlign(CENTER);
      text(current,
        width/2+25, 60);
      // clock
      noStroke();
      ellipse(width/2-25, 50, 35, 35);
      // hands
      stroke(0);
      strokeWeight(3);
      line(width/2-25, 50, width/2-25, 35);
      line(width/2-25, 50, width/2-13, 53);
    }
  }
  
  public String failReason() {
    if (current < 1) {
      return "You ran out of time.";
    } else if (correct == 0) {
      return "You didn't get any right.";
    } else {
      return "You only got " + correct + " right.";
    }
  }

  public String message() {
    if (totalCorrect > 22) {
      return "You are a master of light.";
    } else if (totalCorrect > 19) {
      return "You are a wizard of the spectrum.";
    } else if (totalCorrect > 17) {
      return "You are one with the spectrum.";
    } else if (totalCorrect > 16) {
      return "You are learning the ways of light.";
    } else if (totalCorrect > 15) {
      return "The spectrum is above you.";
    } else {
      return "Are you blind?";
    }
  }
  
  public void checkIntro() {
    if (intro) {
      if (introStart.overButton()) {
        intro = false;
      } else if (introExit.overButton()) {
        exit();
      }
    }
  }
  
  public void checkNext() {
    // for next and try again
    if (correct >= 3 && level < highestLvl) {
      // next level
      if (nextLevel.overButton()) {
        level++; // before boot
        ei.bootStrap();
      }
    } else if (correct <= 2) {
      // try again
      if (tryYes.overButton()) {
        // subtract unearned points
        totalScore -= score;
        totalCorrect -= correct;
        ei.bootStrap();
      } else if (tryNo.overButton()) {
        exit();
      }
    }
  }
  
  public void checkYesNo() {
    if (level >= highestLvl && correct >= 3) {
      if (playYes.overButton()) {
        level = 1;
        ei.bootStrap();
        totalScore = 0;
        totalCorrect = 0;
      } else if (playNo.overButton()) {
        exit();
      }
    }
  }
  
  public void checkPause() {
    if (overPause()) {
      paused = true;
    }
  }
  
  public void checkMenu() {
    if (paused && !helping) {
      if (resume.overButton()) {
        paused = false;
      } else if (reset.overButton()) {
        level = 1;
        totalScore = 0;
        totalCorrect = 0;
        ei.bootStrap();
      } else if (exit.overButton()) {
        exit();
      } else if (help.overButton()) {
        redV = greenV = blueV = 255;
        helping = true;
      } 
    }
  }
  
  public void checkHelp() {
    // down to reset to 255 if 5
    float colorCR = 50;
    if (paused && helping) {
      if (back.overButton()) {
        helping = false;
      } else if (redUp.overButton() && redV < 255) {
        redV += colorCR;
      } else if (redDown.overButton() && redV > 5) {
        redV -= colorCR;
      } else if (greenUp.overButton() && greenV < 255) {
        greenV += colorCR;
      } else if (greenDown.overButton() && greenV > 5) {
        greenV -= colorCR;
      } else if (blueUp.overButton() && blueV < 255) {
        blueV += colorCR;
      } else if (blueDown.overButton() && blueV > 5) {
        blueV -= colorCR;
      }
    }
  }
  
  public void pauseButton() {
    if (!paused) {
      noStroke();
      fill(guiColor);
      rectMode(CENTER);
      rect(pX, pY, pW, pH);
      rect(pX+25, pY, pW, pH);
    }
  }
  
  public boolean overPause() {
    float disX = pX+pW - mouseX;
    float disY = pY - mouseY;
    if (sqrt(sq(disX) + sq(disY)) < pW*4/2) {
      return true;
    } else return false;
  }
  
  public void colorMorph() {
    float colorCR;
    float min = 100;
    float max = 230;
    // Red
    colorCR = random(1.5f);
    if (red < min) {
      fatR = false;
    } else if (red > max) {
        fatR = true;
    } if (fatR) {
        red -= colorCR;
    } else red += colorCR;
    // Green
    colorCR = random(1.5f);
    if (green < min) {
      fatG = false;
    } else if (green > max) {
        fatG = true;
    } if (fatG) {
        green -= colorCR;
    } else green += colorCR;
    // Blue
    colorCR = random(1.5f);
    if (blue < min) {
      fatB = false;
    } else if (blue > max) {
        fatB = true;
    } if (fatB) {
        blue -= colorCR;
    } else blue += colorCR;
    guiColor = color(red, green, blue);
  }
}
// increase number of cubes with levels

class Prism {
  int mode = 0;
  int bestMatch;
  int sColor;
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
  
  public void render(Prism[] p) {
    if (!used && !paused) {
      fadeAway();
      strokeWeight(3);
      stroke(sColor);
      move(p);
      cube();
    }
  }

  public void move(Prism[] p) {
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
  
  public void collision(Prism[] p) {
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
  
  public void bounce() {
    if (x < width*0.1f || x > width-(width*0.1f)) {
      xSpeed = -xSpeed;
    } if (y < height*0.15f || y > height-(height*0.3f)) {
        ySpeed = -ySpeed;
    }
  }
  
  public boolean othersTouched(Prism[] p) {
    boolean others = false;
    for (int i=0; i < p.length; i++) {
      if (p[i].touched && p[i] != this) {
        others = true;
      } else others = false;
    } if (others) return true;
    else return false;
  }
  
  public void bestMatch(Choices[] c) {
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
  
  public void fadeAway() {
    float easing = 0.03f;
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
  
  public void fadeTo(float toX, float toY) {
    choiceX = toX;
    choiceY = toY;
  }

  public void overCube(Prism[] p) {
    float disX = x - mouseX;
    float disY = y - mouseY;
    if (sqrt(sq(disX) + sq(disY)) < (w+50)/2 &&
      !othersTouched(p)) {
        touched = true;
    }
  }
  
  public void cube() {
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

  public void spin() {
    spin += 0.005f;
    switch (PApplet.parseInt(spinDirect)) {
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
  
  public void colorPalette() {
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

  public void colorMorph() {
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

  public int sketchWidth() { return displayWidth; }
  public int sketchHeight() { return displayHeight; }
  public String sketchRenderer() { return P3D; }
}
