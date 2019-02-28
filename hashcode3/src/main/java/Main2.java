
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Main2 {

  String[] files = new String[]{"a_example.txt", "b_lovely_landscapes.txt", "c_memorable_moments.txt",
      "d_pet_pictures.txt", "e_shiny_selfies.txt"};

  public void doIt() throws IOException {
    int[] o;
    int[] t;
    List<String>[] tags;

    boolean system = false;
    int runOne = -1;

    for (int kk = 1; kk <= files.length; kk++) {
      System.out.println();
      if (runOne != -1) {
        kk = runOne;
      }
      String filenameIn = "files/";
      String filenameOut = "src/main/java/";

      if (!system) {
        String filename = files[kk - 1];

        System.out.println("Start " + filename);

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
        writer = new PrintWriter(new FileWriter(filenameOut.replace(".txt", ".out")));
      }

      // ////
      String s = br.readLine();

      int N = Integer.parseInt(s);
      o = new int[N];
      t = new int[N];
      tags = new List[N];
      for (int i = 0; i < N; i++) {
        s = br.readLine();
        String[] ss = s.split(" ");
        if (ss[0].equals("H")) {
          o[i] = 1;
        }
        t[i] = Integer.parseInt(ss[1]);
        tags[i] = new ArrayList<String>();
        for (int j = 0; j < t[i]; j++) {
          tags[i].add(ss[2 + j]);
        }
      }
      System.out.println("Photos : " + N);
      int x = 0;
      for (int i = 0; i < N; i++) {
        if (o[i] == 1) {
          x++;
        }
      }
      int horizontal = x;
      int vertical = N - x;
      System.out.println("Horizontal : " + x + " Vertical : " + (N - x));

      // Arrays.sort(t);
      Set<String> set = new HashSet<String>();
      for (int i = 0; i < t.length; i++) {
        // System.out.print(t[i] + " ");
        // if (i %100 == 0) {
        // System.out.println();
        // }
        for (String tt : tags[i]) {
          set.add(tt);
        }
      }
      Map<String, Integer> map = new HashMap<String, Integer>();
      int ii = 0;
      for (String string : set) {
        map.put(string, ii);
        ii++;
      }
       System.out.println("Max tags per photo: " + t[t.length - 1]);
      // System.out.println();
       System.out.println("Total different tags: " + set.size());

      List<Photo> h = new ArrayList<Photo>();
      List<Photo> v = new ArrayList<Photo>();
      for (int i = 0; i < N; i++) {
        Photo p = new Photo();
        p.index = i;
        p.tags = tags[i];
        int[] ts = new int[p.tags.size()];
        int j = 0;
        for (String ss : p.tags) {
          ts[j] = map.get(ss);
          j++;
        }
        p.ts = ts;
        Arrays.sort(p.ts);

        if (o[i] == 1) {
          h.add(p);
        } else {
          v.add(p);
        }
      }

      int indH = 0;
      int indV = 0;
      List<Photo> result = new ArrayList<Photo>();

      boolean done = false;
      while (!done) {
        // get 20 photos, make a dp for best score
        int nextH = Math.min(h.size() - indH, 10);
        int nextV = Math.min(v.size() - indV, 20) / 2 * 2;
        if (nextV < 10) {
          nextH = Math.min(h.size() - indH, 10 + 10 - nextV);
        }
        if (nextH < 10) {
          nextV = Math.min(v.size() - indV, 20 + 20 - nextH * 2) / 2 * 2;
        }

        if (nextH == 0 && nextV == 0) {
          break;
        }
        indH = indH + nextH;
        indV = indV + nextV;
      }

      // Sort photos according to number of tags
      h.sort(new Comparator<Photo>() {
        @Override
        public int compare(Photo p1, Photo p2) {
          return p1.ts.length - p2.ts.length;
        }
      });
      LinkedList<Photo> sorted = new LinkedList<Photo>();
      for (Photo photo : h) {
        sorted.add(photo);
      }
      
      done = false;
      Photo p = sorted.remove(0);
      result.add(p);
      while (!done) {
        if (sorted.isEmpty()) {
          done = true;
          break;
        }
        int ix = 0;
        Photo best = null;
        int bests = -1;
        int bestix = 0;
        for (Photo photo : sorted) {
          int tbests = getScore(p, photo);
          if (tbests > bests) {
            best = photo;
            bestix = ix;
            bests = tbests;
          }
          ix++;
          if (ix > 10000) {
            break;
          }
        }
        p = best;
        result.add(p);
        sorted.remove(bestix);
      }


      writer.println(result.size());
      for (Photo photo : result) {
        if (photo.index2 != -1) {
          writer.println(photo.index + " " + photo.index2);
        } else {
          writer.println(photo.index);
        }
      }

      System.out.println("Score : " + calcScore(result));

      writer.flush();
      if (system) {
        break;
      } else {
        writer.close();
      }
      if (runOne != -1) {
        break;
      }

    }
    System.out.println("Done");
  }

  private int calcScore(List<Photo> result) {
    int score = 0;
    Photo lastPhoto = null;
    for (Photo photo : result) {
      if (lastPhoto != null) {
        score += getScore(lastPhoto, photo);
      }
      lastPhoto = photo;
    }
    return score;
  }

  public int getScore(Photo p1, Photo p2) {
    int common = 0;
    int first = 0;
    int second = 0;

    int x1 = 0;
    int x2 = 0;
    while (x1 < p1.ts.length && x2 < p2.ts.length) {
      if (p1.ts[x1] < p2.ts[x2]) {
        first++;
        x1++;
      } else if (p1.ts[x1] > p2.ts[x2]) {
        second++;
        x2++;
      } else {
        common++;
        x1++;
        x2++;
      }
    }
    while (x1 < p1.ts.length) {
      first++;
      x1++;
    }
    while (x2 < p2.ts.length) {
      second++;
      x2++;
    }

    return Math.min(common, Math.min(first, second));
  }

  public Photo merge(Photo p1, Photo p2) {
    Photo p = new Photo();
    p.index = p1.index;
    p.index2 = p2.index;

    int[] ts = new int[p1.ts.length + p2.ts.length];
    int x1 = 0;
    int x2 = 0;
    int i = 0;
    while (x1 < p1.ts.length && x2 < p2.ts.length) {
      if (p1.ts[x1] < p2.ts[x2]) {
        ts[i] = p1.ts[x1];
        i++;
        x1++;
      } else if (p1.ts[x1] > p2.ts[x2]) {
        ts[i] = p2.ts[x2];
        i++;
        x2++;
      } else {
        ts[i] = p1.ts[x1];
        i++;
        x1++;
        x2++;
      }
    }
    while (x1 < p1.ts.length) {
      ts[i] = p1.ts[x1];
      i++;
      x1++;
    }
    while (x2 < p2.ts.length) {
      ts[i] = p2.ts[x2];
      i++;
      x2++;
    }
    int[] tts = new int[i];
    for (int j = 0; j < tts.length; j++) {
      tts[j] = ts[j];
    }
    Arrays.sort(tts);
    p.ts = tts;
    return p;
  }

  public static void main(String[] args) throws IOException {
    Main2 main = new Main2();
    main.doIt();
  }

}
