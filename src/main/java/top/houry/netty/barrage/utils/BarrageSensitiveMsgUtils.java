package top.houry.netty.barrage.utils;

import top.houry.netty.barrage.config.BarrageServerRunner;
import top.houry.netty.barrage.entity.BarrageMsgSensitive;

import java.util.*;

public class BarrageSensitiveMsgUtils {

    private static List<BarrageMsgSensitive> msgSensitives = new ArrayList<>(256);

    public static void setSensitiveWords(List<BarrageMsgSensitive> msgSensitives) {
        BarrageSensitiveMsgUtils.msgSensitives = msgSensitives;
    }

    private static final Map<Object, Object> sensitiveWordMap = new HashMap<>(256);


    public static void main(String[] args) {
        List<BarrageMsgSensitive> SENSITIVE_WORDS = new ArrayList<>();
        BarrageMsgSensitive a= new BarrageMsgSensitive();
        a.setSensitiveMsg("小日本");
        a.setShowMsg("八格牙路");
        BarrageMsgSensitive b= new BarrageMsgSensitive();

        b.setSensitiveMsg("我是你爸爸");
        b.setShowMsg("哈哈哈");
        SENSITIVE_WORDS.add(a);
        SENSITIVE_WORDS.add(b);


        setSensitiveWords(SENSITIVE_WORDS);


        initSensitiveWordMap();
        System.out.println(getSensitiveWord("卖淫嫖娼select杀人犯法共小日本产党国民党select法轮大法"));
    }


    private static void initSensitiveWordMap() {
        Map<Object, Object> tempMap;
        Map<Object, Object> newWorMap;
        for (BarrageMsgSensitive word : msgSensitives) {
            tempMap = sensitiveWordMap;
            String key = word.getSensitiveMsg();
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);
                Object wordMap = tempMap.get(keyChar);
                //如果存在该key,直接赋值,用于下一个循环获取
                if (wordMap != null) {
                    tempMap = (Map) wordMap;
                } else {
                    newWorMap = new HashMap<>(2);
                    newWorMap.put("isEnd", false);
                    tempMap.put(keyChar, newWorMap);
                    tempMap = newWorMap;
                }
                if (i == key.length() - 1) {
                    tempMap.put("isEnd", true);
                }
            }
        }
    }


    public static boolean contains(String txt) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = checkSensitiveWord(txt, i);
            if (matchFlag > 0) {
                flag = true;
            }
        }
        return flag;
    }


    private static int checkSensitiveWord(String txt, int beginIndex) {
        boolean flag = false;
        int matchFlag = 0;
        char word;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);
            if (nowMap != null) {
                matchFlag++;
                if ((Boolean) nowMap.get("isEnd")) {
                    flag = true;
                }
            } else {
                break;
            }
        }
        if (matchFlag < 2 || !flag) {
        }
        return matchFlag;
    }


    public static List<String> getSensitiveWord(String txt) {
        List<String> sensitiveWordList = new ArrayList<>();
        for (int i = 0; i < txt.length(); i++) {
            int length = checkSensitiveWord(txt, i);
            if (length > 0) {
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1;
            }
        }
        return sensitiveWordList;
    }


}
