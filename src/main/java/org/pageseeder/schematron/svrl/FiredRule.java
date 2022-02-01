package org.pageseeder.schematron.svrl;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *  # only rules that are fired are reported,
 *  fired-rule =
 *     element fired-rule {
 *         attribute id { xsd:ID }?,
 *         attribute name { text }?,
 *         attribute context { text },
 *         attribute role { xsd:NMTOKEN }?,
 *         attribute flag { xsd:NMTOKEN }?,
 *         empty
 *     }
 * </pre>
 */
public class FiredRule implements XMLStreamable {
  private String id;
  private String name;
  private String context;
  private String role;
  private String flag;

  private List<AssertOrReport> assertsAndReports = new ArrayList<>();

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getContext() {
    return context;
  }

  public String getRole() {
    return role;
  }

  public String getFlag() {
    return flag;
  }

  public List<AssertOrReport> getAssertsAndReports() {
    return assertsAndReports;
  }

  public void AddAssertOrReport(AssertOrReport assertOrReport) {
    this.assertsAndReports.add(assertOrReport);
  }

  @Override
  public void toXMLStream(XMLStreamWriter xml) throws XMLStreamException {
    xml.writeEmptyElement("svrl", "fired-rule", SVRL.NAMESPACE_URI);
    if (this.id != null) xml.writeAttribute("id", this.id);
    if (this.name != null) xml.writeAttribute("name", this.name);
    if (this.context != null) xml.writeAttribute("context", this.context);
    if (this.role != null) xml.writeAttribute("role", this.role);
    if (this.flag != null) xml.writeAttribute("flag", this.flag);
    // Asserts and reports come after the `fired-rule` element
    for (AssertOrReport assertOrReport : this.assertsAndReports) {
      assertOrReport.toXMLStream(xml);
    }
  }
}
