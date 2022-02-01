package org.pageseeder.schematron.svrl;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *  # property-reference
 *  property-reference =
 *     element property-reference {
 *         attribute property { xsd:NMTOKEN },
 *         attribute role { text }?,
 *         attribute scheme { text }?,
 *         human-text
 *     }
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
