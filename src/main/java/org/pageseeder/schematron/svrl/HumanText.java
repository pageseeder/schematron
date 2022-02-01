package org.pageseeder.schematron.svrl;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

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
  private String richText;
  private String plainText;

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

  public String getRichText() {
    return richText;
  }

  public String getPlainText() {
    return plainText;
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

  void setRichText(String richText) {
    this.richText = richText;
  }

  void setPlainText(String plainText) {
    this.plainText = plainText;
  }

  @Override
  public void toXMLStream(XMLStreamWriter xml) throws XMLStreamException {
    xml.writeStartElement("svrl", "text", SVRL.NAMESPACE_URI);
    if (this.space != null) xml.writeAttribute("xml:space", this.space);
    if (this.lang != null) xml.writeAttribute("xml:lang", this.lang);
    if (this.see != null) xml.writeAttribute("see", this.see);
    if (this.icon != null) xml.writeAttribute("icon", this.icon);
    if (this.fpi != null) xml.writeAttribute("fpi", this.fpi);
    if (this.richText != null) {
      // TODO
    }
    xml.writeEndElement();
  }
}
