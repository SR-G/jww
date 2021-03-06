/*
 * 
 */
package org.tensin.jww;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.JobStore;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.strategy.Visitor;
import org.simpleframework.xml.strategy.VisitorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.common.EmbeddedJetty;
import org.tensin.jww.configuration.Configuration;
import org.tensin.jww.configuration.ConfigurationSet;
import org.tensin.jww.configuration.JWWConfigurationVisitor;
import org.tensin.jww.elements.IElement;
import org.tensin.jww.notifiers.INotifier;
import org.tensin.jww.quartz.CronConstants;
import org.tensin.jww.quartz.CronElementJob;
import org.tensin.jww.quartz.CronNotifierJob;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

/**
 * The Class JWWServer.
 */
public class Starter {

    /** The Constant LOGGER. */
    private final static Logger LOGGER = LoggerFactory.getLogger(Starter.class);

    /** The Constant DEFAULT_JOB_GROUP. */
    private static final String DEFAULT_ELEMENTS_JOB_GROUP = "jww.jobs.elements";

    /** The Constant DEFAULT_NOTIFIERS_JOB_GROUP. */
    private static final String DEFAULT_NOTIFIERS_JOB_GROUP = "jww.jobs.notifiers";

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {
        try {
            LogInitializer.initLog();
            final Starter starter = new Starter();
            starter.parseArguments(args);
            starter.start();
        } catch (final CoreException e) {
            LOGGER.error("Error while starting JWWServer", e);
        }
    }

    /** The debug. */
    @Parameter(names = "--debug", description = "Debug mode", required = false)
    private boolean debug;

    /** The usage. */
    @Parameter(names = { "-h", "--usage", "--help" }, description = "Shows available commands", required = false)
    private boolean usage;

    /** The configuration file name. */
    @Parameter(names = { "--configuration", "-c" }, description = "Configuration file name to load. If not provided, a file named \"mirror4j.xml\" on the current path will be used", required = false)
    private String configurationFileName;

    /** The configuration. */
    private Configuration configuration;

    private EmbeddedJetty jetty ;

    /**
     * Inits the configuration.
     * 
     * @throws CoreException
     *             the core exception
     */
    private void initConfiguration() throws CoreException {
        if (!new File(configurationFileName).isFile()) {
            LOGGER.error("Configuration file [" + configurationFileName + " doesn't exist");
        } else {
            LOGGER.info("Loading initial configuration from [" + configurationFileName + "]");

            final Visitor visitor = new JWWConfigurationVisitor();
            final Strategy strategy = new VisitorStrategy(visitor);
            final Serializer serializer = new Persister(strategy);

            final File source = new File(configurationFileName);
            try {
                configuration = serializer.read(Configuration.class, source);
                configuration.updateAdditionnalInformations();
                LOGGER.info("\n" + configuration.toString());
            } catch (final Exception e) {
                throw new CoreException("Can't load configuration from [" + configurationFileName + "]", e);
            }
        }
    }

    /**
     * Inits the jobs.
     * 
     * @throws CoreException
     *             the core exception
     */
    private void initJobs() throws CoreException {
        for (final ConfigurationSet set : configuration.getSets()) {
            registerElementsCronJob(set);
            registerNotifiersCronJob(set);
        }
    }

    /**
     * Inits the scheduler.
     * 
     * @throws CoreException
     *             the core exception
     */
    private void initScheduler() throws CoreException {
        try {
            final JobStore jobStore = new RAMJobStore();
            final SimpleThreadPool threadPool = new SimpleThreadPool(50, Thread.NORM_PRIORITY);
            threadPool.setInstanceName("MAIN-SCHEDULER");
            threadPool.initialize();
            DirectSchedulerFactory.getInstance().createScheduler(threadPool, jobStore);
            DirectSchedulerFactory.getInstance().getScheduler().start();

        } catch (final SchedulerException e) {
            throw new CoreException(e);
        }
    }

    /**
     * Parses the arguments.
     * 
     * @param args
     *            the args
     */
    private void parseArguments(final String[] args) {
        JCommander jCommander = null;
        try {
            jCommander = new JCommander(this, args);
        } catch (final ParameterException e) {
            LOGGER.error("Unrecognized options");
            jCommander = new JCommander(this);
            usage(jCommander);
        }
        if (usage) {
            usage(jCommander);
        }
        if (debug) {
            LOGGER.info("Debug activated");
        }

    }

