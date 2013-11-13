/*
 * Adapted from work by Christophe Lauret and Willy Ekasalim
 *
 * Open Source Initiative OSI - The MIT License:Licensing
 * [OSI Approved License]
 *
 * The MIT License
 *
 * Copyright (c) 2008 Rick Jelliffe, Topologi Pty. Ltd, Allette Systems
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.weborganic.schematron;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * A ValidatorFactory instance can be used to create a Validator object.
 *
 * @author Christophe Lauret
 *
 * @version 11 March 2013
 */
public final class ValidatorFactory {

  public enum QueryBinding {XSLT1, XSLT2, AUTO};

  /**
   * Where all the XSLT used by Schematron are located.
   */
  private static final String XSLT_ROOT = "/org/weborganic/schematron/xslt";

  /** The standard schematron preprocessor */
  private static final String INCLUDE_PREPROCESSOR = XSLT_ROOT+"/iso_dsdl_include.xsl";
  private static final String ABSTRACT_PREPROCESSOR = XSLT_ROOT+"/iso_abstract_expand.xsl";

  private static final String PREPROCESSOR_MESSAGE_XSLT1 = XSLT_ROOT+"/iso_schematron_message.xsl";
  private static final String PREPROCESSOR_MESSAGE_XSLT2 = XSLT_ROOT+"/iso_schematron_message_xslt2.xsl";

  private static final String PREPROCESSOR_SVRL_XSLT1 = XSLT_ROOT+"/iso_svrl_for_xslt1.xsl";
  private static final String PREPROCESSOR_SVRL_XSLT2 = XSLT_ROOT+"/iso_svrl_for_xslt2.xsl";

  /**
   * Default XSLT processor.
   */
  private TransformerFactory _factory = TransformerFactory.newInstance();

  /**
   * The entity resolver class.
   */
  private Class<URIResolver> _resolver;

  /**
   * The error event listener for the ValidatorFactory.
   */
  private ErrorListener _listener = this._factory.getErrorListener();

  /**
   * The parameters to be sent to the preprocessor.
   */
  private Map<String, Object> _parameters = new HashMap<String, Object>();

  /** The preprocessor to use. */
  private final Source _abstract_preprocessor;
  private final Source _include_preprocessor;
  private final Source _preprocessor;

  /**
   * If set to <code>true</code> (default is <code>false</code>), then preprocessing stylesheet will be outputted to
   * file debug.xslt
   */
  private boolean debugMode = false;

  // constructors
  // -----------------------------------------------------------------------------------

  /**
   * Constructs a new factory using the default preprocessor.
   */
  public ValidatorFactory() {
    this._preprocessor = resolveDefaultPreprocessor();
    this._include_preprocessor = resolvePreprocessor(INCLUDE_PREPROCESSOR);
    this._abstract_preprocessor = resolvePreprocessor(ABSTRACT_PREPROCESSOR);
  }

  /**
   * Constructs a new factory object using the specified preprocessor.
   *
   * @param preprocessor The preprocessor which generates the validating stylesheet.
   *
   * @throws NullPointerException If preprocessor is <code>null</code>.
   */
  public ValidatorFactory(Source preprocessor) {
    if (preprocessor == null) throw new NullPointerException("The preprocessor must not be null.");
    this._preprocessor = preprocessor;
    this._include_preprocessor = resolvePreprocessor(INCLUDE_PREPROCESSOR);
    this._abstract_preprocessor = resolvePreprocessor(ABSTRACT_PREPROCESSOR);
  }

