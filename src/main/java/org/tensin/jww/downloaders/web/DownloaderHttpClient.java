package org.tensin.jww.downloaders.web;

import java.net.URL;

import org.tensin.jww.CoreException;

/**
 * The Class DownloaderHttpClient.
 * 
 * @author u248663
 * @version $Revision: 1.1 $
 * @since 19 déc. 2009 02:24:58
 */
public class DownloaderHttpClient implements IDownloader {

    /**
     * {@inheritDoc}
     * 
     * @see org.tensin.jiget.IDownloader#download(java.lang.String, java.lang.String, org.tensin.jiget.JiGetNotifier)
     */
    @Override
    public DownloadResult download(final URL destURL, final String destinationFileName) throws CoreException {
        /*
		byte[] sresponse = null;

		try {
			sresponse = getResource(url);

			File file = new File(createFileName(url, prefixSubstitute, substituteReplacement));

			saveToFile(file.getAbsolutePath(), sresponse);

			substitutePrefix(file.getAbsolutePath(), prefixSubstitute, substituteReplacement);
		} catch (MalformedURLException e) {
			log.error(".downloadUsingHttpClient(): ", e);
		} catch (FileNotFoundException e) {
			log.error(".downloadUsingHttpClient(): ", e);
		} catch (IOException e) {
			log.error(".downloadUsingHttpClient(): ", e);
		}
         */
        return new DownloadResult ();
    }
}
