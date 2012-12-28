package org.tensin.jww.notifiers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.jww.AnalyzeResult;
import org.tensin.jww.CoreException;

/**
 * The Class URLNotifier.
 */
@Root(name = "url")
// @Description("Notification by activating a single URL.")
public class URLNotifier extends AbstractNotifier implements INotifier {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(URLNotifier.class);

    /**
     * The url.
     */
    @Attribute(required = false)
    private String url;

    /**
     * Activate url.
     * 
     * @param address
     *            the address
     */
    protected void activateUrl(final String address) {
        InputStream is = null;
        try {
            final URL u = new URL(address);
            is = u.openStream();
        } catch (final MalformedURLException e) {
            LOGGER.error("Error while activating to [" + address + "]", e);
        } catch (final IOException e) {
            LOGGER.error("Error while activating to [" + address + "]", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /* (non-Javadoc)
     * @see org.tensin.jww.notifiers.INotifier#compareTo(org.tensin.jww.notifiers.INotifier)
     */
    @Override
    public int compareTo(final INotifier o) {
        return -1;
    }

    /* (non-Javadoc)
     * @see org.tensin.jww.notifiers.INotifier#execute(org.tensin.jww.AnalyzeResult)
     */
    @Override
    public void execute(final AnalyzeResult result) throws CoreException {
        activateUrl(url);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "URLNotifier [url=" + url + "]";
    }

}
