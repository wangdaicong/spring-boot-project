package cn.goitman.util;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nicky
 * @version 1.0
 * @className CreateRsaSecrteKeyUtil
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 生成RSA公钥私钥，RSA与AES加解密
 * @date 2020/11/2 15:51
 */

public class CreateRsaSecrteKeyUtil {

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    // RSA算法名称
    public static final String RSA_ALGORITHM = "RSA";

    // RSA填充算法
    public static final String RSA_PADDING_ALGORITHM = "RSA/ECB/PKCS1Padding";
//    public static final String RSA_PADDING_ALGORITHM = "RSA/CBC/PKCS1Padding";

    // RSA加签算法
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    // RSA最大加密明文大小
    private static final int MAX_ENCRYPT_BLOCK = 117;

    // RSA最大解密密文大小
    private static final int MAX_DECRYPT_BLOCK = 128;

    // 编码方式
    public static final String UTF_8 = "UTF-8";

    // AES算法名称
    public static final String AES_ALGORITHM = "AES";

    // AES填充算法
//    public static final String AES_PADDING_ALGORITHM = "AES/ECB/PKCS5Padding";
    public static final String AES_PADDING_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     只有在填充算法模式为CBC时才使用
     初始向量，不可以为32位，只能为数字或字母
     否则异常java.security.InvalidAlgorithmParameterException: Wrong IV length: must be 16 bytes long
     */
    private final static String iv = "初始向量";