  /**
   * Constructs a new ValidatorFactory object using the specified preprocessor.
   *
   * <p>Actually, this is not necessary.
   *
   * <p>Future versions may remove this so that only the SVRL is output.
   *
   * @param preprocessor The preprocessor which generates the validating stylesheet.
   *
   * @throws IllegalArgumentException
   *           If preprocessor is <code>null</code>. */
  public ValidatorFactory(QueryBinding binding, String formatter) {
    if (binding == null) throw new NullPointerException("The preprocessor must not be null.");
    String preprocessor = null;

    if (binding == QueryBinding.XSLT2)
      if (formatter.equalsIgnoreCase("message"))
        preprocessor = PREPROCESSOR_MESSAGE_XSLT2;
      else
        preprocessor = PREPROCESSOR_SVRL_XSLT2;

    else if (binding == QueryBinding.XSLT1)
      if (formatter.equalsIgnoreCase("message"))
        preprocessor = PREPROCESSOR_MESSAGE_XSLT1;
      else
        preprocessor = PREPROCESSOR_SVRL_XSLT1;

    // TODO "auto" is handled as the default
    else
      if (formatter.equalsIgnoreCase("message"))
        preprocessor = PREPROCESSOR_MESSAGE_XSLT1;
      else
        preprocessor = PREPROCESSOR_SVRL_XSLT1;

    // Resolve the preprocessors from the resource
    this._preprocessor = resolvePreprocessor(preprocessor);
    this._include_preprocessor = resolvePreprocessor(INCLUDE_PREPROCESSOR);
    this._abstract_preprocessor = resolvePreprocessor(ABSTRACT_PREPROCESSOR);
  }

  // public methods
  // ----------------------------------------------------------------------------------

  /**
   * Set the error event listener for the ValidatorFactory, which is used for the processing of the
   * Schematron schema, not for the Schematron validation itself.
   *
   * @param listener The error listener.
   *
   * @throws IllegalArgumentException If listener is <code>null</code>. */
  public void setErrorListener(ErrorListener listener) {
    if (listener == null) throw new NullPointerException("The error listener must not be null.");
    this._listener = listener;
  }

  /**
   * Get the error event handler for this factory.
   *
   * @return The current error handler, which should never be <code>null</code>.
   */
  public ErrorListener getErrorListener() {
    return this._listener;
  }

  /**
   * Add a parameter to be sent to the preprocessor.
   *
   * @see javax.xml.transform.Transformer#setParameter(String, Object)
   *
   * @param name  The name of the parameter.
   * @param value The value object.
   */
  public void setParameter(String name, Object value) {
    this._parameters.put(name, value);
  }

  /**
   * Returns the parameters value for hte specified name.
   *
   * @param nameThe name of the parameter.
   *
   * @return The parameter value or <code>null</code> if the parameter was not specified.
   */
  public Object getParameter(String name) {
    return this._parameters.get(name);
  }

  /**
   * If debug mode is set to true, then preprocessing stylesheet will be outputted in file
   * debug.xslt This has to be called before <code>newValidator()</code> to take effect.
   */
  public void setDebugMode(boolean debugMode) {
    this.debugMode = debugMode;
  }

  /**
   * Set the class name of the resolver to use, overriding built-in Apache resolver
   */
  public void setResolver(Class<URIResolver> resolver) {
    this._resolver = resolver;
  }

  // helper methods for the constructors
  // -------------------------------------------------------------

  /**
   * Returns the default preprocessor which is included in the classpath or the jar file resp.
   *
   * @return the default preprocessor which is included in the classpath or the jar file resp.
   *
   * @throws IllegalArgumentException If the pre-processor cannot be found in the classpath.
   */
  private Source resolveDefaultPreprocessor() {
    TransformerFactory f = TransformerFactory.newInstance();
    // TODO Detect if XSLT2 is supported. system-property('xsl:version')??
    if (f.getClass().getName().startsWith("net.sf.saxon")) {
      return resolvePreprocessor(PREPROCESSOR_SVRL_XSLT2);
    } else {
      return resolvePreprocessor(PREPROCESSOR_SVRL_XSLT1);
    }
  }

  /**
   * Returns the preprocessor which is included in the classpath or the jar file resp.
   *
   * @return the preprocessor which is included in the classpath or the jar file resp.
   *
   * @throws IllegalArgumentException If the pre-processor cannot be found in the classpath.
   */
  private Source resolvePreprocessor(String stylesheet) {
    URL url = ValidatorFactory.class.getResource(stylesheet);
    if (url == null)
      throw new IllegalArgumentException("preprocessor '"+stylesheet+"' cannot be found in the classpath.");
    return new StreamSource(url.toString());
  }

