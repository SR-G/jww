package org.tensin.jww.notifiers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.tensin.jww.AnalyzeResult;

/**
 * The Class StoreAnalyzeResultsRAM.
 */
public class StoreAnalyzeResultsRAM implements IStoreAnalyzeResults {

    /** The current user results. */
    private final Map<String, Collection<AnalyzeResult>> currentUserResults = new HashMap<String, Collection<AnalyzeResult>>();

    /* (non-Javadoc)
     * @see org.tensin.jww.notifiers.IStoreAnalyzeResults#clearResultsFor(java.lang.String)
     */
    @Override
    public void clearResultsFor(final String name) {
        synchronized (currentUserResults) {
            currentUserResults.put(name, new ArrayList<AnalyzeResult>());
        }
    }

    /* (non-Javadoc)
     * @see org.tensin.jww.notifiers.IStoreAnalyzeResults#extractResults(java.lang.String)
     */
    @Override
    public Collection<AnalyzeResult> extractResults(final String name) {
        Collection<AnalyzeResult> results = null;
        synchronized (currentUserResults) {
            // Get current results for current user
            if (hasResultsFor(name)) {
                results = getResultsFor(name);
                // Purge results
                clearResultsFor(name);
            }
        }
        return results;
    }

    /* (non-Javadoc)
     * @see org.tensin.jww.notifiers.IStoreAnalyzeResults#getResultsFor(java.lang.String)
     */
    @Override
    public Collection<AnalyzeResult> getResultsFor(final String name) {
        synchronized (currentUserResults) {
            return currentUserResults.get(name);
        }
    }

    /* (non-Javadoc)
     * @see org.tensin.jww.notifiers.IStoreAnalyzeResults#hasResultsFor(java.lang.String)
     */
    @Override
    public boolean hasResultsFor(final String name) {
        synchronized (currentUserResults) {
            return currentUserResults.containsKey(name);
        }
    }

    /* (non-Javadoc)
     * @see org.tensin.jww.notifiers.IStoreAnalyzeResults#store(org.tensin.jww.AnalyzeResult)
     */
    @Override
    public void store(final AnalyzeResult result) {
        final String name = result.getName();
        synchronized (currentUserResults) {
            Collection<AnalyzeResult> results;
            if (currentUserResults.containsKey(name)) {
                results = currentUserResults.get(name);
            } else {
                results = new ArrayList<AnalyzeResult>();
            }
            results.add(result);
            currentUserResults.put(name, results);
        }
    }
}
