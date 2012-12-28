package org.tensin.jww.notifiers;

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

    @Override
    public int compareTo(final INotifier o) {
        // TODO Auto-generated method stub
        return 0;
    }

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

}
