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

import org.w3c.dom.Document;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import java.util.List;

/**
 * Schematron compiler.
 *
 * <p>Compilers are not thread-safe, but may be reused/</p>
 *
 * @author Christophe Lauret
 * @version 2.0
 * @since 2.0
 */
final class Compiler {

  private final List<Transformer> _transformers;

  Compiler(List<Transformer> transformers) {
    this._transformers = transformers;
  }

  public Document compile(Source document) throws SchematronException {
    try {
      DOMResult result = null;
      Source source = document;
      for (Transformer transformer : this._transformers) {
        result = new DOMResult();
        transformer.transform(source, result);
        source = new DOMSource(result.getNode(), source.getSystemId());
      }
      return (Document) result.getNode();
    } catch (TransformerException ex) {
      throw new SchematronException("Unable to compile Schematron to transformation stylesheet", ex);
    }
  }

}