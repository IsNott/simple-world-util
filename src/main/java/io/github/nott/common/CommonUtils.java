package io.github.nott.common;

import io.github.nott.factory.MyFactory;
import org.slf4j.Logger;

/**
 * @author Nott
 * @date 2024-8-1
 */
public class CommonUtils {

    private static Logger log = MyFactory.sfl4jLogger;

    public static void requireFalse(boolean... methods) {
        boolean flag = true;
        for (boolean method : methods) {
            flag = flag && method;
        }
        if (flag) {
            log.error("{} require false", methods);
            throw new RuntimeException("Common Utils requireFalse");
        }
    }

    public static void requireTrue(boolean... methods) {
        boolean flag = true;
        for (boolean method : methods) {
            flag = flag && method;
        }
        if (!flag) {
            log.error("{} require false", methods);
            throw new RuntimeException("Common Utils requireFalse");
        }
    }
}
