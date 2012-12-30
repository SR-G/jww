package org.tensin.jww;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.HtmlEmail;
import org.junit.Test;
import org.tensin.jww.velocity.VelociMail;


/**
 * The Class VelociMailTestCase.
 */
public class VelociMailTestCase {

    private AnalyzeResult buildAnalyzeResult(final String url) {
        final AnalyzeResult result = new AnalyzeResult();
        result.setUrl(url);
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

        final Collection<AnalyzeResult> results = new ArrayList<AnalyzeResult>();
        results.add(buildAnalyzeResult("http://jupiter/"));

        final HtmlEmail email = new HtmlEmail();

        final VelociMail layout = new VelociMail();
        layout.setTemplate("org/tensin/jww/velocity/layouts/mail/template.vm");
        layout.setLayout("org/tensin/jww/velocity/layouts/mail/");

        layout.addContext("charset", "UTF-8");
        layout.addContext("results", results);

        final String content = layout.render(email);
        System.out.println(content);

        final String filename = "target/tests/test.html";
        FileUtils.writeStringToFile(new File(filename), content);
    }

}
