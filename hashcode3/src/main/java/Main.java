
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Building {

  int i;
  int t;
  int[][] z;
  int h;
  int w;
  int r;
}

class P {
  public P(int s, int x, int y) {
    this.s = s;
    this.x = x;
    this.y = y;
  }
  int x;
  int y;
  int s;
}

class Placement {

  Set<Integer> utilities;
  public Placement(Building b, int i, int x, int y) {
    this.b = b;
    this.x = x;
    this.y = y;
    this.i = i;
    utilities = new HashSet<Integer>();
  }

  Building b;
  int i;
  int x;
  int y;
}

public class Main {

  private int H;
  private int W;
  private int HH;
  private int WW;
  private int D;
  int[][] map;
  int[] mapY;

  public void doIt() throws IOException {
    boolean system = false;

    for (int kk = 1; kk <= 6; kk++) {
      String filenameIn = "files/";
      String filenameOut = "src/main/java/";
//      if (kk != 2) {
//        continue;
//      }
      if (!system) {
        String filename = "";
        if (kk == 1) {
          filename += "a_example.in";
        }
        if (kk == 2) {
          filename += "b_short_walk.in";
        }
        if (kk == 3) {
          filename += "c_going_green.in";
        }
        if (kk == 4) {
          filename += "d_wide_selection.in";
        }
        if (kk == 5) {
          filename += "e_precise_fit.in";
        }
        if (kk == 6) {
          filename += "f_different_footprints.in";
        }
        filenameIn += filename;
        filenameOut += filename;
      }
      BufferedReader br = null;
      if (system) {
        br = new BufferedReader(new InputStreamReader(System.in));
      } else {
        br = new BufferedReader(new FileReader(filenameIn));
      }
      PrintWriter writer = null;
      if (system) {
        writer = new PrintWriter(System.out);
      } else {
        writer = new PrintWriter(new FileWriter(filenameOut.replace(".in", ".out")));
      }

      // ////
      String s = br.readLine();
      String[] ss = s.split(" ");
      HH = Integer.parseInt(ss[0]);
      WW = Integer.parseInt(ss[1]);
      int dd = 10;
      if (HH > 100) {
        H = HH / dd;
      } else {
        H = HH;
      }
      if (WW > 100) {
        W = WW / dd;
      } else {
        W = WW;
      }
      D = Integer.parseInt(ss[2]);
      int B = Integer.parseInt(ss[3]);

      Map<Integer, Building> res = new HashMap<Integer, Building>();
      Map<Integer, Building> util = new HashMap<Integer, Building>();
      List<Building> resList = new ArrayList<Building>();

      for (int i = 0; i < B; i++) {
        s = br.readLine();
        ss = s.split(" ");
        String t = ss[0];
        int h = Integer.parseInt(ss[1]);
        int w = Integer.parseInt(ss[2]);
        int ru = Integer.parseInt(ss[3]);
        int[][] z = new int[w][h];
        for (int j = 0; j < h; j++) {
          s = br.readLine();
          for (int j2 = 0; j2 < s.length(); j2++) {
            z[j2][j] = s.charAt(j2) == '#' ? 1 : 0;
          }
        }
        Building b = new Building();
        b.i = i;
        b.h = h;
        b.w = w;
        b.r = ru;
        b.z = z;
        if (t.equals("R")) {
          b.t = 1;
          res.put(i, b);
          resList.add(b);
        } else {
          b.t = 2;
          util.put(i, b);
        }
      }

      Collections.sort(resList, new Comparator<Building>() {
        @Override
        public int compare(Building o1, Building o2) {
          int x = o2.r - o1.r;
          if (x == 0) {
            return o1.w * o1.h - o2.w * o2.h;
          }
          return x;
        }
      });
      List<Placement> places = new ArrayList<Placement>();
      // Stats
      System.out.println();
      System.out.println("#############################################");
      System.out.println(filenameOut);
      System.out.println("H: " + H + " W:" + W + " D:" + D + " B:" + B);
      System.out.println("Res:" + res.size() + " Util:" + util.size());
      for (int x : res.keySet()) {
        Building b = res.get(x);
        System.out.print(b.w + " " + b.h + " ");
      }
      System.out.println();
      for (int x : util.keySet()) {
        Building b = util.get(x);
        System.out.print(b.w + " " + b.h + " ");
      }
      System.out.println();

      List<Integer> r = new ArrayList<Integer>();
      for (int x : res.keySet()) {
        Building b = res.get(x);
        r.add(b.r);
      }
      Collections.sort(r);
      for (int x : r) {

        System.out.print(x + " ");
      }
      System.out.println();

      r = new ArrayList<Integer>();
      for (int x : util.keySet()) {
        Building b = util.get(x);
        r.add(b.r);
      }
      Collections.sort(r);
      for (int x : r) {
        System.out.print(x + " ");
      }
      System.out.println();

      // Solve
      Building bb1 = res.get(res.keySet().iterator().next());
//      Building b2 = util.get(util.keySet().iterator().next());
      // List<Building> utilList = new ArrayList<Buildin>();

      map = new int[W][H];
      mapY = new int[W];
      for (int i = 0; i < W; i++) {
        mapY[i] = -1;
      }
      places.add(new Placement(bb1, bb1.i, 0, 0));
      for (int i = 0; i < 0 + bb1.w; i++) {
        for (int j = 0; j < 0 + bb1.h; j++) {
          if (bb1.z[i - 0][j - 0]== 1) {
            map[i][j] = 1;
            mapY[i] = Math.max(mapY[i], j);
          }
        }
      }
//      places.add(new Placement(b2, b2.i, b1.w, b1.h));

//      places.remove(b1.i);
      
      
      while (true) {
        P maxz = new P(0, W / 2, H / 2);
        Building b = null;
        int zz = 0;
        for (Building  b1: res.values()) {
          P z = placeUtil(places, b1);
                    
          if (z.s > maxz.s) {
            maxz = z;
            b = b1;
          }
          
//          if (zz++ > 0) {
//            break;
//          }
        }
        zz = 0;
        for (Building  b2: util.values()) {
          P z = placeUtil(places, b2);
          if (z.s > maxz.s) {
            maxz = z;
            b = b2;
          }
//          if (zz++ > 0) {
//            break;
//          }
        }
        if (maxz.s == 0) {
          break;
        }
        
        places.add(new Placement(b, b.i, maxz.x, maxz.y));
//        if (places.size() % 100== 0) {
//          System.out.println(places.size());
//        }
        for (int i = maxz.x; i < maxz.x + b.w; i++) {
          for (int j = maxz.y; j < maxz.y + b.h; j++) {
            if (b.z[i - maxz.x][j - maxz.y]== 1) {
              map[i][j] = 1;
              mapY[i] = Math.max(mapY[i], j);
            }
          }
        }
        score(places, D);
//        if (places.size() > 1000) {
//          break;
//        }
//        res.remove(b.i);
//        util.remove(b.i);
      }
      
      
      // REZ
      int rez = score(places, D);
      System.out.println(rez);

      writer.println(places.size());
      if (HH > 100) {
        for (int i = 0; i < dd; i++) {
          for (int j = 0; j < dd; j++) {
            for (Placement place : places) {
              writer.println(place.i + " " + (H * j + place.y) + " " + (W * i + place.x));
            }        
            
          }
        }
      } else {
          
          for (Placement place : places) {
            writer.println(place.i + " " + place.y + " " + place.x);
          }        
        
      }

      writer.flush();
      if (system) {
        break;
      } else {
        writer.close();
      }

    }

  }

