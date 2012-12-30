package org.tensin.jww.notifiers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.jww.AnalyzeResult;
import org.tensin.jww.CoreException;
import org.tensin.jww.configuration.SMTP;
import org.tensin.jww.velocity.VelociMail;

/**
 * The Class MailNotifier.
 */
@Root(name = "mail")
public class MailNotifier extends AbstractNotifier implements INotifier {

    /** The Constant Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MailNotifier.class);

    /** The to. */
    @Attribute(required = true)
    private String to;

    /** The smtp. */
    private SMTP smtp;

    /**
     * The recipients.
     * 
     * @param o
     *            the o
     * @return the int
     */
    // @ElementList(name = "destinataires", required = true)
    // @Description("People to be notified by mail")
    // public Collection<Recipient> recipients = new ArrayList<Recipient>();

    /* (non-Javadoc)
     * @see org.tensin.jww.notifiers.INotifier#compareTo(org.tensin.jww.notifiers.INotifier)
     */
    @Override
    public int compareTo(final INotifier o) {
        if (!(o instanceof MailNotifier)) {
            return -1;
        }
        final MailNotifier m = (MailNotifier) o;
        return new CompareToBuilder().append(to, m.getTo()).toComparison();
    }

    /* (non-Javadoc)
     * @see org.tensin.jww.notifiers.INotifier#doNotification()
     */
    @Override
    public void execute(final AnalyzeResult result) throws CoreException {
        final Collection<AnalyzeResult> results = new ArrayList<AnalyzeResult>();
        results.add(result);
        notifyResults(results);
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
     * Gets the smtp.
     * 
     * @return the smtp
     */
    public SMTP getSmtp() {
        return smtp;
    }

    /**
     * Gets the to.
     * 
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * Gets the urls from results.
     * 
     * @param results
     *            the results
     * @return the urls from results
     */
    private String getUrlsFromResults(final Collection<AnalyzeResult> results) {
        final StringBuilder sb = new StringBuilder();
        int cnt = 0;
        for (final AnalyzeResult result : results) {
            if (cnt++ > 0) {
                sb.append(", ");
            }
            sb.append(result.getUrl());
        }
        return sb.toString();
    }

    /**
     * Notify results.
     * 
     * @param results
     *            the results
     * @throws CoreException
     *             the core exception
     */
    public void notifyResults(final Collection<AnalyzeResult> results) throws CoreException {
        LOGGER.info("Sending notification with [" + results.size() + "] results : " + getUrlsFromResults(results));
        final Date d = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

        try {
            final HtmlEmail email = new HtmlEmail();
            email.setCharset("UTF-8");
            email.setHostName(smtp.getHostname());
            email.setSmtpPort(smtp.getPort());
            email.setAuthenticator(new DefaultAuthenticator(smtp.getLogin(), smtp.getPassword()));
            // email.setDebug(true);
            email.getMailSession().getProperties().put("mail.debug", "true");
            if (smtp.isTls()) {
                email.getMailSession().getProperties().put("mail.smtps.auth", "true");
                email.getMailSession().getProperties().put("mail.smtps.port", String.valueOf(smtp.getPort()));
                email.getMailSession().getProperties().put("mail.smtps.socketFactory.port", String.valueOf(smtp.getPort()));
                email.getMailSession().getProperties().put("mail.smtps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                email.getMailSession().getProperties().put("mail.smtps.socketFactory.fallback", "false");
                email.getMailSession().getProperties().put("mail.smtp.starttls.enable", "true");
            }
            email.setFrom(smtp.getLogin(), "JWW");
            email.setSubject("JWW mail report [" + sdf.format(d) + "]");

            final VelociMail layout = new VelociMail();
            layout.setTemplate("org/tensin/jww/velocity/layouts/mail/template.vm");
            layout.setLayout("org/tensin/jww/velocity/layouts/mail/");
            layout.addContext("charset", "UTF-8");
            layout.addContext("results", results);
            layout.addContext("base_url", "http://localhost:8080/");

            email.setMsg("This browser doesn't render HTML mails, no display");
            email.setHtmlMsg(layout.render(email));
            for (final String s : extractItemsFromList(to)) {
                email.addTo(s);
            }
            email.setTLS(true);
            email.send();
        } catch (final EmailException e) {
            LOGGER.error("Can't send mail to [" + smtp.getHostname() + ":" + smtp.getPort() + "] with login [" + smtp.getLogin() + "] : " + e.getCause());
            // throw new CoreException(e);
        } finally {

        }

    }

    /**
     * Sets the smtp.
     * 
     * @param smtp
     *            the new smtp
     */
    public void setSmtp(final SMTP smtp) {
        this.smtp = smtp;
    }

    /**
     * Sets the to.
     * 
     * @param to
     *            the new to
     */
    public void setTo(final String to) {
        this.to = to;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MailNotifier [to=" + to + "]";
    }

}
