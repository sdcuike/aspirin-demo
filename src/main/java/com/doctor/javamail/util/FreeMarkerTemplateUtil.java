package com.doctor.javamail.util;

import java.io.IOException;
import java.io.StringWriter;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author sdcuike
 *
 *         Create At 2016年6月14日 上午11:25:25
 * 
 *         Utility class for working with FreeMarker.
 *         Provides convenience methods to process a FreeMarker template with a model.
 * 
 * @see org.springframework.ui.freemarker.FreeMarkerTemplateUtils
 * 
 */
public final class FreeMarkerTemplateUtil {
    /**
     * Process the specified FreeMarker template with the given model and write
     * the result to the given Writer.
     * <p>
     * When using this method to prepare a text for a mail to be sent with Spring's
     * mail support, consider wrapping IO/TemplateException in MailPreparationException.
     * 
     * @param model
     *            the model object, typically a Map that contains model names
     *            as keys and model objects as values
     * @return the result as String
     * @throws IOException
     *             if the template wasn't found or couldn't be read
     * @throws freemarker.template.TemplateException
     *             if rendering failed
     * @see org.springframework.mail.MailPreparationException
     */
    public static String processTemplateIntoString(Template template, Object model)
            throws IOException, TemplateException {

        StringWriter result = new StringWriter();
        template.process(model, result);
        return result.toString();
    }
}
