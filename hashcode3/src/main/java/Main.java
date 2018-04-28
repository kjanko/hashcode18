

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class Main {
  public static void main(String[] args) throws IOException {
    boolean system = false;
    for (int kk = 1; kk <= 5; kk++) {
      String filenameIn = "files/";
      String filenameOut = "src/main/java/";
      
      if (!system) {
        String filename = "";
        if (kk == 1) {
          filename += "a_.in";
        } else if (kk == 2) {
          filename += "b_.in";
        } else if (kk == 3) {
          filename += "c_.in";
        } else if (kk == 4) {
          filename += "d_.in";
        } else {
          filename += "e_.in";
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
      
      ////// 
      String s = br.readLine();
      String[] ss = s.split(" ");
//      int R = Integer.parseInt(ss[0]);
//      int C = Integer.parseInt(ss[1]);
//      int F = Integer.parseInt(ss[2]);
//      int N = Integer.parseInt(ss[3]);
//      int B = Integer.parseInt(ss[4]);
//      int T = Integer.parseInt(ss[5]);
      

      
      writer.flush();
      if (system) {
        break;
      } else {
        writer.close();
      }
    } 
   
  }

}
