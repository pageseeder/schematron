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
package org.pageseeder.schematron.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * A XMLStreamWriter class delegating the events another XMLStreamWriter implementation.
 *
 * @author Christophe Lauret
 *
 * @version 2.0
 * @since 2.0
 */
public class XMLStreamWriterWrapper implements XMLStreamWriter {

  protected final XMLStreamWriter writer;

  public XMLStreamWriterWrapper(XMLStreamWriter writer) {
    this.writer = writer;
  }

  @Override
  public void writeStartElement(String localName) throws XMLStreamException {
    this.writer.writeStartElement(localName);
  }

  @Override
  public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
    this.writer.writeStartElement(namespaceURI, localName);
  }

  @Override
  public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
    this.writer.writeStartElement(prefix, localName, namespaceURI);
  }

  @Override
  public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
    this.writer.writeEmptyElement(namespaceURI, localName);
  }

  @Override
  public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
    this.writer.writeEmptyElement(prefix, localName, namespaceURI);
  }

  @Override
  public void writeEmptyElement(String localName) throws XMLStreamException {
    this.writer.writeEmptyElement(localName);
  }

  @Override
  public void writeEndElement() throws XMLStreamException {
    this.writer.writeEndElement();
  }

  @Override
  public void writeEndDocument() throws XMLStreamException {
    this.writer.writeEndDocument();
  }

  @Override
  public void close() throws XMLStreamException {
    this.writer.close();
  }

  @Override
  public void flush() throws XMLStreamException {
    this.writer.flush();
  }

  @Override
  public void writeAttribute(String localName, String value) throws XMLStreamException {
    this.writer.writeAttribute(localName, value);
  }

  @Override
  public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
    this.writer.writeAttribute(prefix, namespaceURI, localName, value);
  }

  @Override
  public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
    this.writer.writeAttribute(namespaceURI, localName, value);
  }

  @Override
  public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
    this.writer.writeNamespace(prefix, namespaceURI);
  }

  @Override
  public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
    this.writer.writeDefaultNamespace(namespaceURI);
  }

  @Override
  public void writeComment(String data) throws XMLStreamException {
    this.writer.writeComment(data);
  }

  @Override
  public void writeProcessingInstruction(String target) throws XMLStreamException {
    this.writer.writeProcessingInstruction(target);
  }

  @Override
  public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
    this.writer.writeProcessingInstruction(target, data);
  }

  @Override
  public void writeCData(String data) throws XMLStreamException {
    this.writer.writeCData(data);
  }

  @Override
  public void writeDTD(String dtd) throws XMLStreamException {
    this.writer.writeDTD(dtd);
  }

  @Override
  public void writeEntityRef(String name) throws XMLStreamException {
    this.writer.writeEntityRef(name);
  }

  @Override
  public void writeStartDocument() throws XMLStreamException {
    this.writer.writeStartDocument();
  }

  @Override
  public void writeStartDocument(String version) throws XMLStreamException {
    this.writer.writeStartDocument(version);
  }

  @Override
  public void writeStartDocument(String encoding, String version) throws XMLStreamException {
    this.writer.writeStartDocument(encoding, version);
  }

  @Override
  public void writeCharacters(String text) throws XMLStreamException {
    this.writer.writeCharacters(text);
  }

  @Override
  public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
    this.writer.writeCharacters(text, start, len);
  }

  @Override
  public String getPrefix(String uri) throws XMLStreamException {
    return this.writer.getPrefix(uri);
  }

  @Override
  public void setPrefix(String prefix, String uri) throws XMLStreamException {
    this.writer.setPrefix(prefix, uri);
  }

  @Override
  public void setDefaultNamespace(String uri) throws XMLStreamException {
    this.writer.setDefaultNamespace(uri);
  }

  @Override
  public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
    this.writer.setNamespaceContext(context);
  }

  @Override
  public NamespaceContext getNamespaceContext() {
    return this.writer.getNamespaceContext();
  }

  @Override
  public Object getProperty(String name) throws IllegalArgumentException {
    return this.writer.getProperty(name);
  }
}
