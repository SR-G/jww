package org.tensin.jww;
import org.apache.commons.mail.HtmlEmail;
import org.junit.Test;
import org.tensin.jww.velocity.VelociMail;


/**
 * The Class VelociMailTestCase.
 */
public class VelociMailTestCase {

    /**
     * Test report.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testReport() throws Exception {
        LogInitializer.initLog();
        final HtmlEmail email = new HtmlEmail();

        final VelociMail layout = new VelociMail();
        layout.setTemplate("org/tensin/jww/velocity/layouts/mail/evil-jip-reports-tester.vm");
        layout.setLayout("org/tensin/jww/velocity/layouts/mail/");

        layout.addContext("charset", "UTF-8");

        System.out.println(layout.render(email));
    }

}
