package org.tensin.jww.downloaders.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.commons.io.IOUtils;
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

    /** BUFFER_SIZE. */
    private static final int BUFFER_SIZE = 16384 * 16;

    /**
     * {@inheritDoc}
     * 
     * @see org.tensin.jiget.IDownloader#download(java.lang.String, java.lang.String, org.tensin.jiget.JiGetNotifier)
     */
    @Override
    public DownloadResult download(final URL destURL, final String destinationFileName) throws CoreException {
        final DownloadResult result = new DownloadResult();
        result.setUrl(destURL.toString());
        result.setDestFileName(destinationFileName);
        InputStream in = null;
        BufferedInputStream bis = null;
        FileOutputStream out = null;
        try {

            final File file = new File(destinationFileName);
            final File parent = new File(file.getParent());

            if (!parent.exists()) {
                parent.mkdirs();
            }

            in = destURL.openStream();
            bis = new BufferedInputStream(in);
            final byte[] buffer = new byte[BUFFER_SIZE];
            int c = 0;
            out = new FileOutputStream(file.getAbsolutePath());
            int count = 0;
            while ((c = bis.read(buffer)) != -1) {
                out.write(buffer, 0, c);
                out.flush();
                count += c;
                result.setDownloadedSize(count);
            }
            result.setTotalSize(count);
            result.setFinished(true);
        } catch (final UnknownHostException e) {
            result.setCr(1);
        } catch (final MalformedURLException e) {
            result.setCr(2);
        } catch (final IOException e) {
            result.setCr(3);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(in);
        }

        return result;
    }
}
