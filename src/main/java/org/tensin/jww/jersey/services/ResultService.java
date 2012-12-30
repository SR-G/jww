package org.tensin.jww.jersey.services;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class ResultService.
 */
@Path("result")
public class ResultService {

    /** The Constant LOGGER. */
    private final static Logger LOGGER = LoggerFactory.getLogger(ResultService.class);

    @GET
    @Path("diff/{name}/{hash}")
    @Produces(MediaType.TEXT_HTML + ";charset=utf-8")
    public Object getDiff(@PathParam("name") final String name, @PathParam("hash") final String hash) {
        final String path = "tmp/" + name + "/diff/" + hash;
        return producesContentFromFile(path);
    }

    /**
     * Gets the html.
     * 
     * @param hash
     *            the hash
     * @return the html
     */
    @GET
    @Path("html/{name}/{hash}")
    @Produces(MediaType.TEXT_HTML + ";charset=utf-8")
    public Object getHTML(@PathParam("name") final String name, @PathParam("hash") final String hash) {
        final String path = "tmp/" + name + "/html/" + hash;
        return producesContentFromFile(path);
    }

    /**
     * Produces content from file.
     * 
     * @param path
     *            the path
     * @return the object
     */
    private Object producesContentFromFile(final String path) {
        LOGGER.info("Serving [" + path + "]");
        final File f = new File(path);
        if (f.exists()) {
            try {
                return FileUtils.readFileToString(f);
            } catch (final IOException e) {
                LOGGER.error("Can't read [" + path + "], e");
            }
        }

        return "File [" + path + "] not found";
    }

}
