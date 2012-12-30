package org.tensin.jww;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.tensin.jww.downloaders.web.DownloaderBufferedReader;

/**
 * The Class DownloaderTestCase.
 */
public class DownloaderTestCase {

    /**
     * Test encoding.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testEncoding() throws Exception {
        final String url = "http://sergio:none@jupiter:9002/";
        final DownloaderBufferedReader d = new DownloaderBufferedReader();
        d.download(new URL(url), "tmp/test.html");
    }

    /**
     * Test url.
     * 
     * @throws Exception
     *             the exception
     */
    public void testURL() throws Exception {
        final DownloaderBufferedReader d = new DownloaderBufferedReader();
        final URL url = new URL("http://sergio:none@jupiter:9002/");
        Assert.assertEquals(true, d.isBasicAuthURL(url));
        Assert.assertEquals("http://jupiter:9002/", d.extractURL(url).toString());
        Assert.assertEquals("sergio:none", d.extractAuth(url));

        // d.download(url, "target/toto");

    }

}
