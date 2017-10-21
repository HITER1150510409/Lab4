
public class GraphDraft {
  private Point[] points;
  private int[] pointstate;
  private MyEdge[] edges;
  private int[][] edgestate;

  public Point[] getPoints() {
	return points;
}

  public void setPoints(Point[] points) {
    this.points = points;
}

public int[] getPointstate() {
	return pointstate;
}

public void setPointstate(int[] pointstate) {
	this.pointstate = pointstate;
}

public MyEdge[] getMyEdges() {
	return edges;
}

public void setMyEdges(MyEdge[] edges) {
	this.edges = edges;
}

public int[][] getMyEdgestate() {
	return edgestate;
}

public void setMyEdgestate(int[][] edgestate) {
	this.edgestate = edgestate;
}

GraphDraft(Point[] points, MyEdge[] edges) {
    this.points = points;
    this.edges = edges;
    pointstate = new int[points.length];
    edgestate = new int[points.length][points.length];
  }
}