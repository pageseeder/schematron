package org.pageseeder.schematron.svrl;

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

  public HumanText getText() {
    return text;
  }

  public void setFailedAssert(boolean failedAssert) {
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

  void addPropertyReference(DiagnosticReference diagnosticReference) {
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
