package org.tensin.common;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.tensin.jww.CoreException;
import org.tensin.jww.LogInitializer;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * Lancement Jetty. Dépendances Maven =
 * 
 * <pre>
 *                 <dependency>
 *                         <groupId>org.eclipse.jetty</groupId>
 *                         <artifactId>jetty-server</artifactId>
 *                 </dependency>
 *                 <dependency>
 *                         <groupId>org.eclipse.jetty</groupId>
 *                         <artifactId>jetty-webapp</artifactId>
 *                 </dependency>
 *                 <dependency>
 *                         <groupId>org.eclipse.jetty</groupId>
 *                         <artifactId>jetty-jsp-2.1</artifactId>
 *                 </dependency>
 *                 <dependency>
 *                         <groupId>org.eclipse.jetty</groupId>
 *                         <artifactId>jetty-servlet</artifactId>
 *                 </dependency>
 *                 <dependency>
 *                         <groupId>org.apache.tomcat</groupId>
 *                         <artifactId>jasper</artifactId>
 *                 </dependency>
 * 
 * </pre>
 * 
 * @author u248663
 * @version $Revision: 1.16 $
 * @since 25 nov. 2010 15:36:32
 */
public class EmbeddedJetty {

    /**
     * Thread d'arrêt de Jetty (en externe).
     * 
     * @author u248663
     * @version $Revision: 1.16 $
     * @since 10 févr. 2011 20:23:35
     * 
     */
    private static class MonitorThread extends Thread {

        /** socket. */
        private ServerSocket socket;

        /**
         * Constructor.
         */
        public MonitorThread() {
            setDaemon(true);
            setName("THREAD-JETTY-MONITOR");
            try {
                socket = new ServerSocket(8079, 1, InetAddress.getByName(DEFAULT_LOCALHOST));
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Run.
         * 
         * {@inheritDoc}
         * 
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            Socket accept = null;
            try {
                accept = socket.accept();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                reader.readLine();
                log.info("Stopping jetty embedded server");
                JETTY.stop();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    accept.close();
                    socket.close();
                } catch (final IOException e) {
                    log.error(e);
                }
            }
        }
    }

    /** LOCALHOST. */
    public static final String DEFAULT_LOCALHOST = "127.0.0.1";

    /** The Constant DEFAULT_PORT. */
    public static final int DEFAULT_PORT = 8080;

    /**
     * Méthode de test.
     * 
     * @param args
     *            the arguments
     * @throws Exception
     *             the exception
     */
    public static void main(final String[] args) throws Exception {
        final EmbeddedJetty j = new EmbeddedJetty();
        j.start("/", ".");
    }

