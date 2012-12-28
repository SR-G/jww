package org.tensin.jww.velocity;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class VelocityLogger.
 */
public class VelocityLogger implements LogChute {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(VelocityLogger.class);

    /**
     * Initialisation.
     * 
     * @param runtimeServices
     *            RuntimeServices.
     * @throws Exception
     *             Erreur.
     */
    public void init(final RuntimeServices runtimeServices) throws Exception {
    }

    /**
     * 
     * Loggin autorisé.
     * 
     * @param level
     *            Niveau.
     * @return true/false.
     */
    public boolean isLevelEnabled(final int level) {
        return true;
    }

    /**
     * Logging.
     * 
     * @param level
     *            Level.
     * @param message
     *            Message.
     */
    public void log(final int level, final String message) {
        LOGGER.debug(message);
    }

    /**
     * Logging.
     * 
     * @param level
     *            Level.
     * @param message
     *            Message.
     * @param throwable
     *            Throwable.
     */
    public void log(final int level, final String message, final Throwable throwable) {
        LOGGER.debug(message);
    }
}
