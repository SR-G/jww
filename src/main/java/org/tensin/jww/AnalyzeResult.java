package org.tensin.jww;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class AnalyzeResult.
 */
public class AnalyzeResult {

    /** The has to be notified. */
    private boolean hasToBeNotified = false;

    /** The url. */
    private String url;

    /** The content. */
    private String content;

    /** The content file name. */
    private String contentFileName;

    /** The timestamp. */
    private long timestamp;

    /** The diff. */
    private String diff;

    /** The name. */
    private String name;

    /** The change count. */
    private long changeCount = 1;

    /** The title. */
    private String title;

    /** The Constant SHORTENED_URL_SIZE. */
    private final static int SHORTENED_URL_SIZE = 100;

    /**
     * Instantiates a new analyze result.
     */
    public AnalyzeResult() {
        super();
        timestamp = new Date().getTime();
    }

    /**
     * Gets the change count.
     * 
     * @return the change count
     */
    public long getChangeCount() {
        return changeCount;
    }

    /**
     * Gets the content.
     * 
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets the content file base name.
     * 
     * @return the content file base name
     */
    public String getContentFileBaseName() {
        return new File(contentFileName).getName();
    }

    /**
     * Gets the content file name.
     * 
     * @return the content file name
     */
    public String getContentFileName() {
        return contentFileName;
    }

    /**
     * Gets the date.
     * 
     * @return the date
     */
    public String getDate() {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }

    /**
     * Gets the diff.
     * 
     * @return the diff
     */
    public String getDiff() {
        return diff;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the shortened url.
     * 
     * @return the shortened url
     */
    public String getShortenedUrl() {
        if (StringUtils.isNotEmpty(url)) {
            if (url.length() < SHORTENED_URL_SIZE) {
                return url;
            } else {
                return url.substring(0, SHORTENED_URL_SIZE) + "...";
            }
        }
        return "";
    }

    /**
     * Gets the timestamp.
     * 
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the url.
     * 
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Checks for to be notified.
     * 
     * @return true, if successful
     */
    public boolean hasToBeNotified() {
        return hasToBeNotified;
    }

    /**
     * Sets the change count.
     * 
     * @param changeCount
     *            the new change count
     */
    public void setChangeCount(final long changeCount) {
        this.changeCount = changeCount;
    }

    /**
     * Sets the content.
     * 
     * @param content
     *            the new content
     */
    public void setContent(final String content) {
        this.content = content;
    }

    /**
     * Sets the content file name.
     * 
     * @param contentFileName
     *            the new content file name
     */
    public void setContentFileName(final String contentFileName) {
        this.contentFileName = contentFileName;
    }

    /**
     * Sets the diff.
     * 
     * @param diff
     *            the new diff
     */
    public void setDiff(final String diff) {
        this.diff = diff;
    }

    /**
     * Sets the checks for to be notified.
     * 
     * @param hasToBeNotified
     *            the new checks for to be notified
     */
    public void setHasToBeNotified(final boolean hasToBeNotified) {
        this.hasToBeNotified = hasToBeNotified;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the timestamp.
     * 
     * @param timestamp
     *            the new timestamp
     */
    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Sets the title.
     * 
     * @param title
     *            the new title
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Sets the url.
     * 
     * @param url
     *            the new url
     */
    public void setUrl(final String url) {
        this.url = url;
    }

}
