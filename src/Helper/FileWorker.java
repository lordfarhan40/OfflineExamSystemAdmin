package Helper;

import Model.AnswerSheet;
import Model.MainExam;
import Model.MasterAnswerSheet;
import com.google.gson.Gson;

import java.io.*;

/**
 * Created by Farhan on 24-07-2015.
 * This is a custom made Filereader and writer for this project only!
 */
public class FileWorker {

    public static String PASSWORD_PROTECTED = "SetPassword=true";
    public static String NOT_PASSWORD_PROTECTED = "SetPassword=false";

    //Read text from file, under Test
    public static MainExam readFromFile(File f, String password) {
        try {
            Gson gson = new Gson();
            FileInputStream fin = new FileInputStream(f);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
            String passwordChecker = reader.readLine();
            if (passwordChecker.equals(PASSWORD_PROTECTED)) {
                String appender = new String();
                StringBuffer stringBuffer = new StringBuffer();
                while ((appender = reader.readLine()) != null) {
                    stringBuffer.append(appender);
                }
                String decrypted = EncryptDecrypter.decrypt(stringBuffer.toString(), password);
                reader.close();
                return gson.fromJson(decrypted, MainExam.class);
            } else {
                String appender = new String();
                StringBuffer stringBuffer = new StringBuffer();
                while ((appender = reader.readLine()) != null) {
                    stringBuffer.append(appender);
                }
                reader.close();
                return gson.fromJson(stringBuffer.toString(), MainExam.class);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Write the MainExam to file
    public static boolean writeToFile(MainExam mainExam, File f, String password) {
        try {
            Gson gson = new Gson();
            FileOutputStream fout = new FileOutputStream(f);
            String gsonString = new String();
            //if encrypted needed
            if (password != null) {
                //Encrypt the file
                fout.write((PASSWORD_PROTECTED + "\n").getBytes());
                gsonString = gson.toJson(mainExam);
                gsonString = EncryptDecrypter.encrypt(gsonString, password);
            } else {
                fout.write((NOT_PASSWORD_PROTECTED + "\n").getBytes());
                gsonString = gson.toJson(mainExam);
            }
            fout.write(gsonString.getBytes());
            fout.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Generic Method to readFile and  return String
    public static String readText(File f) {
        if (f.exists()) {
            try {
                FileInputStream fin = new FileInputStream(f);
                byte[] readBytes = new byte[fin.available()];
                fin.read(readBytes);
                fin.close();
                return new String(readBytes);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }
        return null;
    }

    //Read first line of file
    public static String readFirst(File f) {
        if (f.exists()) {
            try {
                FileInputStream fin = new FileInputStream(f);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
                String string = reader.readLine();
                reader.close();
                return string;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static MasterAnswerSheet readMasterAnswerSheet(File f) {
        try {
            Gson gson = new Gson();
            FileInputStream fin = new FileInputStream(f);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
            String appender = new String();
            StringBuffer stringBuffer = new StringBuffer();
            while ((appender = reader.readLine()) != null) {
                stringBuffer.append(appender);
            }
            reader.close();
            return gson.fromJson(stringBuffer.toString(), MasterAnswerSheet.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean writeMasterAnswerSheet(MasterAnswerSheet masterAnswerSheet, File f) {
        try {
            Gson gson = new Gson();
            FileOutputStream fout = new FileOutputStream(f);
            String gsonString = new String();
            gsonString = gson.toJson(masterAnswerSheet);
            fout.write(gsonString.getBytes());
            fout.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void writeFile(File file, String string) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(string.getBytes());
            fileOutputStream.close();
        } catch (Exception exception) {
        }
    }

    public static AnswerSheet readAnswerSheet(File f) {
        try {
            Gson gson = new Gson();
            String stringAnswer = readText(f);
            return gson.fromJson(stringAnswer, AnswerSheet.class);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
