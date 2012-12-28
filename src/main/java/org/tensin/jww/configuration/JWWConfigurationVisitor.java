package org.tensin.jww.configuration;

import java.util.Set;

import org.reflections.Reflections;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Visitor;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensin.jww.CoreException;
import org.tensin.jww.elements.IElement;
import org.tensin.jww.helpers.DumpHelper;
import org.tensin.jww.notifiers.INotifier;

/**
 * The Class JWWConfigurationVisitor.
 */
public class JWWConfigurationVisitor implements Visitor {

    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(JWWConfigurationVisitor.class);

    /** The available operations. */
    private static Set<Class<? extends IElement>> availableElements = getAvailableElements();

    /** The available notifiers. */
    private static Set<Class<? extends INotifier>> availableNotifiers = getAvailableNotifiers();

    /** The reflections. */
    private static Reflections reflections;

    /**
     * Gets the available operations.
     * 
     * @return the available operations
     */
    private static Set<Class<? extends IElement>> getAvailableElements() {
        final Set<Class<? extends IElement>> l = getReflections().getSubTypesOf(IElement.class);
        LOGGER.info("Registered elements are : " + DumpHelper.singleDump(l));
        return l;
    }

    /**
     * Gets the available notifiers.
     * 
     * @return the available notifiers
     */
    private static Set<Class<? extends INotifier>> getAvailableNotifiers() {
        final Set<Class<? extends INotifier>> l = reflections.getSubTypesOf(INotifier.class);
        LOGGER.info("Registered notifiers are : " + DumpHelper.singleDump(l));
        return l;
    }

    private static Reflections getReflections() {
        if ( reflections  == null ) {
            reflections = new Reflections("org.tensin");
        }
        return reflections;
    }

    /**
     * Match class with.
     * 
     * @param elementNode
     *            the element node
     * @param inputNode
     *            the input node
     * @return true, if successful
     */
    private boolean matchClassWithElements(final String elementNode, final InputNode inputNode) {
        for (final Class<? extends IElement> clazz : availableElements) {
            final Root annotation = clazz.getAnnotation(Root.class);
            if (annotation.name().equals(elementNode)) {
                LOGGER.info("Registering element [" + elementNode + "] with associated class [" + clazz.getName() + "]");
                inputNode.getAttributes().put("class", clazz.getName());
                return true;
            }
        }
        return false;
    }

    /**
     * Match class with.
     * 
     * @param elementNode
     *            the element node
     * @param inputNode
     *            the input node
     * @return true, if successful
     */
    private boolean matchClassWithNotifiers(final String elementNode, final InputNode inputNode) {
        for (final Class<? extends INotifier> clazz : availableNotifiers) {
            final Root annotation = clazz.getAnnotation(Root.class);
            if (annotation.name().equals(elementNode)) {
                LOGGER.info("Registering node [" + elementNode + "] with associated class [" + clazz.getName() + "]");
                inputNode.getAttributes().put("class", clazz.getName());
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.simpleframework.xml.strategy.Visitor#read(org.simpleframework.xml.strategy.Type, org.simpleframework.xml.stream.NodeMap)
     */
    @Override
    public void read(final Type type, final NodeMap<InputNode> node) throws Exception {
        boolean foundAsNotifier = false;
        boolean foundAsElement = false;
        final String elementNode = node.getName();
        final InputNode inputNode = node.getNode();

        if (type.getType().equals(INotifier.class) || type.getType().equals(IElement.class)) {
            if (type.getType().equals(IElement.class)) {
                foundAsElement = matchClassWithElements(elementNode, inputNode);
            }

            if (type.getType().equals(INotifier.class)) {
                foundAsNotifier = matchClassWithNotifiers(elementNode, inputNode);
            }

            if (!foundAsElement && !foundAsNotifier) {
                throw new CoreException("Found unknow element [" + elementNode + "] with attributes : " + inputNode.getAttributes().toString());
            }
        }
    }

    /* (non-Javadoc)
     * @see org.simpleframework.xml.strategy.Visitor#write(org.simpleframework.xml.strategy.Type, org.simpleframework.xml.stream.NodeMap)
     */
    @Override
    public void write(final Type type, final NodeMap<OutputNode> node) throws Exception {
    }

}
