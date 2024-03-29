/*
 * Copyright 2015 Allette Systems (Australia)
 * http://www.allette.com.au
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pageseeder.schematron;

import org.pageseeder.schematron.svrl.SVRLByteArray;
import org.pageseeder.schematron.svrl.SVRLDataFile;
import org.pageseeder.schematron.svrl.SVRLStreamWriter;
import org.pageseeder.schematron.svrl.SVRLString;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.*;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stream.StreamSource;

/**
 * An object representing a single Schematron schema, used to validate multiple XML instances.
 *
 * <p>Use the {@link ValidatorFactory} to create validators. Validators are thread-safe.
 *
 * @implNote A validator stores the precompiled XSLT templates used for validation
 *
 * @author Christophe Lauret
 * @author Willy Ekasalim
 * @author Rick Jelliffe
 *
 * @version 2.0
 * @since 1.0
 */
public final class Validator {

  /**
   * Does not report errors
   */
  private static final ErrorListener QUIET_LISTENER = new ErrorListener() {
    @Override
    public void warning(TransformerException ex) {}
    @Override
    public void error(TransformerException ex) {}
    @Override
    public void fatalError(TransformerException ex) {}
  };

  /**
   * The generated Schematron validator transformer templates.
   */
  private final Templates _validator;

  /**
   * The output options to use for this validator.
   */
  private final OutputOptions _options;

  /**
   * The URI resolver to use during validation.
   */
  private final URIResolver _resolver;

  /**
   * Constructs a new Validator object for a given Schematron templates.
   *
   * @param templates The Schematron templates.
   *
   * @throws NullPointerException If the templates are <code>null</code>.
   */
  Validator(Templates templates) {
    this(templates, OutputOptions.defaults(), null);
  }

  private Validator(Templates templates, OutputOptions options, URIResolver resolver) {
    if (templates == null)
      throw new NullPointerException("A validator cannot be constructed with null templates");
    this._validator = templates;
    this._options = Objects.requireNonNull(options);
    this._resolver = resolver;
  }

  /**
   * Return a new validator with the specified output options.
   *
   * @param options The output options to use for this validator.
   * @return A new validator
   */
  public Validator options(OutputOptions options) {
    return new Validator(this._validator, options, this._resolver);
  }

  public OutputOptions options() {
    return this._options;
  }

  /**
   * Return a new validator with the specified URI resolver.
   *
   * @param resolver The default resolved to use for this validator.
   * @return A new validator
   */
  public Validator resolver(URIResolver resolver) {
    return new Validator(this._validator, this._options, resolver);
  }

  /**
   * Performs validation of the passed XML data.
   *
   * @return the results of the validation.
   *
   * @throws SchematronException Should an error occur during validation.
   */
  public SchematronResult validate(File xml) throws SchematronException {
    return validate(new StreamSource(xml), Collections.emptyMap());
  }

  /**
   * Performs validation of the passed XML data.
   *
   * @return the results of the validation.
   *
   * @throws SchematronException Should an error occur during validation.
   */
  public SchematronResult validate(Source xml) throws SchematronException {
    return validate(xml, Collections.emptyMap());
  }

  /**
   * Validates the XML data.
   *
   * @implNote This method is thread-safe and create a new instance
   *
   * @param xml        XML file to validate
   * @param parameters The parameters to send to the schema during validation
   *
   * @return the results of the validation.
   *
   * @throws SchematronException Should an error occur during validation.
   */
  public SchematronResult validate(File xml, Map<String, Object> parameters) throws SchematronException {
    return validate(new StreamSource(xml), parameters);
  }

  /**
   * Validates the XML data.
   *
   * @implNote This method is thread-safe and create a new instance
   *
   * @param xml        XML source to validate
   * @param parameters The parameters to send to the schema during validation
   *
   * @return the results of the validation.
   *
   * @throws SchematronException Should an error occur during validation.
   */
  public SchematronResult validate(Source xml, Map<String, Object> parameters) throws SchematronException {
    return newInstance().validate(xml, parameters);
  }

  public Instance newInstance() throws SchematronException {
    Transformer transformer = newTransformer(this._validator, this._options, this._resolver);
    return new Instance(transformer, this._options);
  }

  private static Transformer newTransformer(Templates validator, OutputOptions options, URIResolver resolver)
      throws SchematronException {
    Transformer transformer;
    try {
      transformer = validator.newTransformer();
    } catch (TransformerConfigurationException ex) {
      throw new SchematronException("Unable to create new validator", ex);
    }

    // Output properties
    transformer.setOutputProperty(OutputKeys.ENCODING, options.encoding());
    transformer.setOutputProperty(OutputKeys.INDENT, options.isIndent() ? "yes" : "no");
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, options.isOmitXmlDeclaration() ? "yes" : "no");

