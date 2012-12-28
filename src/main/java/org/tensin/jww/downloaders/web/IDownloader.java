package org.tensin.jww.downloaders.web;

import java.net.URL;

import org.tensin.jww.CoreException;

/**
 * IDownloader.
 * 
 * @author u248663
 * @version $Revision: 1.1 $
 * @since 19 déc. 2009 02:21:08
 * 
 */
public interface IDownloader {

    /**
     * Méthode download.
     * 
     * @param downloadId
     *            the download id
     * @param destURL
     *            the dest url
     * @param length
     *            the length
     * @param destinationFileName
     *            the destination file name
     * @param notifier
     *            the notifier
     * @return the download result
     * @throws JiGetException
     *             the ji get exception
     */
    public DownloadResult download(final URL destURL, final String destinationFileName) throws CoreException;
}
