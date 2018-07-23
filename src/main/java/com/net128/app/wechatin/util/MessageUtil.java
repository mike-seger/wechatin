package com.net128.app.wechatin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.net128.app.wechatin.domain.message.EncMessage;
import com.net128.app.wechatin.domain.message.Message;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class MessageUtil {
	private static Charset CHARSET = StandardCharsets.UTF_8;
	private byte[] aesKey;
	private String token;
	private String appId;

	public MessageUtil(String token, String encodingAesKey, String appId) {
		if (encodingAesKey.length() != 43) {
			throw new AesException(AesError.IllegalAesKey, null);
		}

		this.token = token;
		this.appId = appId;
		aesKey = Base64.getMimeDecoder().decode(encodingAesKey + "=");
	}

	private byte[] getNetworkBytesOrder(int sourceNumber) {
		byte[] orderBytes = new byte[4];
		orderBytes[3] = (byte) (sourceNumber & 0xFF);
		orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
		orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
		orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
		return orderBytes;
	}

	private int recoverNetworkBytesOrder(byte[] orderBytes) {
		int sourceNumber = 0;
		for (int i = 0; i < 4; i++) {
			sourceNumber <<= 8;
			sourceNumber |= orderBytes[i] & 0xff;
		}
		return sourceNumber;
	}

	public String getRandomStr() {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 16; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public String encrypt(String text) {
		return encrypt(getRandomStr(), text);
	}

	public String encrypt(String randomStr, String text) {
		ByteList byteCollector = new ByteList();
		byte[] randomStrBytes = randomStr.getBytes(CHARSET);
		byte[] textBytes = text.getBytes(CHARSET);
		byte[] networkBytesOrder = getNetworkBytesOrder(textBytes.length);
		byte[] appidBytes = appId.getBytes(CHARSET);

		byteCollector.addBytes(randomStrBytes);
		byteCollector.addBytes(networkBytesOrder);
		byteCollector.addBytes(textBytes);
		byteCollector.addBytes(appidBytes);

		byte[] padBytes = pkcs7Encode(byteCollector.size());
		byteCollector.addBytes(padBytes);

		byte[] unencrypted = byteCollector.toBytes();

		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

			byte[] encrypted = cipher.doFinal(unencrypted);

			String base64Encrypted = DatatypeConverter.printBase64Binary(encrypted);

			return base64Encrypted;
		} catch (Exception e) {
			throw new AesException(AesError.EncryptAESError, e);
		}
	}

	public String decrypt(String text) {
		byte[] original;
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
			cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

			byte[] encrypted = Base64.getMimeDecoder().decode(text);

			original = cipher.doFinal(encrypted);
		} catch (Exception e) {
			throw new AesException(AesError.DecryptAESError, e);
		}

		String xmlContent, from_appid;
		try {
			byte[] bytes = pkcs7Decode(original);
			byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);
			int xmlLength = recoverNetworkBytesOrder(networkOrder);
			xmlContent = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), CHARSET);
			from_appid = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length), CHARSET);
		} catch (Exception e) {
			throw new AesException(AesError.IllegalBuffer, e);
		}

		if (!from_appid.equals(appId)) {
			throw new AesException(AesError.ValidateAppidError, null);
		}
		return xmlContent;
	}

	public EncMessage encryptMessage(Message message, String timeStamp, String nonce) {
		return encryptMessage(getRandomStr(), message, timeStamp, nonce);
	}

	public EncMessage encryptMessage(String randomStr, Message message, String timeStamp, String nonce) {
		String encrypt = encrypt(randomStr, message.toXml());

		if (timeStamp == "") {
			timeStamp = Long.toString(System.currentTimeMillis());
		}

		String signature = getSHA1(token, timeStamp, nonce, encrypt);
		EncMessage encMessage=new EncMessage();
		encMessage.Encrypt=encrypt;
		encMessage.MsgSignature=signature;
		encMessage.Nonce=nonce;
		encMessage.TimeStamp=timeStamp;
		return encMessage;
	}

	public String decryptMessageXml(String messageXml) {
		return decryptMessage(new EncMessage().fromXml(messageXml));
	}

	public String decryptMessage(EncMessage message) {
		String result = decrypt(message.Encrypt);
		String signature = getSHA1(token, message.TimeStamp, message.Nonce, message.Encrypt);
		if (!signature.equals(message.MsgSignature)) {
			throw new AesException(AesError.ValidateSignatureError, null);
		}
		return result;
	}

	public String verifyUrl(String msgSignature, String timeStamp, String nonce, String echoStr) {
		String signature = getSHA1(token, timeStamp, nonce, echoStr);

		if (!signature.equals(msgSignature)) {
			throw new AesException(AesError.ValidateSignatureError, null);
		}

		String result = decrypt(echoStr);
		return result;
	}

	private String getSHA1(String token, String timestamp, String nonce, String encrypt) {
		try {
			String[] array = new String[] { token, timestamp, nonce, encrypt };
			StringBuffer sb = new StringBuffer();
			Arrays.sort(array);
			for (int i = 0; i < 4; i++) {
				sb.append(array[i]);
			}
			String str = sb.toString();
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());
			byte[] digest = md.digest();

			StringBuffer hexstr = new StringBuffer();
			String shaHex = "";
			for (int i = 0; i < digest.length; i++) {
				shaHex = Integer.toHexString(digest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexstr.append(0);
				}
				hexstr.append(shaHex);
			}
			return hexstr.toString();
		} catch (Exception e) {
			throw new AesException(AesError.ComputeSignatureError, e);
		}
	}

	private static byte[] pkcs7Encode(int count) {
		int BLOCK_SIZE = 32;
		int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
		if (amountToPad == 0) {
			amountToPad = BLOCK_SIZE;
		}
		char padChr = chr(amountToPad);
		String tmp = new String();
		for (int index = 0; index < amountToPad; index++) {
			tmp += padChr;
		}
		return tmp.getBytes(CHARSET);
	}

	private static byte[] pkcs7Decode(byte[] decrypted) {
		int pad = (int) decrypted[decrypted.length - 1];
		if (pad < 1 || pad > 32) {
			pad = 0;
		}
		return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
	}

	private static char chr(int a) {
		byte target = (byte) (a & 0xFF);
		return (char) target;
	}

	private class ByteList {
		ArrayList<Byte> byteContainer = new ArrayList<Byte>();

		public byte[] toBytes() {
			byte[] bytes = new byte[byteContainer.size()];
			for (int i = 0; i < byteContainer.size(); i++) {
				bytes[i] = byteContainer.get(i);
			}
			return bytes;
		}

		public ByteList addBytes(byte[] bytes) {
			for (byte b : bytes) {
				byteContainer.add(b);
			}
			return this;
		}

		public int size() {
			return byteContainer.size();
		}
	}

	@SuppressWarnings("serial")
	private class AesException extends RuntimeException {
		public AesException(AesError aesError, Throwable cause) {
			super(aesError.toString(), cause);
		}
	}

	private enum AesError {
		ValidateSignatureError(-40001),
		ParseXmlError(-40002),
		ComputeSignatureError(-40003),
		IllegalAesKey(-40004),
		ValidateAppidError(-40005),
		EncryptAESError(-40006),
		DecryptAESError(-40007),
		IllegalBuffer(-40008);

		private int code;
		AesError(int code) {
			this.code=code;
		}

		public String toString() {
			return name() + ": " + code;
		}
	}

	public static void main(String [] args) {
		if(args.length<4) {
			System.out.printf("Usage: %s <appid> <aeskey> <token> <message> [<timestamp> <nonce> <signature>]", MessageUtil.class.getSimpleName());
			System.exit(1);
		}
		String appId=args[0];
		String aesKey=args[1];
		String token=args[2];
		String message=args[3];
		EncMessage encMessage=new EncMessage().fromXml(message);
		if(args.length==7) {
			encMessage.TimeStamp=args[4];
			encMessage.Nonce=args[5];
			encMessage.MsgSignature=args[6];
		}
		MessageUtil messageUtil=new MessageUtil(token, aesKey, appId);
		System.out.println(messageUtil.decryptMessage(encMessage));
	}
}