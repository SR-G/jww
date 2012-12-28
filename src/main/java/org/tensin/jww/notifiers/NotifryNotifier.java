package org.tensin.jww.notifiers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.jww.AnalyzeResult;
import org.tensin.jww.CoreException;

/**
 * The Class NotifryNotification.
 * Use : https://notifrier.appspot.com (Android only)
 * 
 */
@Root(name = "notifry")
// @Description("Notification by activating a Notifry notification. See http://notifrier.appspot.com/.")
public class NotifryNotifier extends AbstractNotifier implements INotifier {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NotifryNotifier.class);

    /** The Constant NOT_IMPLEMENTED_501. */
    public static final int NOT_IMPLEMENTED_501 = 501;

    /** The pushto url. */
    @Attribute(name = "notifry-url", required = false)
    // @Description("Notifry URL to use. Something like 'https://notifrier.appspot.com/notifry'.")
    private String notifryUrl = "https://notifrier.appspot.com/notifry";

    /** The pushto id. */
    @Attribute(name = "source")
    // @Description("The Notifry source identification (to be grabbed from your notifry configuration panel).")
    private String notifrySource;

    /** The pushto signature. */
    @Attribute(name = "signature", required = false)
    // @Description("Signature that will be shown in the notifry notification sent.")
    private String notifrySignature = "beerduino";

    @Override
    public int compareTo(final INotifier o) {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.tensin.beerduino.notifications.URLNotification#execute(org.tensin.beerduino.TemperatureResults)
     */
    @Override
    public void execute(final AnalyzeResult result) throws CoreException {
        LOGGER.info("Sending PushTo notification to [" + notifrySource + "]");

        final StringBuilder sb = new StringBuilder();

        final HttpClient client = new HttpClient();
        // client.getParams().setParameter("http.useragent", "Test Client");
        client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        client.getParams().setParameter("http.socket.timeout", new Integer(1000));
        client.getParams().setParameter("http.protocol.content-charset", "UTF-8");

        BufferedReader br = null;

        final PostMethod method = new PostMethod(getNotifryUrl());
        method.addParameter("format", "json");
        method.addParameter("source", notifrySource);
        method.addParameter("message", sb.toString());
        method.addParameter("title", getNotifrySignature());

        try {
            final int returnCode = client.executeMethod(method);
            if (returnCode == NOT_IMPLEMENTED_501) {
                LOGGER.error("The Post method is not implemented by this URI");
                // still consume the response body
                method.getResponseBodyAsString();
            } else {
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String readLine;
                while (((readLine = br.readLine()) != null)) {
                    sb.append(readLine);
                }
                LOGGER.info(sb.toString());
            }
        } catch (final HttpException e) {
            LOGGER.error("Error while pushing to [" + getUrl() + "]", e);
        } catch (final IOException e) {
            LOGGER.error("Error while pushing to [" + getUrl() + "]", e);
        } finally {
            method.releaseConnection();
            IOUtils.closeQuietly(br);
        }

    }

    /**
     * Gets the pushto signature.
     * 
     * @return the pushto signature
     */
    public String getNotifrySignature() {
        return notifrySignature;
    }

    /**
     * Gets the pushto id.
     * 
     * @return the pushto id
     */
    public String getNotifrySource() {
        return notifrySource;
    }

    /**
     * Gets the pushto url.
     * 
     * @return the pushto url
     */
    public String getNotifryUrl() {
        return notifryUrl;
    }

    /**
     * Gets the url.
     * 
     * @return the url
     */
    private String getUrl() {
        return getNotifryUrl() + getNotifrySource() + "/";
    }

    /**
     * Sets the pushto signature.
     * 
     * @param notifrySignature
     *            the new notifry signature
     */
    public void setNotifrySignature(final String notifrySignature) {
        this.notifrySignature = notifrySignature;
    }

    /**
     * Sets the pushto id.
     * 
     * @param notifryId
     *            the new notifry source
     */
    public void setNotifrySource(final String notifryId) {
        notifrySource = notifryId;
    }

    /**
     * Sets the pushto url.
     * 
     * @param notifryUrl
     *            the new notifry url
     */
    public void setNotifryUrl(final String notifryUrl) {
        this.notifryUrl = notifryUrl;
    }

    @Override
    public String toString() {
        return "NotifryNotifier [notifryUrl=" + notifryUrl + ", notifrySource=" + notifrySource + ", notifrySignature=" + notifrySignature + "]";
    }
}
