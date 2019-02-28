
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Photo {

  int index;
  int index2 = -1;
  List<String> tags;
  int[] ts;
  @Override
  public int hashCode() {
    return index * 37;
  }
  @Override
  public boolean equals(Object obj) {
    Photo p = (Photo) obj;
    return index == p.index;
  }


}

public class Main {

  String[] files2 = new String[]{"a_example.txt", "b_lovely_landscapes.txt", "c_memorable_moments.txt",
      "d_pet_pictures.txt", "e_shiny_selfies.txt"};
  
  String[] files3 = new String[]{ "e_shiny_selfies.txt"};
  
  String[] files4 = new String[]{"b_lovely_landscapes.txt"};
  
  String[] files = new String[]{"c_memorable_moments.txt",
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
      Map<String, Integer> tagMap = new HashMap<String, Integer>();
      int ii = 0;
      for (String string : set) {
        tagMap.put(string, ii);
        ii++;
      }
      int totalTags = ii;
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
          ts[j] = tagMap.get(ss);
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
      List<Photo> toSort = new ArrayList<Photo>();
      toSort.addAll(h);
      
      v.sort(new Comparator<Photo>() {
        @Override
        public int compare(Photo p1, Photo p2) {
          return p1.ts.length - p2.ts.length;
        }
      });  
      System.out.println("ToSortBefore : " + toSort.size());
      int counter2 = 0;
      int vsortAlgo = 1;
      if (vsortAlgo == 1) {

        int minTag = 30;
        int maxTag = 0;
        Set<Photo>[] vset = new Set[30];
        for (int k = 0; k < vset.length; k++) {
          vset[k] = new HashSet<Photo>();
        }
        for (Photo p : v) {
          vset[p.ts.length].add(p);
          minTag = Math.min(minTag, p.ts.length);
          maxTag = Math.max(maxTag, p.ts.length);
        }

        while (minTag <= maxTag) {
          counter2++;
          if (counter2 % 10000 == 0) {
            System.out.println(counter2);
          }
          while (vset[minTag].size() == 0 && minTag < 29) {
            minTag++;
          }
          while (vset[maxTag].size() == 0 && maxTag > 0) {
            maxTag--;
          }
          if (minTag > maxTag) {
            break;
          }
          int sc = 10000;
          Photo bestp1 = null;
          Photo bestp2 = null;
          
          int ip1 = 0;
          for (Photo p1 : vset[minTag]) {
            ip1++;
            if (ip1 == 200) {
              break;
            }
            int ip2 = 0;
            for (Photo p2 : vset[maxTag]) {
              ip2++;
              if (ip2 == 200) {
                break;
              }
              if (p1 == p2) {
                continue;
              }
              int sc2 = getOverlap(p1, p2);
              if (sc2 < sc) {
                sc = sc2;
                bestp1 = p1;
                bestp2 = p2;
              }
            }
          }
          if (bestp1 == null) {
            break;
          }
          vset[minTag].remove(bestp1);
          vset[maxTag].remove(bestp2);
          Photo p = merge(bestp1, bestp2);
          toSort.add(p);
        }
      }
      if (vsortAlgo == 2) {
        for (int j = 0; j < v.size() / 2; j++) {
          Photo p1 = v.get(j);
          Photo p2 = v.get(v.size() - j - 1);
          Photo p = merge(p1, p2);
          toSort.add(p);
        }
      }
      System.out.println("ToSort : " + toSort.size());
      

      
      toSort.sort(new Comparator<Photo>() {
        @Override
        public int compare(Photo p1, Photo p2) {
          return p1.ts.length - p2.ts.length;
        }
      });
      LinkedList<Photo> sorted = new LinkedList<Photo>();
      for (Photo photo : toSort) {
        sorted.add(photo);
      }
      
      int counter = 0;
      // Algo 1
      int algo = 1;
      if (algo == 1) {
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
          counter++;
          if (counter % 10000 == 0) {
            System.out.println(counter);
          }
          sorted.remove(bestix);
        }
      }
      if (algo == 2) {
        Map<Integer, Set<Photo>> map = new HashMap<Integer, Set<Photo>>();
        for (int i = 0; i < totalTags; i++) {
          map.put(i,  new HashSet<Photo>());
        }
        for (Photo photo : sorted) {
          for (int tagId : photo.ts) {
            map.get(tagId).add(photo);
          }
        }
        
        Photo p = sorted.get(0);
        
        done = false;
        result.add(p);
        for (int i = 0; i < p.ts.length; i++) {
          map.get(p.ts[i]).remove(p);
        }

        while (!done) {
          Photo nextPhoto = null;
          int bestsc = -1;
          Set<Integer> photoIdx = new HashSet<Integer>();
          for (int i = 0; i < p.ts.length; i++) {
            for (Photo photo : map.get(p.ts[i])) {
              if (photoIdx.contains(photo.index)) {
                continue;
              }
              int sc = getScore(p, photo);
              if (sc > bestsc) {
                nextPhoto = photo;
                bestsc = sc;
              } else if (sc == bestsc) {
                if (nextPhoto.ts.length > photo.ts.length) {
                  nextPhoto = photo;
                  bestsc = sc;                  
                }
              }
            }
          }
          
          if (nextPhoto == null) {
            for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
              Integer integer = (Integer) iterator.next();
              Set<Photo> photoset = map.get(integer);
              if (photoset == null) {
                //iterator.remove();
                continue;
              }
              if (!photoset.isEmpty()) {
                nextPhoto = photoset.iterator().next();
                break;
              }
            }
          }
          if (nextPhoto == null) {
            break;
          }
          p = nextPhoto;
          counter++;
          if (counter % 10000 == 0) {
            System.out.println(counter);
          }
          result.add(p);
          for (int i = 0; i < p.ts.length; i++) {
            map.get(p.ts[i]).remove(p);
          }
        }
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

  private int getOverlap(Photo p1, Photo p2) {
    int common = 0;

    int x1 = 0;
    int x2 = 0;
    while (x1 < p1.ts.length && x2 < p2.ts.length) {
      if (p1.ts[x1] < p2.ts[x2]) {

        x1++;
      } else if (p1.ts[x1] > p2.ts[x2]) {

        x2++;
      } else {
        common++;
        x1++;
        x2++;
      }
    }
    return common;
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
    Main main = new Main();
    main.doIt();
  }

}
