package hashnode.cli.credentials;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class ApiKeysManager {

    public static Key generateKey(String password) {
        return new SecretKeySpec(password.getBytes(), "AES");
    }

    public static String encrypt(String apiKey, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encValue = cipher.doFinal(apiKey.getBytes(StandardCharsets.UTF_8));
        // the .doFinal() output is unprintable, so we need to encode the result in base64 to be able
        // to save the content in a text file
        byte[] encryptedValue = Base64.getEncoder().encode(encValue);

        return new String(encryptedValue, StandardCharsets.UTF_8);
    }

    public static String decrypt(String apiKey, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decValue = cipher.doFinal(Base64.getDecoder().decode(apiKey));

        return new String(decValue, StandardCharsets.UTF_8);
    }
}