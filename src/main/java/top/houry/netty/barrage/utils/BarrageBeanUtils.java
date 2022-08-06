package top.houry.netty.barrage.utils;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @Desc
 * @Author houruiyang
 * @Date 2021/9/8
 **/
public class BarrageBeanUtils extends BeanUtils {

    /**
     * List拷贝工具
     *
     * @param sources 源集合
     * @param target 目标集合
     * @param <S>  源集合
     * @param <T> 目标集合
     * @return 拷贝结果
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            list.add(t);
        }
        return list;
    }

    /**
     * List拷贝工具
     *
     * @param sources 源集合
     * @param target 目标集合
     * @param <S>  源集合
     * @param <T> 目标集合
     * @return 拷贝结果
     */
    public static <S, T> List<T> copyListPropertiesOfNullable(List<S> sources, Supplier<T> target) {
        if (CollectionUtil.isEmpty(sources)){
            sources = new ArrayList<>();
        }
        return copyListProperties(sources, target);
    }
}