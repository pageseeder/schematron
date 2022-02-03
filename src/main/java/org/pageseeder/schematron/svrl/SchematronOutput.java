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

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Top-level element of an SVRL document.
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
 *
 *  # only namespaces from sch:ns need to be reported
 *  ns-prefix-in-attribute-values =
 *     element ns-prefix-in-attribute-values {
 *         attribute prefix { xsd:NMTOKEN },
 *         attribute uri { text },
 *         empty
 *     }
 * </pre>
 *
 * @author Christophe Lauret
 *
 * @version 2.0
 * @since 2.0
 */
public final class SchematronOutput implements XMLStreamable {

  private String title;

  private String phase;

  private String schemaVersion;

  private boolean isCompact;

  private final List<Namespace> nsDeclarations = new ArrayList<>();

  private final List<Namespace> nsPrefixInAttributeValues = new ArrayList<>();

  private final List<HumanText> texts = new ArrayList<>();

  private final List<ActivePattern> activePatterns = new ArrayList<>();

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

  void setTitle(String title) {
    this.title = title;
  }

  public void setCompact(boolean compact) {
    this.isCompact = compact;
  }

  void setPhase(String phase) {
    this.phase = phase;
  }

  void setSchemaVersion(String schemaVersion) {
    this.schemaVersion = schemaVersion;
  }

  void addText(HumanText text) {
    this.texts.add(text);
  }

  void addNsDeclaration(Namespace namespace) {
    this.nsDeclarations.add(namespace);
  }

  void addNsPrefixInAttributeValues(Namespace namespace) {
    this.nsPrefixInAttributeValues.add(namespace);
  }

  void addActivePattern(ActivePattern activePattern) {
    this.activePatterns.add(activePattern);
  }

  public void toXMLStream(XMLStreamWriter xml) throws XMLStreamException {
    xml.writeStartElement("svrl", "schematron-output", SVRL.NAMESPACE_URI);
    for (Namespace ns : this.nsDeclarations) {
      xml.writeNamespace(ns.getPrefix(), ns.getUri());
    }
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
    if (this.isCompact) {
      for (ActivePattern activePattern : this.activePatterns) {
        for (FiredRule rule : activePattern.getFiredRules()) {
          for (AssertOrReport assertOrReport : rule.getAssertsAndReports()) {
            assertOrReport.toXMLStream(xml);
          }
        }
      }
    } else {
      for (ActivePattern activePattern : this.activePatterns) {
        activePattern.toXMLStream(xml);
      }
    }
    xml.writeEndElement();
  }

  public String toXML() {
    StringWriter xml = new StringWriter();
    toXML(xml);
    return xml.toString();
  }

  public void toXML(Writer xml) {
    try {
      XMLOutputFactory factory = XMLOutputFactory.newInstance();
      XMLStreamWriter writer = factory.createXMLStreamWriter(xml);
      writer.setPrefix("svrl", SVRL.NAMESPACE_URI);
      toXMLStream(writer);
    } catch (XMLStreamException ex) {
      // We might need to find a more appropriate
      throw new IllegalStateException(ex);
    }
  }
}
