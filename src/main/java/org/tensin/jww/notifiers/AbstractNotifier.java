package org.tensin.jww.notifiers;

import org.simpleframework.xml.Attribute;


/**
 * The Class AbstractNotifier.
 */
public abstract class AbstractNotifier {

    /** The id. */
    @Attribute(required = true)
    private String id;

    /** The cron. */
    @Attribute(required = false)
    private String cron;

    public String getCron() {
        return cron;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    public void setCron(final String cron) {
        this.cron = cron;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the new id
     */
    public void setId(final String id) {
        this.id = id;
    }

}
