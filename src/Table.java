
public class Table {

  private String[] stringshash;
  private String[] strings;
  private int[] order;
  private int wordnum;
  
  public Table() {

  }
  

  /**not null.
 * n@author Administrator
 *
 */
  public void initTable(int wordnum) {
    this.wordnum = wordnum;
    stringshash = new String[wordnum];
    strings = new String[wordnum];
    order = new int[wordnum];
  }

  // This method will return the same value with search if st already exists.

  /**
   * add. n@param st n@param num n@return
   */
  public int add(String st, int num) {
    int k = hash(st);
    while (stringshash[k] != null && !stringshash[k].equals(st)) {
      k++;
      if (k == wordnum) {
        k = 0;
      }
    }
    // System.out.println("k = " + k);
    if (stringshash[k] == null) {
      stringshash[k] = st;
      strings[num] = st;
      order[k] = num;
      return num;
    } else {
      return order[k];
    }
  }

  /**
   * search. n@param st
   */
  public int search(String st) {
    int k = hash(st);
    while (stringshash[k] != null && !stringshash[k].equals(st)) {
      k++;
      if (k == wordnum) {
        k = 0;
      }
    }
    if (stringshash[k] == null) {
      return -1;
    } else {
      return order[k];
    }
  }

  public String get(int num) {
    return strings[num];
  }

  private int hash(String st) {
    return Math.abs(st.hashCode() % wordnum);
  }
}