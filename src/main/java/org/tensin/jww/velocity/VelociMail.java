package org.tensin.jww.velocity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.jww.CoreException;
import org.tensin.jww.helpers.DumpHelper;

/**
 * The Class VelociMail.
 */
public class VelociMail {

    /** loader. */
    private static final ClassLoader loader = Thread.currentThread().getContextClassLoader();

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(VelociMail.class);

    /** LOGGER. */

    /**
     * Method.
     * 
     * @param resourceName
     *            the resource name
     * @return true, if is image
     */
    public static boolean isImage(final String resourceName) {
        if (StringUtils.isNotEmpty(resourceName)) {
            final String s = resourceName.toLowerCase();
            return s.endsWith(".jpg") || s.endsWith(".gif") || s.endsWith(".bmp") || s.endsWith(".jpeg") || s.endsWith(".png");
        }
        return false;
    }

    /** template */
    private String template;

    /** layout. */
    private String layout;

    /** velocityContext. */
    private VelocityContext velocityContext;

    /**
     * Constructor.
     */
    public VelociMail() {
        super();
        initVelocity();
    }

    /**
     * Method.
     * 
     * @param key
     *            the key
     * @param object
     *            the object
     */
    public void addContext(final String key, final Object object) {
        velocityContext.put(key, object);
    }

    /**
     * Method.
     * 
     * @param templatePath
     *            the template path
     * @return the input stream reader
     * @throws CoreException
     *             the mojo execution exception
     */
    private Reader getInputStreamReader(final String templatePath) throws CoreException {
        final InputStream is = loader.getResourceAsStream(templatePath);
        if (is == null) {
            throw new CoreException("La ressource '" + templatePath + "' est introuvable");
        }
        return new InputStreamReader(is);
    }

    /**
     * Method.
     * 
     * @return the layout
     */
    public String getLayout() {
        return layout;
    }

    /**
     * Method.
     * 
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Method.
     * 
     * @return the velocity context
     */
    public VelocityContext getVelocityContext() {
        return velocityContext;
    }

    /**
     * Method.
     * 
     */
    private void initVelocity() {
        try {
            Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM, new VelocityLogger());
            Velocity.init();
            velocityContext = new VelocityContext();
        } catch (final Exception e) {
            LOGGER.error("Error while initializing velocity", e);
        }
    }

    /**
     * Method.
     * 
     * @param email
     *            the email
     * @param listUsedImages
     *            the list used images
     */
    private void loadImagesIntoContext(final HtmlEmail email, final Set<String> listUsedImages) {
        final List<String> registeredImages = new ArrayList<String>();
        try {
            // final Resource[] resources = pmrspr.getResources("classpath:" + layout + "**/*");
            final Reflections r = new Reflections("org.tensin");

            String baseResourceName;
            String imgResourceName;
            // String resourceName;
            // for(final Resource resource : resources) {
            for (final String resourceName : r.getResources(Pattern.compile(".*\\.xml"))) {
                // resourceName = resource.getFilename();
                if (isImage(resourceName)) {
                    baseResourceName = resourceName.substring(0, resourceName.lastIndexOf("."));
                    imgResourceName = "img" + StringUtils.capitalize(baseResourceName);
                    if (listUsedImages.contains(imgResourceName)) {
                        LOGGER.debug("Merging image [" + imgResourceName + "]");
                        velocityContext.put(imgResourceName, email.embed(new URL(resourceName), StringUtils.capitalize(baseResourceName)));
                        registeredImages.add(resourceName + " (cid:" + imgResourceName + ")");
                    } else {
                        LOGGER.debug("Not merging image [" + imgResourceName + "] as it is not used in the current template");
                    }
                }
            }
        } catch (final IOException e) {
            LOGGER.error("Can't load layout resource [" + layout + "] into template", e);
        } catch (final EmailException e) {
            LOGGER.error("Can't load layout resource [" + layout + "] into template", e);
        } finally {

        }
        LOGGER.info("Registered images : \n" + DumpHelper.dump(registeredImages));
    }

    /**
     * Method.
     * 
     * @return the sets the
     * @throws CoreException
     *             the mojo execution exception
     */
    private Set<String> loadUsedImagesFromTemplate() throws CoreException {
        final Set<String> listUsedImages = new TreeSet<String>();
        final BufferedReader bis = new BufferedReader(getInputStreamReader(template));
        String line;
        String buffer = "";
        try {
            while ((line = bis.readLine()) != null) {
                buffer += line;
            }
            final Pattern pattern = Pattern.compile("cid:\\$\\{(img[^}]*)\\}");
            final Matcher matcher = pattern.matcher(buffer);
            while (matcher.find()) {
                final String s = matcher.group(1);
                listUsedImages.add(s);
            }
        } catch (final IOException e) {
            throw new CoreException("Error while parsing template [" + template + "]", e);
        } finally {
            // Do not make any close here
        }
        return listUsedImages;
    }

    /**
     * Method.
     * 
     * @param email
     *            the email
     * @return the string
     */
    public String render(final HtmlEmail email) {
        final StringWriter w = new StringWriter();
        try {
            // embed the image and get the content id
            final Set<String> listUsedImages = loadUsedImagesFromTemplate();
            loadImagesIntoContext(email, listUsedImages); // load only images really used in the template
            Velocity.evaluate(velocityContext, w, null, getInputStreamReader(template));
            return w.toString();
        } catch (final ParseErrorException e) {
            LOGGER.error("Error while rendering template", e);
        } catch (final MethodInvocationException e) {
            LOGGER.error("Error while rendering template", e);
        } catch (final ResourceNotFoundException e) {
            LOGGER.error("Error while rendering template", e);
        } catch (final Exception e) {
            LOGGER.error("Error while rendering template", e);
        } finally {
        }

        return null;
    }

    /**
     * Method.
     * 
     * @param layout
     *            the new layout
     */
    public void setLayout(final String layout) {
        this.layout = layout;
    }

    /**
     * Method.
     * 
     * @param template
     *            the new template
     */
    public void setTemplate(final String template) {
        this.template = template;
    }

    /**
     * Method.
     * 
     * @param velocityContext
     *            the new velocity context
     */
    public void setVelocityContext(final VelocityContext velocityContext) {
        this.velocityContext = velocityContext;
    }
}