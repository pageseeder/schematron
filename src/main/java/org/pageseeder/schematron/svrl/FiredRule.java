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
 *
 * @author Christophe Lauret
 *
 * @version 2.0
 * @since 2.0
 */
public class FiredRule implements XMLStreamable {
  private String id;
  private String name;
  private String context;
  private String role;
  private String flag;

  private final List<AssertOrReport> assertsAndReports = new ArrayList<>();

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

  void addAssertOrReport(AssertOrReport assertOrReport) {
    this.assertsAndReports.add(assertOrReport);
  }

  void setId(String id) {
    this.id = id;
  }

  void setName(String name) {
    this.name = name;
  }

  void setContext(String context) {
    this.context = context;
  }

  void setRole(String role) {
    this.role = role;
  }

  void setFlag(String flag) {
    this.flag = flag;
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