  /**
   * Process the specified schema into a Validator object.
   *
   * @param schema The Schematron schema to use.
   *
   * @return A Validator instance for the specified schema.
   *
   * @throws TransformerExceptionShould an exception be while attempting to instantiate a validator.
   */
  public Validator newValidator(File schema) throws SchematronException {
    if (!schema.exists())
      throw new SchematronException("Unable to find schema", new FileNotFoundException(schema.getPath()));
    StreamSource source = new StreamSource(schema);
    return newValidator(source);
  }

  /**
   * Process the specified schema into a Validator object.
   *
   * @param schema The Schematron schema to use.
   *
   * @return A Validator instance for the specified schema.
   *
   * @throws SchematronException Will wrap any exception occurring while attempting to instantiate a validator.
   */
  public Validator newValidator(Source schema) throws SchematronException {
    Transformer include_transformer = compile(this._factory, this._include_preprocessor);
    Transformer abstract_transformer = compile(this._factory, this._abstract_preprocessor);

    // Set URIResolver for transformer to locate xsl:include and xsl:import
    this._factory.setURIResolver(new XSLTURIFinder());
    Transformer transformer = compile(this._factory, this._preprocessor);

    // Configure each transformer
    prepare(include_transformer, this._listener, this._parameters);
    prepare(abstract_transformer, this._listener, this._parameters);
    prepare(transformer, this._listener, this._parameters);

    // Check that results can be output as a stream
    if (!this._factory.getFeature(StreamResult.FEATURE))
      throw new SchematronException("The XSLT processor must support following feature: " + StreamResult.FEATURE);

    // Recipient for the transformation
    Interim interim = new Interim(schema.getSystemId());

    // Make the transformation using the preprocessor
    try {

      // 1) Preprocess inclusions
      DOMResult r1 = new DOMResult();
      include_transformer.transform(schema, r1);

      // 2) Preprocess abstracts
      DOMResult r2 = new DOMResult();
      DOMSource s2 = new DOMSource(r1.getNode());
      abstract_transformer.transform(s2, r2);
      r1 = null; // housekeeping for large schemas

      // 3) generate output
      DOMSource s3 = new DOMSource(r2.getNode());
      transformer.transform(s3, interim.makeEmptyResult());
      r2 = null; // housekeeping for large schemas

    } catch (TransformerException ex) {
      throw new SchematronException("Unable to process "+schema.getSystemId()+" to generate new Validator", ex);
    }

    // check debug mode, if true then print the preprocessing results to debug.xslt
    if (this.debugMode) {
      try {
        interim.saveAs(new File("debug.xslt"));
      } catch (IOException ex) {
        throw new SchematronException("Unable to save debug.xslt", ex);
      }
    }

    // Generate the templates from the preprocessing results
    if (this._resolver != null) {
      try {
        this._factory.setURIResolver(this._resolver.newInstance());
      } catch (Throwable t) {
        throw new SchematronException("Unable to instantiate new resolver", t);
      }
    }
    Templates validator = null;
    try {
      validator = this._factory.newTemplates(interim.getSource());
    } catch (TransformerException ex) {
      throw new SchematronException("Unable to generate new Validator from preprocessed "+schema.getSystemId(), ex);
    }
    return new Validator(validator);
  }


  /**
   * Return a new transformer from the Schematron preprocessors XSLT source.
   *
   * @param factory The transformer factory to use.
   * @param source  The Schematron preprocessors XSLT source.
   *
   * @return the corresponding transformer
   *
   * @throws SchematronException Wraps a TransformerConfigurationException
   */
  private static Transformer compile(TransformerFactory factory, Source source) throws SchematronException{
    Transformer transformer = null;
    try {
      transformer = factory.newTransformer(source);
    } catch (TransformerConfigurationException ex) {
      throw new SchematronException("Unable to compile "+source.getSystemId()+" to generate new Validator", ex);
    }
    return transformer;
  }

  /**
   * Set the error listener and parameters to the specified transformer
   *
   * @param transformer Transformer to be updated
   * @param listener    The listener to use
   * @param parameters  The parameters to specify
   */
  private static void prepare(Transformer transformer, ErrorListener listener, Map<String, Object> parameters) {
    transformer.setErrorListener(listener);

    // set some parameters if specified (All transformers get all parameters)
    if (!parameters.isEmpty()) {
      for (Entry<String, Object> p : parameters.entrySet()) {
        String name = p.getKey();
        transformer.setParameter(name, p.getValue());
      }
    }
  }


