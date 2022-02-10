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
 * <pre>
 *  # only references are reported, not the diagnostic
 *  Diagnostic-reference =
 *     element diagnostic-reference {
 *         attribute diagnostic { xsd:NMTOKEN },
 *         human-text
 *     }
 * </pre>
 *
 * @author Christophe Lauret
 *
 * @version 2.0
 * @since 2.0
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

  public String getTextAsString() {
    return this.text != null? this.text.toPlainText() : null;
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
