// different animations in finish screen, showing time/number correct, circles filling up
// an intro screen to greet users, animations when transitioning to game
// help screen from pause menu for rgb info on colors, user could adjust values -
// interactively to create colors to learn, with triangle buttons
// explain why level failed
// help could have two buttons, one for colors and one for info/explantion

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
  color guiColor, helpColor;
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
    introStart = new Button(width*0.3, height*0.6);
    introExit = new Button(width*0.7, height*0.6);
    nextLevel = new Button(width/2, height*0.8);
    tryYes = new Button(width*0.3, height*0.7);
    tryNo = new Button(width*0.7, height*0.7);
    playYes = new Button(width*0.35, height*0.75);
    playNo = new Button(width*0.65, height*0.75);
    playYes.w = playNo.w = 150;
    playYes.h = playNo.h = 100;
    // pause button
    pX = width-60;
    pY = 50;
    pW = 20;
    pH = 40;
    // pause menu
    resume = new Button(width/2, height*0.2);
    reset = new Button(width/2, height*0.4);
    exit = new Button(width/2, height*0.6);
    help = new Button(width/2, height*0.8);
    back = new Button(50, 75);
    // help buttons
    redUp = new Button(width*0.17, height*0.4);
    greenUp = new Button(width*0.47, height*0.4);
    blueUp = new Button(width*0.77, height*0.4);
    redDown = new Button(width*0.17, height*0.6);
    greenDown = new Button(width*0.47, height*0.6);
    blueDown = new Button(width*0.77, height*0.6);
  }
  
  void intro() {
    colorMorph();
    fill(guiColor);
    textSize(100);
    textAlign(CENTER);
    text("Spectrum", width/2, height*0.4);
    introStart.display("Start", guiColor);
    introExit.display("Exit", guiColor);
  }
  
  void nextLevel() {
    if (correct >= 3 && level < highestLvl) {
      colorMorph();
      fill(guiColor);
      textAlign(CENTER);
      textSize(50);
      text("Time Left: " + current + " sec.",
        width/2, height*0.2);
      text("Score: " + int(levelScore),
        width/2, height*0.4);
      text("Correct: " + correct,
        width/2, height*0.6);
      nextLevel.display("Level " + (level+1), guiColor);
    }
  }
  
  void tryAgain() {
    if (correct <= 2) {
      colorMorph();
      fill(guiColor);
      textAlign(CENTER);
      textSize(80);
      text("Level Failed", width/2, height*0.2);
      textSize(40);
      text(failReason(), width/2, height*0.4);
      textSize(70);
      text("Try again?", width/2, height*0.6);
      tryYes.display("Yes", guiColor);
      tryNo.display("No", guiColor);
    }
  }

  void playAgain() {
    if (level >= highestLvl && correct >= 3) {
      colorMorph();
      fill(guiColor);
      textAlign(CENTER);
      // Total score result
      textSize(75);
      text("Total Score: " + int(totalScore),
        width/2, height*0.2);
      // message for user
      textSize(40);
      text(message(), width/2, height*0.4);
      // Play again question
      textSize(60);
      text("Play again?",
        width/2, height*0.65);
      playYes.display("Yes", guiColor);
      playNo.display("No", guiColor);
    }
  }
  
  void pauseScreen() {
    if (paused && !helping) {
      colorMorph();
      resume.display("Resume", guiColor);
      reset.display("Reset", guiColor);
      exit.display("Exit", guiColor);
      help.display("Help", guiColor);
    }
  }
  
  void help() {
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
      text("R:"+int(redV), width*0.2, height/2);
      redDown.down();
      // Green
      greenUp.up();
      text("G:"+int(greenV), width/2, height/2);
      greenDown.down();
      // Blue
      blueUp.up();
      text("B:"+int(blueV), width*0.8, height/2);
      blueDown.down();
    }
  }
  
  void total() {
    if (!paused) {
      colorMorph();
      fill(guiColor);
      textSize(55);
      textAlign(CENTER);
      text(int(shownTotal), 50, 70);
      if (shownTotal < levelScore) {
        shownTotal += 1;
      }
    }
  }
  
  void clock() {
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
  
  String failReason() {
    if (current < 1) {
      return "You ran out of time.";
    } else if (correct == 0) {
      return "You didn't get any right.";
    } else {
      return "You only got " + correct + " right.";
    }
  }

  String message() {
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
  
  void checkIntro() {
    if (intro) {
      if (introStart.overButton()) {
        intro = false;
      } else if (introExit.overButton()) {
        exit();
      }
    }
  }
  
  void checkNext() {
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
  
  void checkYesNo() {
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
  
  void checkPause() {
    if (overPause()) {
      paused = true;
    }
  }
  
  void checkMenu() {
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
  
  void checkHelp() {
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
  
  void pauseButton() {
    if (!paused) {
      noStroke();
      fill(guiColor);
      rectMode(CENTER);
      rect(pX, pY, pW, pH);
      rect(pX+25, pY, pW, pH);
    }
  }
  
  boolean overPause() {
    float disX = pX+pW - mouseX;
    float disY = pY - mouseY;
    if (sqrt(sq(disX) + sq(disY)) < pW*4/2) {
      return true;
    } else return false;
  }
  
  void colorMorph() {
    float colorCR;
    float min = 100;
    float max = 230;
    // Red
    colorCR = random(1.5);
    if (red < min) {
      fatR = false;
    } else if (red > max) {
        fatR = true;
    } if (fatR) {
        red -= colorCR;
    } else red += colorCR;
    // Green
    colorCR = random(1.5);
    if (green < min) {
      fatG = false;
    } else if (green > max) {
        fatG = true;
    } if (fatG) {
        green -= colorCR;
    } else green += colorCR;
    // Blue
    colorCR = random(1.5);
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
