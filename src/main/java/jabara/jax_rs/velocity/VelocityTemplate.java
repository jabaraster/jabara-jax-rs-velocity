/**
 * 
 */
package jabara.jax_rs.velocity;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;

/**
 * @author jabaraster
 */
public class VelocityTemplate {

    private final Class<?>        templateBase;
    private final String          templatePath;
    private final VelocityContext context;

    /**
     * @param pTemplateBase -
     * @param pTemplatePath -
     * @param pContext -
     */
    public VelocityTemplate(final Class<?> pTemplateBase, final String pTemplatePath, final Map<String, Object> pContext) {
        checkNull(pTemplateBase, "pTemplateBase"); //$NON-NLS-1$
        checkNull(pTemplatePath, "pTemplatePath"); //$NON-NLS-1$
        checkNull(pContext, "pContext"); //$NON-NLS-1$

        this.templateBase = pTemplateBase;
        this.templatePath = pTemplatePath;
        this.context = new VelocityContext(new HashMap<String, Object>(pContext));
    }

    /**
     * @return contextを返す.
     */
    public VelocityContext getContext() {
        return this.context;
    }

    /**
     * @return templateBaseを返す.
     */
    public Class<?> getTemplateBase() {
        return this.templateBase;
    }

    /**
     * @return templatePathを返す.
     */
    public String getTemplatePath() {
        return this.templatePath;
    }

    private static void checkNull(final Object pValue, final String pArgumentName) {
        if (pValue == null) {
            throw new IllegalArgumentException(pArgumentName + " is not allowed null."); //$NON-NLS-1$
        }
        if (pValue instanceof String && ((String) pValue).length() == 0) {
            throw new IllegalArgumentException(pArgumentName + " is not allowed null or empty."); //$NON-NLS-1$
        }
    }
}
