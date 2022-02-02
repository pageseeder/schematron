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

import org.pageseeder.schematron.OutputOptions;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Writer;
import java.util.*;

/**
 * XML Stream writer for SVRL that intercepts events and ensure that the correct output options are
 * applied.
 *
 * @author Christophe Lauret
 * @version 2.0
 * @since 2.0
 */
public class SVRLStreamWriter extends XMLStreamWriterWrapper {

  private static final QName TEXT = new QName(SVRL.NAMESPACE_URI, "text");

  private final Deque<QName> elements = new ArrayDeque<>();

  private final OutputOptions options;

  public SVRLStreamWriter(Writer out) throws XMLStreamException {
    this(out, OutputOptions.defaults());
  }

  public SVRLStreamWriter(Writer out, OutputOptions options) throws XMLStreamException {
    super(newXMLStreamWriter(out));
    this.options = options;
  }

  private static XMLStreamWriter newXMLStreamWriter(Writer out) throws XMLStreamException {
    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    // We assume the XSLT produce the correct namespace context, so no need for repairing
    factory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, false);
    return factory.createXMLStreamWriter(out);
  }

  @Override
  public void writeAttribute(String localName, String value) throws XMLStreamException {
    if ("location".equals(localName) && this.options.usePrefixInLocation()) {
      NamespaceContext context = this.getNamespaceContext();
      super.writeAttribute(localName, SVRL.toLocationPrefix(value, context));
    } else {
      super.writeAttribute(localName, value);
    }
  }

  @Override
  public void writeStartElement(String localName) throws XMLStreamException {
    this.elements.push(new QName(localName));
    indentIfRequired();
    super.writeStartElement(localName);
  }

  @Override
  public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
    this.elements.push(new QName(namespaceURI, localName));
    indentIfRequired();
    if (isEmptySvrlElement(namespaceURI, localName)) {
      super.writeEmptyElement(localName, namespaceURI);
    } else {
      super.writeStartElement(localName, namespaceURI);
    }
  }

  @Override
  public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
    this.elements.push(new QName(namespaceURI, localName, prefix));
    indentIfRequired();
    if (isEmptySvrlElement(namespaceURI, localName)) {
      super.writeEmptyElement(prefix, localName, namespaceURI);
    } else {
      super.writeStartElement(prefix, localName, namespaceURI);
    }
  }

  @Override
  public void writeEmptyElement(String localName) throws XMLStreamException {
    indentIfRequired();
    super.writeEmptyElement(localName);
  }

  @Override
  public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
    indentIfRequired();
    super.writeEmptyElement(namespaceURI, localName);
  }

  @Override
  public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
    indentIfRequired();
    super.writeEmptyElement(prefix, localName, namespaceURI);
  }

  @Override
  public void writeEndElement() throws XMLStreamException {
    QName name = this.elements.pop();
    if (!isEmptySvrlElement(name.getNamespaceURI(), name.getLocalPart())) {
      if (this.options.isIndent() && isSvrlElement(name.getNamespaceURI())) {
        if (!this.elements.contains(TEXT) && !TEXT.equals(name)) {
          super.writeCharacters("\n");
          for (int i = 0; i < this.elements.size(); i++) {
            super.writeCharacters("  ");
          }
        }
      }
      super.writeEndElement();
    }
  }

  @Override
  public void writeCharacters(String text) throws XMLStreamException {
    super.writeCharacters(text);
  }

  @Override
  public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
    super.writeCharacters(text, start, len);
  }

  @Override
  public void writeStartDocument() throws XMLStreamException {
    if (this.options.isOmitXmlDeclaration()) return;
    super.writeStartDocument();
  }

  @Override
  public void writeStartDocument(String version) throws XMLStreamException {
    if (this.options.isOmitXmlDeclaration()) return;
    super.writeStartDocument(version);
  }

  @Override
  public void writeStartDocument(String encoding, String version) throws XMLStreamException {
    if (this.options.isOmitXmlDeclaration()) return;
    super.writeStartDocument(encoding, version);
  }

  private boolean isSvrlElement(String namespaceURI) {
    return SVRL.NAMESPACE_URI.equals(namespaceURI);
  }

  private boolean isEmptySvrlElement(String namespaceURI, String localName) {
    return isSvrlElement(namespaceURI) && (
               "ns-prefix-in-attribute-values".equals(localName)
            || "active-pattern".equals(localName)
            || "fired-rule".equals(localName)
    );
  }

  private void indentIfRequired() throws XMLStreamException {
    if (this.options.isIndent()) {
      if (this.elements.size() > 1 && (!this.elements.contains(TEXT) || TEXT.equals(this.elements.peek()))) {
        super.writeCharacters("\n");
        for (int i = 0; i < this.elements.size()-1; i++) {
          super.writeCharacters("  ");
        }
      }
    }
  }

}
