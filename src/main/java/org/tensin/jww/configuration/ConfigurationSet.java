package org.tensin.jww.configuration;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.jww.elements.AbstractElement;
import org.tensin.jww.elements.IElement;
import org.tensin.jww.notifiers.INotifier;
import org.tensin.jww.notifiers.MailNotifier;

/**
 * The Class ConfigurationSet.
 */
@Root(name = "set")
public class ConfigurationSet {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationSet.class);

    /** The user. */
    @Attribute(required = false)
    private String name;

    /** The elements. */
    @ElementList(name = "elements")
    private Collection<IElement> elements;

    /** The notifiers. */
    @ElementList(name = "notifiers", required = false)
    private Collection<INotifier> notifiers;

    /**
     * Gets the elements.
     * 
     * @return the elements
     */
    public Collection<IElement> getElements() {
        return elements;
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
    public Collection<INotifier> getNotifiers() {
        return notifiers;
    }

    /**
     * Sets the elements.
     * 
     * @param elements
     *            the new elements
     */
    public void setElements(final Collection<IElement> elements) {
        this.elements = elements;
    }

    /**
     * Sets the user.
     * 
     * @param user
     *            the new user
     */
    public void setNameUser(final String user) {
        name = user;
    }

    /**
     * Sets the notifiers.
     * 
     * @param notifiers
     *            the new notifiers
     */
    public void setNotifiers(final Collection<INotifier> notifiers) {
        this.notifiers = notifiers;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final IElement element : elements) {
            sb.append("   - ").append(element.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Update additionnal informations.
     */
    public void updateAdditionnalInformations(final Configuration configuration) {
        if (StringUtils.isNotEmpty(name)) {
            if (CollectionUtils.isNotEmpty(getElements())) {
                LOGGER.info("Updating set [" + name + "] for [" + getElements().size() + "] elements");
                for (final IElement element : getElements()) {
                    if (element instanceof AbstractElement) {
                        ((AbstractElement) element).setName(name);
                    }
                }
            }
        }

        for (final IElement element : getElements()) {
            if (element instanceof AbstractElement) {
                ((AbstractElement) element).check();
            }
        }

        for (final INotifier notifier : getNotifiers()) {
            if (notifier instanceof MailNotifier) {
                ((MailNotifier) notifier).setSmtp(configuration.getSmtp());
            }
        }

    }
}
