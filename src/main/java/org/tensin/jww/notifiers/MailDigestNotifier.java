package org.tensin.jww.notifiers;

import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.jww.AnalyzeResult;
import org.tensin.jww.CoreException;

/**
 * The Class MailDigestNotifier.
 */
@Root(name = "digest")
public class MailDigestNotifier extends MailNotifier {

    /** The Constant LOGGER. */
    private final static Logger LOGGER = LoggerFactory.getLogger(MailDigestNotifier.class);

    /* (non-Javadoc)
     * @see org.tensin.jww.notifiers.MailNotifier#execute(org.tensin.jww.AnalyzeResult)
     */
    @Override
    public void execute(final AnalyzeResult result) throws CoreException {
        StoreAnalyzeResults.getInstance().store(result);
    }

    /* (non-Javadoc)
     * @see org.tensin.jww.notifiers.MailNotifier#toString()
     */
    @Override
    public String toString() {
        return "MailDigestNotifier []";
    }
}
