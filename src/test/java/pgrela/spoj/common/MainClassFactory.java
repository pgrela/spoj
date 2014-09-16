package pgrela.spoj.common;

import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MainClassFactory {

    public<T> T getMain(Class<T> clazz, Reader reader, OutputStream output) {
        Constructor<?> constructor;
        try {
            constructor = clazz.getConstructor(Reader.class, OutputStream.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        T instance;

        try {
            instance = (T) constructor.newInstance(reader,output);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return instance;
    }
    public <T> T getMain(Class<T> clazz){
        return getMain(clazz, new InputStreamReader(System.in), new BufferedOutputStream(System.out));
    }
}