    // If a URI resolver is specified include it
    if (resolver != null) {
      transformer.setURIResolver(resolver);
    }
    return transformer;
  }

  /**
   * A validating instance lets you reuse a validator that uses the same output options and resolver.
   *
   * <p>It is not thread-safe</p>
   *
   * @implNote This class lets you reuse a transformer which more efficient when validating a collection of files, but not thread-safe.
   *
   * @author Christophe Lauret
   *
   * @version 2.0
   * @since 2.0
   */
  public static class Instance {

    private final Transformer _transformer;
    private final OutputOptions _options;

    private volatile boolean validating = false;

    private Instance(Transformer transformer, OutputOptions options) {
      this._transformer = transformer;
      this._options = options;
    }

    public SchematronResult validate(File xml) throws SchematronException {
      return this.validate(new StreamSource(xml));
    }

    public SchematronResult validate(Source xml) throws SchematronException {
      return this.validate(xml, null);
    }

    public SchematronResult validate(File xml, Map<String, Object> parameters) throws SchematronException {
      return this.validate(new StreamSource(xml), parameters);
    }

    /**
     * Validates the XML data.
     *
     * @param xml XML source to validate
     * @param parameters Parameters to pass to the validators
     *
     * @return the results of the validation.
     *
     * @throws SchematronException Should an error occur during validation.
     */
    public SchematronResult validate(Source xml, Map<String, Object> parameters) throws SchematronException {
      // Generate the result
      StringWriter writer = new StringWriter();
      SchematronResult.Builder result = transform(xml, parameters, writer);
      result.setSVRL(new SVRLString(writer.toString()));
      return result.build();
    }

    /**
     * Validates the XML data.
     *
     * @param xml XML source to validate
     * @param parameters Parameters to pass to the validators
     *
     * @return the results of the validation.
     *
     * @throws SchematronException Should an error occur during validation.
     */
    public SchematronResult validateToBytes(Source xml, Map<String, Object> parameters) throws SchematronException {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      Charset charset = Charset.forName(this._options.encoding());
      OutputStreamWriter writer = new OutputStreamWriter(out, charset);
      SchematronResult.Builder result = transform(xml, parameters, writer);
      result.setSVRL(new SVRLByteArray(out.toByteArray(), charset));
      return result.build();
    }

    /**
     * Validates the XML data.
     *
     * @param xml XML source to validate
     * @param parameters Parameters to pass to the validators
     *
     * @return the results of the validation.
     *
     * @throws SchematronException Should an error occur during validation.
     */
    public SchematronResult validateToFile(Source xml, Map<String, Object> parameters, File file) throws SchematronException {
      try (FileOutputStream out = new FileOutputStream(file)) {
        Charset charset = Charset.forName(this._options.encoding());
        OutputStreamWriter writer = new OutputStreamWriter(out, charset);
        SchematronResult.Builder result = transform(xml, parameters, writer);
        result.setSVRL(new SVRLDataFile(file, charset));
        return result.build();
      } catch (IOException ex) {
        throw new SchematronException(ex);
      }
    }

    private SchematronResult.Builder transform(Source xml, Map<String, Object> parameters, Writer writer) throws SchematronException {
      if (this.validating) throw new IllegalStateException("Unable to validate multiple source concurrently");
      this.validating = true;
      SchematronResult.Builder result = new SchematronResult.Builder();
      result.setSystemID(xml.getSystemId());
      try {
        // Set the parameters if any
        if (parameters != null && parameters.size() > 0) {
          for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            this._transformer.setParameter(parameter.getKey(), parameter.getValue());
          }
        }
        this._transformer.setErrorListener(QUIET_LISTENER);

        // NB Saxon does not support XMLEventWriter, so we use XMLStreamWriter instead
        SVRLStreamWriter svrl = new SVRLStreamWriter(writer, this._options);
        this._transformer.transform(xml, new StAXResult(svrl));
        result.setAssertsCount(svrl.getAssertsCount());
        result.setReportsCount(svrl.getReportsCount());

      } catch (TransformerException ex) {
        throw new ValidationException(ex);
      } catch (XMLStreamException ex) {
        throw new SchematronException("Unable to process SVRL results", ex);
      } finally {
        this.validating = false;
      }
      return result;
    }

  }

}
