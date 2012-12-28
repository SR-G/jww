package org.tensin.jww.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/**
 * Dump Helper.
 * 
 * @author u248663
 * @version $Revision: 1.5 $
 * @since 2 avr. 2009 09:55:19
 * 
 */
public final class DumpHelper {

    /**
     * Dump du contenu d'une énumération.
     * 
     * @param e
     *            L'énumération à dumper
     * @return La représentation textuelle
     */
    public static String dump(final Enumeration e) {
        final StringBuffer sb = new StringBuffer("");
        if (e == null) {
            sb.append("Variable non initialisée (null)");
        } else {
            sb.append("Elements : \n");
            while (e.hasMoreElements()) {
                sb.append("<");
                sb.append(e.nextElement().toString());
                sb.append(">\n");
            }
        }

        return sb.toString();
    }

    /**
     * Dump du contenu d'une Hashmap.
     * 
     * @param h
     *            La map a dumper
     * @return La représentation textuelle
     */
    public static String dump(final HashMap h) {
        final StringBuffer sb = new StringBuffer("");
        Object item = null;
        if (h == null) {
            sb.append("Variable non initialisée (null)");
        } else {
            if (h.isEmpty()) {
                sb.append("Tableau vide");
            } else {
                final Vector v = new Vector(h.keySet());
                Collections.sort(v);
                final Iterator iterator = v.iterator();
                sb.append("Nombre d'elements <" + h.size() + ">\n");
                while (iterator.hasNext()) {
                    item = iterator.next();
                    sb.append("<");
                    sb.append(item.toString());
                    sb.append("> = <");
                    sb.append(h.get(item).toString());
                    sb.append(">\n");
                }
            }
        }
        return (sb.toString());
    }

    /**
     * Dump du contenu d'une liste.
     * 
     * @param l
     *            La liste à dumper
     * @return La représentation textuelle
     */
    public static String dump(final List l) {
        final StringBuffer sb = new StringBuffer();
        if (l == null) {
            sb.append("Variable non initialisée (null)");
        } else if (l.isEmpty()) {
            sb.append("Liste vide");
        } else {
            final Iterator iterator = l.iterator();
            sb.append("Nombre d'elements <" + l.size() + ">\n");
            while (iterator.hasNext()) {
                sb.append("<");
                sb.append(iterator.next().toString());
                sb.append(">\n");
            }
        }
        return (sb.toString());
    }

    /**
     * Dump du contenu d'une map.
     * 
     * @param h
     *            La map a dumper
     * @return La représentation textuelle
     */
    public static String dump(final Map h) {
        return dump((HashMap) h);
    }

    /**
     * Dump d'un objet.
     * 
     * @param o
     *            L'objet a dumper
     * @return La représentation textuelle
     */
    public static String dump(final Object o) {
        return o.toString();
    }

    /**
     * Affichage du contenu d'un tableau.
     * 
     * @param objects
     *            Objects[]
     * @return String.
     */
    public static String dump(final Object[] objects) {
        final ArrayList a = new ArrayList(Arrays.asList(objects));
        return (dump(a));
    }

    /**
     * Dump d'un ensemble de properties.
     * 
     * @param p
     *            L'objet Properties à dumper
     * @return La représentation textuelle
     */
    public static String dump(final Properties p) {
        final Enumeration enumProperties = p.propertyNames();
        final StringBuffer sb = new StringBuffer();

        int cnt = 0;
        String propName = null;
        String propValue = null;
        for (; enumProperties.hasMoreElements(); cnt++) {
            // Get property name
            propName = (String) enumProperties.nextElement();

            // Get property value
            propValue = (String) p.get(propName);

            sb.append("<").append(propName).append("> = <").append(propValue).append(">\n");
        }

        if (cnt == 0) {
            return "Ensemble Properties vide.";
        } else {
            return "Nombre d'elements <" + cnt + ">\n" + sb.toString();
        }
    }

    /**
     * Dump du contenu d'une liste.
     * 
     * @param l
     *            La liste à dumper
     * @return La représentation textuelle
     */
    public static String singleDump(final Collection l) {
        final StringBuffer sb = new StringBuffer();
        if (l == null) {
            sb.append("[]");
        } else if (l.isEmpty()) {
            sb.append("[]");
        } else {
            final Iterator iterator = l.iterator();
            int cnt = 0;
            while (iterator.hasNext()) {
                if (cnt++ > 0) {
                    sb.append(", ");
                }
                sb.append(iterator.next().toString());
            }
        }
        return (sb.toString());
    }

    /**
     * Method.
     * 
     * @param objects
     *            Object[]
     * @return String
     */
    public static String singleDump(final Object[] objects) {
        final ArrayList a = new ArrayList(Arrays.asList(objects));
        return (singleDump(a));
    }

    /**
     * Constructor. Cannot instanciate.
     */
    private DumpHelper() {
    }
}
