// URL: https://cat-fact.herokuapp.com/facts

import java.io.*;
import java.net.*;

public class Main{
    public static void main(String[] args) throws Exception{
        int[] w = {5};
        String URLSTRING = "https://cat-fact.herokuapp.com/facts/";
        StringBuilder result = new StringBuilder();
        URL url = new URL(URLSTRING);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
      try (BufferedReader reader = new BufferedReader(
                  new InputStreamReader(conn.getInputStream()))) {
          for (String line; (line = reader.readLine()) != null; ) {
              result.append(line);
          }
      }
      String result1 = result.toString().substring(118);
      result1 = result1.substring(0, result1.indexOf('\"'));
      System.out.println("Result: "+result1);
    }
};