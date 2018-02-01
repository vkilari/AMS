package com.mobisolutions.ams.utils;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by vkilari on 7/17/17.
 */

public class Crypto {

    private static final String LOG_TAG = Crypto.class.getSimpleName();
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String ENCRYPTION = "AES";
    private static final String FILENAME = "fknjew.kdf";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int KEY_LENGTH = 256;
    private static final int IV_LENGTH = 16;

    private SecretKey mSecretKey;
    private Context mContext;

    public Crypto(final Context context) throws NoSuchAlgorithmException, IOException {

        mContext = context;
        mSecretKey = getSecretKey();
    }

    /**
     * Encrypts a given {@link java.lang.String}
     *
     * @param text {@link java.lang.String} to be encrypted
     * @return encrypted {@link java.lang.String}
     */
    public String encrypt(final String text) throws GeneralSecurityException {

        final byte[] iv = generateIv();
        final IvParameterSpec ivspec = new IvParameterSpec(iv);

        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, mSecretKey, ivspec);

        byte[] ciphertext;
        try {
            ciphertext = cipher.doFinal(text.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            ciphertext = cipher.doFinal(text.getBytes());
        }

        return String.format("%s:%s", bytesToStorableString(iv), bytesToStorableString(ciphertext));
    }

    /**
     * Decrypts a given {@link java.lang.String}
     *
     * @param encrypted {@link java.lang.String} to be decrypted
     * @return decrypted {@link java.lang.String}
     */
    public String decrypt(final String encrypted) {
        try {
            return new String(decryptToBytes(mSecretKey, encrypted), "utf-8");
        } catch (final UnsupportedEncodingException e) {
            // Should never happen since the supported encoding types should never change on the
            // device
            throw new RuntimeException(e);
        }
    }

    private SecretKey getSecretKey() throws NoSuchAlgorithmException, IOException {

        if (mSecretKey == null) {

            try {
                mSecretKey = readSecretFromLocalStorage();
            } catch (final FileNotFoundException e) {
                // Likely didn't save this yet or it was deleted, so just print it and continue
                Log.e(LOG_TAG, "error retrieving secret key", e);
            }

            if (mSecretKey == null) {
                mSecretKey = generateNewKey();
                writeSecretToLocalStorage(mSecretKey);
            }
        }

        return mSecretKey;
    }

    private SecretKey generateNewKey() throws NoSuchAlgorithmException {

        // Generate a 256-bit key
        final int outputKeyLength = KEY_LENGTH;

        // Do *not* seed secureRandom! Automatically seeded from system entropy.
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPTION);
        keyGenerator.init(outputKeyLength, SECURE_RANDOM);

        return keyGenerator.generateKey();
    }

    private void writeSecretToLocalStorage(SecretKey secretKey) throws IOException {

        final FileOutputStream fos = mContext.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        fos.write(secretKey.getEncoded());
        fos.close();
    }

    private SecretKey readSecretFromLocalStorage() throws IOException {

        final FileInputStream fis = mContext.openFileInput(FILENAME);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int bytesRead;
        final byte[] buffer = new byte[8192];
        while ((bytesRead = fis.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        final byte[] bytes = baos.toByteArray();
        baos.close();
        fis.close();

        return new SecretKeySpec(bytes, 0, bytes.length, ENCRYPTION);
    }

    private byte[] generateIv() {

        final byte[] bytes = new byte[IV_LENGTH];
        SECURE_RANDOM.nextBytes(bytes);

        return bytes;
    }

    private byte[] decryptToBytes(final SecretKey key, final String ciphertext) {

        try {

            final String[] parts = ciphertext.split(":");
            final String iv = parts[0];
            final String ctext = parts[1];

            final IvParameterSpec ivspec = new IvParameterSpec(bytesFromStorableString(iv));

            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, ivspec);

            return cipher.doFinal(bytesFromStorableString(ctext));

        } catch (final GeneralSecurityException e) {

            Log.e(LOG_TAG,
                    String.format("Unable to decrypt data: %s : %s",
                            bytesToStorableString(key.getEncoded()), ciphertext), e);

            return new byte[0];

        } catch (final RuntimeException e) {
            return new byte[0];
        }
    }

    private byte[] bytesFromStorableString(final String string) {
        return Base64.decode(string, Base64.DEFAULT);
    }

    private String bytesToStorableString(final byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
