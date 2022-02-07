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

import org.pageseeder.schematron.svrl.SVRLStreamWriter;

import java.io.File;
import java.io.StringWriter;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stream.StreamSource;

/**
 * An object representing a single Schematron schema, used to validate multiple XML instances.
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
   * The generated Schematron validator transformer templates.
   */
  private final Templates _validator;

  /**
   * The default URI resolver to use
   */
  private URIResolver _resolver;

  /**
   * Constructs a new Validator object for a given Schematron templates.
   *
   * @param templates The Schematron templates.
   *
   * @throws NullPointerException If the templates are <code>null</code>.
   */
  Validator(Templates templates) {
    if (templates == null)
      throw new NullPointerException("A validator cannot be constructed with null templates");
    this._validator = templates;
    this._resolver = null;
  }

  /**
   * @param resolver A URI resolver to use during validation.
   */
  public void setResolver(URIResolver resolver) {
    this._resolver = resolver;
  }

  /**
   * Performs validation of the passed XML data.
   *
   * @return the results of the validation.
   *
   * @throws SchematronException Should an error occur during validation.
   */
  public SchematronResult validate(File xml) throws SchematronException {
    return validate(new StreamSource(xml));
  }

  /**
   * Performs validation of the passed XML data.
   *
   * @return the results of the validation.
   *
   * @throws SchematronException Should an error occur during validation.
   */
  public SchematronResult validate(File xml, OutputOptions options) throws SchematronException {
    return validate(new StreamSource(xml), options);
  }

  /**
   * Performs validation of the passed XML data.
   *
   * @return the results of the validation.
   *
   * @throws SchematronException Should an error occur during validation.
   */
  public SchematronResult validate(Source xml) throws SchematronException {
    return validate(xml, OutputOptions.defaults(), this._resolver);
  }

  /**
   * Validates the XML data using the default resolver.
   *
   * @param xml      XML source to validate
   * @param options  The output options
   *
   * @return the results of the validation.
   *
   * @throws SchematronException Should an error occur during validation.
   */
  public SchematronResult validate(Source xml, OutputOptions options) throws SchematronException {
    return validate(xml, options, this._resolver);
  }

  /**
   * Validates the XML data.
   *
   * @param xml      XML source to validate
   * @param options  The output options
   * @param resolver The URI resolver to use (overrides default)
   *
   * @return the results of the validation.
   *
   * @throws SchematronException Should an error occur during validation.
   */
  public SchematronResult validate(Source xml, OutputOptions options, URIResolver resolver) throws SchematronException {
    Transformer transformer = newTransformer(this._validator, options, resolver);

    // Generate the result
    StringWriter writer = new StringWriter();
    SchematronResult result = new SchematronResult(xml.getSystemId());

    try {
      // NB Saxon does not support XMLEventWriter, so we use XMLStreamWriter instead
      SVRLStreamWriter svrl = new SVRLStreamWriter(writer, options);
      transformer.transform(xml, new StAXResult(svrl));
      result.setSVRL(writer.toString(), svrl.getAssertsCount(), svrl.getReportsCount());
//      transformer.transform(xml, new StreamResult(writer));

    } catch (TransformerException ex) {
      throw new SchematronException("Unable to process file with schematron", ex);
    } catch (XMLStreamException ex) {
      throw new SchematronException("Unable to process SVRL results", ex);
    }

    return result;
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


}
