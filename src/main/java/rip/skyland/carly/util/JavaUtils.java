package rip.skyland.carly.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

// improved code a bit, still ugly though.
public class JavaUtils {

    public static <K,V> Map<K,V> mapOf(K k1, V v1) {
        return new HashMap<K, V>() {{
            put(k1, v1);
        }};
    }

    public static <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2) {
        return mapOf(Arrays.asList(k1, k2), Arrays.asList(v1, v2));
    }

    public static <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3) {
        return mapOf(Arrays.asList(k1, k2, k3), Arrays.asList(v1, v2, v3));
    }

    public static <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return mapOf(Arrays.asList(k1, k2, k3, k4), Arrays.asList(v1, v2, v3, v4));
    }

    public static <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return mapOf(Arrays.asList(k1, k2, k3, k4, k5), Arrays.asList(v1, v2, v3, v4, v5));
    }

    public static <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return mapOf(Arrays.asList(k1, k2, k3, k4, k5, k6), Arrays.asList(v1, v2, v3, v4, v5, v6));
    }

    public static <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return mapOf(Arrays.asList(k1, k2, k3, k4, k5, k6, k7), Arrays.asList(v1, v2, v3, v4, v5, v6, v7));
    }

    public static <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        return mapOf(Arrays.asList(k1, k2, k3, k4, k5, k6, k7, k8), Arrays.asList(v1, v2, v3, v4, v5, v6, v7, v8));
    }

    public static <K,V> Map<K,V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return mapOf(Arrays.asList(k1, k2, k3, k4, k5, k6, k7, k8, k9), Arrays.asList(v1, v2, v3, v4, v5, v6, v7, v8, v9));
    }

    public static <K, V> Map<K, V> mapOf(List<K> keys, List<V> values) {
        if(keys.size() != values.size()) {
            throw new IndexOutOfBoundsException("amount of keys and values is not equal");
        }

        return new HashMap<K, V>() {{
            IntStream.range(0, keys.size()).forEach(index -> put(keys.get(index), values.get(index)));
        }};
    }
}
