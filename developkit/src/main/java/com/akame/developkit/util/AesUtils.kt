package com.akame.developkit.util

import android.util.Base64
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

object AesUtils {
    /**
     * 生成密钥
     *
     * @return
     */
    fun genSecretKey(): String? {
        return try {
            val kg: KeyGenerator = KeyGenerator.getInstance("AES").apply {
                //要生成多少位，只需要修改这里即可128, 192或256
                init(128)
            }
            val b: ByteArray = kg.generateKey().encoded
            byteToHexString(b)
        } catch (e: NoSuchAlgorithmException) {
            throw java.lang.RuntimeException(e)
        }
    }

    /**
     * byte数组转化为16进制字符串
     *
     * @param bytes
     * @return
     */
    fun byteToHexString(bytes: ByteArray): String? {
        val sb = StringBuffer()
        for (i in bytes.indices) {
            val strHex = Integer.toHexString(bytes[i].toInt())
            if (strHex.length > 3) {
                sb.append(strHex.substring(6))
            } else {
                if (strHex.length < 2) {
                    sb.append("0$strHex")
                } else {
                    sb.append(strHex)
                }
            }
        }
        return sb.toString()
    }

    /**
     * 加密
     *
     * @param data
     * @param secretKey
     * @return
     */
    fun encrypt(data: String, secretKey: String): String? {
        return try {
            val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding") // 加密算法/工作模式/填充方式
            val dataBytes = data.toByteArray(charset("UTF-8"))
            cipher.init(
                Cipher.ENCRYPT_MODE,
                SecretKeySpec(secretKey.toByteArray(charset("UTF-8")), "AES")
            )
            val result: ByteArray = cipher.doFinal(dataBytes)
            Base64.encodeToString(result, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 解密
     *
     * @param encryptedDataBase64
     * @param securityKey
     * @return
     */
    fun decrypt(encryptedDataBase64: String?, securityKey: String): String? {
        return try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding") // 加密算法/工作模式/填充方式
            val dataBytes: ByteArray = Base64.decode(encryptedDataBase64, Base64.NO_WRAP)
            cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(securityKey.toByteArray(charset("UTF-8")), "AES")
            )
            val result = cipher.doFinal(dataBytes)
            String(result)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }
}