  /**
   * An object to store information about the compiled stylesheet before it is parsed as a template.
   *
   * @author Christophe Lauret
   * @version 4 January 2007
   */
  private static class Interim {

    /** The string writer to use. */
    private StringWriter writer;

    /** The system identifier of the source Schematron Schema. */
    private final String systemId;

    /**
     * Creates a new Interim instance.
     *
     * @param systemId The system identifier of the source Schematron Schema.
     */
    public Interim(String systemId) {
      this.systemId = systemId;
    }

    /**
     * Returns a <code>Result</code> document ready to use by the transformer.
     *
     * @return a <code>Result</code> ready to use by a <code>Transformer</code>.
     *
     * @throws IllegalStateException If this method has already been invoked.
     */
    public Result makeEmptyResult() throws IllegalStateException {
      if (this.writer != null) throw new IllegalStateException("The templates have already been produced.");
      this.writer = new StringWriter();
      return new StreamResult(this.writer);
    }

    /**
     * Returns a <code>Source</code> ready to use by a <code>Transformer</code> from the previously produced result.
     *
     * @return a <code>Source</code> ready to use by a <code>Transformer</code>.
     *
     * @throws IllegalStateException If this method is invoked before results have been produced.
     */
    public Source getSource() throws IllegalStateException {
      if (this.writer == null) throw new IllegalStateException("The templates have not been produced.");
      StreamSource source = new StreamSource(new StringReader(this.writer.toString()));
      // source.setSystemId( this.systemId);
      source.setPublicId("compiled:" + this.systemId);
      return source;
    }

    /**
     * Print out the preprocessing stylesheet to given <code>File</code>.
     *
     * @throws IOException if there are restriction when accessing file debug.xslt.
     */
    public void saveAs(File file) throws IOException {
      PrintStream pout = new PrintStream(file, "utf-8");
      pout.print(this.writer.toString());
      pout.close();
    }

  }

  /**
   * An Object class that implements URIResolver interface This class implements "resolve" method
   * that will be called whenever <code>xsl:include</code> and <code>xsl:import</code> is
   * encountered.
   *
   * This class is designed to allow stylesheets to find other imported / included stylesheets from
   * within the same jar.
   *
   * (RJ: guess It is used by the compiling stylesheet to allow location within the same jar? Really
   * not sure why this is needed.)
   *
   * @author Christophe lauret
   * @author Willy Ekasalim
   *
   * @version 9 February 2007
   */
  private static class XSLTURIFinder implements URIResolver {

    /**
     * Returns the <code>Source</code> reference of file from the XSLT instructions
     * <code>xsl:include</code> or <code>xsl:import</code> by using the relative path. Don't do this
     * for URLs with a scheme (have ":")
     *
     * Initially, href would have, for example, iso_schematron_skeleton_for_xslt1.xsl and base would
     * have
     * jar:file:/C:/Users/ricko/workspace/Schematron-AntTask/test/lib/ant-schematron.jar!/iso_svrl
     * .xsl
     *
     * However, for referencing other files, it could have href= sch-messages-en.xhtml and base =
     * file:/C:/Users/ricko/workspace/Schematron-AntTask/test/schemas/test-message.sch
     *
     * {@inheritDoc}
     */
    @Override
    public Source resolve(String href, String base) {
      URL url;

      try {
        if (href == null || href.length() == 0) {
          // Inside a JAR or ZIP
          int bang = base.indexOf('!');
          url = ValidatorFactory.class.getResource(base.substring(bang + 1));

        } else if (href.indexOf(':') != -1) {
          //
          url = ValidatorFactory.class.getResource(href);

        } else {
          // We don't rebase the resource on the schema path, but in the current archive
          url = ValidatorFactory.class.getResource("/" + href);

        }

        // fall back
        if (url == null) {
          url = new URL(base.substring(0, base.lastIndexOf('/')+1) + href);
        }

      } catch (Exception ex) {
        ex.printStackTrace();
        return null; // swallow the exception and make it URLResolver's business
      }
      return new StreamSource(url.toString());
    }
  }

}
