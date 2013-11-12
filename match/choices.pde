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
  
  void render() {
    if (!paused) {
      stroke(sColor);
      strokeWeight(3);
      fill(sColor, alpha);
      cube();
    }
  }
  
  void chosen(Prism[] p) {
    if (mouseY > height-(height*0.3)) {
      for (int i=0; i < p.length; i++) {
        if (ps[i].touched) {
          ps[i].fadeTo(x, y);
          ps[i].fading = true;
        }
      } shapeCompare(p);
    }
  }
  
  void shapeCompare(Prism[] p) {
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
  
  void cube() {
    if (mode == 0) {
      pushMatrix();
      // move/change here
      translate(x, y, z);
      box(w);
      popMatrix();
    }
  }
  
  void ball() {
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
