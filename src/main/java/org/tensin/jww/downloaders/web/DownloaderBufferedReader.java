package org.tensin.jww.downloaders.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.jww.CoreException;

/**
 * 
 * 
 * <pre>
 * 
--2009-12-19 01:06:14--  http://trictrac.net/jeux/forum/images/avatars/51f8a5c03f94cc0f510dc.gif
Résolution de trictrac.net... 94.23.3.166
Connexion vers trictrac.net|94.23.3.166|:80...connecté.
requête HTTP transmise, en attente de la réponse...200 OK
Longueur: 4580 (4,5K) [image/gif]
Saving to: `51f8a5c03f94cc0f510dc.gif'

100%[=========================================================================================================================================>] 4 580       --.-K/s   in 0,1s

2009-12-19 01:06:14 (46,8 KB/s) - « 51f8a5c03f94cc0f510dc.gif » sauvegardé [4580/4580]


 * 
 * </pre>
 * 
 * 
 * @author u248663
 *
 */
public class DownloaderBufferedReader implements IDownloader {

    /** The Constant Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloaderBufferedReader.class);

    /** BUFFER_SIZE. */
    private static final int BUFFER_SIZE = 16384 * 16;

    /** The Constant PATTERN_URL_REGEXP. */
    private static final String PATTERN_URL_REGEXP = "([^/]*)//([^:]*:[^@]*)@(.*)";

    /** The Constant p. */
    private static final Pattern PATTERN_URL = Pattern.compile(PATTERN_URL_REGEXP);

    /**
     * {@inheritDoc}
     * 
     * @see org.tensin.jiget.IDownloader#download(java.lang.String, java.lang.String, org.tensin.jiget.JiGetNotifier)
     */
    @Override
    public DownloadResult download(final URL destURL, final String destinationFileName) throws CoreException {
        LOGGER.info("Downloading [" + destURL.toString() + "] to [" + destinationFileName + "]");
        final DownloadResult result = new DownloadResult();
        result.setDestFileName(destinationFileName);
        InputStream in = null;
        FileOutputStream fis = null;
        try {
            final File file = new File(destinationFileName);
            final File parent = new File(file.getParent());
            FileUtils.forceMkdir(parent);

            HttpURLConnection connection;
            if (isBasicAuthURL(destURL) ) {
                final URL url = extractURL(destURL);
                final String auth = extractAuth(destURL);
                connection = (HttpURLConnection) url.openConnection();
                final sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
                connection.setRequestProperty("Authorization", "Basic " + encoder.encode(auth.getBytes()));
                result.setUrl(url.toString());
            } else {
                connection = (HttpURLConnection) destURL.openConnection();
                result.setUrl(destURL.toString());
            }
            in = connection.getInputStream();
            fis = new FileOutputStream(file.getAbsolutePath());
            final int read = IOUtils.copy(in, fis);
            result.setTotalSize(read);
            result.setFinished(true);
        } catch (final UnknownHostException e) {
            result.setCr(1);
        } catch (final MalformedURLException e) {
            result.setCr(2);
        } catch (final IOException e) {
            result.setCr(3);
        } finally {
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(in);
        }
        return result;
    }

    /**
     * Extract auth.
     * 
     * @param destURL
     *            the dest url
     * @return the string
     */
    public String extractAuth(final URL destURL) {
        final Matcher m = PATTERN_URL.matcher(destURL.toString());
        m.matches();

        if (m.groupCount() == 3) {
            final String auth = m.group(2);
            return auth;
        }
        return "";
    }

    /**
     * Extract encoding.
     * 
     * @param contentType
     *            the content type
     * @return the string
     */
    private String extractEncoding(final String contentType) {
        System.out.println("!!!!!!!" + contentType);
        final String[] values = contentType.split(";"); //The values.length must be equal to 2...
        String charset = "";

        for (String value : values) {
            value = value.trim();

            if (value.toLowerCase().startsWith("charset=")) {
                charset = value.substring("charset=".length());
            }
        }

        if (StringUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }
        return charset;
    }

    /**
     * Extract url.
     * 
     * @param destURL
     *            the dest url
     * @return the url
     * @throws MalformedURLException
     *             the malformed url exception
     */
    public URL extractURL(final URL destURL) throws MalformedURLException {
        final Matcher m = PATTERN_URL.matcher(destURL.toString());
        m.matches();

        if (m.groupCount() == 3) {
            final String protocol = m.group(1);
            final String url = protocol + "//" + m.group(3);
            return new URL(url);
        }
        return destURL;
    }

    /**
     * Checks if is basic auth url.
     * 
     * @param destURL
     *            the dest url
     * @return true, if is basic auth url
     */
    public boolean isBasicAuthURL(final URL destURL) {
        return destURL.toString().matches("http://[^:]*:[^@]*@.*");
    }
}
