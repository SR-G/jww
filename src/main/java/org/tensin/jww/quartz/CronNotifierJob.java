package org.tensin.jww.quartz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.jww.AnalyzeResult;
import org.tensin.jww.CoreException;
import org.tensin.jww.notifiers.MailDigestNotifier;
import org.tensin.jww.notifiers.StoreAnalyzeResults;

/**
 * The Class CronJob.
 */
public class CronNotifierJob implements Job {

    /** The Constant Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CronNotifierJob.class);

    /**
     * Consolidate results.
     * 
     * @param resultsToConsolidate
     *            the results to consolidate
     * @return the collection
     */
    private Collection<AnalyzeResult> consolidateResults(final Collection<AnalyzeResult> resultsToConsolidate) {
        final Collection<AnalyzeResult> results = new ArrayList<AnalyzeResult>();
        for (final String distinctUrl : extractDistinctURLs(resultsToConsolidate)) {
            final List<AnalyzeResult> currentResultsForThisUrl = extractResultsForThisUrl(distinctUrl, resultsToConsolidate);

            if (CollectionUtils.isNotEmpty(currentResultsForThisUrl)) {
                final AnalyzeResult lastAnalyzeResult = currentResultsForThisUrl.get(currentResultsForThisUrl.size() - 1);
                lastAnalyzeResult.setChangeCount(currentResultsForThisUrl.size());
                results.add(lastAnalyzeResult);
            }
        }
        return results;
    }

    /* (non-Javadoc)
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(final JobExecutionContext ctx) throws JobExecutionException {
        final JobDetail jobDetail = ctx.getJobDetail();
        final JobDataMap jobDataMap = jobDetail.getJobDataMap();
        final String name = (String) jobDataMap.get(CronConstants.KEY_DATA_NAME);
        final MailDigestNotifier notifier = (MailDigestNotifier) jobDataMap.get(CronConstants.KEY_DATA_NOTIFIER_CONFIGURATION);

        LOGGER.debug("MailDigestNotification for [" + name + "]");

        final Collection<AnalyzeResult> results = StoreAnalyzeResults.getInstance().getAllResults().extractResults(name);

        // Send digests
        if (CollectionUtils.isNotEmpty(results)) {
            LOGGER.info("Sending digest for [" + name + "] to [" + notifier.getTo() + "] with [" + results.size() + "] webpage modifications to be notified");
            final Collection<AnalyzeResult> consolidatedResults = consolidateResults(results);
            try {
                notifier.notifyResults(consolidatedResults);
            } catch (final CoreException e) {
                throw new JobExecutionException(e);
            }
        }
    }

    /**
     * Extract distinct urls.
     * 
     * @param resultsToConsolidate
     *            the results to consolidate
     * @return the collection
     */
    private Collection<String> extractDistinctURLs(final Collection<AnalyzeResult> resultsToConsolidate) {
        final Collection<String> results = new HashSet<String>();
        for (final AnalyzeResult analyzeResult : resultsToConsolidate) {
            results.add(analyzeResult.getUrl());
        }
        return results;
    }

    /**
     * Extract results for this url.
     * 
     * @param distinctUrl
     *            the distinct url
     * @param resultsToConsolidate
     *            the results to consolidate
     * @return the list
     */
    private List<AnalyzeResult> extractResultsForThisUrl(final String distinctUrl, final Collection<AnalyzeResult> resultsToConsolidate) {
        final List<AnalyzeResult> results = new ArrayList<AnalyzeResult>();
        for (final AnalyzeResult analyzeResult : resultsToConsolidate) {
            if (StringUtils.equalsIgnoreCase(distinctUrl, analyzeResult.getUrl())) {
                results.add(analyzeResult);
            }
        }

        return results;
    }

}
