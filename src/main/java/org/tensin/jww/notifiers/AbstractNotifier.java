package org.tensin.jww.notifiers;

import org.simpleframework.xml.Attribute;


/**
 * The Class AbstractNotifier.
 */
public abstract class AbstractNotifier {

    /** The id. */
    @Attribute(required = true)
    private String id;

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
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
