// take color off as arg for display

class Button {
  color guiColor;
  float x, y, w, h;
  
  Button (float xLoc, float yLoc) {
    x = xLoc;
    y = yLoc;
    w = 200;
    h = 100;
  }
  
  void display(String label, color gColor) {
    guiColor = gColor;
    noFill();
    if (overButton()) {
      stroke(255);
      strokeWeight(6);
      textSize(45);
    } else {
      stroke(guiColor);
      strokeWeight(4.5);
      textSize(40);
    } rectMode(CENTER);
    rect(x, y, w, h, 15);
    if (overButton()) {
      fill(255);
    } else fill(guiColor);
    text(label, x, y+15);
  }
  
  void goBack() {
    noStroke();
    triangle(x, y, x+50, y-25, x+50, y+25);
  }
  
  void up() {
    noStroke();
    triangle(/*left*/x, y,
      /*right*/x+60, y,
      /*top*/x+30, y-65);
  }
  
  void down() {
    noStroke();
    triangle(x, y-20, x+60, y-20, x+30, y+50);
  }

  boolean overButton() {
    float disX = x - mouseX;
    float disY = y - mouseY;
    if(sqrt(sq(disX) + sq(disY)) < w/2) {
      return true;
    } else return false;
  }
}
