package org.tensin.jww;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * The Class LogInitializer.
 */
public class LogInitializer {

    /** The init done. */
    private static boolean initDone;

    /**
     * Inits the log.
     */
    public static void initLog() {
        if (!initDone) {
            // BasicConfigurator.configureDefaultContext();

            final Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            root.setLevel(Level.INFO);

            // org.reflections.Reflections
            final Logger reflections = (Logger) LoggerFactory.getLogger("org.reflections.Reflections");
            reflections.setLevel(Level.WARN);

            initDone = true;
        }
    }

    /**
     * Sets the debug.
     * 
     * @param b
     *            the new debug
     */
    public static void setDebug(final boolean b) {
        final Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        if (b) {
            root.setLevel(Level.DEBUG);
        } else {
            root.setLevel(Level.INFO);
        }
    }
}
