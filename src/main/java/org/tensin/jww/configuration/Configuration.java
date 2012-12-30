package org.tensin.jww.configuration;

import java.util.ArrayList;
import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Configuration.
 */
@Root(name = "configuration")
public class Configuration {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    /** The sets. */
    @ElementList(name = "set", inline = true)
    private Collection<ConfigurationSet> sets = new ArrayList<ConfigurationSet>();

    /** The smtp. */
    @Element(required = false)
    private SMTP smtp;

    /** The proxy. */
    @Element(required = false)
    private Proxy proxy;

    /**
     * Gets the proxy.
     * 
     * @return the proxy
     */
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * Gets the sets.
     * 
     * @return the sets
     */
    public Collection<ConfigurationSet> getSets() {
        return sets;
    }

    /**
     * Gets the smtp.
     * 
     * @return the smtp
     */
    public SMTP getSmtp() {
        return smtp;
    }

    /**
     * Sets the proxy.
     * 
     * @param proxy
     *            the new proxy
     */
    public void setProxy(final Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Sets the sets.
     * 
     * @param sets
     *            the new sets
     */
    public void setSets(final Collection<ConfigurationSet> sets) {
        this.sets = sets;
    }

    /**
     * Sets the smtp.
     * 
     * @param smtp
     *            the new smtp
     */
    public void setSmtp(final SMTP smtp) {
        this.smtp = smtp;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final ConfigurationSet set : sets) {
            sb.append("Set [" + set.getName() + "] \n" + set.toString());
        }
        return sb.toString();
    }

    /**
     * Update additionnal informations.
     */
    public void updateAdditionnalInformations() {
        for (final ConfigurationSet set : sets) {
            set.updateAdditionnalInformations(this);
        }
    }

}
