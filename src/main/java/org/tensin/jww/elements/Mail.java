package org.tensin.jww.elements;

import org.simpleframework.xml.Root;
import org.tensin.jww.AnalyzeResult;
import org.tensin.jww.CoreException;

/**
 * The Class Mail.
 */
@Root(name = "mail")
public class Mail extends AbstractElement implements IElement {

    /* (non-Javadoc)
     * @see org.tensin.jww.elements.IElement#analyze()
     */
    @Override
    public AnalyzeResult analyze() throws CoreException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.tensin.jww.elements.IElement#getName()
     */
    @Override
    public String getName() {
        return null;
    }

}
