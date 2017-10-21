
public class Point {
  
  private int px, py;
  private String st;

  
public int getPx() {
	return px;
}

public void setPx(int px) {
	this.px = px;
}

public int getPy() {
	return py;
}

public void setPy(int py) {
	this.py = py;
}

public String getSt() {
	return st;
}

public void setSt(String st) {
	this.st = st;
}

/** point.
   * n@param x
   * n@param y
   * n@param st
   */
  public Point(int x, int y, String st) {
    this.px = x;
    this.py = y;
    this.st = st;
  }

  public boolean isDummy() {
    return st == null;
  }
}