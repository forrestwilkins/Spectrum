Prism[] ps = new Prism[5];
Choices[] cs = new Choices[4];
Engine ei;

int level = 1, highestLvl = 5, totalCorrect = 0, correct, current;
float score, levelScore, shownTotal, totalScore = 0;
boolean intro = true, paused, helping = false;

void setup() {
  size(displayWidth, displayHeight, P3D);
  orientation(PORTRAIT);
  ei = new Engine();
  ei.bootStrap();
}

void draw() {
  lights();
  background(0);
  ei.display();
}

void mousePressed() {
  for (int i=0; i < ps.length; i++) {
    ps[i].overCube(ps);
  }
}

void mouseReleased() {
  ei.checkButtons();
  cs[ei.which()].chosen(ps);
  for (int i=0; i < ps.length; i++) {
    ps[i].touched = false;
  } mouseX = 0;
  mouseY = 0;
}
