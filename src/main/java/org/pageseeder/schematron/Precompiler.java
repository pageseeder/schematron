/*
 * Copyright 2022 Allette Systems (Australia)
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

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Precompiler for a specific query binding.
 *
 * <p>The precompiler is thread-safe and generate a set of templates independently
 * of the template options.
 *
 * @author Christophe Lauret
 * @version 2.0
 * @since 2.0
 */
final class Precompiler {

  /**
   * Name of templates files for pipeline.
   */
  private static final List<String> STEPS = Arrays.asList("include.xsl", "expand.xsl", "compile-for-svrl.xsl");

  /**
   * XSLT implementation version
   */
  private final String _version;

  /**
   * Templates for the pipeline.
   */
  private final List<Templates> _pipeline;

  private Precompiler(String version, List<Templates> templates) {
    this._version = version;
    this._pipeline = templates;
  }

  public static Precompiler create(TransformerFactory transformerFactory, QueryBinding binding) throws SchematronException {
    List<Templates> pipeline = new ArrayList<>(3);
    try {
      for (String step : STEPS) {
        String path = "xslt/"+binding.version()+"/"+step;
        URL url = Precompiler.class.getResource(path);
        if (url == null)
          throw new IllegalArgumentException("Preprocessor '"+path+"' cannot be found in the classpath.");
        Source source = new StreamSource(url.toString());
        Templates templates = transformerFactory.newTemplates(source);
        pipeline.add(templates);
      }
    } catch (TransformerException ex) {
      throw new SchematronException("Unable to precompile transformation stylesheets", ex);
    }
    return new Precompiler(binding.version(), pipeline);
  }

  /**
   * Generate a compiler using the specified values
   *
   * @param options
   * @param listener
   *
   * @return A compiler usnig the specific options
   *
   * @throws SchematronException
   */
  public Compiler prepare(ErrorListener listener, Map<String, Object> options) throws SchematronException {
    List<Transformer> transformers = new ArrayList<>(3);
    try {
      for (Templates templates : this._pipeline) {
        Transformer transformer = templates.newTransformer();
        transformer.setErrorListener(listener);
        // set some parameters if specified (All transformers get all parameters)
        if (!options.isEmpty()) {
          for (Map.Entry<String, Object> p : options.entrySet()) {
            String name = p.getKey();
            transformer.setParameter(name, p.getValue());
          }
        }
        transformers.add(transformer);
      }
    } catch (TransformerException ex) {
      throw new SchematronException("Unable to compile Schematron to transformation stylesheet", ex);
    }
    return new Compiler(transformers);
  }

}