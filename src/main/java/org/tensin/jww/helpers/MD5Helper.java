package org.tensin.jww.helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The Class MD5Helper.
 */
public final class MD5Helper {

    /** The Constant MD5. */
    private static final String MD5 = "MD5";

    /** The md. */
    private static MessageDigest md;

    /** The Constant carr. */
    private static final char[] carr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * Encode file name.
     * 
     * @param url
     *            the url
     * @return the string
     */
    public static String encodeFileName(final String url) {
        final byte[] rawData = getMessageDigest().digest(url.getBytes());

        final StringBuffer printable = new StringBuffer();
        for (final byte element : rawData) {
            printable.append(carr[((element & 0xF0) >> 4)]);
            printable.append(carr[(element & 0x0F)]);
        }

        return printable.toString();
    }

    /**
     * Gets the message digest.
     * 
     * @return the message digest
     */
    private static MessageDigest getMessageDigest() {
        if (md == null) {
            try {
                md = MessageDigest.getInstance(MD5);
            } catch (final NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return md;
    }

    /**
     * Instantiates a new m d5 helper.
     */
    private MD5Helper() {

    }

}
