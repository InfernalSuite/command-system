package net.endrealm.libraries.utils;

import java.lang.reflect.Array;

@SuppressWarnings("unchecked")
public class ArrayUtils {

    public static <T> T[] preReduceArray(T[] array) {
        return preReduceArray(array, 1);
    }

    public static <T> T[] preReduceArray(T[] array, int amount) {
        Class<T> componentType = (Class<T>) array.getClass().getComponentType();
        T[] minor = (T[]) Array.newInstance(componentType, array.length-amount);

        if(minor.length == 0)
            return minor;

        System.arraycopy(array, amount, minor, 0, minor.length);
        return minor;
    }

}
