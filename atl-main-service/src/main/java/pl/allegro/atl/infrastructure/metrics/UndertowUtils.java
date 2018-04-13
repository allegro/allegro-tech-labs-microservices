package pl.allegro.atl.infrastructure.metrics;

import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.xnio.channels.AcceptingChannel;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

class UndertowUtils {

    private static final String UNDERTOW_CHANNELS_PATH = "webServer.undertow.channels";

    private static final String UNDERTOW_THREAD_POOL_EXECUTOR_PATH = "webServer.undertow.worker.taskPool";

    private final ServletWebServerApplicationContext ctx;

    UndertowUtils(ServletWebServerApplicationContext ctx) {
        this.ctx = ctx;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getUsingReflection(Object obj, String path) {
        String[] fieldNames = path.split("\\.");

        Object curObj = obj;
        for (String fieldName: fieldNames) {
            curObj = getFieldUsingReflection(curObj, fieldName);
        }

        return (T) curObj;
    }

    private static Object getFieldUsingReflection(Object obj, String fieldName) {
        try {
            Class clazz = obj.getClass();

            while (clazz != Object.class) {
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    return field.get(obj);
                }
                catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }

            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    ThreadPoolExecutor getUndertowTaskPool() {
        return getUsingReflection(ctx, UNDERTOW_THREAD_POOL_EXECUTOR_PATH);
    }

    List<AcceptingChannel> getChannels() {
        return getUsingReflection(ctx, UNDERTOW_CHANNELS_PATH);
    }

    Object getSsl(AcceptingChannel channel) {
        return getUsingReflection(channel, "ssl");
    }
}