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
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *  # only failed assertions are reported
 *  failed-assert =
 *     element failed-assert {
 *        attlist.assert-and-report, diagnostic-reference*, property-reference*, human-text
 *     }
 *
 *  # only successful asserts are reported
 *  successful-report =
 *     element successful-report {
 *         attlist.assert-and-report, diagnostic-reference*, property-reference*, human-text
 *     }
 *
 *  attlist.assert-and-report =
 *     attribute id { xsd:ID }?,
 *     attribute location { text },
 *     attribute test { text },
 *     attribute role { xsd:NMTOKEN }?,
 *     attribute flag { xsd:NMTOKEN }?
 * </pre>
 *
 * @author Christophe Lauret
 *
 * @version 2.0
 * @since 2.0
 */
public final class AssertOrReport implements XMLStreamable {

  private boolean isFailedAssert = false;

  private String id;
  private String location;
  private String test;
  private String role;
  private String flag;

  private final List<DiagnosticReference> diagnosticReferences = new ArrayList<>();
  private final List<PropertyReference> propertyReferences = new ArrayList<>();
  private HumanText text = null;

  public boolean isFailedAssert() {
    return isFailedAssert;
  }

  public String getId() {
    return id;
  }

  public String getLocation() {
    return location;
  }

  public String getTest() {
    return test;
  }

  public String getRole() {
    return role;
  }

  public String getFlag() {
    return flag;
  }

  public List<DiagnosticReference> getDiagnosticReferences() {
    return diagnosticReferences;
  }

  public List<PropertyReference> getPropertyReferences() {
    return propertyReferences;
  }

  public PropertyReference getPropertyReference(String property) {
    for (PropertyReference ref : this.propertyReferences) {
      if (property.equals(ref.getProperty())) return ref;
    }
    return null;
  }

  public DiagnosticReference getDiagnosticReference(String diagnostic) {
    for (DiagnosticReference ref : this.diagnosticReferences) {
      if (diagnostic.equals(ref.getDiagnostic())) return ref;
    }
    return null;
  }

  public String getPropertyText(String property) {
    for (PropertyReference ref : this.propertyReferences) {
      if (property.equals(ref.getProperty())) return ref.getTextAsString();
    }
    return null;
  }

  public String getDiagnosticText(String diagnostic) {
    for (DiagnosticReference ref : this.diagnosticReferences) {
      if (diagnostic.equals(ref.getDiagnostic())) return ref.getTextAsString();
    }
    return null;
  }

  public HumanText getText() {
    return text;
  }

  /**
   * @return a message string for the console including location and text
   */
  public String toMessageString() {
    return toMessageString(true);
  }

  /**
   * @return a message string for the console including location and text
   */
  public String toMessageString(boolean includeDiagnostics) {
    StringBuilder out = new StringBuilder();
    out.append(isFailedAssert? "[assert] " : "[report] ");
    out.append(this.location).append(" - ").append(this.text.toPlainText());
    if (includeDiagnostics) {
      out.append(' ');
      for (DiagnosticReference diagnostic : this.diagnosticReferences) {
        out.append('(').append(diagnostic.getDiagnostic()).append(':');
        HumanText diagnosticText = diagnostic.getText();
        if (diagnosticText != null) {
          out.append(diagnosticText.toPlainText()).append(')');
        }
      }
    }
    return out.toString();
  }

  void setFailedAssert(boolean failedAssert) {
    this.isFailedAssert = failedAssert;
  }

  void setId(String id) {
    this.id = id;
  }

  void setLocation(String location) {
    this.location = location;
  }

  void setTest(String test) {
    this.test = test;
  }

  void setRole(String role) {
    this.role = role;
  }

  void setFlag(String flag) {
    this.flag = flag;
  }

  void setText(HumanText text) {
    this.text = text;
  }

  void addDiagnosticReference(DiagnosticReference diagnosticReference) {
    this.diagnosticReferences.add(diagnosticReference);
  }

  void addPropertyReference(PropertyReference propertyReference) {
    this.propertyReferences.add(propertyReference);
  }

  @Override
  public void toXMLStream(XMLStreamWriter xml) throws XMLStreamException {
    xml.writeStartElement("svrl", isFailedAssert? "failed-assert" : "successful-report", SVRL.NAMESPACE_URI);
    if (this.id != null) xml.writeAttribute("id", this.id);
    if (this.location != null) xml.writeAttribute("location", this.location);
    if (this.test != null) xml.writeAttribute("test", this.test);
    if (this.role != null) xml.writeAttribute("role", this.role);
    if (this.flag != null) xml.writeAttribute("flag", this.flag);
    if (this.role != null) xml.writeAttribute("role", this.role);
    for (DiagnosticReference diagnosticReference : diagnosticReferences) {
      diagnosticReference.toXMLStream(xml);
    }
    for (PropertyReference propertyReference : propertyReferences) {
      propertyReference.toXMLStream(xml);
    }
    if (this.text != null) this.text.toXMLStream(xml);
    xml.writeEndElement();
  }
}
