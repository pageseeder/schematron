/*
 * The MIT License
 *
 * Copyright (c) 2012 Christophe Lauret, Allette Systems
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
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * An object representing a single Schematron schema, used to validate multiple XML instances.
 *
 * @author Christophe Lauret
 * @author Willy Ekasalim
 * @author Rick Jelliffe
 *
 * @version 14 February 2010
 */
public final class Validator {

  /**
   * The generated Schematron validator transformer templates.
   */
  private final Templates _validator;

  /**
   * A URI resolver.
   */
  private URIResolver _resolver;

  /**
   * Constructs a new Validator object for a given Schematron templates.
   *
   * @param templates The Schematron templates.
   *
   * @throws NullPointerException If the templates are <code>null</code>.
   */
  protected Validator(Templates templates) {
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
  public SchematronResult validate(Source xml) throws SchematronException {
    Transformer transformer = newTransformer(this._validator, this._resolver, "utf-8");

    String systemId = xml.getSystemId();
    String archiveId = "";
    if (systemId != null && systemId.startsWith("jar:") || systemId.startsWith("zip:")) {
      archiveId = systemId.substring(0, systemId.lastIndexOf('!'));
      systemId = systemId.substring(systemId.lastIndexOf('!')+1);
    }

    if (archiveId != null && archiveId.length() > 0){
      transformer.setParameter("archiveNameParameter", archiveId.substring(archiveId.lastIndexOf('/')+1));
    }

    if (archiveId != null && archiveId.length() > 0 && archiveId.lastIndexOf('/') > -1){
      transformer.setParameter("archiveDirParameter", archiveId.substring(0, archiveId.lastIndexOf('/')));
    }

    // provide the filenames
    if (systemId != null && systemId.length() > 0){
      transformer.setParameter("fileNameParameter", systemId.substring(systemId.lastIndexOf('/')+1));
    }

    if (systemId != null && systemId.length() > 0 && systemId.lastIndexOf('/') > -1) {
      transformer.setParameter("fileDirParameter", systemId.substring(0, systemId.lastIndexOf('/')));
    }

    // Generate the result
    StringWriter writer = new StringWriter();
    try {
      transformer.transform(xml, new StreamResult(writer));
    } catch (TransformerException ex) {
      throw new SchematronException("Unable to process file with schematron", ex);
    }
    SchematronResult result = new SchematronResult(xml.getSystemId());
    result.setSVRL(writer.toString());
    return result;
  }

  /**
   * Performs validation of the passed XML data.
   *
   * @param xml    The XML data to be validated.
   * @param fnp    the file name if available separately
   * @param fdp    the file directory path or the whole system identifier otherwise
   *
   *
   * @return the results of the validation.
   *
   * @throws TransformerConfigurationException Should an error occur while instantiating a transformer.
   * @throws TransformerException              If an error occurs while performing the transformation.
   */
  public SchematronResult validate(Source xml, String fnp, String fdp, String anp, String adp, String encoding)
      throws SchematronException {
    Transformer transformer = newTransformer(this._validator, this._resolver, encoding);

    String sid = xml.getSystemId();
    String aid = "";
    if ( sid.startsWith("jar:") || sid.startsWith("zip:")) {
      aid = sid.substring(0, sid.lastIndexOf("!" ));
      sid = sid.substring(sid.lastIndexOf("!" )+1);
    }

    if (anp != null && anp.length() > 0)
      transformer.setParameter("archiveNameParameter", anp);
    else if (aid != null && aid.length() > 0){
      transformer.setParameter("archiveNameParameter", aid.substring(aid.lastIndexOf("/" )+1));
    }

    if (adp != null && adp.length() > 0)
      transformer.setParameter("archiveDirParameter", adp);
    else if (aid != null && aid.length() > 0 && aid.lastIndexOf("/" )> -1){
      transformer.setParameter("archiveDirParameter", aid.substring(0, aid.lastIndexOf("/" )));
    }

    // provide the filenames
    if (fnp != null && fnp.length() > 0)
      transformer.setParameter("fileNameParameter", fnp);
    else if (sid != null && sid.length() > 0){
      transformer.setParameter("fileNameParameter", sid.substring(sid.lastIndexOf("/" )+1));
    }

    if (fdp != null && fdp.length() > 0)
      transformer.setParameter("fileDirParameter", fdp);
    else if (sid != null && sid.length() > 0 && sid.lastIndexOf("/" )> -1){
      transformer.setParameter("fileDirParameter", sid.substring(0, sid.lastIndexOf("/" )));
    }

    // Generate the result
    StringWriter writer = new StringWriter();
    try {
      transformer.transform(xml, new StreamResult(writer));
    } catch (TransformerException ex) {
      throw new SchematronException("Unable to process file with schematron", ex);
    }
    SchematronResult result = new SchematronResult(xml.getSystemId());
    result.setSVRL(writer.toString());
    return result;
  }

  /**
   *
   * @param validator
   * @param resolver
   * @param encoding
   * @throws SchematronException
   */
  private static Transformer newTransformer(Templates validator, URIResolver resolver, String encoding)
      throws SchematronException {
    Transformer transformer = null;
    try {
      transformer = validator.newTransformer();
    } catch (TransformerConfigurationException ex) {
      throw new SchematronException("Unable to create new validator", ex);
    }

    // If an encoding is specified.
    if (encoding != null && encoding.length() > 0 ) {
      transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
    }

    // Let's indent the SVRL output by default
    transformer.setOutputProperty("indent","yes");

    // If a URI resolver is specified include it
    if (resolver != null) {
      transformer.setURIResolver(resolver);
    }
    return transformer;
  }

}
