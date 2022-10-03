package springexample;


import java.io.*;

public class PPCPVisualizerFactory {
   public static String fromConfigFile(File file) {
       String content = "";
       try {
           content = readFile(file);
       } catch (IOException e) {
           e.printStackTrace();
       }

       return content;
    }

    public static String readFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStream in = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + System.lineSeparator());
        }

        return sb.toString();
    }
}
