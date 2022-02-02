package org.pageseeder.schematron.svrl;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <pre>
 *  # human text
 *  human-text =
 *     element text {
 *         attribute xml:space { text }?,
 *         attribute xml:lang { text }?,
 *         attribute see { text }?,
 *         attribute icon { text }?,
 *         attribute fpi { text }?,
 *         rich-text
 *     }
 *  # rich text
 *  rich-text = (foreign | dir | span | emph | text)*
 *  # directionality
 *  dir =
 *     element dir {
 *         attribute class { text }?,
 *         attribute dir { text }?,
 *         text
 *             }
 * # emphasis
 * emph =
 *     element emph {
 *         attribute class { text }?,
 *         text
 *     }
 * # arbitrary markup
 * span =
 *     element span {
 *         attribute class { text },
 *         text
 *     }
 * # foreign
 * foreign = foreign-attributes | foreign-element
 * foreign-attributes = attribute * - (xml:* | local:*) { text }*
 * foreign-element =
 *     element * - svrl:* {
 *         (attribute * { text }
 *          | foreign-element
 *          | text)*
 *     }
 * </pre>
 */
public class HumanText implements XMLStreamable {

  private String space;
  private String lang;
  private String see;
  private String icon;
  private String fpi;
  private List<XMLEvent> richText = new ArrayList<>();

  public String getSpace() {
    return space;
  }

  public String getLang() {
    return lang;
  }

  public String getSee() {
    return see;
  }

  public String getIcon() {
    return icon;
  }

  public String getFpi() {
    return fpi;
  }

  public String toPlainText() {
    StringBuilder out = new StringBuilder();
    for (XMLEvent event : this.richText) {
      if (event.isCharacters()) {
        out.append(event.asCharacters().getData());
      }
    }
    return out.toString();
  }

  void setSpace(String space) {
    this.space = space;
  }

  void setLang(String lang) {
    this.lang = lang;
  }

  void setSee(String see) {
    this.see = see;
  }

  void setIcon(String icon) {
    this.icon = icon;
  }

  void setFpi(String fpi) {
    this.fpi = fpi;
  }

  void addContent(XMLEvent event) {
    this.richText.add(event);
  }

  @Override
  public void toXMLStream(XMLStreamWriter xml) throws XMLStreamException {
    xml.writeStartElement("svrl", "text", SVRL.NAMESPACE_URI);
    if (this.space != null) xml.writeAttribute("xml:space", this.space);
    if (this.lang != null) xml.writeAttribute("xml:lang", this.lang);
    if (this.see != null) xml.writeAttribute("see", this.see);
    if (this.icon != null) xml.writeAttribute("icon", this.icon);
    if (this.fpi != null) xml.writeAttribute("fpi", this.fpi);
    List<XMLEvent> text = this.richText;
    for (int i = 0, textSize = text.size(); i < textSize; i++) {
      XMLEvent event = text.get(i);
      if (event.isStartElement()) {
        if (i < textSize-1 && text.get(i+1).isEndElement()) {
          writeEmptyElement(xml, event.asStartElement());
          i++;
        } else {
          writeStartElement(xml, event.asStartElement());
        }

      } else if (event.isCharacters()) {
        xml.writeCharacters(event.asCharacters().getData());
      } else if (event.isEndElement()) {
        xml.writeEndElement();
      }
    }
    xml.writeEndElement();
  }

  private void writeEmptyElement(XMLStreamWriter xml, StartElement start) throws XMLStreamException {
    QName name = start.getName();
    if (name.getPrefix() != null) {
      xml.writeEmptyElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
    } else if (name.getNamespaceURI() != null) {
      xml.writeEmptyElement(name.getNamespaceURI(), name.getLocalPart());
    } else  {
      xml.writeEmptyElement(name.getLocalPart());
    }
    for (Iterator<Attribute> it = start.getAttributes(); it.hasNext(); ) {
      writeAttribute(xml, it.next());
    }
  }

  private void writeStartElement(XMLStreamWriter xml, StartElement start) throws XMLStreamException {
    QName name = start.getName();
    if (name.getPrefix() != null) {
      xml.writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
    } else if (name.getNamespaceURI() != null) {
      xml.writeStartElement(name.getNamespaceURI(), name.getLocalPart());
    } else  {
      xml.writeStartElement(name.getLocalPart());
    }
    for (Iterator<Attribute> it = start.getAttributes(); it.hasNext(); ) {
      writeAttribute(xml, it.next());
    }
  }

  private void writeAttribute(XMLStreamWriter xml, Attribute attribute) throws XMLStreamException {
    QName name = attribute.getName();
    if (name.getPrefix() != null) {
      xml.writeAttribute(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart(), attribute.getValue());
    } else if (name.getNamespaceURI() != null) {
      xml.writeAttribute(name.getNamespaceURI(), name.getLocalPart(), attribute.getValue());
    } else {
      xml.writeAttribute(name.getLocalPart(), attribute.getValue());
    }
  }

}
