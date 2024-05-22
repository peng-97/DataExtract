package com.example.shape.Util.encrypt;

import cn.com.westone.wcspsdk.AuthSecretParameterSpec;
import cn.com.westone.wcspsdk.CryptoServicePlatform;
import cn.com.westone.wcspsdk.WCSPException;
import cn.com.westone.wcspsdk.baseservice.co.COService;
import cn.com.westone.wcspsdk.baseservice.co.Decryptor;
import cn.com.westone.wcspsdk.baseservice.co.Encryptor;
import cn.com.westone.wcspsdk.baseservice.km.KMSecretKey;
import cn.com.westone.wcspsdk.baseservice.km.KMService;
import cn.com.westone.wcspsdk.typicalservice.RecordSecurityService;
import cn.com.westone.wcspsdk.util.ConversionUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WcspEncrypt {

    final static String realIp = "172.29.0.232";
    final static String host = "wcsp.k8s.dev";
    final static String url = String.format("https://%s:32443", host);
    final static String tenantId = "zuvdyvdrm2rc0ewc";
    final static String appId = "4f372913aece421dabd2da638bf3b1e8";
    final static String secret = "404452E20C480236AC6B250DEF7C8336";
    //    static final String path = "I:\\test\\new";
    final static String path = "/opt/projects/containers/file/wcspencrypt";

    public WcspEncrypt() {
        init();
    }


    public static void main(String[] args) {
    }

    String keyId = "00004395133755422801941";
    RecordSecurityService signService;
    COService coService;
    KMSecretKey kmSecretKey;
    byte[] iv = new byte[]{-115, 81, -108, 53, -29, 26, -56, 3, -51, -26, 100, 91, -42, -117, 16, 102};

    static {
        try {
            if (!new File(path).exists()) {
                new File(path).mkdirs();
            }
            String fileName = null;
            // 判断系统
            if ("linux".equalsIgnoreCase(System.getProperty("os.name"))) {
                fileName = "/etc/hosts";
            } else {
                fileName = "C://WINDOWS//system32//drivers//etc//hosts";
            }
            File source = new File(fileName);


            List<String> lines = null;
            lines = FileUtils.readLines(source);
            boolean had = false;
            for (String line : lines) {
                if (line != null && line.contains(realIp) && line.toLowerCase().contains(host)) {
                    had = true;
                    break;
                }
            }
            if (!had) {
                lines.add(String.format("%s %s", realIp, host));
                File dest = new File(fileName + "_" + System.currentTimeMillis());
                System.out.println("备份历史hosts文件：" + dest.toPath());
                // 备份hosts文件
                // 需管理员权限
                Files.copy(source.toPath(), dest.toPath());

                // 插入hosts配置

                FileUtils.writeLines(new File(fileName), lines);
            }
            System.out.println("国密密码池信息已配置到hosts文件");

        } catch (IOException e) {
            System.out.println("配置国密密码池信息到hosts文件失败");
            throw new RuntimeException(e);
        }
    }

    public void init() {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put(CryptoServicePlatform.INIT_PARAM_URL, url);
            params.put(CryptoServicePlatform.INIT_PARAM_WORKDIR, path);


            //            System.out.println("连接至国密密码池...");
            CryptoServicePlatform platform = CryptoServicePlatform.getInstance();
            platform.init(tenantId, appId, params);
            platform.authorize((new AuthSecretParameterSpec.Builder()).setSecret(secret).build());
            //            System.out.println("连接至国密密码池-签名服务...");
            signService = RecordSecurityService.getInstance(platform, keyId);

            //            System.out.println("连接至国密密码池-加解密服务...");
            KMService kmService = KMService.getInstance(platform);
            kmService.init();
            kmSecretKey = kmService.getSecretKey(keyId);
            coService = COService.getInstance(COService.SERVICE_TYPE_CO_REMOTE, platform);

            coService.init();

            //            System.out.print("连接至国密密码池完毕.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void finalize() {

        if (kmSecretKey != null) {
            kmSecretKey.close();
            kmSecretKey = null;
        }
    }

    /**
     * 数据签名
     *
     * @param text
     * @return
     */
    public String signDigest(String... text) {
        try {
            String value = signService.calculateMAC(text);
//            System.out.printf("进行国密数据签名：%s, 签名值: %s\n", Arrays.toString(text), value);
            return value;
        } catch (WCSPException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 数据签名验证
     *
     * @param signText
     * @param text
     * @return
     */
    public Boolean verifyDigest(String signText, String... text) {
        try {
            return signService.verifyMAC(signText, text);
        } catch (WCSPException e) {
            throw new RuntimeException(e);
        }
    }


    public String decrypt(Object text) {
        if (text == null) {
            return null;
        }
        return decrypt(text.toString());
    }

    public String decrypt(String text) {
        if (text == null || "".equals(text)|| "null".equals(text)) {
            return text;
        }
        try {
            try (Decryptor decryptor = coService.getDecryptor(kmSecretKey, iv,
                    COService.ALGORITHM_MODE_CBC, COService.ALGORITHM_PADDING_PKCS7)) {
                return ConversionUtils.Data.toUTF8String(decryptor.doFinal(ConversionUtils.Data.fromBase64String(text)));
            }
        } catch (Exception e) {
            System.out.println("解密失败: " + text);
            throw new RuntimeException("数据解密失败", e);
        }
    }

    public String encrypt(Object text) {
        if (text == null) {
            return null;
        }
        return encrypt(text.toString());
    }

    public String encrypt(String text) {
        if (text == null || "".equals(text)) {
            return text;
        }
        try {
            try (Encryptor encryptor = coService.getEncryptor(kmSecretKey, iv,
                    COService.ALGORITHM_MODE_CBC, COService.ALGORITHM_PADDING_PKCS7)) {
                return ConversionUtils.Data.toBase64String(encryptor.doFinal(ConversionUtils.Data.fromUTF8String(text)));
            }
        } catch (Exception e) {
            System.out.println("加密失败: " + text);
            e.printStackTrace();
            throw new RuntimeException("数据加密失败", e);
        }
    }

    /**
     * 加密数据表的数据
     */
    public void encryptDataList(Iterable<Map> dataList, String[] fields) {
        for (Map data : dataList) {
            for (String field : fields) {
                if (data.containsKey(field)) {
                    data.put(field, encrypt(data.get(field)));
                }
            }
        }
    }

    /**
     * 加密数据表的数据
     */
    public void encryptData(Map data, String[] fields) {
        for (String field : fields) {
            if (data.containsKey(field)) {
                data.put(field, encrypt(data.get(field)));
            }
        }
    }

    /**
     * 解密数据表的数据
     */
    public void decryptDataList(Iterable<Map> dataList, String[] fields) {
        for (Map data : dataList) {
            decryptData(data, fields);
        }
    }

    /**
     * 解密数据表的数据
     */
    public void decryptData(Map data, String[] fields) {
        for (String field : fields) {
            if (data.containsKey(field)) {
                data.put(field, decrypt(data.get(field)));
            }
        }
    }

    /**
     * 生成数据签名 - 数据库数据
     * 目前写入时是列名+列值 再计算md5值 最终对md5值进行签名
     */
    public String signData(Map data, String[] fields) {
        StringBuilder builder = new StringBuilder();
        for (String field : fields) {
            builder.append(field);
            builder.append(data.get(field) == null ? "" : data.get(field).toString());
        }
        return signDigest(md5(builder.toString()));
    }

    /**
     * 验证数据签名 - 数据库数据
     * 目前写入时是列名+列值 再计算md5值 最终对md5值进行签名
     */
    public boolean verifyData(Map data, String[] fields, String singValue) {
        return Objects.equals(signData(data, fields), singValue);
    }


    /**
     * 计算字符串的md5值
     * digest()最后确定返回md5 hash值，返回值为8位字符串。
     * 因为md5 hash值是16位的hex值，实际上就是8位的字符
     * BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
     * 一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
     *
     * @param text
     */
    private String md5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            System.out.println("生成md5值失败：" + text);
            throw new RuntimeException("生成md5值失败");
        }
    }

    public static Object getAttribute(Object o, String field) {
        try {
            Field f = o.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return f.get(o);
        } catch (Exception e) {
            //            e.printStackTrace();
            return null;
        }
    }

    public static void setAttribute(Object o, String field, Object value) {
        try {
            Field f = o.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(o, value);
        } catch (Exception e) {
            //            e.printStackTrace();
        }
    }

    public static Map<String, Object> objectToMap(Object object) {
        Map<String, Object> dataMap = new HashMap<>();
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                dataMap.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return dataMap;
    }
}
