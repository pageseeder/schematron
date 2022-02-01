package org.pageseeder.schematron.svrl;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * <pre>
 *   schematron-output =
 *     element schematron-output {
 *         attribute title { text }?,
 *         attribute phase { xsd:NMTOKEN }?,
 *         attribute schemaVersion { text }?,
 *         human-text*,
 *         ns-prefix-in-attribute-values*,
 *         (active-pattern, (fired-rule, (failed-assert | successful-report)*)+)+
 * </pre>
 */
public final class SchematronOutput implements XMLStreamable {

  private String title;

  private String phase;

  private String schemaVersion;

  private List<Namespace> nsPrefixInAttributeValues = new ArrayList<>();

  private List<HumanText> texts = new ArrayList<>();

  private List<ActivePattern> activePatterns = new ArrayList<>();

  public String getTitle() {
    return title;
  }

  public String getPhase() {
    return phase;
  }

  public String getSchemaVersion() {
    return schemaVersion;
  }

  public List<HumanText> getTexts() {
    return texts;
  }

  public List<ActivePattern> getActivePatterns() {
    return activePatterns;
  }

  public List<AssertOrReport> getFailedAsserts() {
    List<AssertOrReport> failedAsserts = new ArrayList<>();
    for (ActivePattern activePattern : activePatterns) {
      for (FiredRule firedRule : activePattern.getFiredRules()) {
        for (AssertOrReport assertOrReport : firedRule.getAssertsAndReports()) {
          if (assertOrReport.isFailedAssert()) {
            failedAsserts.add(assertOrReport);
          }
        }
      }
    }
    return failedAsserts;
  }

  public List<AssertOrReport> getSuccessfulReports() {
    List<AssertOrReport> successfulReports = new ArrayList<>();
    for (ActivePattern activePattern : activePatterns) {
      for (FiredRule firedRule : activePattern.getFiredRules()) {
        for (AssertOrReport assertOrReport : firedRule.getAssertsAndReports()) {
          if (!assertOrReport.isFailedAssert()) {
            successfulReports.add(assertOrReport);
          }
        }
      }
    }
    return successfulReports;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setPhase(String phase) {
    this.phase = phase;
  }

  public void setSchemaVersion(String schemaVersion) {
    this.schemaVersion = schemaVersion;
  }

  public void addText(HumanText text) {
    this.texts.add(text);
  }

  public void addActivePattern(ActivePattern activePattern) {
    this.activePatterns.add(activePattern);
  }

  public void toXMLStream(XMLStreamWriter xml) throws XMLStreamException {
    xml.writeStartElement("svrl", "schematron-output", SVRL.NAMESPACE_URI);
    if (this.title != null) xml.writeAttribute("title", this.title);
    if (this.phase != null) xml.writeAttribute("phase", this.phase);
    if (this.schemaVersion != null) xml.writeAttribute("schemaVersion", this.schemaVersion);
    for (HumanText text : this.texts) {
      text.toXMLStream(xml);
    }
    for (Namespace namespace : this.nsPrefixInAttributeValues) {
      xml.writeEmptyElement("svrl", "ns-prefix-in-attribute-values", SVRL.NAMESPACE_URI);
      xml.writeAttribute("prefix", namespace.getPrefix());
      xml.writeAttribute("uri", namespace.getUri());
    }
    for (ActivePattern activePattern : this.activePatterns) {
      activePattern.toXMLStream(xml);
    }
    xml.writeEndElement();
  }
}
