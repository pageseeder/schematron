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

import org.pageseeder.schematron.xml.XMLStreamable;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * <pre>
 *  # property-reference
 *  property-reference =
 *     element property-reference {
 *         attribute property { xsd:NMTOKEN },
 *         attribute role { text }?,
 *         attribute scheme { text }?,
 *         human-text
 *     }
 * </pre>
 *
 * @author Christophe Lauret
 *
 * @version 2.0
 * @since 2.0
 */
public final class PropertyReference implements XMLStreamable {

  private String property;
  private String role;
  private String scheme;
  private HumanText text;

  public String getProperty() {
    return property;
  }

  public String getRole() {
    return role;
  }

  public String getScheme() {
    return scheme;
  }

  public HumanText getText() {
    return text;
  }

  void setProperty(String property) {
    this.property = property;
  }

  void setRole(String role) {
    this.role = role;
  }

  void setScheme(String scheme) {
    this.scheme = scheme;
  }

  void setText(HumanText text) {
    this.text = text;
  }

  @Override
  public void toXMLStream(XMLStreamWriter xml) throws XMLStreamException {
    xml.writeStartElement("svrl", "property-reference", SVRL.NAMESPACE_URI);
    if (this.property != null) xml.writeAttribute("property", this.property);
    if (this.role != null) xml.writeAttribute("role", this.role);
    if (this.scheme != null) xml.writeAttribute("scheme", this.scheme);
    if (this.text != null) this.text.toXMLStream(xml);
    xml.writeEndElement();
  }
}
