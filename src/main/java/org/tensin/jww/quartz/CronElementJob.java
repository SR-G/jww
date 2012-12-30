package org.tensin.jww.quartz;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.jww.AnalyzeResult;
import org.tensin.jww.CoreException;
import org.tensin.jww.elements.IElement;
import org.tensin.jww.notifiers.INotifier;

/**
 * The Class CronJob.
 */
public class CronElementJob implements Job {

    /** The Constant Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CronElementJob.class);

    /* (non-Javadoc)
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    /**
     * Builds the notifiers list.
     * 
     * @param liste1
     *            the liste1
     * @param liste2
     *            the liste2
     * @return the collection
     */
    private Collection<INotifier> buildNotifiersList(final String idNotifiers, final Collection<INotifier> definedNotifiers) {
        final Collection<INotifier> result = new ArrayList<INotifier>();
        final Collection<String> ids = extractItemsFromList(idNotifiers);
        if (CollectionUtils.isEmpty(ids)) {
            result.addAll(definedNotifiers);
        } else {
            for (final String idNotifier : extractItemsFromList(idNotifiers)) {
                for (final INotifier notifier : definedNotifiers) {
                    if (idNotifier.equalsIgnoreCase(notifier.getId()) && !isAlreadyDefined(notifier, result)) {
                        result.add(notifier);
                    }
                }
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(final JobExecutionContext ctx) throws JobExecutionException {
        final JobDetail jobDetail = ctx.getJobDetail();
        final JobDataMap jobDataMap = jobDetail.getJobDataMap();
        final IElement element = (IElement) jobDataMap.get(CronConstants.KEY_DATA_ELEMENT_CONFIGURATION);
        final String name = (String) jobDataMap.get(CronConstants.KEY_DATA_NAME);
        final Collection<INotifier> globalNotifiers = (Collection<INotifier>) jobDataMap.get(CronConstants.KEY_DATA_NOTIFIERS);
        if (element != null) {
            LOGGER.debug("Going to analyze set [" + name + "], [" + element.getName() + "]");
            try {
                final AnalyzeResult result = element.analyze();
                if (result.hasToBeNotified()) {
                    for (final INotifier notifier : buildNotifiersList(element.getNotifiers(), globalNotifiers)) {
                        LOGGER.info("User [" + name + "], notifying [" + notifier.toString() + "]");
                        notifier.execute(result);
                    }
                }
            } catch (final CoreException e) {
                throw new JobExecutionException(e);
            }
        }
    }

    /**
     * Extract items from list.
     * 
     * @param s
     *            the notifiers defined
     * @return the collection
     */
    private Collection<String> extractItemsFromList(final String s) {
        final Collection<String> results = new ArrayList<String>();
        for (final String item : s.split(",")) {
            results.add(item.trim());
        }
        return results;
    }

    /**
     * Checks if is already defined.
     * 
     * @param notifier
     *            the notifier
     * @param notifiers
     *            the notifiers
     * @return true, if is already defined
     */
    private boolean isAlreadyDefined(final INotifier notifier, final Collection<INotifier> notifiers) {
        for (final INotifier currentNotifier : notifiers) {
            if (notifier.compareTo(currentNotifier) == 0) {
                return true;
            }
        }
        return false;
    }
}
