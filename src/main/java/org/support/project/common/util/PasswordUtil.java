package org.support.project.common.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * パスワードのエンコード／デコードに利用するユーティリティ
 * 
 * CBC（IVを使用）イニシャルバリュー（IV(アイブイ)）は使っていない。
 * 
 * @author koda
 *
 */
public class PasswordUtil {

	private static final String CIPHER_ALGORITHM = "AES";

	// private static final String CIPHER_TRANSFORMATION = CIPHER_ALGORITHM + "/CBC/PKCS5Padding";

	private static byte[] generatSecretKey(String key) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		byte[] hash = digest.digest(key.getBytes());
		return hash;
	}
	private static byte[] sha256(String key) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(key.getBytes());
		return hash;
	}

	private static Key makeKey(byte[] secret_key) {
		return new SecretKeySpec(secret_key, CIPHER_ALGORITHM);
	}

	/**
	 * 暗号化
	 * 
	 * @param string
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static final String encrypt(String string, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		if (string == null) {
			return null;
		}
		Key secretKey = makeKey(generatSecretKey(key));

		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] bytes = cipher.doFinal(string.getBytes());

		return Base64Utils.toBase64(bytes);
	}

	/**
	 * 複合化
	 * 
	 * @param string
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static final String decrypt(String string, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		if (string == null) {
			return null;
		}
		Key secretKey = makeKey(generatSecretKey(key));

		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);

		byte[] bytes = Base64Utils.fromBase64(string);
		byte[] dec = cipher.doFinal(bytes);
		return new String(dec);
	}

	/**
	 * 文字列をハッシュ文字列にする
	 * 
	 * @param string
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static String hash(String string) throws NoSuchAlgorithmException {
		byte[] bytes = sha256(string);
		return Base64Utils.toBase64(bytes);
	}

	public static String getSalt() {
		String randam = RandomUtil.randamGen(254);
		return randam;
	}
	
	/**
	 * パスワード用のハッシュを生成
	 * @param password
	 * @param salt
	 * @param hashIterations
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getStretchedPassword(String password, String salt, int hashIterations) throws NoSuchAlgorithmException {
		String hash = "";
		for (int i = 0; i < hashIterations; i++) {
			hash = hash(hash + salt + password);
		}
		return hash;
	}
}
