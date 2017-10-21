
public class MyEdge {
  private int ex;
  private int ey;
  private int weight;
  private int weight2;
  private int type;



  public int getEx() {
	return ex;
  }

public void setEx(int ex) {
	this.ex = ex;
}

public int getEy() {
	return ey;
}

public void setEy(int ey) {
	this.ey = ey;
}

public int getWeight() {
	return weight;
}

public void setWeight(int weight) {
	this.weight = weight;
}

public int getWeight2() {
	return weight2;
}

public void setWeight2(int weight2) {
	this.weight2 = weight2;
}

public int getType() {
	return type;
}

public void setType(int type) {
	this.type = type;
}

/**edge.
   * @author Administrator
   *
   */
  public MyEdge(int x, int y, int weight, int weight2, int type) {
    this.type = type;
    this.weight = weight;
    this.weight2 = weight2;
    this.ex = x;
    this.ey = y;
  }

  /**edge.
   * @author Administrator
   *
   */
  public MyEdge(int x, int y, int weight, int type) {
    this.type = type;
    this.weight = weight;
    this.weight2 = 0;
    this.ex = x;
    this.ey = y;
  }
}