package org.tensin.jww.elements;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.jww.AnalyzeResult;
import org.tensin.jww.CoreException;
import org.tensin.jww.diff_match_patch;
import org.tensin.jww.diff_match_patch.Diff;
import org.tensin.jww.downloaders.web.DownloadResult;
import org.tensin.jww.downloaders.web.DownloaderFactory;
import org.tensin.jww.downloaders.web.IDownloader;
import org.tensin.jww.helpers.MD5Helper;

/**
 * The Class Webpage.
 */
@Root(name = "webpage")
public class Webpage extends AbstractElement implements IElement {

    /** The Constant Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Webpage.class);

    /** The url. */
    @Attribute
    private String url;

    /**
     * Analyse content.
     * 
     * @param oldTempFile
     *            the old temp file
     * @param newTempFile
     *            the new temp file
     * @param charset
     *            the charset
     * @param analyzeResult
     *            the analyze result
     * @param diffFileName
     *            the diff file name
     * @throws CoreException
     *             the core exception
     */
    private void analyseContent(final File oldTempFile, final File newTempFile, final String charset,
            final AnalyzeResult analyzeResult, final String diffFileName) throws CoreException {
        try {
            final Document jsoup1 = Jsoup.parse(oldTempFile, null, url);
            final Document jsoup2 = Jsoup.parse(newTempFile, null, url);
            final String s1 = jsoup1.text();
            final String s2 = jsoup2.text();

            if (!StringUtils.equalsIgnoreCase(s1, s2)) {
                final Elements titles = jsoup2.getElementsByTag("title");
                if (titles.size() > 0) {
                    analyzeResult.setTitle(titles.get(0).text());
                }
                analyzeResult.setContent(jsoup2.toString());
                analyzeResult.setContentFileName(oldTempFile.getAbsolutePath());
                analyzeResult.setHasToBeNotified(true);
                analyzeResult.setName(getName());
                analyzeResult.setDiff(htmlDiff2(diffFileName, s1, s2));
            }
            makeUrlAbsolute(jsoup2);
            FileUtils.writeStringToFile(oldTempFile, jsoup2.toString());
        } catch (final IOException e) {
            throw new CoreException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.tensin.jww.elements.IElement#analyze()
     */
    @Override
    public AnalyzeResult analyze() throws CoreException {
        final AnalyzeResult result = new AnalyzeResult();
        final IDownloader downloader = DownloaderFactory.getDownloader();
        result.setUrl(url);

        try {
            final File destHTMLPath = new File("tmp/" + getName() + "/html");
            final File destDiffPath = new File("tmp/" + getName() + "/diff");
            FileUtils.forceMkdir(destHTMLPath);
            FileUtils.forceMkdir(destDiffPath);
            final String encodedFileName = MD5Helper.encodeFileName(url);
            final String oldTempFileName = destHTMLPath.getAbsolutePath() + "/" + encodedFileName;
            final String diffFileName = destDiffPath.getAbsolutePath() + "/" + encodedFileName;

            final String newTempFileName = oldTempFileName + "_";
            final DownloadResult downloadResult = downloader.download(new URL(url), newTempFileName);

            if (downloadResult.isFinished() && (downloadResult.getTotalSize() > 0)) {

                final File oldTempFile = new File(oldTempFileName);
                final File newTempFile = new File(newTempFileName);
                if (oldTempFile.exists()) {
                    try {
                        analyseContent(oldTempFile, newTempFile, "UTF-8", result, diffFileName);
                    } catch (final CoreException e) {
                        LOGGER.error("Error while analyzing [" + newTempFileName + "]", e);
                    } finally {
                        FileUtils.deleteQuietly(newTempFile);
                    }
                } else {
                    FileUtils.moveFile(newTempFile, oldTempFile);
                }
            } else {
                LOGGER.error("Can't download [" + url + "]");
            }

            return result;
        } catch (final MalformedURLException e) {
            throw new CoreException(e);
        } catch (final IOException e) {
            throw new CoreException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.tensin.jww.elements.AbstractElement#check()
     */
    @Override
    public void check() {
        if (StringUtils.isNotEmpty(url)) {
            if (!url.startsWith("http://") && !url.startsWith("file:") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
        }
    }

    /**
     * Html diff2.
     * 
     * @param diffFileName
     *            the diff file name
     * @param oldContent
     *            the old content
     * @param newContent
     *            the new content
     * @return the string
     * @throws CoreException
     *             the core exception
     */
    private String htmlDiff2(final String diffFileName, final String oldContent, final String newContent) throws CoreException {
        // try {
        // final DetailedDiff myDiff = new DetailedDiff(new Diff(oldContent, newContent));
        // final List differences = myDiff.getAllDifferences();
        // final StringBuilder sb = new StringBuilder();
        // for (final Object object : differences) {
        // final Difference difference = (Difference) object;
        // sb.append(difference).append("\n");
        // }
        // // FileUtils.writeStringToFile(new File(diffFileName), sb.toString());
        // return sb.toString();
        // } catch (final SAXException e) {
        // throw new CoreException(e);
        // } catch (final IOException e) {
        // throw new CoreException(e);
        // }
        final diff_match_patch dmp = new diff_match_patch();
        dmp.Diff_Timeout = 5.0f;
        final LinkedList<Diff> diffs = dmp.diff_main(oldContent, newContent);

        final String result = dmp.diff_prettyHtml(diffs);
        try {
            FileUtils.writeStringToFile(new File(diffFileName), result);
        } catch (final IOException e) {
            throw new CoreException(e);
        }

        return result;
    }

    private void makeUrlAbsolute(final Document doc) {
        final Elements links = doc.select("a[href]"); // a with href
        for (final Element element : links) {
            final String absoluteUrl = element.attr("abs:href");
            element.attr("href", absoluteUrl);
        }

        final Elements images = doc.select("img[src]");
        for (final Element element : images) {
            final String absoluteUrl = element.attr("abs:src");
            element.attr("src", absoluteUrl);
        }

    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Webpage [url=" + url + "]";
    }

}
