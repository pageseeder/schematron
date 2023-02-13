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
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * A ValidatorFactory instance can be used to create a Validator object.
 *
 * <p>This class uses a fluent style API.</p>
 *
 * @author Christophe Lauret
 *
 * @version 2.0
 * @since 1.0
 */
public final class ValidatorFactory {

  private static final DebugOutput NO_DEBUG = (systemId) -> null;

  private static final DebugOutput BASIC_DEBUG = (systemId) -> {
    String filename = "debug."+removePathExtension(systemId)+".xsl";
    return new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8);
  };

  /**
   * Default XSLT processor.
   */
  private final TransformerFactory _factory;

  /**
   * The URI resolver class.
   */
  private final Class<URIResolver> _resolver;

  /**
   * The error event listener for the ValidatorFactory.
   */
  private final ErrorListener _listener;

  /**
   * Indicates where the compiled stylesheets should be saved for debugging.
   */
  private final DebugOutput _debug;

  /**
   * Default compile options for this factory.
   */
  private final CompileOptions _options;

  /**
   * We keep a shared copy of precompilers (they are thread-safe)
   */
  private static volatile Precompiler precompiler1 = null;
  private static volatile Precompiler precompiler2 = null;

  /**
   * Constructs a new factory.
   */
  public ValidatorFactory() {
    this(CompileOptions.defaults());
  }

  /**
   * Constructs a new factory with the specified compile options.
   */
  public ValidatorFactory(CompileOptions options) {
    this._factory = newSafeTransformerFactory();
    this._options = Objects.requireNonNull(options);
    this._listener = this._factory.getErrorListener();
    this._resolver = null;
    this._debug = NO_DEBUG;
  }

  private ValidatorFactory(TransformerFactory factory, CompileOptions options, ErrorListener listener, Class<URIResolver> resolver, DebugOutput debug) {
    this._factory = factory;
    this._options = Objects.requireNonNull(options);
    this._listener = Objects.requireNonNull(listener);
    this._resolver = resolver;
    this._debug = debug;
  }

  public ValidatorFactory options(CompileOptions options) {
    return new ValidatorFactory(this._factory, options, this._listener, this._resolver, this._debug);
  }

  @Deprecated
  public CompileOptions getOptions() {
    return this._options;
  }

  public CompileOptions options() {
    return this._options;
  }

  /**
   * Set the error event listener for the ValidatorFactory, which is used for the processing of the
   * Schematron schema, not for the Schematron validation itself.
   *
   * @param listener The error listener.
   */
  public ValidatorFactory errorListener(ErrorListener listener) {
    return new ValidatorFactory(this._factory, this._options, listener, this._resolver, this._debug);
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
   * Enable debug mode to save a copy of the generated stylesheet to a file for debugging.
   *
   * <p>This method assigned a default <code>DebugOutput</code> implementation. To specify your
   * own use the {@link #debug(DebugOutput)} method.</p>
   *
   * @return A new factory if debug is not already enabled.
   */
  public ValidatorFactory enableDebug() {
    // Debug is already enabled
    if (this._debug != null && this._debug != NO_DEBUG) return this;
    return new ValidatorFactory(this._factory, this._options, this._listener, this._resolver, BASIC_DEBUG);
  }

  /**
   * Disable debug mode.
   *
   * @return A new factory if debug is not already disabled.
   */
  public ValidatorFactory disableDebug() {
    // Debug is already disabled
    if (this._debug == null || this._debug == NO_DEBUG) return this;
    return new ValidatorFactory(this._factory, this._options, this._listener, this._resolver, NO_DEBUG);
  }

  /**
   * Enable debug mode to save a copy of the generated stylesheet when it is generated.
   *
   * @return A new factory if debug is not already enabled.
   */
  public ValidatorFactory debug(DebugOutput debug) {
    return new ValidatorFactory(this._factory, this._options, this._listener, this._resolver, debug);
  }

  /**
   * Set the class name of the resolver to use, overriding built-in Apache resolver
   */
  public ValidatorFactory resolver(Class<URIResolver> resolver) {
    return new ValidatorFactory(this._factory, this._options, this._listener, resolver, this._debug);
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

    // Prepare the compiler
    QueryBinding binding = getQueryBinding(schematron, this._options);
    Precompiler precompiler = getPrecompiler(binding);
    Compiler compiler = precompiler.prepare(this._listener, this._options.toParameters(phase));

    DOMSource schemaSource = new DOMSource(schematron, systemId);
    Document stylesheet = compiler.compile(schemaSource);
    stylesheet.setDocumentURI(systemId);

    // check debug mode, if true then print the preprocessing results to debug.xslt
    if (this._debug != null) {
      try {
        Writer writer = this._debug.getWriter(systemId);
        if (writer != null) {
          StreamResult result = new StreamResult(writer);
          Transformer transformer = this._factory.newTransformer();
          transformer.setOutputProperty(OutputKeys.INDENT, this._debug.indent() ? "yes" : "no");
          transformer.transform(new DOMSource(stylesheet), result);
        }
      } catch (TransformerException | IOException ex) {
        throw new SchematronException("Unable to save debug output", ex);
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

  private Precompiler getPrecompiler(QueryBinding binding) throws SchematronException {
    if ("1.0".equals(binding.version()) && precompiler1 != null) return precompiler1;
    if ("2.0".equals(binding.version()) && precompiler2 != null) return precompiler2;
    Precompiler precompiler = Precompiler.create(this._factory, binding);
    if ("1.0".equals(binding.version())) precompiler1 = precompiler;
    else if ("2.0".equals(binding.version())) precompiler2 = precompiler;
    return precompiler;
  }

  /**
   * Given full path to the path and return file name only
   *
   * @param filepath full path to a file
   * @return name of the file only without any of the path
   */
  private static String removePathExtension(String filepath) {
    String[] split = filepath.split("[/\\\\]");
    return split[split.length - 1].replaceAll(".sch$", "");
  }

  private static TransformerFactory newSafeTransformerFactory() {
    TransformerFactory factory = TransformerFactory.newInstance();
    // If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    return factory;
  }

}
