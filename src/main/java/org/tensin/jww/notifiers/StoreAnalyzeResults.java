package org.tensin.jww.notifiers;

import org.tensin.jww.AnalyzeResult;

/**
 * The Class StoreAnalyzeResults.
 */
public class StoreAnalyzeResults {

    /** The instance. */
    private static StoreAnalyzeResults INSTANCE;

    /**
     * Gets the single instance of StoreAnalyzeResults.
     * 
     * @return single instance of StoreAnalyzeResults
     */
    public static StoreAnalyzeResults getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StoreAnalyzeResults();
        }
        return INSTANCE;
    }

    /** The Constant currentUserResults. */
    private final IStoreAnalyzeResults currentUserResults = new StoreAnalyzeResultsRAM();

    /**
     * Gets the all results.
     * 
     * @return the all results
     */
    public IStoreAnalyzeResults getAllResults() {
        return currentUserResults;
    }

    /**
     * Store.
     * 
     * @param result
     *            the result
     */
    public void store(final AnalyzeResult result) {
        currentUserResults.store(result);
    }

}
