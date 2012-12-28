package org.tensin.jww.downloaders.web;

import java.net.URL;

import org.tensin.jww.CoreException;

/**
 * The Class DownloaderBuffededStream.
 * 
 * @author u248663
 * @version $Revision: 1.1 $
 * @since 19 déc. 2009 02:24:46
 */
public class DownloaderBuffededStream implements IDownloader {

    /**
     * {@inheritDoc}
     * 
     * @see org.tensin.jiget.IDownloader#download(java.lang.String, java.lang.String, org.tensin.jiget.JiGetNotifier)
     */
    @Override
    public DownloadResult download(final URL destURL, final String destinationFileName) throws CoreException {
        return new DownloadResult ();
    }
}
