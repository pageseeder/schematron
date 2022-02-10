package org.pageseeder.schematron;

import org.junit.Assert;
import org.junit.Test;
import org.pageseeder.schematron.svrl.AssertOrReport;
import org.pageseeder.schematron.svrl.SchematronOutput;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidatorTest {

  @Test
  public void testValidateDefault() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-default.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult result = validator.validate(sample);
    Assert.assertTrue(result.isValid());
    System.out.println(result.getSVRLAsString());
  }

  @Test
  public void testValidateXslt1() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-xslt1.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult result = validator.validate(sample);
    System.out.println(result.isValid());
    System.out.println(result.getSVRLAsString());
  }

  @Test
  public void testValidateXslt2() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-xslt2.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult result = validator.validate(sample);
    System.out.println(result.isValid());
    System.out.println(result.getSVRLAsString());
  }

  @Test
  public void testValidateXslt3() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-xslt3.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult result = validator.validate(sample);
    System.out.println(result.isValid());
    System.out.println(result.getSVRLAsString());
  }

  @Test
  public void testValidateSplitXslt2() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/split-xslt2.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult resultNo = validator.validate(sample);
  }

  @Test
  public void testValidateWithMetadata() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory(CompileOptions.defaults().metadata(true));
    File schema = new File("src/test/resources/sch/basic-xslt2.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult result = validator.validate(sample);
    Assert.assertTrue(result.getSVRLAsString().contains("<svrl:metadata "));
  }

  @Test
  public void testValidateNoMetadata() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory(CompileOptions.defaults().metadata(false));
    File schema = new File("src/test/resources/sch/basic-xslt2.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult result = validator.validate(sample);
    Assert.assertFalse(result.getSVRLAsString().contains("<svrl:metadata "));
  }

  @Test
  public void testValidateIndent() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/basic-xslt2.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    String svrlIndent = validator.options(OutputOptions.defaults().indent(true)).validate(sample).getSVRLAsString();
    String svrlNoIndent = validator.options(OutputOptions.defaults().indent(false)).validate(sample).getSVRLAsString();
    Assert.assertTrue(svrlIndent.length() > svrlNoIndent.length());
    Assert.assertEquals(
        svrlIndent.replaceAll("\\s+", ""),
        svrlNoIndent.replaceAll("\\s+", ""));
  }

  @Test
  public void testValidateOmitXmlDeclaration() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/basic-xslt2.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult resultNo = validator.options(OutputOptions.defaults().omitXmlDeclaration(false)).validate(sample);
    Assert.assertTrue(resultNo.getSVRLAsString().contains("<?xml"));
    SchematronResult resultYes = validator.options(OutputOptions.defaults().omitXmlDeclaration(true)).validate(sample);
    Assert.assertFalse(resultYes.getSVRLAsString().contains("<?xml"));
  }

  @Test
  public void testValidatePhase() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    factory.setOptions(CompileOptions.defaults().metadata(false));
    File schema = new File("src/test/resources/sch/standalone-phase.sch");
    Validator validator = factory.newValidator(schema,"authoring");
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult result = validator.validate(sample);
    System.out.println(result.isValid());
    System.out.println(result.getSVRLAsString());
  }

  @Test
  public void testValidateNamespaces() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/namespaces-xslt2.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/namespaces.xml");
    OutputOptions options = OutputOptions.defaults().usePrefixInLocation(true).indent(true);
    SchematronResult result = validator.options(options).validate(sample);
    System.out.println(result.isValid());
    System.out.println(result.getSVRLAsString());
  }

  @Test
  public void testValidateCompatibility() throws SchematronException {
    System.setProperty("org.pageseeder.schematron.compatibility", "1.0");
    Assert.assertEquals("xslt2", CompileOptions.defaults().defaultQueryBinding());
    Assert.assertTrue(OutputOptions.defaults().isIndent());
    Assert.assertTrue(OutputOptions.defaults().isOmitXmlDeclaration());
    Assert.assertTrue(OutputOptions.defaults().usePrefixInLocation());
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-default.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/namespaces.xml");
    SchematronResult result = validator.validate(sample);
    System.clearProperty("org.pageseeder.schematron.compatibility");
    Assert.assertEquals("xslt", CompileOptions.defaults().defaultQueryBinding());
    Assert.assertFalse(OutputOptions.defaults().isIndent());
    Assert.assertFalse(OutputOptions.defaults().isOmitXmlDeclaration());
    Assert.assertFalse(OutputOptions.defaults().usePrefixInLocation());
  }

  @Test
  public void testValidateParameter() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/params-xslt2.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult result = validator.newInstance().validate(sample, Collections.singletonMap("gotit", "Yes!!"));
    Assert.assertTrue(result.isValid());
    SchematronOutput svrl = result.toSchematronOutput();
    Assert.assertNotNull(svrl);
    List<AssertOrReport> reports = svrl.getSuccessfulReports();
    Assert.assertEquals(1, reports.size());
    String message = reports.get(0).toMessageString();
    Assert.assertTrue(message.contains("Yes!!"));
  }

  @Test
  public void testValidateParameterTypes() throws SchematronException, ParserConfigurationException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/params-types-xslt2.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");

    // Create DOM element
    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    Element element = doc.createElement("greeting");
    element.setAttribute("lang", "fr");
    element.setTextContent("Bonjour!");

    // Create parameters
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("name", "John Smith"); // String
    parameters.put("age", 33); // Integer
    parameters.put("dob", LocalDate.parse("1999-12-31")); // Date
    parameters.put("id", 1234567890L); // Long
    parameters.put("greeting", element); // DOM element

    // Validate
    SchematronResult result = validator.newInstance().validate(sample, parameters);
    AssertOrReport report = result.toSchematronOutput().getSuccessfulReports().get(0);

    // And check parameters
    Assert.assertEquals("John Smith",  report.getPropertyText("name"));
    Assert.assertEquals("33",  report.getPropertyText("age"));
    Assert.assertEquals("1999-12-31",  report.getPropertyText("dob"));
    Assert.assertEquals("1234567890",  report.getPropertyText("id"));
    Assert.assertEquals("fr",  report.getPropertyText("lang"));
    Assert.assertEquals("Bonjour!",  report.getPropertyText("greeting"));
  }


  @Test
  public void testValidateInstanceSpeed() throws SchematronException, ParserConfigurationException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/basic-xslt2.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");

    // Validate
    long t0 = System.currentTimeMillis();
    for (int i=0; i < 1000; i++) {
      SchematronResult result = validator.validate(sample);
      Assert.assertNotNull(result);
    }
    t0 = System.currentTimeMillis() - t0;

    long t1 = System.currentTimeMillis();
    Validator.Instance instance = validator.newInstance();
    for (int i=0; i < 1000; i++) {
      SchematronResult result = instance.validate(sample);
      Assert.assertNotNull(result);
    }
    t1 = System.currentTimeMillis() - t1;
    System.out.println(t0+"ms");
    System.out.println(t1+"ms");

    Assert.assertTrue(t0 > t1);
  }


}
