package org.pageseeder.schematron.svrl;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * <pre>
 *  # only references are reported, not the diagnostic
 *  Diagnostic-reference =
 *     element diagnostic-reference {
 *         attribute diagnostic { xsd:NMTOKEN },
 *         human-text
 *     }
 * </pre>
 */
public final class DiagnosticReference implements XMLStreamable {

  private String diagnostic;
  private HumanText text;

  public String getDiagnostic() {
    return diagnostic;
  }

  public HumanText getText() {
    return text;
  }

  public void setDiagnostic(String diagnostic) {
    this.diagnostic = diagnostic;
  }

  public void setText(HumanText text) {
    this.text = text;
  }

  @Override
  public void toXMLStream(XMLStreamWriter xml) throws XMLStreamException {
    xml.writeStartElement("svrl", "diagnostic-reference", SVRL.NAMESPACE_URI);
    if (this.diagnostic != null) xml.writeAttribute("diagnostic", this.diagnostic);
    if (this.text != null) this.text.toXMLStream(xml);
    xml.writeEndElement();
  }

}
