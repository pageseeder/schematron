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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *  # only active patterns are reported
 *  active-pattern =
 *     element active-pattern {
 *         attribute id { xsd:ID }?,
 *         attribute documents { text }?,
 *         attribute name { text }?,
 *         attribute role { xsd:NMTOKEN }?,
 *         empty
 *     }
 * </pre>
 */
public final class ActivePattern implements XMLStreamable {
  private String id;
  private String documents;
  private String name;
  private String role;
  private List<FiredRule> firedRules = new ArrayList<>();

  public String getId() {
    return id;
  }

  public String getDocuments() {
    return documents;
  }

  public String getName() {
    return name;
  }

  public String getRole() {
    return role;
  }

  public List<FiredRule> getFiredRules() {
    return firedRules;
  }

  void setDocuments(String documents) {
    this.documents = documents;
  }

  void setId(String id) {
    this.id = id;
  }

  void setName(String name) {
    this.name = name;
  }

  void setRole(String role) {
    this.role = role;
  }

  void addFiredRule(FiredRule firedRule) {
    this.firedRules.add(firedRule);
  }

  public void toXMLStream(XMLStreamWriter xml) throws XMLStreamException {
    xml.writeEmptyElement("svrl", "active-pattern", SVRL.NAMESPACE_URI);
    if (this.id != null) xml.writeAttribute("id", this.id);
    if (this.name != null) xml.writeAttribute("name", this.name);
    if (this.documents != null) xml.writeAttribute("documents", this.documents);
    if (this.role != null) xml.writeAttribute("role", this.role);
    // Rules come after the `active-pattern` element
    for (FiredRule rule : this.firedRules) {
      rule.toXMLStream(xml);
    }
  }
}
