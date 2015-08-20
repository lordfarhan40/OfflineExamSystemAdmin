package Model;

import java.io.*;

/**
 * Created by Farhan on 20-07-2015.
 */
public class FileManager {
    public static String fileRead(String address, Object j) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(address));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String buffer;

            while ((buffer = bufferedReader.readLine()) != null) {
                stringBuffer.append(buffer);
            }
            inputStreamReader.close();
            bufferedReader.close();
            return stringBuffer.toString();
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void fileWrite(String address, Object j) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(address);
            fileOutputStream.write(j.toString().getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
