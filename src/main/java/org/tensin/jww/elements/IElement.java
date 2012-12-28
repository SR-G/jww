package org.tensin.jww.elements;

import org.tensin.jww.AnalyzeResult;
import org.tensin.jww.CoreException;

/**
 * The Interface IElement.
 */
public interface IElement {

    /**
     * Analyze.
     * 
     * @throws CoreException
     *             the core exception
     */
    AnalyzeResult analyze() throws CoreException;

    /**
     * Gets the cron.
     * 
     * @return the cron
     */
    String getCron();

    /**
     * Gets the name.
     * 
     * @return the name
     */
    String getName();

    /**
     * Gets the notifiers.
     * 
     * @return the notifiers
     */
    String getNotifiers();

}
