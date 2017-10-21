import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


/**
 * @author Administrator
 *
 */
public class DrawPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private GraphDraft gg;
  public static final int RATIO = 100; // 200 130
  public static final int SIZE = 20; // 40 26
  public static final double ARROW_LEN = 15; // 30 19.5
  public static final double ARROW_H = 10; // 20 13
  public static final double ARROW_L = 4; // 8 5.2
  private Dimension theSize = new Dimension(2000, 2000);

  /**
   * not null. n@author Administrator
   */
  public void display(GraphDraft g) {
    this.gg = g;
    this.repaint();
  }

  /**
   * not null. n@author Administrator
   */
  public void savePicture(int WIDTH, int HEIGHT) {
	  BufferedImage targetImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	  System.out.println(""+this.getHeight()+" "+this.getWidth());
	  Graphics graphics = targetImg.getGraphics();
		    paint(graphics);
		    File f = new File("E:\\test.jpg");
		    OutputStream out = null;
		    try {
		      out = new FileOutputStream(f);
		    } catch (FileNotFoundException e1) {
		      // TODO Auto-generated catch block
		      e1.printStackTrace();
		    }
		    try {
		      ImageIO.write(targetImg, "JPEG", out);
		    } catch (IOException e2) {
		      // TODO Auto-generated catch block
		      e2.printStackTrace();
		    }
  }

  /**
   * not null. n@author Administrator
   */
  public void paint(Graphics g) {
    super.paint(g);
    if (this.gg != null) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setFont(new Font("宋体", Font.BOLD, 10));
      // draw point
      for (int i = 0; i < gg.getPoints().length; i++) {
        if (gg.getPoints()[i].getSt() != null) {
          if (gg.getPointstate()[i] == 0) {
            g2d.drawOval(gg.getPoints()[i].getPx() * RATIO - SIZE,
                gg.getPoints()[i].getPy() * RATIO - SIZE, SIZE * 2, SIZE * 2);
            int indexstring = gg.getPoints()[i].getPx() * RATIO
                - gg.getPoints()[i].getSt().length() * 6 / 2;
            g2d.drawString(gg.getPoints()[i].getSt(), indexstring,
                gg.getPoints()[i].getPy() * RATIO);
          } else if (gg.getPointstate()[i] == 1) {
            g2d.setColor(Color.red);
            g2d.drawOval(gg.getPoints()[i].getPx() * RATIO - SIZE,
                gg.getPoints()[i].getPy() * RATIO - SIZE, SIZE * 2, SIZE * 2);
            int indexstring = gg.getPoints()[i].getPx() * RATIO
                - gg.getPoints()[i].getSt().length() * 6 / 2;
            g2d.drawString(gg.getPoints()[i].getSt(), indexstring,
                gg.getPoints()[i].getPy() * RATIO);
            g2d.setColor(Color.black);
          } else {
            g2d.setColor(Color.green);
            g2d.drawOval(gg.getPoints()[i].getPx() * RATIO - SIZE,
                gg.getPoints()[i].getPy() * RATIO - SIZE, SIZE * 2, SIZE * 2);
            int indexstring = gg.getPoints()[i].getPx() * RATIO
                - gg.getPoints()[i].getSt().length() * 6 / 2;
            g2d.drawString(gg.getPoints()[i].getSt(), indexstring,
                gg.getPoints()[i].getPy() * RATIO);
            g2d.setColor(Color.black);
          }
        }
      }

      // draw line
      for (int i = 0; i < gg.getMyEdges().length; i++) {
        if (gg.getMyEdges()[i] == null) {
          break;
        }
        int p1 = gg.getMyEdges()[i].getEx();
        int p2 = gg.getMyEdges()[i].getEy();
        if (gg.getMyEdges()[i].getType() == 1) {

          if (gg.getMyEdgestate()[p1][p2] == 0) {
            int[] loc = newLoc(p1, p2, gg);
            drawAl(loc[0], loc[1], loc[2], loc[3], g2d);
            g2d.drawString(String.valueOf(gg.getMyEdges()[i].getWeight()),
                 (int)((loc[0] + loc[2]) / 2), (int)((loc[1] + loc[3]) / 2));
          } else {
            int[] loc = newLoc(p1, p2, gg);
            g2d.setColor(Color.red);
            drawAl(loc[0], loc[1], loc[2], loc[3], g2d);
            g2d.drawString(String.valueOf(gg.getMyEdges()[i].getWeight()),
                (int) ((loc[0] + loc[2]) / 2), (int) ((loc[1] + loc[3]) / 2));
            g2d.setColor(Color.black);
          }
        } else if (gg.getMyEdges()[i].getType() == 2) {
          if (gg.getMyEdgestate()[p1][p2] == 0 && gg.getMyEdgestate()[p2][p1] == 0) {
            int[] loc = newLoc(p1, p2, gg);
            drawAl(loc[0] + 5, loc[1], loc[2] + 5, loc[3], g2d);
            g2d.drawString(String.valueOf(gg.getMyEdges()[i].getWeight()),
                (int) ((loc[0] + 5 + loc[2] + 5) / 2),
                (int) ((loc[1] + 5 + loc[3] + 5) / 2));
            drawAl(loc[2] - 5, loc[3], loc[0] - 5, loc[1], g2d);
            g2d.drawString(String.valueOf(gg.getMyEdges()[i].getWeight2()),
                (int) ((loc[0] - 5 + loc[2] - 5) / 2),
                (int) ((loc[1] - 5 + loc[3] - 5) / 2));
          } else if (gg.getMyEdgestate()[p1][p2] == 1) {
            int[] loc = newLoc(p1, p2, gg);
            g2d.setColor(Color.red);
            drawAl(loc[0] + 5, loc[1], loc[2] + 5, loc[3], g2d);
            g2d.drawString(String.valueOf(gg.getMyEdges()[i].getWeight()),
                (int) ((loc[0] + 5 + loc[2] + 5) / 2),
                (int) ((loc[1] + 5 + loc[3] + 5) / 2));
            g2d.setColor(Color.black);
            drawAl(loc[2] - 5, loc[3], loc[0] - 5, loc[1], g2d);
            g2d.drawString(String.valueOf(gg.getMyEdges()[i].getWeight2()),
                (int) ((loc[0] - 5 + loc[2] - 5) / 2),
                (int) ((loc[1] - 5 + loc[3] - 5) / 2));
          } else if (gg.getMyEdgestate()[p2][p1] == 1) {
            int[] loc = newLoc(p1, p2, gg);
            drawAl(loc[0] + 5, loc[1], loc[2] + 5, loc[3], g2d);
            g2d.drawString(String.valueOf(gg.getMyEdges()[i].getWeight()),
                (int) ((loc[0] + 5 + loc[2] + 5) / 2),
                (int) ((loc[1] + 5 + loc[3] + 5) / 2));
            g2d.setColor(Color.red);
            drawAl(loc[2] - 5, loc[3], loc[0] - 5, loc[1], g2d);
            g2d.drawString(String.valueOf(gg.getMyEdges()[i].getWeight2()),
                (int) ((loc[0] - 5 + loc[2] - 5) / 2),
                (int) ((loc[1] - 5 + loc[3] - 5) / 2));
            g2d.setColor(Color.black);
          }
        } else if (gg.getMyEdges()[i].getType() == 3) {
          if (gg.getMyEdgestate()[p1][p2] == 0) {
            int[] loc = newLoc(p1, p2, gg);
            g2d.drawLine(loc[0], loc[1], loc[2], loc[3]);
          } else {
            int[] loc = newLoc(p1, p2, gg);
            g2d.setColor(Color.red);
            g2d.drawLine(loc[0], loc[1], loc[2], loc[3]);
            g2d.setColor(Color.black);
          }
        } else if (gg.getMyEdges()[i].getType() == 4) {
          if (gg.getMyEdgestate()[p1][p2] == 0 && gg.getMyEdgestate()[p2][p1] == 0) {
            int[] loc = newLoc(p1, p2, gg);
            g2d.drawLine(loc[0] + 5, loc[1], loc[2] + 5, loc[3]);
            g2d.drawLine(loc[2] - 5, loc[3] - 5, loc[0] - 5, loc[1] - 5);
          } else if (gg.getMyEdgestate()[p1][p2] == 1) {
            int[] loc = newLoc(p1, p2, gg);
            g2d.setColor(Color.red);
            g2d.drawLine(loc[0] + 5, loc[1], loc[2] + 5, loc[3]);
            g2d.setColor(Color.black);
            g2d.drawLine(loc[2] - 5, loc[3], loc[0] - 5, loc[1]);
          } else if (gg.getMyEdgestate()[p2][p1] == 1) {
            int[] loc = newLoc(p1, p2, gg);
            g2d.drawLine(loc[0] + 5, loc[1], loc[2] + 5, loc[3]);
            g2d.setColor(Color.red);
            g2d.drawLine(loc[2] - 5, loc[3], loc[0] - 5, loc[1]);
            g2d.setColor(Color.red);
          }
        } else {
          if (gg.getMyEdgestate()[p1][p2] == 0 && gg.getMyEdgestate()[p2][p1] == 0) {
            int[] loc = newLoc(p1, p2, gg);
            drawAl(loc[0] + 5, loc[1], loc[2] + 5, loc[3], g2d);
            g2d.drawString(String.valueOf(gg.getMyEdges()[i].getWeight()),
                (int) ((loc[0] + 5 + loc[2] + 5) / 2),
                (int) ((loc[1] + 5 + loc[3] + 5) / 2));
            g2d.drawLine(loc[2] - 5, loc[3] - 5, loc[0] - 5, loc[1] - 5);
          } else if (gg.getMyEdgestate()[p1][p2] == 1) {
            int[] loc = newLoc(p1, p2, gg);
            g2d.setColor(Color.red);
            drawAl(loc[0] + 5, loc[1], loc[2] + 5, loc[3], g2d);
            g2d.drawString(String.valueOf(gg.getMyEdges()[i].getWeight()),
                (int) ((loc[0] + 5 + loc[2] + 5) / 2),
                (int) ((loc[1] + 5 + loc[3] + 5) / 2));
            g2d.setColor(Color.black);
            g2d.drawLine(loc[2] - 5, loc[3], loc[0] - 5, loc[1]);
          } else if (gg.getMyEdgestate()[p2][p1] == 1) {
            int[] loc = newLoc(p1, p2, gg);
            drawAl(loc[0] + 5, loc[1], loc[2] + 5, loc[3], g2d);
            g2d.drawString(String.valueOf(gg.getMyEdges()[i].getWeight()),
                (int) ((loc[0] + 5 + loc[2] + 5) / 2),
                (int) ((loc[1] + 5 + loc[3] + 5) / 2));
            g2d.setColor(Color.red);
            g2d.drawLine(loc[2] - 5, loc[3], loc[0] - 5, loc[1]);
            g2d.setColor(Color.black);
          }
        }
      }

    }
  }

  public Dimension getPreferredSize() {
    return this.theSize;
  }

  /**
   * not null. n@param sx n@param sy n@param ex n@param ey n@param g2
   */
  public static void drawAl(int sx, int sy, int ex, int ey, Graphics2D g2) {

    double ah = ARROW_H; // 箭头高度
    double al = ARROW_L; // 底边的一半

    double awrad = Math.atan(al / ah); // 箭头角度
    double arraowlen = Math.sqrt(al * al + ah * ah); // 箭头的长度
    double[] arrXY1 = rotateVec(ex - sx, ey - sy, awrad, true, arraowlen);
    double[] arrXY2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraowlen);
    double xx3 = ex - arrXY1[0]; // (x3,y3)是第一端点
    double yy3 = ey - arrXY1[1];

    int x3 = 0;
    int y3 = 0;

    Double ax3 = new Double(xx3);
    x3 = ax3.intValue();
    Double ay3 = new Double(yy3);
    y3 = ay3.intValue();
    double xx4 = ex - arrXY2[0]; // (x4,y4)是第二端点
    Double ax4 = new Double(xx4);
    int x4 = 0;
    x4 = ax4.intValue();
    double yy4 = ey - arrXY2[1];
    Double ay4 = new Double(yy4);
    int y4 = 0;
    y4 = ay4.intValue();
    // 画线
    g2.drawLine(sx, sy, ex, ey);
    //
    GeneralPath triangle = new GeneralPath();
    triangle.moveTo(ex, ey);
    triangle.lineTo(x3, y3);
    triangle.lineTo(x4, y4);
    triangle.closePath();
    // 实心箭头
    g2.fill(triangle);
    // 非实心箭头
    // g2.draw(triangle);

  }

  // 计算
  /**
   * not null. n@param px n@param py n@param ang n@param isChLen n@param newLen
   * n@return
   */
  public static double[] rotateVec(int px, int py, double ang, boolean isChLen,
      double newLen) {
    double[] mathstr = new double[2];
    // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
    double vx = px * Math.cos(ang) - py * Math.sin(ang);
    double vy = px * Math.sin(ang) + py * Math.cos(ang);
    if (isChLen) {
      double d = Math.sqrt(vx * vx + vy * vy);
      vx = vx / d * newLen;
      vy = vy / d * newLen;
      mathstr[0] = vx;
      mathstr[1] = vy;
    }
    return mathstr;
  }

  // 得到边的两端的坐标

  /**
   * not null. n@param p1 n@param p2 n@param g n@return
   */
  public static int[] newLoc(int p1, int p2, GraphDraft g) {
    int[] loc = new int[4];
    int x1 = g.getPoints()[p1].getPx() * RATIO;
    int y1 = g.getPoints()[p1].getPy() * RATIO;
    int x2 = g.getPoints()[p2].getPx() * RATIO;
    int y2 = g.getPoints()[p2].getPy() * RATIO;
    // p1为空
    if (g.getPoints()[p1].getSt() == null && g.getPoints()[p2].getSt() != null) {
      loc[0] = x1;
      loc[1] = y1;
      if (x1 != x2 && y1 != y2) {
        double theta = Math.atan(Math.abs(y1 - y2) / Math.abs(x1 - x2)); // 箭头角度
        if (x1 < x2 && y1 < y2) {
          loc[2] = (int) (x2 - Math.cos(theta) * SIZE);
          loc[3] = (int) (y2 - Math.sin(theta) * SIZE);
        } else if (x1 < x2 && y1 > y2) {
          loc[2] = (int) (x2 - Math.cos(theta) * SIZE);
          loc[3] = (int) (y2 + Math.sin(theta) * SIZE);
        } else if (x1 > x2 && y1 < y2) {
          loc[2] = (int) (x2 + Math.cos(theta) * SIZE);
          loc[3] = (int) (y2 - Math.sin(theta) * SIZE);
        } else {
          loc[2] = (int) (x2 + Math.cos(theta) * SIZE);
          loc[3] = (int) (y2 + Math.sin(theta) * SIZE);
        }
      } else if (x1 == x2) {
        if (y1 < y2) {
          loc[2] = x2;
          loc[3] = y2 - SIZE;
        } else {
          loc[2] = x2;
          loc[3] = y2 + SIZE;
        }
      } else {
        if (x1 < x2) {
          loc[2] = x2 - SIZE;
          loc[3] = y2;
        } else {
          loc[2] = x2 + SIZE;
          loc[3] = y2;
        }
      }
    } else if (g.getPoints()[p1].getSt() != null && g.getPoints()[p2].getSt() == null) {
      loc[2] = x2;
      loc[3] = y2;
      if (x1 != x2 && y1 != y2) {
        double theta = Math.atan(Math.abs(y1 - y2) / Math.abs(x1 - x2)); // 箭头角度

        if (x1 < x2 && y1 < y2) {
          loc[0] = (int) (x1 + Math.cos(theta) * SIZE);
          loc[1] = (int) (y1 + Math.sin(theta) * SIZE);
        } else if (x1 < x2 && y1 > y2) {
          loc[0] = (int) (x1 + Math.cos(theta) * SIZE);
          loc[1] = (int) (y1 - Math.sin(theta) * SIZE);
        } else if (x1 > x2 && y1 < y2) {
          loc[0] = (int) (x1 - Math.cos(theta) * SIZE);
          loc[1] = (int) (y1 + Math.sin(theta) * SIZE);
        } else {
          loc[0] = (int) (x1 - Math.cos(theta) * SIZE);
          loc[1] = (int) (y1 - Math.sin(theta) * SIZE);
        }
      } else if (x1 == x2) {
        if (y1 < y2) {
          loc[0] = x1;
          loc[1] = y1 + SIZE;
        } else {
          loc[0] = x1;
          loc[1] = y1 - SIZE;
        }
      } else {
        if (x1 < x2) {
          loc[0] = x1 + SIZE;
          loc[1] = y1;
        } else {
          loc[0] = x1 - SIZE;
          loc[1] = y1;
        }
      }
    } else if (g.getPoints()[p1].getSt() == null && g.getPoints()[p2].getSt() == null) {
      loc[0] = x1;
      loc[1] = y1;
      loc[2] = x2;
      loc[3] = y2;
    } else {
      if (x1 != x2 && y1 != y2) {
        double theta = Math.atan(Math.abs(y1 - y2) / Math.abs(x1 - x2)); // 箭头角度

        if (x1 < x2 && y1 < y2) {
          loc[0] = (int) (x1 + Math.cos(theta) * SIZE);
          loc[1] = (int) (y1 + Math.sin(theta) * SIZE);
          loc[2] = (int) (x2 - Math.cos(theta) * SIZE);
          loc[3] = (int) (y2 - Math.sin(theta) * SIZE);
        } else if (x1 < x2 && y1 > y2) {
          loc[0] = (int) (x1 + Math.cos(theta) * SIZE);
          loc[1] = (int) (y1 - Math.sin(theta) * SIZE);
          loc[2] = (int) (x2 - Math.cos(theta) * SIZE);
          loc[3] = (int) (y2 + Math.sin(theta) * SIZE);
        } else if (x1 > x2 && y1 < y2) {
          loc[0] = (int) (x1 - Math.cos(theta) * SIZE);
          loc[1] = (int) (y1 + Math.sin(theta) * SIZE);
          loc[2] = (int) (x2 + Math.cos(theta) * SIZE);
          loc[3] = (int) (y2 - Math.sin(theta) * SIZE);
        } else {
          loc[0] = (int) (x1 - Math.cos(theta) * SIZE);
          loc[1] = (int) (y1 - Math.sin(theta) * SIZE);
          loc[2] = (int) (x2 + Math.cos(theta) * SIZE);
          loc[3] = (int) (y2 + Math.sin(theta) * SIZE);
        }
      } else if (x1 == x2) {
        if (y1 < y2) {
          loc[0] = x1;
          loc[1] = y1 + SIZE;
          loc[2] = x2;
          loc[3] = y2 - SIZE;
        } else {
          loc[0] = x1;
          loc[1] = y1 - SIZE;
          loc[2] = x2;
          loc[3] = y2 + SIZE;
        }
      } else {
        if (x1 < x2) {
          loc[0] = x1 + SIZE;
          loc[1] = y1;
          loc[2] = x2 - SIZE;
          loc[3] = y2;
        } else {
          loc[0] = x1 - SIZE;
          loc[1] = y1;
          loc[2] = x2 + SIZE;
          loc[3] = y2;
        }
      }
    }

    return loc;
  }
}
