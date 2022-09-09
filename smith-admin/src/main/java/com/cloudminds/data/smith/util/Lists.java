package com.cloudminds.data.smith.util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 集合常用工具类
 *
 * @author Tao.Liu
 * @date 2021/9/27 14:28
 */
public abstract class Lists {

    /**
     * 空集合
     *
     * @param <E>
     * @return
     */
    public static <E> List<E> emptyList() {
        return Collections.EMPTY_LIST;
    }

    /**
     * 判断一个集合是否为空或空集
     *
     * @param c 集合对象
     * @return true-空或空集
     */
    public static boolean isEmpty(final Collection c) {
        return c == null || c.isEmpty();
    }

    /**
     * 判断一个集合不为空
     *
     * @param c
     * @return
     */
    public static boolean isNotEmpty(final Collection c) {
        return !isEmpty(c);
    }

    /**
     * 将集合转换为Map
     *
     * @param collection 集合
     * @param keyMapper  key转换Function
     * @param <K>        转换后的key
     * @param <V>        转换后的Value
     * @return
     */
    public static <K, V> Map<K, V> toMap(final Collection<V> collection, final Function<? super V, ? extends K> keyMapper) {
        if (Lists.isEmpty(collection)) {
            return Collections.EMPTY_MAP;
        }
        return collection.stream().collect(Collectors.toMap(keyMapper, Function.identity(), (k1, k2) -> k1));
    }

    /**
     * 将集合转换为另一集合
     *
     * @param collection 集合
     * @param mapper     转换Function
     * @param <T>        入参元素
     * @param <R>        出参元素
     * @return
     */
    public static <T, R> List<R> toList(final Collection<T> collection, final Function<? super T, ? extends R> mapper) {
        if (Lists.isEmpty(collection)) {
            return Lists.emptyList();
        }
        final List<R> list = collection.stream().map(mapper).collect(Collectors.toList());
        return list;
    }

    /**
     * 根据一组可变参数对象构建array list
     *
     * @param objs 可变参数对象
     * @param <E>  item类型
     * @return
     */
    public static <E> List<E> asList(final E... objs) {
        final List<E> list = new ArrayList<>();
        if (objs != null && objs.length > 0) {
            for (final E obj : objs) {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 拆分数据
     *
     * @param origin
     * @param size
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> split(final List<T> origin, final int size) {
        if (Lists.isEmpty(origin)) {
            return emptyList();
        }
        if (origin.size() <= size) {
            return Lists.asList(origin);
        }
        int block = (origin.size() + size - 1) / size;
        return IntStream.range(0, block).boxed().map(i -> {
            int start = i * size;
            int end = Math.min(start + size, origin.size());
            return origin.subList(start, end);
        }).collect(Collectors.toList());
    }


}