  public static void main(String[] args) throws IOException {
    Main main = new Main();
    main.doIt();
  }

  private P placeUtil(List<Placement> places, Building b1) {
    P best = new P(0, W / 2, H / 2);
    for (int x = 0; x < W; x++) {
      int xx = x;
      int yx = mapY[x] + 1;
      if (yx == -1) {
        boolean firstRow = false;
        if (x > 0) {
          for (int i = 0; i < b1.h; i++) {
            if (map[x - 1][yx + 1 + i]  >0) {
              firstRow = true;
            }
          }
        }
        if (firstRow) {
          yx = 0;
        } else {
          continue;
        }
      }
      
      int sc = check(places, xx, yx, b1);
      if (sc > best.s) {
        best = new P(sc, xx, yx);
        return best;
      }
    
    }
    return best;
  }

  private int check(List<Placement> places, int xx, int yx, Building b1) {
    boolean good = true;
    
    if (yx < 0) {
      return -1;
    }
    if (yx + b1.h >= H) {
      return -1;
    }

    for (int i = xx; i < xx + b1.w; i++) {
      if (i >= W) {
        good = false;
        break;
      }
      if (mapY[i] >= yx) {
        good = false;
        break;
      }
      
    }

//    for (int i = xx; i < xx + b1.w; i++) {
//      for (int j = yx; j < yx + b1.h; j++) {
//        if (i >= W) {
//          return -1;
//        }
//        if (j >= H) {
//          return -1;
//        }
//        if (map[i][j] > 0) {
//          good = false;
//        }
//      }
//    }
    if (!good) {
      return -1;
    }

    int sc = score2(places, new Placement(b1, b1.i, xx, yx));
    return sc;
  }

