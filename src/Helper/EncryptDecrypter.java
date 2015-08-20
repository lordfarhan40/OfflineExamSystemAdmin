package Helper;

import org.jasypt.util.text.StrongTextEncryptor;

import java.lang.reflect.Field;

/**
 * Created by Farhan on 24-07-2015.
 */
public class EncryptDecrypter {
    public static String encrypt(String unencryptedStuff, String password) {
        //Fixing the exception with this code
        try {
            Field field = Class.forName("javax.crypto.JceSecurity").
                    getDeclaredField("isRestricted");
            field.setAccessible(true);
            field.set(null, java.lang.Boolean.FALSE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //End of fixed code
        StrongTextEncryptor encrypter = new StrongTextEncryptor();
        encrypter.setPassword(password);
        return encrypter.encrypt(unencryptedStuff);
    }

    public static String decrypt(String encryptedStuff, String password) {
        //Fixing the exception with this code
        try {
            Field field = Class.forName("javax.crypto.JceSecurity").
                    getDeclaredField("isRestricted");
            field.setAccessible(true);
            field.set(null, java.lang.Boolean.FALSE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //End of fixed code
        StrongTextEncryptor decrypter = new StrongTextEncryptor();
        decrypter.setPassword(password);
        return decrypter.decrypt(encryptedStuff);

    }
}
