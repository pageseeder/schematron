package org.pageseeder.schematron.svrl;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.Reader;
import java.io.StringReader;

public class SVRLReader {

  private SchematronOutput schematronOutput;
  private ActivePattern currentActivePattern;
  private FiredRule currentFiredRule;
  private AssertOrReport currentAssertOrReport;
  private DiagnosticReference currentDiagnosticReference;
  private PropertyReference currentPropertyReference;
  private HumanText currentHumanText;

  public SchematronOutput parse(String svrl) {
    return parse(new StringReader(svrl));
  }

  public SchematronOutput parse(Reader svrl) {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    try {
      XMLEventReader eventReader = factory.createXMLEventReader(svrl);
      while (eventReader.hasNext()) {

        XMLEvent event = eventReader.nextEvent();

        if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
          StartElement startElement = event.asStartElement();
          QName name = startElement.getName();
          if (SVRL.NAMESPACE_URI.equals(name.getNamespaceURI())) {
            switch (name.getLocalPart()) {
              case "schematron-output":
                handleSchematronOutput(startElement);
                break;
              case "ns-prefix-in-attribute-values":
                handleNsPrefixInAttributeValues(startElement);
                break;
              case "active-pattern":
                handleActivePattern(startElement);
                break;
              case "fired-rule":
                handleFiredRule(startElement);
                break;
              case "failed-assert":
              case "successful-report":
                handleAssertOrReport(startElement);
                break;
              case "diagnostic-reference":
                handleDiagnosticReference(startElement);
                break;
              case "property-reference":
                handlePropertyReference(startElement);
                break;
              case "text":
                handleHumanText(startElement);
                break;
              default:
            }
          }

        } else if (event.getEventType() == XMLStreamConstants.CHARACTERS) {
          Characters chars = event.asCharacters();
          
        } else if (event.getEventType() == XMLStreamConstants.END_ELEMENT) {
          EndElement endElement = event.asEndElement();
          QName name = endElement.getName();
          if (SVRL.NAMESPACE_URI.equals(name.getNamespaceURI())) {
            switch (name.getLocalPart()) {
              case "failed-assert":
              case "successful-report":
                this.currentAssertOrReport = null;
                break;
              case "diagnostic-reference":
                this.currentDiagnosticReference = null;
                break;
              case "property-reference":
                this.currentPropertyReference = null;
                break;
              case "text":
                if (this.currentDiagnosticReference != null)
                  this.currentPropertyReference.setText(this.currentHumanText);
                else if (this.currentPropertyReference != null)
                  this.currentPropertyReference.setText(this.currentHumanText);
                else if (this.currentAssertOrReport != null)
                  this.currentAssertOrReport.setText(this.currentHumanText);
                else
                  this.schematronOutput.addText(this.currentHumanText);
                this.currentHumanText = null;
                break;
              default:
            }
          }
        }
      }


    } catch (XMLStreamException ex) {
      ex.printStackTrace();
    }
    return this.schematronOutput;
  }

  private void handleSchematronOutput(StartElement startElement) {
    this.schematronOutput = new SchematronOutput();
    String title = getAttributeValue(startElement, "title");
    String phase = getAttributeValue(startElement, "phase");
    String schemaVersion = getAttributeValue(startElement, "schemaVersion");
    if (title != null) this.schematronOutput.setTitle(title);
    if (phase != null) this.schematronOutput.setPhase(phase);
    if (schemaVersion != null) this.schematronOutput.setSchemaVersion(schemaVersion);
  }

  private void handleActivePattern(StartElement startElement) {
    ActivePattern activePattern = new ActivePattern();
    String id = getAttributeValue(startElement, "id");
    String name = getAttributeValue(startElement, "name");
    String documents = getAttributeValue(startElement, "documents");
    String role = getAttributeValue(startElement, "role");
    if (id != null) activePattern.setId(id);
    if (name != null) activePattern.setName(name);
    if (documents != null) activePattern.setDocuments(documents);
    if (role != null) activePattern.setDocuments(role);
    this.currentActivePattern = activePattern;
  }

  private void handleAssertOrReport(StartElement startElement) {
    AssertOrReport assertOrReport = new AssertOrReport();
    assertOrReport.setFailedAssert("failed-assert".equals(startElement.getName().getLocalPart()));
    String id = getAttributeValue(startElement, "id");
    String location = getAttributeValue(startElement, "location");
    String test = getAttributeValue(startElement, "test");
    String role = getAttributeValue(startElement, "role");
    String flag = getAttributeValue(startElement, "flag");
    if (id != null) assertOrReport.setId(id);
    if (location != null) assertOrReport.setLocation(location);
    if (test != null) assertOrReport.setTest(test);
    if (role != null) assertOrReport.setRole(role);
    if (flag != null) assertOrReport.setFlag(flag);
    this.currentFiredRule.addAssertOrReport(assertOrReport);
    this.currentAssertOrReport = assertOrReport;
  }

  private void handleDiagnosticReference(StartElement startElement) {
    DiagnosticReference diagnosticReference = new DiagnosticReference();
    String diagnostic = getAttributeValue(startElement, "diagnostic");
    if (diagnostic != null) diagnosticReference.setDiagnostic(diagnostic);
    this.currentAssertOrReport.addDiagnosticReference(diagnosticReference);
    this.currentDiagnosticReference = diagnosticReference;
  }

  private void handleFiredRule(StartElement startElement) {
    FiredRule firedRule = new FiredRule();
    String id = getAttributeValue(startElement, "id");
    String name = getAttributeValue(startElement, "name");
    String context = getAttributeValue(startElement, "context");
    String role = getAttributeValue(startElement, "role");
    String flag = getAttributeValue(startElement, "flag");
    if (id != null) firedRule.setId(id);
    if (name != null) firedRule.setName(name);
    if (context != null) firedRule.setContext(context);
    if (role != null) firedRule.setRole(role);
    if (flag != null) firedRule.setFlag(flag);
    this.currentActivePattern.addFiredRule(firedRule);
    this.currentFiredRule = firedRule;
  }

  private void handlePropertyReference(StartElement startElement) {
    PropertyReference propertyReference = new PropertyReference();
    String property = getAttributeValue(startElement, "property");
    String role = getAttributeValue(startElement, "role");
    String scheme = getAttributeValue(startElement, "scheme");
    if (property != null) propertyReference.setProperty(property);
    if (role != null) propertyReference.setRole(role);
    if (scheme != null) propertyReference.setScheme(scheme);
    this.currentAssertOrReport.addPropertyReference(propertyReference);
    this.currentPropertyReference = propertyReference;
  }

  private void handleNsPrefixInAttributeValues(StartElement startElement) {
    Namespace ns = new Namespace();
    String prefix = getAttributeValue(startElement, "prefix");
    String uri = getAttributeValue(startElement, "uri");
    ns.setPrefix(prefix);
    ns.setUri(uri);
    this.schematronOutput.addNsPrefixInAttributeValues(ns);
  }

  private void handleHumanText(StartElement startElement) {
    HumanText humanText = new HumanText();
    String space = getAttributeValue(startElement, "xml:space");
    String lang = getAttributeValue(startElement, "xml:lang");
    String see = getAttributeValue(startElement, "see");
    String icon = getAttributeValue(startElement, "icon");
    String fpi = getAttributeValue(startElement, "fpi");
    if (space != null) humanText.setSpace(space);
    if (lang != null) humanText.setLang(lang);
    if (see != null) humanText.setLang(see);
    if (icon != null) humanText.setIcon(icon);
    if (fpi != null) humanText.setFpi(fpi);
    this.currentHumanText = humanText;
  }


  private static String getAttributeValue(StartElement startElement, String name) {
    Attribute att = startElement.getAttributeByName(new QName(name));
    return att != null ? att.getValue() : null;
  }
}
