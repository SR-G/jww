package org.tensin.jww.configuration;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * The Class SMTP.
 */
@Root(name = "smtp")
public class SMTP {

    /** The smtp hostname. */
    @Attribute(required = false)
    // @Description("SMTP server for sending the mail")
    private String hostname;

    /** The smtp login. */
    @Attribute(required = false)
    // @Description("SMTP username")
    private String login;

    /** The smtp password. */
    @Attribute(required = false)
    // @Description("SMTP password")
    private String password;

    /** The smtp port. */
    @Attribute(required = false)
    // @Description("SMTP port. Default to 587 for gmail (TLS)")
    private int port;

    /** The tls. */
    @Attribute(required = false)
    private boolean tls;

    /**
     * Gets the hostname.
     * 
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Gets the login.
     * 
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Gets the password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the port.
     * 
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Checks if is tls.
     * 
     * @return true, if is tls
     */
    public boolean isTls() {
        return tls;
    }

    /**
     * Sets the hostname.
     * 
     * @param hostname
     *            the new hostname
     */
    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    /**
     * Sets the login.
     * 
     * @param login
     *            the new login
     */
    public void setLogin(final String login) {
        this.login = login;
    }

    /**
     * Sets the password.
     * 
     * @param password
     *            the new password
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Sets the port.
     * 
     * @param port
     *            the new port
     */
    public void setPort(final int port) {
        this.port = port;
    }

    /**
     * Sets the tls.
     * 
     * @param tls
     *            the new tls
     */
    public void setTls(final boolean tls) {
        this.tls = tls;
    }

}
