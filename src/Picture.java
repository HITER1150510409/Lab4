
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Random;

public class Picture {
  private int[] indegree;
  private int[] selfedge;
  private int[] outdegree;
  private int[] outdegreesave;
  private int[][] edge;
  private int[][] internalmap;
  private boolean[][] processed;
  private Table table = new Table();
  private GraphDraft graphDraft;
  int an;
  int dummy;
  int wordNum;
  // an for head[], K for point[][]. After initializing, an denotes number of
  // vertexes, K denotes number of edges.

  /**
   * not null. n@param path
   */
  public void initPicture() {
    indegree = new int[wordNum];
    selfedge = new int[wordNum];
    outdegree = new int[wordNum];
    outdegreesave = new int[wordNum];
    edge = new int[wordNum][wordNum];
    table.initTable(wordNum);
  }

  /**
   * not null. n@param path
   */
  public Picture(String path) {
    nioread(path);
    sugiyama();
  }

  /**
   * not null. n@param path
   */
  public void nioread(String path) {
    FileInputStream fis;
    String[] words = null;
    HashSet<String> mapWords = new HashSet<>();
    try {
      fis = new FileInputStream(path);
      FileChannel channel = fis.getChannel();
      int fileSize = (int) channel.size();
      ByteBuffer byteBuffer = ByteBuffer.allocate(fileSize);
      channel.read(byteBuffer);
      byteBuffer.flip();
      byte[] bytes = byteBuffer.array();
      String text = new String(bytes, 0, bytes.length);
      System.out.println(text);
      byteBuffer.clear();
      channel.close();
      fis.close();  
      words = text.split("[^a-zA-Z0-9]");
      for (int i = 0; i < words.length; i++) {
        if (words[i].length() > 0) {
          words[i] = words[i].toLowerCase();
          mapWords.add(words[i]);
        }
      }
      wordNum = mapWords.size();
      wordNum = wordNum * wordNum / 4 + wordNum;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    initPicture();
    an = 0;
    int lastnum = -1;
    int nownum;
    for (String string : words) {
      if (string.length() > 0) {
        System.out.println(string);
      }
    }
    for (String string : words) {
      if (string.length() > 0) {
        nownum = table.add(string, an);
        if (nownum == an) {
          an++;
        }
        if (lastnum == nownum) {
          selfedge[nownum]++;
        } else {
          tree_insert(lastnum, nownum);
        }
        lastnum = nownum;
      }
    }
    for (int i = 0; i < an; ++i) {
      outdegreesave[i] = outdegree[i];
    }
  }


  public GraphDraft getGraphDraft() {
    return graphDraft;
  }

  // array[0]的值
  // 大于等于0，表示桥接词的个数，0表示没有
  // -1:word1 doesn't exist
  // -2:word2 doesn't exist
  // -3:word1 and word2 don't exist
  // -4:not bridge

  /**
   * findBridge. n@param st1 n@param st2 n@return
   */
  public int[] findBridge(String st1, String st2) {
    int[] array = new int[100];
    if (!Option.CaseSensitive) {
      st1 = st1.toLowerCase();
      st2 = st2.toLowerCase();
    }
    int x = table.search(st1);
    int y = table.search(st2);
    if (x == -1 && y == -1) {
      array[0] = -3;
      return array;
    } else if (x == -1) {
      array[0] = -1;
      return array;
    } else if (y == -1) {
      array[0] = -2;
      return array;
    } else {
      for (int i = 0; i < an; ++i) {
        if (edge[x][i] > 0 && edge[i][y] > 0) {
          array[++array[0]] = i;
        }
      }
      if (!Option.AvoidSelfedge) {
        if (selfedge[x] > 0 && edge[x][y] > 0) {
          array[++array[0]] = x;
        }
        if (selfedge[y] > 0 && edge[x][y] > 0) {
          array[++array[0]] = y;
        }
      }
    }
    return array;
  }

  /**
   * not null. n@author Administrator
   *
   */
  public String newText(String st) {
    Random r = new Random();
    if (st == null || st.isEmpty()) {
      return "";
    }
    String[] strings = st.split(" ");
    int[] words;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(strings[0]);
    for (int i = 1; i < strings.length; ++i) {
      stringBuilder.append(' ');
      words = findBridge(strings[i - 1], strings[i]);
      if (words[0] > 0) {
        stringBuilder.append(table.get(words[r.nextInt(words[0]) + 1]));
        stringBuilder.append(' ');
      }
      stringBuilder.append(strings[i]);
    }
    return stringBuilder.toString();
  }

  /**
   * not null. n@author Administrator
   *
   */
  public int[] shortestPath(String st1) {
    int x;
    int[] array = new int[wordNum];
    x = table.search(st1);
    if (x == -1) {
      array[0] = -1;
      return array;
    } else {
      int[] father = new int[dummy];
      int[] count = new int[dummy];
      boolean[] vis = new boolean[dummy];
      for (int i = 0; i < dummy; ++i) {
        count[i] = wordNum;
        vis[i] = false;
      }
      count[x] = 0;
      int seed = x;
      int nseed = wordNum;
      int ncount = wordNum;
      boolean find = true;
      while (find) {
        vis[seed] = true;
        System.out.println("Seed = " + seed);
        if (seed < an) {
          for (int i = 0; i < dummy; ++i) {
            if (internalmap[seed][i] > 0
                && count[i] > count[seed] + edge[seed][i]) {
              count[i] = count[seed] + edge[seed][i];
              father[i] = seed;
            }
          }
        } else {
          for (int i = 0; i < dummy; ++i) {
            if (internalmap[seed][i] > 0) {
              if (count[i] > count[seed]) {
                count[i] = count[seed];
                father[i] = seed;
              }
              break;
            }
          }
        }
        find = false;
        for (int i = 0; i < dummy; ++i) {
          if (!vis[i]) {
            if (count[i] < ncount) {
              ncount = count[i];
              nseed = i;
              find = true;
            }
          }
        }
        seed = nseed;
        ncount = wordNum;
      }
      return count;
    }
  }

  // 0: not reachable
  // -1:word1 doesn't exist
  // -2:word2 doesn't exist
  // -3:word1 and word2 don't exist

  /**
   * shortestPath. n@param st1 n@param st2 n@return
   */
  public int[] shortestPath(String st1, String st2) {
    int x;
    int y;
    int[] array = new int[wordNum];
    x = table.search(st1);
    y = table.search(st2);
    if (x == -1 && y == -1) {
      array[0] = -3;
      return array;
    } else if (x == -1) {
      array[0] = -1;
      return array;
    } else if (y == -1) {
      array[0] = -2;
      return array;
    } else if (x == y) {
      array[0] = 1;
      array[1] = x;
      return array;
    } else {
      int[] father = new int[dummy];
      int[] count = new int[dummy];
      boolean[] vis = new boolean[dummy];
      for (int i = 0; i < dummy; ++i) {
        count[i] = wordNum;
        vis[i] = false;
      }
      count[y] = 0;
      int seed = y;
      int nseed = wordNum;
      int ncount = wordNum;
      boolean find = true;
      while (find) {
        vis[seed] = true;
        if (seed == x) {
          break;
        }
        find = false;
        // System.out.println("Seed = "+ seed);
        if (seed < an) {
          for (int i = 0; i < dummy; ++i) {
            if (internalmap[i][seed] > 0
                && count[i] > count[seed] + edge[i][seed]) {
              count[i] = count[seed] + edge[i][seed];
              father[i] = seed;
            }
          }
        } else {
          for (int i = 0; i < dummy; ++i) {
            if (internalmap[i][seed] > 0) {
              if (count[i] > count[seed]) {
                count[i] = count[seed];
                father[i] = seed;
              }
              break;
            }
          }
        }
        find = false;
        for (int i = 0; i < dummy; ++i) {
          if (!vis[i]) {
            if (count[i] < ncount) {
              ncount = count[i];
              nseed = i;
              find = true;
            }
          }
        }
        seed = nseed;
        ncount = wordNum;
      }
      if (count[x] == wordNum) {
        array[0] = 0;
        return array;
      }
      int k = x;
      array[++array[0]] = x;
      while (x != y) {
        x = father[x];
        array[++array[0]] = x;
      }
      array[array[0] + 1] = count[k];
      return array;
    }
  }

  /**
   * not null. n@author Administrator
   *
   */
  public int[] randomWalk() {
    Random r = new Random();
    int[] array = new int[wordNum];
    boolean[][] vis = new boolean[dummy][dummy];
    array[++array[0]] = r.nextInt(an);
    for (int i = 0; i < an; ++i) {
      outdegree[i] = outdegreesave[i];
    }
    int fck = -1;
    while (outdegree[array[array[0]]] > 0 || array[array[0]] >= an) {
      if (array[array[0]] < an) {
        int past = 0;
        for (int i = 0; i < dummy; ++i) {
          if (internalmap[array[array[0]]][i] == 1
              && !vis[array[array[0]]][i]) {
            if (r.nextInt(outdegree[array[array[0]]] - past) == 0) {
              vis[array[array[0]]][i] = true;
              outdegree[array[array[0]]]--;
              array[++array[0]] = i;
              break;
            } else {
              past++;
            }
          }
        }
      } else {
        for (int i = 0; i < dummy; ++i) {
          if (internalmap[array[array[0]]][i] == 1) {
            array[++array[0]] = i;
            break;
          }
        }
      }
      if (fck == array[array[0]]) {
        System.out.println("hehe");
        break;
      } else {
        fck = array[array[0]];
      }
    }
    int past = 0;
    outdegree[array[array[0]]] = outdegreesave[array[array[0]]];
    for (int i = 0; i < dummy; ++i) {
      if (internalmap[array[array[0]]][i] == 1) {
        if (r.nextInt(outdegree[array[array[0]]] - past) == 0) {
          outdegree[array[array[0]]]--;
          array[++array[0]] = i;
          break;
        }
      }
    }
    while (array[array[0]] >= an) {
      for (int i = 0; i < dummy; ++i) {
        if (internalmap[array[array[0]]][i] == 1) {
          array[++array[0]] = i;
          break;
        }
      }
    }
    return array;
  }

  private int[] layer;
  private int[] order;
  private int[] order2;
  private int[] orders;
  private int[] orders2;
  private int[][] layers;

  private void sugiyama() {
    // 1.Greedy cycle removal
    int[] mark = new int[wordNum];
    int[][] map = new int[wordNum][wordNum];

    for (int i = 0; i < an; ++i) {
      for (int j = 0; j < an; ++j) {
        if (edge[i][j] > 0) {
          map[i][j] = 1;
        } else {
          map[i][j] = 0;
        }
      }
    }
    boolean flag = true;
    while (flag) {
      flag = false;
      boolean found = true;
      while (found) {
        found = false;
        for (int i = 0; i < an; ++i) {
          if (mark[i] == 0 && outdegree[i] == 0) {
            found = true;
            mark[i] = 2;
            for (int j = 0; j < an; ++j) {
              if (map[j][i] > 0 && outdegree[j] > 0) {
                outdegree[j]--;
              }
            }
          }
        }
      }
      found = true;
      while (found) {
        found = false;
        for (int i = 0; i < an; ++i) {
          if (mark[i] == 0 && indegree[i] == 0) {
            found = true;
            mark[i] = 1;
            for (int j = 0; j < an; ++j) {
              if (map[i][j] > 0 && indegree[j] > 0) {
                indegree[j]--;
              }
            }
          }
        }
      }
      int maxdifference = -2000;
      int maxvertex = 0;
      for (int i = 0; i < an; ++i) {
        if (mark[i] == 0) {
          flag = true;
          if (outdegree[i] - indegree[i] > maxdifference) {
            maxdifference = outdegree[i] - indegree[i];
            maxvertex = i;
          }
        }
      }
      if (flag) {
        mark[maxvertex] = 1;
        for (int j = 0; j < an; ++j) {
          if (map[j][maxvertex] == 1 && mark[j] == 0) {
            map[j][maxvertex] = 0;
            map[maxvertex][j] = 1;
            indegree[j]++;
            outdegree[j]--;
            indegree[maxvertex]--;
            outdegree[maxvertex]++;
          }
        }
        for (int j = 0; j < an; ++j) {
          if (map[maxvertex][j] > 0 && indegree[j] > 0) {
            indegree[j]--;
          }
        }
      }
    }

    // 2.The Longest-Path Algorithm
    layers = new int[wordNum][wordNum];
    internalmap = new int[wordNum][wordNum];
    layer = new int[wordNum];
    int currentlayer = 0;
    flag = true;
    dummy = an;
    while (flag) {
      currentlayer++;
      flag = false;
      for (int i = 0; i < an; ++i) {
        if (layer[i] == 0) {
          flag = true;
          boolean found = true;
          for (int j = 0; j < an; ++j) {
            if (map[j][i] == 1) {
              if (layer[j] == 0 || layer[j] == currentlayer) {
                found = false;
                break;
              }
            }
          }
          if (found) {
            layer[i] = currentlayer;
            layers[currentlayer][++layers[currentlayer][0]] = i;
            for (int j = 0; j < an; ++j) {
              if (map[j][i] == 1) {
                if (layer[j] == currentlayer - 1) {
                  internalmap[j][i] = 1;
                } else {
                  layers[layer[j] + 1][++layers[layer[j] + 1][0]] = dummy;
                  layer[dummy] = layer[j] + 1;
                  internalmap[j][dummy++] = 1;
                  for (int k = layer[j] + 2; k < currentlayer; ++k) {
                    layers[k][++layers[k][0]] = dummy;
                    layer[dummy] = k;
                    internalmap[dummy - 1][dummy] = 1;
                    dummy++;
                  }
                  internalmap[dummy - 1][i] = 1;
                }
              }
            }
          }
        }
      }
    }

    for (int l = 2; l < currentlayer; ++l) {
      int firstchange = 1;
      while (firstchange < dummy) {
        int i = firstchange + 1;
        firstchange = dummy;
        for (; i <= layers[l][0]; ++i) {
          int cuv = 0;
          int cvu = 0;
          int flagu = 0;
          int flagv = 0;
          for (int j = 1; j <= layers[l - 1][0]; ++j) {
            if (internalmap[layers[l - 1][j]][layers[l][i - 1]] == 1) {
              cuv += flagv;
            }
            if (internalmap[layers[l - 1][j]][layers[l][i]] == 1) {
              cvu += flagu;
            }
            if (internalmap[layers[l - 1][j]][layers[l][i]] == 1) {
              flagv++;
            }
            if (internalmap[layers[l - 1][j]][layers[l][i - 1]] == 1) {
              flagu++;
            }
          }
          if (cuv > cvu) {
            int temp = layers[l][i - 1];
            layers[l][i - 1] = layers[l][i];
            layers[l][i] = temp;
            if (firstchange == dummy) {
              firstchange = i;
            }
          }
        }
      }
    }

    // 4。X coordination
    orders = new int[wordNum];// record most right position indeed
    orders2 = new int[wordNum];
    order = new int[wordNum];
    order2 = new int[wordNum];
    int[] trueorder = new int[wordNum];
    for (int i = 1; i <= layers[1][0]; ++i) {
      coordination(layers[1][i], 1, currentlayer - 1);
    }
    for (int i = layers[1][0]; i > 0; --i) {
      coordination2(layers[1][i], 1, currentlayer - 1);
    }
    int max = 0;
    for (int i = 0; i < currentlayer; ++i) {
      if (orders2[i] > max) {
        max = orders2[i];
      }
    }
    max++;
    for (int i = 0; i < dummy; ++i) {
      trueorder[i] = (max + order[i] - order2[i]) / 2;
    }

    Point[] points = new Point[dummy];
    for (int i = 0; i < an; ++i) {
      points[i] = new Point(trueorder[i], layer[i], table.get(i));
    }
    for (int i = an; i < dummy; ++i) {
      points[i] = new Point(trueorder[i], layer[i], null);
    }
    MyEdge[] edges = new MyEdge[an * an + dummy];
    int k = 0;

    processed = new boolean[dummy][dummy];

    for (int i = 0; i < an; ++i) {
      for (int j = i; j < an; ++j) {
        if (internalmap[i][j] > 0 || internalmap[j][i] > 0) {
          if (edge[i][j] > 0) {
            internalmap[i][j] = 1;
            if (edge[j][i] > 0) {
              edges[k++] = new MyEdge(i, j, edge[i][j], edge[j][i], 2);
              internalmap[j][i] = 1;
            } else {
              edges[k++] = new MyEdge(i, j, edge[i][j], 1);
              internalmap[j][i] = 0;
            }
          } else {
            internalmap[j][i] = 1;
            internalmap[i][j] = 0;
            edges[k++] = new MyEdge(j, i, edge[j][i], 1);
          }
        }
      }
      for (int j = an; j < dummy; ++j) {
        if (internalmap[i][j] > 0) {
          Point p;
          if (!processed[j][i]) {
            p = findDirection(i, j);
            processed[i][j] = true;
            processed[j][i] = true;
            edge[i][j] = p.getPx();
            edge[j][i] = p.getPy();
          } else {
            p = new Point(edge[i][j], edge[j][i], "balabala");
          }
          if (p.getPx() > 0 && p.getPy() > 0) {
            edges[k++] = new MyEdge(j, i, edge[j][i], edge[i][j], 5);
            internalmap[i][j] = 1;
            internalmap[j][i] = 1;
          } else if (p.getPx() > 0) {
            edges[k++] = new MyEdge(i, j, edge[i][j], 3);
            internalmap[i][j] = 1;
            internalmap[j][i] = 0;
          } else if (p.getPy() > 0) {
            edges[k++] = new MyEdge(j, i, edge[j][i], 1);
            internalmap[i][j] = 0;
            internalmap[j][i] = 1;
          } else {
            System.out.print("Confused1?????????/doge ");
            System.out.print("(" + i + "<" + table.get(i) + ">," + j + "<"
                + table.get(j) + ">) ");
            System.out.println(" p.x = " + p.getPx() + ", p.y = " + p.getPy()
                + " " + p.getSt());
          }
        } else if (internalmap[j][i] > 0) {
          Point p;
          if (!processed[i][j]) {
            p = findDirection2(i, j);
            processed[i][j] = true;
            processed[j][i] = true;
            edge[i][j] = p.getPx();
            edge[j][i] = p.getPy();
          } else {
            p = new Point(edge[i][j], edge[j][i], null);
          }
          if (p.getPx() > 0 && p.getPy() > 0) {
            edges[k++] = new MyEdge(j, i, edge[j][i], edge[i][j], 5);
            internalmap[i][j] = 1;
            internalmap[j][i] = 1;
          } else if (p.getPx() > 0) {
            edges[k++] = new MyEdge(i, j, edge[i][j], 3);
            internalmap[i][j] = 1;
            internalmap[j][i] = 0;
          } else if (p.getPy() > 0) {
            edges[k++] = new MyEdge(j, i, edge[j][i], 1);
            internalmap[i][j] = 0;
            internalmap[j][i] = 1;
          } else {
            System.out.print("Confused2?????????/doge ");
            System.out.print("(" + i + "<" + table.get(i) + ">," + j + "<"
                + table.get(j) + ">) ");
            System.out.println(" p.x = " + p.getPx() + ", p.y = " + p.getPy()
                + " " + p.getSt());
          }
        }
      }
    }
    for (int i = an; i < dummy; ++i) {
      for (int j = i; j < dummy; ++j) {
        if (internalmap[i][j] > 0 && internalmap[j][i] > 0) {
          edges[k++] = new MyEdge(i, j, edge[i][j], edge[j][i], 4);
        } else if (internalmap[i][j] > 0) {
          edges[k++] = new MyEdge(i, j, edge[i][j], 3);
        } else if (internalmap[j][i] > 0) {
          edges[k++] = new MyEdge(j, i, edge[j][i], 3);
        }
      }
    }
    for (int i = 0; i < an; ++i) {
      if (selfedge[i] > 0) {
        edges[k++] = new MyEdge(i, i, selfedge[i], 1);
      }
    }
    graphDraft = new GraphDraft(points, edges);
  }

  private Point findDirection2(int x, int y) {
    if (y < an) {
      return new Point(edge[x][y], edge[y][x], null);
    } else {
      for (int i = 0; i < dummy; ++i) {
        if (internalmap[i][y] > 0) {
          Point p = findDirection2(x, i);
          processed[y][i] = true;
          edge[y][i] = p.getPx();
          edge[i][y] = p.getPy();
          if (p.getPx() > 0 && p.getPy() > 0) {
            internalmap[y][i] = 1;
            internalmap[i][y] = 1;
          } else if (p.getPx() > 0) {
            internalmap[y][i] = 1;
            internalmap[i][y] = 0;
          } else {
            internalmap[y][i] = 0;
            internalmap[i][y] = 1;
          }
          return p;
        }
      }
      return new Point(edge[x][y], edge[y][x], null);
    }
  }

  private Point findDirection(int x, int y) {
    if (y < an) {
      return new Point(edge[x][y], edge[y][x], null);
    } else {
      for (int i = 0; i < dummy; ++i) {
        if (internalmap[y][i] > 0) {
          // System.out.println(y + ", " + i + " " + an);
          Point p = findDirection(x, i);
          processed[y][i] = true;
          edge[y][i] = p.getPx();
          edge[i][y] = p.getPy();
          if (p.getPx() > 0 && p.getPy() > 0) {
            internalmap[y][i] = 1;
            internalmap[i][y] = 1;
          } else if (p.getPx() > 0) {
            internalmap[y][i] = 1;
            internalmap[i][y] = 0;
          } else {
            internalmap[y][i] = 0;
            internalmap[i][y] = 1;
          }
          return p;
        }
      }
      return new Point(edge[x][y], edge[y][x], null);
    }
  }

  private int coordination(int point, int father, int maxlayer) {
    int currentcoordination;
    int currentlayer = layer[point];
    if (orders[currentlayer] < father) {
      currentcoordination = father;
    } else {
      currentcoordination = orders[currentlayer] + 1;
    }
    if (currentlayer < maxlayer) {
      int nxtlayer = currentlayer + 1;
      int maxleft = wordNum;
      int maxson = 0;
      for (int i = 1; i <= layers[nxtlayer][0]; ++i) {
        if (internalmap[point][layers[nxtlayer][i]] == 1) {
          maxson = i;
        }
      }
      for (int i = 1; i <= maxson; ++i) {
        if (order[layers[nxtlayer][i]] == 0) {
          coordination(layers[nxtlayer][i], currentcoordination, maxlayer);
        }
        if (maxleft > order[layers[nxtlayer][i]]) {
          maxleft = order[layers[nxtlayer][i]];
        }
      }
      if (maxleft < wordNum && maxleft > currentcoordination) {
        currentcoordination = maxleft;
      }
    }
    orders[currentlayer] = currentcoordination;
    order[point] = currentcoordination;
    return currentcoordination;
  }

  private int coordination2(int point, int father, int maxlayer) {
    int currentcoordination;
    int currentlayer = layer[point];
    if (orders2[currentlayer] < father) {
      currentcoordination = father;
    } else {
      currentcoordination = orders2[currentlayer] + 1;
    }
    if (currentlayer < maxlayer) {
      int nxtlayer = currentlayer + 1;
      int maxleft = wordNum;
      int maxson = 1;
      for (int i = layers[nxtlayer][0]; i > 0; --i) {
        if (internalmap[point][layers[nxtlayer][i]] == 1) {
          maxson = i;
        }
      }
      for (int i = layers[nxtlayer][0]; i >= maxson; --i) {
        if (order2[layers[nxtlayer][i]] == 0) {
          coordination2(layers[nxtlayer][i], currentcoordination, maxlayer);
        }
        if (maxleft > order2[layers[nxtlayer][i]]) {
          maxleft = order2[layers[nxtlayer][i]];
        }
      }
      if (maxleft < wordNum && maxleft > currentcoordination) {
        currentcoordination = maxleft;
      }
    }
    orders2[currentlayer] = currentcoordination;
    order2[point] = currentcoordination;
    return currentcoordination;
  }

  private void tree_insert(int x, int y) {
    if (x < 0) {
      return;
    }
    edge[x][y]++;
    if (edge[x][y] == 1) {
      outdegree[x]++;
      indegree[y]++;
    }
  }
}