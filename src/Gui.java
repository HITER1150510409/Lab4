
import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
/*
 * To do list:
 * 淇coordination
 */



// hard to understand some of the functions
public class Gui {
  JFrame frame;
  Picture G;//
  GraphDraft g;//d
  DrawPanel panel;
  String path;
  JScrollPane scrollPane;
  JScrollPane scrollPane2;
  boolean flag;
  int nowstep;
  int[] walks;
  private JTextField textField1;
  private JTextField textField2;
  private JTextField textField3;
  private JTextField textField4;
  private JTextArea textField5;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Gui window = new Gui();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public Gui() {
    initialize();
  }

  public void showDirectedGraph(Picture g) {
    return;
  }

  public String queryBridgeWords(String word1, String word2) {
    return null;
  }

  public String gernerateNewText(String inputText) {
    return null;
  }

  public String calcShortestPath(String word1, String word2) {
    return null;
  }

  public String randomWalk() {
    return null;
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 1339, 871);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);

    JButton btnNewButton = new JButton("閫夋嫨鏂囦欢");
    btnNewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFileChooser jfc = new JFileChooser();
        if (jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
        	for(int i = 1;i<=1000;i++){
        	G = new Picture(jfc.getSelectedFile().getAbsolutePath());
        	}
          path = jfc.getSelectedFile().getAbsolutePath();

          // 鑾峰彇鍥剧殑淇℃伅锛岄噷闈㈠寘鍚墍鏈夐渶瑕佺敾鐨勭偣鐨勪俊鎭拰杈圭殑淇℃伅
          g = G.getGraphDraft();
          panel.display(g);
          walks = G.randomWalk();
          nowstep = 1;
          flag = false;
          // 杩欓噷鏄浣曡鍙栦竴涓偣鐨勪俊鎭紝x鍜寉鏄潗鏍囷紝杩欎釜鍧愭爣鍙槸鐩稿浣嶇疆锛屽疄闄呰鏀惧湪鍝鍏蜂綋璁＄畻銆�
          // 鏅�氱殑鐐箂t瀛樺偍瀛楃涓插唴瀹广�傛湁浜涚偣st=null锛岃繖鏍风殑鐐逛笉闇�瑕佹樉绀哄嚭鏉ワ紝浣嗕粬浠殑鍧愭爣鍦ㄧ敾杈规椂鏈夌敤
        }
      }
    });
    btnNewButton.setBounds(1194, 7, 127, 39);
    frame.getContentPane().add(btnNewButton);

    JLabel lblNewLabel = new JLabel("绗竴涓崟璇嶏細");
    lblNewLabel.setBounds(905, 57, 84, 27);
    frame.getContentPane().add(lblNewLabel);

    textField1 = new JTextField();
    textField1.setBounds(1016, 57, 105, 26);
    frame.getContentPane().add(textField1);
    textField1.setColumns(10);

    JLabel lblNewLabel1 = new JLabel("绗簩涓崟璇�");
    lblNewLabel1.setBounds(1141, 62, 65, 16);
    frame.getContentPane().add(lblNewLabel1);

    textField2 = new JTextField();
    textField2.setBounds(1233, 57, 88, 26);
    frame.getContentPane().add(textField2);
    textField2.setColumns(10);

    JButton btnNewButton1 = new JButton("璁＄畻鏈�鐭矾");
    btnNewButton1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String word1 = textField1.getText().replace(" ", "");
        String word2 = textField2.getText().replace(" ", "");
        if (word2 != null && !word2.equals("")) {
          int[] result = G.shortestPath(word1, word2);

          for (int i = 0; i < g.getPoints().length; i++) {
            g.getPointstate()[i] = 0;
            for (int j = 0; j < g.getPoints().length; j++) {
              g.getMyEdgestate()[i][j] = 0;
            }
          }

          if (result[0] == 0) {
            textField5
                .setText(word1 + " and " + word2 + " are not reachable!");
          } else if (result[0] == -1) {
            textField5.setText(word1 + " doesn't exist!");
          } else if (result[0] == -2) {
            textField5.setText(word2 + " doesn't exist!");
          } else if (result[0] == -3) {
            textField5.setText(word1 + " and " + word2 + " doesn't exist!");
          } else {

            String shortpath = g.getPoints()[result[1]].getSt(); // word1
            g.getPointstate()[result[1]] = 1;
            for (int i = 2; i <= result[0]; i++) {
              g.getMyEdgestate()[result[i - 1]][result[i]] = 1;
              if (g.getPoints()[result[i]].getSt() != null) {
                g.getPointstate()[result[i]] = 1;
                shortpath += " => " + g.getPoints()[result[i]].getSt();
              }
            }
            shortpath += " 璺緞闀垮害涓猴細 " + result[result[0] + 1];
            textField5.setText(shortpath);
            panel.display(g);
          }
        } else {
          boolean have = false;
          for (int i = 0; i < g.getPoints().length; i++) {
            if (g.getPoints()[i].getSt() != null && g.getPoints()[i].getSt().equals(word1)) {
              have = true;
            }
          }
          if (have) {
            int[] result = G.shortestPath(word1);
            String s = "";
            for (int i = 0; i < result.length; i++) {
              if (g.getPoints()[i].getSt() != null) {
                s += g.getPoints()[i].getSt() + ":" + result[i] + "\n";
              }
            }
            textField5.setText(s);
          } else {
            textField5.setText(word1 + " doesn't exist!");
          }
        }
      }
    });
    btnNewButton1.setBounds(1188, 107, 133, 29);
    frame.getContentPane().add(btnNewButton1);

    JButton btnNewButton2 = new JButton("鏌ヨ妗ユ帴璇�");
    btnNewButton2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String word1 = textField1.getText().replace(" ", "");
        String word2 = textField2.getText().replace(" ", "");
        for (int i = 0; i < g.getPoints().length; i++) {
          if (g.getPoints()[i].getSt() != null && (g.getPoints()[i].getSt().equals(word1)
              || g.getPoints()[i].getSt().equals(word2))) {
            g.getPointstate()[i] = 2;
          } else {
            g.getPointstate()[i] = 0;
          }
          for (int j = 0; j < g.getPoints().length; j++) {
            g.getMyEdgestate()[i][j] = 0;
          }
        }
        int[] result = G.findBridge(word1, word2);
        if (result[0] == -1) {
          textField5.setText(word1 + " doesn't exist!");
        } else if (result[0] == -2) {
          textField5.setText(word2 + " doesn't exist!");
        } else if (result[0] == -3) {
          textField5.setText(word1 + " and " + word2 + " doesn't exist!");
        } else if (result[0] == -4) {
          textField5.setText("There is no bridge between them!");
        } else if (result[0] > 0) {
          String text = "";
          for (int i = 0; i < result[0]; i++) {
            text += g.getPoints()[result[i + 1]].getSt();
            text += "  ";
            g.getPointstate()[result[i + 1]] = 1;
          }
          textField5.setText(text);
          panel.display(g);
        } 
      }
    });
    btnNewButton2.setBounds(966, 107, 133, 29);
    frame.getContentPane().add(btnNewButton2);

    textField3 = new JTextField();
    textField3.setBounds(905, 431, 297, 74);
    frame.getContentPane().add(textField3);
    textField3.setColumns(10);

    JButton btnNewButton3 = new JButton("鎻愪氦");
    btnNewButton3.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String line = textField3.getText();
        String result = G.newText(line);
        textField4.setText(result);
      }
    });
    btnNewButton3.setBounds(1216, 454, 105, 51);
    frame.getContentPane().add(btnNewButton3);

    textField4 = new JTextField();
    textField4.setBounds(895, 568, 423, 133);
    frame.getContentPane().add(textField4);
    textField4.setColumns(10);

    JLabel lblNewLabel2 = new JLabel("鏌ヨ缁撴灉");
    lblNewLabel2.setBounds(905, 279, 65, 33);
    frame.getContentPane().add(lblNewLabel2);

    JLabel lblNewLabel3 = new JLabel("鏂扮敓鎴愭枃鏈�");
    lblNewLabel3.setBounds(895, 529, 88, 27);
    frame.getContentPane().add(lblNewLabel3);

    JButton btnNewButton4 = new JButton("闅忔満娓歌蛋");
    btnNewButton4.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String s;
        if (flag) {
          s = textField4.getText();
        } else {
          s = "";
        }
        while (g.getPoints()[walks[nowstep]].getSt() == null) {
          nowstep += 1;
          if (nowstep > walks[0]) {
            walks = G.randomWalk();
            nowstep = 1;
            s = "";
            System.out.println("hahha");
          }
        }
        s += " " + g.getPoints()[walks[nowstep]].getSt();
        nowstep += 1;
        flag = true;
        if (nowstep > walks[0]) {
          walks = G.randomWalk();
          nowstep = 1;
          flag = false;
        }
        textField4.setText(s);
      }
    });
    btnNewButton4.setBounds(1160, 727, 161, 41);
    frame.getContentPane().add(btnNewButton4);
    
    scrollPane = new JScrollPane();
    scrollPane.setBounds(6, 6, 877, 795);
    frame.getContentPane().add(scrollPane);

    scrollPane2 = new JScrollPane();
    scrollPane2.setBounds(993, 186, 328, 204);
    frame.getContentPane().add(scrollPane2);

    JButton btnNewButton5 = new JButton("淇濆瓨鍥剧墖");
    btnNewButton5.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        try {
          int index1 = path.lastIndexOf("/");
          int index2 = path.lastIndexOf(".");
          path = path.substring(0, index1 + 1)
              + path.substring(index1 + 1, index2 + 1) + "jpg";
          BufferedImage myImage = new Robot().createScreenCapture(
              new Rectangle(frame.getX() + 5, frame.getY() + 20,
                  scrollPane.getWidth(), scrollPane.getHeight()));
          ImageIO.write(myImage, "jpg", new File(path));
          System.out.println(path);

        } catch (AWTException e1) {
          e1.printStackTrace();
        } catch (IOException e2) {
          e2.printStackTrace();
        }
      }
    });
    btnNewButton5.setBounds(966, 6, 117, 40);
    frame.getContentPane().add(btnNewButton5);

    panel = new DrawPanel();
    panel.setBounds(25, 25, 3000, 3000);
    frame.getContentPane().add(panel);

    scrollPane.setViewportView(panel);

    textField5 = new JTextArea();
    textField5.setBounds(993, 186, 328, 204);
    frame.getContentPane().add(textField5);
    textField5.setColumns(10);

    scrollPane2.setViewportView(textField5);
  }
}
