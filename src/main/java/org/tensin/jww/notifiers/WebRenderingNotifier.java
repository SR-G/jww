package org.tensin.jww.notifiers;

import org.simpleframework.xml.Root;
import org.tensin.jww.AnalyzeResult;
import org.tensin.jww.CoreException;

/**
 * The Class MailNotifier.
 */
@Root(name = "web")
public class WebRenderingNotifier extends AbstractNotifier implements INotifier {

    @Override
    public int compareTo(final INotifier o) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.tensin.jww.notifiers.INotifier#doNotification()
     */
    @Override
    public void execute(final AnalyzeResult result) throws CoreException {

    }

}
