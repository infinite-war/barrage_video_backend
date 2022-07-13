package top.houry.netty.barrage.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * @Desc redis 工具类
 * @Author houry
 * @Date 2021/3/29 14:10
 **/
public class BarrageRedisUtils {

    private static StringRedisTemplate redisTemplate;

    public static void setStringRedisTemplate(StringRedisTemplate redisTemplate) {
        BarrageRedisUtils.redisTemplate = redisTemplate;
    }

    /**
     * 存放数据集合
     *
     * @param key   数据key
     * @param value 数据值
     */
    public static void listPush(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 获取所有的集合数据
     *
     * @param key 指定的RedisKey
     * @returnK 数据集合
     */
    public static List<String> listGetAll(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 存放hash数据
     *
     * @param key   key
     * @param hKey  hKey
     * @param value value
     */
    public static void hashPut(String key, String hKey, String value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    public static String hashGet(String key, String hKey) {
        return (String) redisTemplate.opsForHash().get(key, hKey);
    }

    /**
     * 给指定的key的数值增加1
     *
     * @param key key
     */
    public static void increment(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    /**
     * 给指定的key的数值减少1
     *
     * @param key key
     */
    public static void decrement(String key) {
        redisTemplate.opsForValue().decrement(key);
    }

    /**
     * 根据key获取value值
     *
     * @param key key
     * @return value值
     */
    public static String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

}