  private static int score(List<Placement> places, int D) {
    int rez = 0;
    for (Placement p1 : places) {
      if (p1.b.t == 2) {
        continue;
      }
      Set<Integer> set = new HashSet<Integer>();
      for (Placement p2 : places) {
        if (p2.b.t == 1) {
          continue;
        }
        if (p1.i == p2.i) {
          continue;
        }
        int d = dist(p1, p2);
        if (d <= D) {
          set.add(p2.b.r);
          p1.utilities.add(p2.b.r);
        }
      }
      rez = rez + p1.b.r * set.size();
    }
    return rez;
  }

  private int score2(List<Placement> places, Placement place) {
    int rez = 0;
    if (place.b.t == 1) {
      Set<Integer> set = new HashSet<Integer>();
      for (Placement p2 : places) {
        if (p2.b.t == 1) {
          continue;
        }
        if (place.i == p2.i) {
          continue;
        }
        int d = dist(place, p2);
        if (d <= this.D) {
          set.add(p2.b.r);
        }
      }
      rez = rez + place.b.r * set.size();
    }
    if (place.b.t == 2) {
      for (Placement p2 : places) {
        if (p2.b.t == 2) {
          continue;
        }
        if (place.i == p2.i) {
          continue;
        }

        int d = dist(place, p2);
        
        if (d <= this.D) {
          if (!p2.utilities.contains(place.b.t)) {
            rez = rez + p2.b.t;
          }
        }
      }
//      rez = rez + place.b.r * set.size();
    }
    return rez;
  }

  private static int dist(Placement p1, Placement p2) {
    int d = Integer.MAX_VALUE;
    for (int i1 = 0; i1 < p1.b.w; i1+=Math.max(p1.b.w - 1, 1)) {
      for (int j1 = 0; j1 < p1.b.h; j1+=Math.max(p1.b.h - 1, 1)) {
        if (p1.b.z[i1][j1] == 0) {
          continue;
        }
        int x1 = p1.x + i1;
        int y1 = p1.y + j1;
        for (int i2 = 0; i2 < p2.b.w; i2+=Math.max(p2.b.w - 1, 1)) {
          for (int j2 = 0; j2 < p2.b.h; j2+= Math.max(p2.b.h - 1, 1)) {
            if (p2.b.z[i2][j2] == 0) {
              continue;
            }
            int x2 = p2.x + i2;
            int y2 = p2.y + j2;
            int D = Math.abs(x1 - x2) + Math.abs(y1 - y2);
            d = Math.min(D, d);
          }
        }
      }
    }

    return d;
  }

}
