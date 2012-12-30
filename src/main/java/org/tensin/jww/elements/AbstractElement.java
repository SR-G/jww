package org.tensin.jww.elements;

import org.simpleframework.xml.Attribute;

/**
 * The Class AbstractElement.
 */
public abstract class AbstractElement {

    /** The notifiers. */
    @Attribute(name = "notifiers", required = false)
    // @Description("The ID of the notifiers that are concerned by this limit. If empty, all notifiers will be notified.")
    private String notifiers;

    /** The cron. */
    @Attribute(required = false)
    private String cron;

    /** The user. */
    private String name = "_default";

    public abstract void check();

    /**
     * Gets the cron.
     * 
     * @return the cron
     */
    public String getCron() {
        return cron;
    }

    /**
     * Gets the user.
     * 
     * @return the user
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the notifiers.
     * 
     * @return the notifiers
     */
    public String getNotifiers() {
        return notifiers;
    }

    /**
     * Sets the cron.
     * 
     * @param cron
     *            the new cron
     */
    public void setCron(final String cron) {
        this.cron = cron;
    }

    /**
     * Sets the user.
     * 
     * @param user
     *            the new user
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the notifiers.
     * 
     * @param notifiers
     *            the new notifiers
     */
    public void setNotifiers(final String notifiers) {
        this.notifiers = notifiers;
    }

}