    /**
     * map对象中存放公私钥
     */
    public static Map<String, Object> initKey() throws Exception {
        //获得对象 KeyPairGenerator 参数 RSA
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        // keysize：表示的是生成key的长度，单位字节(bits，64的整数倍,最小512位)
        // 如果采用2048，上面最大加密和最大解密则为:245、256
        keyPairGen.initialize(1024);
        //通过对象 KeyPairGenerator 获取对象KeyPair
        KeyPair keyPair = keyPairGen.generateKeyPair();

        //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //公私钥对象存入map中
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 获得公钥
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        //获得map中的公钥对象 转为key对象
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 获得私钥
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        //获得map中的私钥对象 转为key对象
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * @param [key 字符串]
     * @return byte[]
     * @method decryptBASE64
     * @description BASE64字符串解码为二进制数据
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * @param [key 字节数组]
     * @return java.lang.String
     * @method encryptBASE64
     * @description 二进制数据编码为BASE64字符串
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * @param [data 需加密数据, publicKey 公钥]
     * @return java.lang.String
     * @method encryptRSADatePub
     * @description 分段公钥加密数据
     */
    public static String encryptRSADate(String data, String publicKey) {
        try {
            byte[] dataByte = data.getBytes();
            byte[] keyBytes = decryptBASE64(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(RSA_PADDING_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicK);

//            IvParameterSpec ivp = new IvParameterSpec(iv.getBytes());
//            cipher.init(Cipher.ENCRYPT_MODE, publicK, ivp);

            // 分段加密
            int inputLen = dataByte.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(dataByte, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataByte, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return encryptBASE64(encryptedData);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * @param [data 需解密数据, privateKey 私钥]
     * @return java.lang.String
     * @method decryptRSADate
     * @description 分段解密数据
     */
    public static String decryptRSADate(String data, String privateKey) {
        try {
            byte[] dataByte = decryptBASE64(data);
            byte[] keyBytes = decryptBASE64(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(RSA_PADDING_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateK);

//            IvParameterSpec ivp = new IvParameterSpec(iv.getBytes());
//            cipher.init(Cipher.DECRYPT_MODE, privateK, ivp);

            // 分段解密
            int inputLen = dataByte.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher
                            .doFinal(dataByte, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher
                            .doFinal(dataByte, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * @param [data 签名数据, privateKey 私钥]
     * @return String
     * @method sign
     * @description 签名
     */
    public static String sign(String data, String privateKey) {
        try {
            // String转私钥对象
            byte[] keyBytes = decryptBASE64(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PrivateKey privateK = keyFactory.generatePrivate(keySpec);

            // 获取加签对象
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initSign(privateK);
            sig.update(data.getBytes(UTF_8));
            return encryptBASE64(sig.sign());
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * @param [data 验签数据, sign 签名, publicKey 公钥]
     * @return boolean
     * @method verify
     * @description 验签
     */
    public static boolean verify(String data, String sign, String publicKey) {
        try {
            // String转公钥对象
            byte[] keyBytes = decryptBASE64(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PublicKey publicK = keyFactory.generatePublic(keySpec);

            // 获取加签对象
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initVerify(publicK);
            sig.update(data.getBytes(UTF_8));
            return sig.verify(decryptBASE64(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @method  encryptAES
     * @description  AES 加密
     * @param  [context 明文, key 密钥8位的倍数，建议16位]
     * @return  java.lang.String
     */
    public static String encryptAES(String context, String key) {
        try {
            byte[] decode = context.getBytes(UTF_8);
            Key secretKeySpec = new SecretKeySpec(key.getBytes(UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_PADDING_ALGORITHM);
//            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            IvParameterSpec ivp = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivp);

            return Base64.encodeBase64String(cipher.doFinal(decode));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * @method  decryptAES
     * @description  AES 解密
     * @param  [context 密文, key 密钥8位的倍数，建议16位]
     * @return  java.lang.String
     */
    public static String decryptAES(String context, String key) {
        try {
            byte[] decode = Base64.decodeBase64(context);
            Key secretKeySpec = new SecretKeySpec(key.getBytes(UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_PADDING_ALGORITHM);
//            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            IvParameterSpec ivp = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivp);
            return new String(cipher.doFinal(decode), UTF_8);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * @method randomKey
     * @description 创建字符串密钥
     */
    public static String randomKey(int lenght) {
        String str = "";
        char[] c = null;
        for (short i = '0'; i <= '9'; i++) {
            str += (char) i;
        }
        for (short i = 'A'; i <= 'Z'; i++) {
            str += (char) i;
        }
        for (short i = 'a'; i <= 'z'; i++) {
            str += (char) i;
        }

        c = new char[lenght];
        for (int i = 0; i < c.length; i++) {
            int index = (int) (Math.random() * str.length());
            c[i] = str.charAt(index);
        }
        System.out.println("密钥：" + new String(c) + "\n");

        return new String(c);
    }


    public static void main(String[] args) {
        Map<String, Object> keyMap;
        try {
            keyMap = initKey();
            String publicKey = getPublicKey(keyMap);
            System.out.println("公钥：" + publicKey + "\n");
            String privateKey = getPrivateKey(keyMap);
            System.out.println("私钥：" + privateKey + "\n");

            System.out.println("===================先加密再加签===================");
            String str = "签名内容！";
            // 公钥加密
            String encryptData = encryptRSADate(str, publicKey);
            System.out.println("公钥加密：" + encryptData + "\n");
            // 私钥对密文加签
            String sign = sign(encryptData, privateKey);
            System.out.println("加签后：" + sign + "\n");
            // 公钥对密文验签
            boolean verify = verify(encryptData, sign, publicKey);
            System.out.println("验签情况：" + verify + "\n");

            // 私钥解密
            String decryptData = decryptRSADate("WXpNTk5yTU90NHJiQWwyZlBzNnJKdC9ZWk80YUJyWiswS1orUjR1clYxa2JuVVJRNnN0eUh4ekNVQlM0cFUxK3R6dU1PYVlsZkZQM2ZHaHB4VXNPbFRRbXZwWithUjVVZWtLVWRic2hqV0FweUpkNDZaLy9XTnVLMk9uYmhTMGdWMkZIK1NHUlc4L0dnR2ZPdmpOVjJrYmkzS25aQWdkUFRKT3NwTlowQ0owPQ==", privateKey);
            System.out.println("私钥解密：" + decryptData);

            System.out.println(randomKey(16));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
