package org.tensin.jww.elements;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.tensin.jww.AnalyzeResult;
import org.tensin.jww.CoreException;
import org.tensin.jww.downloaders.web.DownloadResult;
import org.tensin.jww.downloaders.web.DownloaderFactory;
import org.tensin.jww.downloaders.web.IDownloader;
import org.tensin.jww.elements.AbstractElement;
import org.tensin.jww.elements.IElement;
import org.tensin.jww.helpers.MD5Helper;

/**
 * The Class Webpage.
 */
@Root(name = "webpage")
public class Webpage extends AbstractElement implements IElement {

    /** The url. */
    @Attribute
    private String url;

    /* (non-Javadoc)
     * @see org.tensin.jww.elements.IElement#analyze()
     */
    @Override
    public AnalyzeResult analyze() throws CoreException {
        final AnalyzeResult result = new AnalyzeResult();
        final IDownloader downloader = DownloaderFactory.getDownloader();

        try {
            final File destPath = new File("tmp/" + getName());
            FileUtils.forceMkdir(destPath);
            final String oldTempFileName = destPath.getAbsolutePath() + "/" + MD5Helper.encodeFileName(url);

            final String newTempFileName = oldTempFileName + "_";
            final DownloadResult downloadResult = downloader.download(new URL(url), newTempFileName);

            final File oldTempFile = new File(oldTempFileName);
            final File newTempFile = new File(newTempFileName);
            if (oldTempFile.exists()) {
                final String oldFileContent = FileUtils.readFileToString(oldTempFile);
                final String newFileContent = FileUtils.readFileToString(newTempFile);
                if (!StringUtils.equals(oldFileContent, newFileContent)) {
                    result.setHasToBeNotified(true);
                }
                FileUtils.deleteQuietly(oldTempFile);
            }

            FileUtils.moveFile(newTempFile, oldTempFile);

            return result;
        } catch (final MalformedURLException e) {
            throw new CoreException(e);
        } catch (final IOException e) {
            throw new CoreException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.tensin.jww.elements.IElement#getName()
     */
    @Override
    public String getName() {
        return "webpage watching [" + url + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Webpage [url=" + url + "]";
    }

}
