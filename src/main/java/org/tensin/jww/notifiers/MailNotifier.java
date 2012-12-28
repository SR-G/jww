package org.tensin.jww.notifiers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.tensin.jww.AnalyzeResult;
import org.tensin.jww.CoreException;
import org.tensin.jww.velocity.VelociMail;

/**
 * The Class MailNotifier.
 */
@Root(name = "mail")
public class MailNotifier extends AbstractNotifier implements INotifier {

    /** The to. */
    @Attribute(required = true)
    private String to;

    /** The recipients. */
    // @ElementList(name = "destinataires", required = true)
    // @Description("People to be notified by mail")
    // public Collection<Recipient> recipients = new ArrayList<Recipient>();

    /** The smtp hostname. */
    @Attribute(required = false)
    // @Description("SMTP server for sending the mail")
    public String smtpHostname = "smtp.gmail.com";

    /** The smtp login. */
    @Attribute(required = false)
    // @Description("SMTP username")
    public String smtpLogin = "webmaster@tensin.org";

    /** The smtp password. */
    @Attribute(required = false)
    // @Description("SMTP password")
    public String smtpPassword = "ZZZZZZZZZZZZZ"; // should be put in configuration
    // file

    /** The smtp port. */
    @Attribute(required = false)
    // @Description("SMTP port. Default to 587 for gmail (TLS)")
    public int smtpPort = 587;

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
        final Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtpHostname);
        props.put("mail.smtp.starttls.enable", "true");

        final Date d = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

        try {

            final HtmlEmail email = new HtmlEmail();
            email.setSmtpPort(smtpPort);
            email.setAuthenticator(new DefaultAuthenticator(smtpLogin, smtpPassword));
            // email.setDebug(true);
            email.setHostName(smtpHostname);
            email.setCharset("UTF-8");
            email.getMailSession().getProperties().put("mail.smtps.auth", "true");
            email.getMailSession().getProperties().put("mail.debug", "true");
            email.getMailSession().getProperties().put("mail.smtps.port", String.valueOf(smtpPort));
            email.getMailSession().getProperties().put("mail.smtps.socketFactory.port", String.valueOf(smtpPort));
            email.getMailSession().getProperties().put("mail.smtps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            email.getMailSession().getProperties().put("mail.smtps.socketFactory.fallback", "false");
            email.getMailSession().getProperties().put("mail.smtp.starttls.enable", "true");
            email.setFrom(smtpLogin, "Mr. Beer Duino");
            email.setSubject("Beerduino mail report [" + sdf.format(d) + "]");

            final VelociMail layout = new VelociMail();
            layout.setTemplate("org/tensin/jww/velocity/layouts/mail/template.vm");
            layout.setLayout("org/tensin/jww/velocity/layouts/mail/");
            layout.addContext("charset", "UTF-8");

            email.setMsg(layout.render(email));
            for (final String s : extractItemsFromList(to)) {
                email.addTo(s);
            }
            email.setTLS(true);
            email.send();
        } catch (final EmailException e) {
            throw new CoreException(e);
        } finally {

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
     * Gets the to.
     * 
     * @return the to
     */
    public String getTo() {
        return to;
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
