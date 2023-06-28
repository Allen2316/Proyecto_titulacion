package com.mpt.security

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Descifrado y cifrado de cadenas de texto con el algoritmo de cifrado seguro AES (Advanced
 * Encryption Standard) que es un algoritmo de cifrado simétrico. Esta clase se utiliza para encriptar
 * y desecriptar cadenas de texto.
 * 
 * @author ajcm
 * @version 1.0
 */
class CodeAndDecode {

	private static final Logger logger = Logger.getLogger(CodeAndDecode.class.toString())

	private static final String CHARSET_NAME = "UTF-8";
	private static final String DIGEST_ALGORITHM = "SHA-1";
	private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
	private static final String ALGORITHM = "AES";
	private static final String SECRET_KEY = "theSecretKey";
	private static SecretKeySpec secretKeySpec;
	private static byte[] key;

	/**
	 * Permite establecer una clave secreta
	 * 
	 * @param myKey
	 */
	private static void setKey(String myKey) {
		MessageDigest sha = null;
		try {
			key = myKey.getBytes(CHARSET_NAME);
			sha = MessageDigest.getInstance(DIGEST_ALGORITHM);
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKeySpec = new SecretKeySpec(key, ALGORITHM);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			logger.severe("Se ha producido un error con el algoritmo o la codificación de caracteres: "	+ e.toString());
		}
	}

	/**
	 * Permite encriptar una cadena de caracteres
	 * 
	 * @param stringToEncrypt
	 * @return String
	 */
	static String encrypt(String stringToEncrypt) {
		try {
			setKey(SECRET_KEY);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			return Base64.getEncoder()
					.encodeToString(cipher.doFinal(stringToEncrypt.getBytes(CHARSET_NAME)));
		} catch (Exception e) {
			logger.severe("Se ha producido un error al encriptar: " + e.toString());
		}
		return null;
	}

	/**
	 * Permite desencriptar una cadena de caracteres
	 * 
	 * @param stringToDecrypt
	 * @return String
	 */
	static String decrypt(String stringToDecrypt) {
		try {
			setKey(SECRET_KEY);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			
			logger.info(new String(cipher.doFinal(Base64.getDecoder().decode(stringToDecrypt))));
			
			
			return new String(cipher.doFinal(Base64.getDecoder().decode(stringToDecrypt)));
		} catch (Exception e) {
			logger.severe("Se ha producido un error al desencriptar: " + e.toString());
		}
		return null;
	}
}
