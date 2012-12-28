package org.tensin.jww;
import org.junit.Test;
import org.tensin.jww.helpers.MD5Helper;


public class MessageDigestTestCase {

    @Test
    public void testName() throws Exception {
        final String url = "http://192.168.1.4";

        final String oldTempFileName = "tmp/" + MD5Helper.encodeFileName(url);
        System.out.println(oldTempFileName);

    }

}
