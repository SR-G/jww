package org.tensin.jww.notifiers;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.jww.AnalyzeResult;
import org.tensin.jww.CoreException;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * The Class TwitterNotifier.
 */
@Root(name = "twitter")
// @Description("Notification by sending a Tweet.")
public class TwitterNotifier extends AbstractNotifier implements INotifier {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(TwitterNotifier.class);

    /** The pushto url. */
    @Attribute(name = "dest", required = true)
    // @Description("Recipient ID that will receive the tweets")
    private String recipientId;

    /* (non-Javadoc)
     * @see org.tensin.jww.notifiers.INotifier#compareTo(org.tensin.jww.notifiers.INotifier)
     */
    @Override
    public int compareTo(final INotifier o) {
        if (!(o instanceof TwitterNotifier)) {
            return -1;
        }
        final TwitterNotifier m = (TwitterNotifier) o;
        return new CompareToBuilder().append(recipientId, m.getRecipientId()).toComparison();
    }

    /* (non-Javadoc)
     * @see org.tensin.jww.notifiers.INotifier#execute(org.tensin.jww.AnalyzeResult)
     */
    @Override
    public void execute(final AnalyzeResult result) throws CoreException {
        LOGGER.info("Sending Twitter To notification to [" + recipientId + "]");

        try {
            final StringBuilder sb = new StringBuilder();

            final Twitter sender = new TwitterFactory().getInstance();
            final DirectMessage message = sender.sendDirectMessage(recipientId,
                    sb.toString());
            LOGGER.info("Sent: " + sb.toString() + " to @"
                    + message.getRecipientScreenName());
        } catch (final TwitterException e) {
            LOGGER.error("Error while twitting to [" + recipientId + "]", e);
        } finally {
        }
    }

    /**
     * Gets the recipient id.
     * 
     * @return the recipient id
     */
    public String getRecipientId() {
        return recipientId;
    }

    /**
     * Sets the recipient id.
     * 
     * @param recipientId
     *            the new recipient id
     */
    public void setRecipientId(final String recipientId) {
        this.recipientId = recipientId;
    }

}
