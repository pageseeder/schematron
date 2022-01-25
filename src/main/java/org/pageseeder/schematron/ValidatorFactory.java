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
 *
 * ---------- Original copyright notice for this portion of the code ----------
 *
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
package org.pageseeder.schematron;

import org.w3c.dom.Document;

import java.io.*;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
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
 * @version 2.0
 * @since 1.0
 */
public final class ValidatorFactory {

  /**
   * Default XSLT processor.
   */
  private final TransformerFactory _factory = TransformerFactory.newInstance();

  /**
   * The URI resolver class.
   */
  private Class<URIResolver> _resolver;

  /**
   * The error event listener for the ValidatorFactory.
   */
  private ErrorListener _listener = this._factory.getErrorListener();

  /**
   * If set to <code>true</code> (default is <code>false</code>), then preprocessing stylesheet will be outputted to
   * file debug.xslt
   */
  private boolean debugMode = false;

  /**
   * Default compile options for this factory.
   */
  private CompileOptions options = CompileOptions.defaults();

  /**
   * Constructs a new factory.
   */
  public ValidatorFactory() {
  }

  public void setOptions(CompileOptions options) {
    this.options = options != null ? options : CompileOptions.defaults();
  }

  public CompileOptions getOptions() {
    return this.options;
  }

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
//
//  /**
//   * Add a parameter to be sent to the preprocessor.
//   *
//   * @see javax.xml.transform.Transformer#setParameter(String, Object)
//   *
//   * @param name  The name of the parameter.
//   * @param value The value object.
//   */
//  public void setParameter(String name, Object value) {
//    this._options.put(name, value);
//  }
//
//  /**
//   * Returns the parameters value for the specified name.
//   *
//   * @param name The name of the parameter.
//   *
//   * @return The parameter value or <code>null</code> if the parameter was not specified.
//   */
//  public Object getParameter(String name) {
//    return this._options.get(name);
//  }

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

  /**
   * Process the specified schema into a Validator object.
   *
   * @param schema The Schematron schema to use.
   *
   * @return A Validator instance for the specified schema.
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
   */
  public Validator newValidator(File schema, String phase) throws SchematronException {
    if (!schema.exists())
      throw new SchematronException("Unable to find schema", new FileNotFoundException(schema.getPath()));
    StreamSource source = new StreamSource(schema);
    return newValidator(source, phase);
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
    return newValidator(schema, null);
  }

  /**
   * Process the specified schema into a Validator object.
   *
   * @param schema The Schematron schema to use.
   * @param phase  The phase for this schema.
   *
   * @return A Validator instance for the specified schema.
   *
   * @throws SchematronException Will wrap any exception occurring while attempting to instantiate a validator.
   */
  public Validator newValidator(Source schema, String phase) throws SchematronException {
    Document schematron = this.loadSchema(schema);
    String systemId = schematron.getDocumentURI();
    QueryBinding binding = getQueryBinding(schematron, this.options);

    // TODO Do we still need to do this?
    // this._factory.setURIResolver(new XSLTURIFinder());

    Precompiler precompiler = Precompiler.create(this._factory, binding);

    DOMSource schemaSource = new DOMSource(schematron, systemId);

    Compiler compiler = precompiler.prepare(this._listener, this.options.toParameters(phase));
    Document stylesheet = compiler.compile(schemaSource);
    stylesheet.setDocumentURI(systemId);

    // check debug mode, if true then print the preprocessing results to debug.xslt
    if (this.debugMode) {
      try {
        FileWriter writer = new FileWriter("debug.xslt");
        StreamResult result = new StreamResult(writer);
        Transformer transformer = this._factory.newTransformer();
        transformer.transform(new DOMSource(stylesheet), result);
      } catch (TransformerException | IOException ex) {
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

    // Generate the validator instance
    Templates validator;
    try {
      validator = this._factory.newTemplates(new DOMSource(stylesheet));
    } catch (TransformerException ex) {
      throw new SchematronException("Unable to generate new Validator from preprocessed "+schema.getSystemId(), ex);
    }
    return new Validator(validator);
  }

  private Document loadSchema(Source source) throws SchematronException {
    String systemId = source.getSystemId();
    try {
      DOMResult schema = new DOMResult();
      this._factory.newTransformer().transform(source, schema);
      Document schemaDocument = (Document)schema.getNode();
      schemaDocument.setDocumentURI(systemId);
      return schemaDocument;
    } catch (TransformerException ex) {
      throw new SchematronException("Unable to parse source schema", ex);
    }
  }

  private QueryBinding getQueryBinding(Document schematron, CompileOptions options) throws SchematronException {
    String queryBinding = schematron.getDocumentElement().getAttribute("queryBinding").toLowerCase();
    if ("".equals(queryBinding)) {
      queryBinding = options.defaultQueryBinding();
      schematron.getDocumentElement().setAttribute("queryBinding", queryBinding);
    }
    return QueryBinding.forValue(queryBinding);
  }

}
