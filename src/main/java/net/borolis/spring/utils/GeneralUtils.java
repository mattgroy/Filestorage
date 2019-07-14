package net.borolis.spring.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.uuid.Uuids;

/**
 * Класс со вспомогательными методами общего назначения
 *
 * @author mratkov
 * @since 13 июля, 2019
 */

public class GeneralUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralUtils.class);

    private static MessageDigest digestSHA256;

    static
    {
        try
        {
            digestSHA256 = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Генерация time-based UUID
     *
     * @return UUID
     */
    public static UUID generateUUID()
    {
        return Uuids.timeBased();
    }

    /**
     * Хеширование с помощью SHA256
     *
     * @param bytes Массив байтов, который нужно захешировать
     * @return Хеш данного масива байтов
     */
    public static String getSHA256Hash(byte[] bytes)
    {
        return bytesToHex(digestSHA256.digest(bytes));
    }

    /**
     * Перевод массива байтов в hex-строку
     *
     * @param bytes Массив байтов
     * @return Hex-строка
     */
    public static String bytesToHex(byte[] bytes)
    {
        StringBuffer hexString = new StringBuffer();
        for (byte aByte : bytes)
        {
            String hex = Integer.toHexString(0xff & aByte);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
