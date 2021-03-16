package cn.goitman.util;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
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
 * @className CreateRsaSecrteKey
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 生成RSA公钥私钥，加解密
 * @date 2020/11/2 15:51
 */

public class CreateRsaSecrteKeyUtil {

    public static final String KEY_ALGORITHM = "RSA";   // 算法名称
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA"; // 加签算法
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    // 加密数据和秘钥的编码方式
    public static final String UTF_8 = "UTF-8";


    /**
     * map对象中存放公私钥
     */
    public static Map<String, Object> initKey() throws Exception {
        //获得对象 KeyPairGenerator 参数 RSA
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // keysize：表示的是生成key的长度，单位字节(bits)
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
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 获得私钥
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        //获得map中的私钥对象 转为key对象
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }

    /**
     * @param [key 字符串]
     * @return byte[]
     * @method decryptBASE64
     * @description 解码返回byte
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * @param [key 字节数组]
     * @return java.lang.String
     * @method encryptBASE64
     * @description 编码返回字符串
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * @param [publicKeyStr 公钥, data 需加密数据]
     * @return java.lang.String
     * @method encryptRSADate
     * @description 加密数据
     */
    public static String encryptRSADate(String publicKeyStr, String data) {
        try {
            byte[] keyBytes = decryptBASE64(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return new String(Base64.encodeBase64(cipher.doFinal(data.getBytes(UTF_8))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "加密失败！";
    }

    /**
     * @param [privateKeyStr 私钥, data 加密数据]
     * @return java.lang.String
     * @method decryptRSADate
     * @description 解密数据
     */
    public static String decryptRSADate(String privateKeyStr, String data) {
        try {
            // String转私钥对象
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            byte[] privateKeyArray = privateKeyStr.getBytes();
            byte[] dataArray = data.getBytes();
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyArray));
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            // 获取加解密对象
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            // 初始化加解密对象为解密模式
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(Base64.decodeBase64(dataArray)), UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "解密失败！";
    }

    /**
     * @param [data 原字节数组, privateKeyStr 私钥]
     * @return byte[]
     * @method sign
     * @description 签名
     */
    public static String sign(byte[] data, String privateKeyStr) {
        try {
            // String转私钥对象
            byte[] keyBytes = decryptBASE64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            // 获取加签对象
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initSign(privateKey);
            sig.update(data);
            return encryptBASE64(sig.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param [data 原字节数组, sign 签名后字节数组, publicKeyStr 公钥]
     * @return boolean
     * @method verify
     * @description 验签
     */
    public static boolean verify(byte[] data, String sign, String publicKeyStr) {
        try {
            // String转公钥对象
            byte[] keyBytes = decryptBASE64(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // 获取加签对象
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initVerify(publicKey);
            sig.update(data);
            return sig.verify(decryptBASE64(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
            String encryptData = encryptRSADate(publicKey, str);
            System.out.println("公钥加密：" + encryptData + "\n");
            // 对加密信息，Base64解码
            byte[] encryptBase64 = decryptBASE64(encryptData);
            // 私钥对密文加签
            String sign = sign(encryptBase64, privateKey);
            System.out.println("加签后：" + sign + "\n");
            // 公钥对密文验签
            boolean verify = verify(encryptBase64, sign, publicKey);
            System.out.println("验签情况：" + verify + "\n");

            // 私钥解密
            String decryptData = decryptRSADate(privateKey, encryptData);
            System.out.println("私钥解密：" + decryptData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
