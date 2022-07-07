package top.houry.netty.barrage.utils;

import top.houry.netty.barrage.consts.BarrageMsgSensitiveUtilsConst;
import top.houry.netty.barrage.entity.BarrageMsgSensitive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarrageMsgSensitiveUtils {

    private static List<BarrageMsgSensitive> msgSensitives = new ArrayList<>(256);

    public static void setSensitiveWords(List<BarrageMsgSensitive> msgSensitives) {
        BarrageMsgSensitiveUtils.msgSensitives = msgSensitives;
    }

    private static final Map<Object, Object> sensitiveWordMap = new HashMap<>(256);

    private static void initSensitiveMsgMap() {
        Map<Object, Object> tempMap;
        Map<Object, Object> newMsgMap;
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
                    newMsgMap = new HashMap<>(2);
                    newMsgMap.put(BarrageMsgSensitiveUtilsConst.SENSITIVE_MSG_END_FLAG, false);
                    tempMap.put(keyChar, newMsgMap);
                    tempMap = newMsgMap;
                }
                if (i == key.length() - 1) {
                    tempMap.put(BarrageMsgSensitiveUtilsConst.SENSITIVE_MSG_END_FLAG, true);
                }
            }
        }
    }


    public static boolean contains(String txt) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = checkSensitiveMSg(txt, i);
            if (matchFlag > 0) {
                flag = true;
            }
        }
        return flag;
    }


    private static int checkSensitiveMSg(String txt, int beginIndex) {
        boolean flag = false;
        int matchFlag = 0;
        char word;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);
            if (nowMap != null) {
                matchFlag++;
                if ((Boolean) nowMap.get(BarrageMsgSensitiveUtilsConst.SENSITIVE_MSG_END_FLAG)) {
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


    public static List<String> getSensitiveMsg(String txt) {
        List<String> sensitiveWordList = new ArrayList<>();
        for (int i = 0; i < txt.length(); i++) {
            int length = checkSensitiveMSg(txt, i);
            if (length > 0) {
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1;
            }
        }
        return sensitiveWordList;
    }

    public static String replaceSensitiveMsg(String msg) {
        List<String> sensitiveMsgList = getSensitiveMsg(msg);
        sensitiveMsgList.forEach(sensitiveMsg -> {
            msg.replace(sensitiveMsg, "ss");
        });
        return msg;
    }

}
