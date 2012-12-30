package org.tensin.jww.notifiers;

import java.util.Collection;

import org.tensin.jww.AnalyzeResult;

/**
 * The Interface IStoreAnalyzeResults.
 */
public interface IStoreAnalyzeResults {

    /**
     * Clear results for.
     * 
     * @param name
     *            the name
     */
    void clearResultsFor(String name);

    /**
     * Extract results.
     * 
     * @param name
     *            the name
     * @return the collection
     */
    Collection<AnalyzeResult> extractResults(String name);

    /**
     * Gets the results for.
     * 
     * @param name
     *            the name
     * @return the results for
     */
    Collection<AnalyzeResult> getResultsFor(String name);

    /**
     * Checks for results for.
     * 
     * @param name
     *            the name
     * @return true, if successful
     */
    boolean hasResultsFor(String name);

    /**
     * Store.
     * 
     * @param result
     *            the result
     */
    void store(AnalyzeResult result);

}
