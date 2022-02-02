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
package org.pageseeder.schematron.svrl;

import org.pageseeder.schematron.SchematronException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;

/**
 * Parser for SVRL that generates a <code>SchematronOutput</code> instance.
 *
 * @author Christophe Lauret
 *
 * @version 2.0
 * @since 2.0
 */
public final class SVRLParser {

  private SVRLParser(){}

  public static SchematronOutput parse(File svrl) throws SchematronException, IOException {
    try (Reader reader = new FileReader(svrl)) {
      return parse(reader);
    }
  }

  public static SchematronOutput parse(Reader svrl) throws SchematronException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    try {
      XMLEventReader eventReader = factory.createXMLEventReader(svrl);
      SVRLEventHandler handler = new SVRLEventHandler();
      return handler.parse(eventReader);
    } catch (XMLStreamException ex) {
      throw new SchematronException("Unable to parse SVRL", ex);
    }
  }

}
