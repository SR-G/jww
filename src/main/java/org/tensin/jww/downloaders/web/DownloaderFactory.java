package org.tensin.jww.downloaders.web;

/**
 * A factory for creating Downloader objects.
 * 
 * @author u248663
 * @version $Revision: 1.1 $
 * @since 30 juil. 2010 13:21:31
 */
public class DownloaderFactory {

    /**
     * Gets the downloader.
     * 
     * @return the downloader
     */
    public static IDownloader getDownloader() {
        return new DownloaderBufferedReader();
    }
}
