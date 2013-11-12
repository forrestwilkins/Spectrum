class Engine {
  Gui gui = new Gui();
  
  float backRed, backGreen, backBlue;
  color backColor;
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
  
  void display() {
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
  
  void gameFace() {
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
  
  void checkButtons() {
    if (levelFinished()) {
      gui.checkNext();
      gui.checkYesNo();
    } gui.checkIntro();
    gui.checkPause();
    gui.checkMenu();
    gui.checkHelp();
  }
    
  boolean levelFinished() {
    boolean allUsed = true;
    for (int i=0; i < ps.length; i++) {
      if (!ps[i].used) {
        allUsed = false;
      }
    } if (allUsed || current < 1) return true;
    else return false;
  }
  
  int which() {
    if (mouseX < cs[0].x + (width*0.1)) {
      return 0;
    } else if (mouseX < cs[1].x + (width*0.1)) {
      return 1;
    } else if (mouseX < cs[2].x + (width*0.1)) {
      return 2;
    } else {
      return 3;
    }
  }
  
  void bootStrap() {
    float x = width*0.2;
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
      cs[i].y = height*0.9;
      x += width*0.2;
    } // boot up prisms
    for (int i=0; i < ps.length; i++) {
      ps[i] = new Prism();
      ps[i].bestMatch(cs);
    } grid();
  }
  
  void grid() {
    int i = 0;
    for (float y = height*0.2; y < height*0.7; y += height*0.2) {
      for (float x = width*0.2; x < width*0.8; x += height*0.2) {
        if (i < ps.length) {
          ps[i].x = x;
          ps[i].y = y;
        } i++;
      }
    }
  }
  
  void backMorph() {
    float colorCR = 0.5;
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