    /**
     * Register elements cron job.
     * 
     * @param set
     *            the set
     */
    private void registerElementsCronJob(final ConfigurationSet set) {
        int cnt = 1;
        for (final IElement element : set.getElements()) {
            final String cronExpression = element.getCron();
            if (StringUtils.isNotEmpty(cronExpression)) {
                try {
                    LOGGER.info("Registering element job [" + element.getName() + "-" + cnt + "] with cron [" + cronExpression + "], " + element.toString());
                    final JobDetail jobDetail = org.quartz.JobBuilder.newJob(CronElementJob.class).withIdentity(element.getName() + "-" + cnt, DEFAULT_ELEMENTS_JOB_GROUP).build();
                    final CronTrigger trigger = org.quartz.TriggerBuilder.newTrigger().withIdentity(element.getName() + "-" + cnt, DEFAULT_ELEMENTS_JOB_GROUP).
                            startNow().
                            withSchedule(org.quartz.CronScheduleBuilder.cronSchedule(cronExpression)).
                            build();
                    jobDetail.getJobDataMap().put(CronConstants.KEY_DATA_NOTIFIERS, set.getNotifiers());
                    jobDetail.getJobDataMap().put(CronConstants.KEY_DATA_NAME, set.getName());
                    jobDetail.getJobDataMap().put(CronConstants.KEY_DATA_ELEMENT_CONFIGURATION, element);
                    DirectSchedulerFactory.getInstance().getScheduler().scheduleJob(jobDetail, trigger);
                } catch (final SchedulerException e) {
                    LOGGER.error("Can't register job [" + element.getName() + "] with cron [" + cronExpression + "]", e);
                }
                cnt++;
            }
        }
    }

    /**
     * Register notifiers cron job.
     * 
     * @param set
     *            the set
     */
    private void registerNotifiersCronJob(final ConfigurationSet set) {
        int cnt = 1;
        for (final INotifier notifier : set.getNotifiers()) {
            final String cronExpression = notifier.getCron();
            if (StringUtils.isNotEmpty(cronExpression)) {
                try {
                    LOGGER.info("Registering notifier job [" + notifier.getId() + "-" + cnt + "] with cron [" + cronExpression + "], "
                            + notifier.toString());
                    final JobDetail jobDetail = org.quartz.JobBuilder.newJob(CronNotifierJob.class)
                            .withIdentity(notifier.getId() + "-" + cnt, DEFAULT_NOTIFIERS_JOB_GROUP).build();
                    final CronTrigger trigger = org.quartz.TriggerBuilder.newTrigger()
                            .withIdentity(notifier.getId() + "-" + cnt, DEFAULT_NOTIFIERS_JOB_GROUP).
                            startNow().
                            withSchedule(org.quartz.CronScheduleBuilder.cronSchedule(cronExpression)).
                            build();
                    jobDetail.getJobDataMap().put(CronConstants.KEY_DATA_NOTIFIERS, set.getNotifiers());
                    jobDetail.getJobDataMap().put(CronConstants.KEY_DATA_NAME, set.getName());
                    jobDetail.getJobDataMap().put(CronConstants.KEY_DATA_NOTIFIER_CONFIGURATION, notifier);
                    DirectSchedulerFactory.getInstance().getScheduler().scheduleJob(jobDetail, trigger);
                } catch (final SchedulerException e) {
                    LOGGER.error("Can't register notifier job [" + notifier.getId() + "] with cron [" + cronExpression + "]", e);
                }
                cnt++;
            }
        }

    }

    /**
     * Start.
     * 
     * @throws CoreException
     *             the core exception
     */
    private void start() throws CoreException {
        LOGGER.info("Starting JWW Server");
        initConfiguration();
        initScheduler();
        initJobs();
        startServer();
    }

    /**
     * Start server.
     * 
     * @throws CoreException
     */
    private void startServer() throws CoreException {
        jetty = new EmbeddedJetty();
        jetty.setPort(8080);
        jetty.start("src/main/webapp/", "/");
    }

    /**
     * Stop.
     * 
     * @throws CoreException
     *             the core exception
     */
    public void stop() throws CoreException {
        try {
            DirectSchedulerFactory.getInstance().getScheduler().shutdown(false);
        } catch (final SchedulerException e) {
            throw new CoreException(e);
        }
    }

    /**
     * Usage.
     * 
     * @param jCommander
     *            the j commander
     */
    private void usage(final JCommander jCommander) {
        final StringBuilder sb = new StringBuilder();
        jCommander.usage(sb);
        System.out.println(sb.toString());
        System.exit(0);
    }

}
