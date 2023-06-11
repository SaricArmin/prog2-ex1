package at.ac.fhcampuswien.fhmdb.presentation;

import javafx.util.Callback;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MyFactory implements Callback<Class<?>, Object> {
    private static MyFactory instance;
    private Object controllerInstance;

    private MyFactory() {

    }

    public static MyFactory getInstance() {
        if (instance == null) {
            instance = new MyFactory();
        }
        return instance;
    }

    @Override
    public Object call(Class<?> aClass) {
        if (controllerInstance == null) {
            try {
                Constructor<?> constructor = aClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                controllerInstance = constructor.newInstance();
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return controllerInstance;
    }
}
