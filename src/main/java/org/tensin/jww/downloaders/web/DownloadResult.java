package org.tensin.jww.downloaders.web;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class DownloadResult.
 * 
 * @author u248663
 * @version $Revision: 1.1 $
 * @since 30 juil. 2010 13:22:34
 */
public class DownloadResult {

    /** totalSize. */
    private long totalSize;

    /** downloadedSize. */
    private long downloadedSize;

    /** finished. */
    private boolean finished;

    /** cr. */
    private int cr;

    /** url. */
    private String url;

    /** destFileName. */
    private String destFileName;

    /** description. */
    private String description;

    /** The content encoding. */
    private String contentEncoding;

    /**
     * Constructeur.
     */
    public DownloadResult() {

    }

    /**
     * Constructeur.
     * 
     * @param cr
     *            the cr
     * @param url
     *            the url
     * @param destFileName
     *            the dest file name
     */
    public DownloadResult(final int cr, final String url, final String destFileName) {
        this.cr = cr;
        this.url = url;
        this.destFileName = destFileName;
    }

    /**
     * Gets the content encoding.
     * 
     * @return the content encoding
     */
    public String getContentEncoding() {
        if (StringUtils.isEmpty(contentEncoding)) {
            return "UTF-8";
        } else {
            return contentEncoding;
        }
    }

    /**
     * Méthode getCr.
     * 
     * @return the cr
     */
    public int getCr() {
        return cr;
    }

    /**
     * Méthode getDescription.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Méthode getDestFileName.
     * 
     * @return the dest file name
     */
    public String getDestFileName() {
        return destFileName;
    }

    /**
     * Méthode getDownloadedSize.
     * 
     * @return the downloaded size
     */
    public long getDownloadedSize() {
        return downloadedSize;
    }

    /**
     * Méthode getTotalSize.
     * 
     * @return the total size
     */
    public long getTotalSize() {
        return totalSize;
    }

    /**
     * Méthode getUrl.
     * 
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Méthode isFinished.
     * 
     * @return true, if is finished
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Sets the download encoding.
     * 
     * @param contentEncoding
     *            the new download encoding
     */
    public void setContentEncoding(final String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    /**
     * Méthode setCr.
     * 
     * @param cr
     *            the new cr
     */
    public void setCr(final int cr) {
        this.cr = cr;
    }

    /**
     * Méthode setDescription.
     * 
     * @param description
     *            the new description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Méthode setDestFileName.
     * 
     * @param destFileName
     *            the new dest file name
     */
    public void setDestFileName(final String destFileName) {
        this.destFileName = destFileName;
    }

    /**
     * Méthode setDownloadedSize.
     * 
     * @param downloadedSize
     *            the new downloaded size
     */
    public void setDownloadedSize(final long downloadedSize) {
        this.downloadedSize = downloadedSize;
    }

    /**
     * Méthode setFinished.
     * 
     * @param finished
     *            the new finished
     */
    public void setFinished(final boolean finished) {
        this.finished = finished;
    }

    /**
     * Méthode setTotalSize.
     * 
     * @param totalSize
     *            the new total size
     */
    public void setTotalSize(final long totalSize) {
        this.totalSize = totalSize;
    }

    /**
     * Méthode setUrl.
     * 
     * @param url
     *            the new url
     */
    public void setUrl(final String url) {
        this.url = url;
    }
}
