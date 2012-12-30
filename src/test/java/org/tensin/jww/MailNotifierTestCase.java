package org.tensin.jww;

import org.junit.Test;
import org.tensin.jww.notifiers.MailNotifier;


/**
 * The Class VelociMailTestCase.
 */
public class MailNotifierTestCase {

    private AnalyzeResult buildAnalyzeResult(final String url) {
        final AnalyzeResult result = new AnalyzeResult();
        result.setUrl(url);
        result.setContent("<html><head><title>Test empty page</title></head><body>Lorem ipsum</body></html>");
        return result;
    }

    /**
     * Test report.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testReport() throws Exception {
        LogInitializer.initLog();

        final MailNotifier notifier = new MailNotifier();
        notifier.setTo("serge.simon@gmail.com");
        notifier.execute(buildAnalyzeResult("http://jupiter/"));
    }

}
