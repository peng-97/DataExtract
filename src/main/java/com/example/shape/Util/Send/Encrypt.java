package com.example.shape.Util.Send;


import java.security.MessageDigest;


/**
 * 
* 功能说明：MD5加密
* @ClassName: Encrypt 
* @author 
* @created 2015年5月9日 下午3:48:30 
* @updated
 */
public class Encrypt
{

	public final static String MD5(String s)
	{
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	

	private final static int ENCODE_ID_INSERT_INDEX = 5; // 
	
	public final static String encodeId(int id)
	{
		String _id = String.valueOf(id);
		if (id < 0 || _id.length() > 9) return "";
		StringBuilder buf = new StringBuilder();
		buf.append(System.currentTimeMillis());
		for (int i = 0; i < _id.length(); i ++) {
			buf.insert(ENCODE_ID_INSERT_INDEX, _id.charAt(i));
		}
		buf.append(_id.length()); 
		return buf.toString();
	}
	

	public final static int unencodeId(String str)
	{
		String times = System.currentTimeMillis() + "";
		if (str == null || str.length() < times.length()) return 0;
		if (!str.matches("\\d{13,}")) return 0;
		int len = Integer.parseInt(str.substring(str.length() - 1));
		if (str.length() != (times.length() + len + 1)) return 0;
		return Integer.parseInt(new StringBuilder(str.substring(
				ENCODE_ID_INSERT_INDEX, ENCODE_ID_INSERT_INDEX + len)).reverse()
				.toString());
	}

	public static void main(String[] args)
	{
//		StringUtil.debug(MD5("000000"));
//		String tmp = encodeId(13455787);
//		StringUtil.debug(tmp);
//		StringUtil.debug(unencodeId(tmp));
		System.out.println(MD5("123456"));
	}
}
