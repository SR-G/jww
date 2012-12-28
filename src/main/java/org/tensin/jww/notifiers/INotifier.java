package org.tensin.jww.notifiers;

import org.tensin.jww.AnalyzeResult;
import org.tensin.jww.CoreException;

/**
 * The Interface INotifier.
 */
public interface INotifier {

    /**
     * Compare to.
     * 
     * @param o
     *            the o
     * @return the int
     */
    int compareTo(final INotifier o);

    /**
     * Notify.
     * 
     * @throws CoreException
     *             the core exception
     */
    void execute(final AnalyzeResult result) throws CoreException;

    /**
     * Gets the id.
     * 
     * @return the id
     */
    String getId();

}