    /**
     * Méthode d'arrêt d'un serveur déjà démarré.
     * 
     * @param port
     *            the port
     * @throws CoreException
     *             the pyramide exception
     */
    public static void stopRunningJetty(final int port) throws CoreException {
        OutputStream out = null;
        Socket s = null;
        try {
            s = new Socket(InetAddress.getByName(DEFAULT_LOCALHOST), port);
            out = s.getOutputStream();
            out.write(("\r\n").getBytes());
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
            try {
                s.close();
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /** console. */
    protected final PrintStream standardConsoleOut = System.out;

    /** standardConsoleIn. */
    protected final PrintStream standardConsoleErr = System.err;

    /** initLogger. */
    private boolean initLogger;

    /** JETTY. */
    private static Server JETTY;

    /** monitor. */
    private static Thread monitor;

    /** Logger. */
    private static final Log log = LogFactory.getLog(EmbeddedJetty.class);

    /** The port. */
    private int port = DEFAULT_PORT;

    public int getPort() {
        return port;
    }

    /**
     * Méthode setLoggerLevel.
     * 
     * @param webContentRelativePath
     *            the web content relative path
     * @param webContext
     *            the web context
     * @throws CoreException
     *             the core exception
     * @throws MalformedURLException
     *             the malformed url exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    // public void setLoggerLevel(final String module, final Level level) {
    //        String moduleRenamed = module.replaceAll("/", ".");
    //        Logger logger = Logger.getLogger(moduleRenamed);
    //        if (logger != null) {
    //            logger.setLevel(level);
    //        }
    // }

    /**
     * Method.
     * 
     * @param temp
     *            the temp
     */
    private void purgeJettyCache(final File temp) {
        if ((temp != null) && temp.exists() && temp.isDirectory()) {
            log.info("Purge du répertoire temporaire JETTY [" + temp.getAbsolutePath() + "]");
            temp.delete();
        }
    }

    /**
     * Restore I/O.
     */
    protected void restoreIO() {
        System.setOut(standardConsoleOut);
        System.setErr(standardConsoleErr);
    }

    public void setPort(final int port) {
        this.port = port;
    }

    /**
     * Method.
     * 
     * @param webContentRelativePath
     *            the web content relative path
     * @param webContext
     *            the web context
     * @param resourcesPath
     *            the resources path
     * @throws CoreException
     *             the core exception
     * @throws MalformedURLException
     *             the malformed url exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void start(final String packages, final String webContext) throws CoreException {
        LogInitializer.initLog();

        /*
         * SelectChannelConnector connector = new SelectChannelConnector ();
         * connector.setPort (8080);
         */
        // We explicitly use the SocketConnector because the
        // SelectChannelConnector locks files
        // final Connector connector = new SocketConnector();
        // connector.setPort(getPort());
        // connector.setMaxIdleTime(1000 * 60 * 60 * 4); // 4 heures
        //
        // final WebAppContext app = new WebAppContext();
        // app.setServer(JETTY);
        // app.setWar(webContentRelativePath);
        // app.setContextPath(webContext);
        // app.setCompactPath(true);
        // if (CollectionUtils.isNotEmpty(resourcesPath)) {
        // final ResourceCollection resourceCollection = new ResourceCollection();
        // for (final String url : resourcesPath) {
        // resourceCollection.addPath(url);
        // }
        // app.setBaseResource(resourceCollection);
        // }
        //
        /*
         * app.setDescriptor(webapp+"/WEB-INF/web.xml");
         * app.setResourceBase("../test-jetty-webapp/src/main/webapp");
         */

        /*
         * app.setResourceBase(new
         * ClassPathResource("webapp").getURI().toString());
         */

        // Avoid the taglib configuration because its a PITA if you don't have a
        // net connection
        // app.setConfigurationClasses(new String[] { WebInfConfiguration.class.getName(), WebXmlConfiguration.class.getName() });
        // app.setParentLoaderPriority(true);

        // JETTY.setConnectors(new Connector[] { connector });
        // JETTY.setHandler(app);
        // JETTY.setAttribute("org.mortbay.jetty.Request.maxFormContentSize", 0);
        // JETTY.setStopAtShutdown(true);

        // from http://wiki.eclipse.org/Jetty/Tutorial/Embedding_Jetty
        JETTY = new Server(getPort());
        final ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        JETTY.setHandler(contextHandler);

        // http://stackoverflow.com/questions/9670363/how-do-i-programmatically-configure-jersey-to-use-jackson-for-json-deserializa
        final PackagesResourceConfig prc = new PackagesResourceConfig("org.tensin.jww.jersey");
        // final Map<String, Object> prcProperties = prc.getProperties();
        // prcProperties.put(JSONConfiguration.FEATURE_POJO_MAPPING, true);

        // from http://stackoverflow.com/questions/7421574/embedded-jetty-with-jersey-or-resteasy
        contextHandler.addServlet(new ServletHolder(new ServletContainer(prc)), "/*");

        startJetty();
        // purgeJettyCache(app.getTempDirectory());
    }

    /**
     * Method.
     * 
     * @throws CoreException
     *             the pyramide exception
     */
    private void startJetty() throws CoreException {
        final ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        final ByteArrayOutputStream bytesErr = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(bytesOut));
            System.setErr(new PrintStream(bytesErr));

            monitor = new MonitorThread();
            monitor.start();

            JETTY.start();
        } catch (final Exception e) {
            throw new CoreException(e);
        } finally {
            restoreIO();
            if (!StringUtils.isEmpty(bytesOut.toString())) {
                log.info(bytesOut.toString().trim()); } if
                (!StringUtils.isEmpty(bytesErr.toString())) {
                    log.error(bytesErr.toString().trim());
                }
        }
    }

    /**
     * Méthode stop.
     * 
     * @throws CoreException
     *             Erreur technique
     */
    public void stop() throws CoreException {
        try {
            if (monitor != null) {
                monitor.interrupt();
            }
            JETTY.stop();
        } catch (final Exception e) {
            throw new CoreException(e);
        }
    }
}
