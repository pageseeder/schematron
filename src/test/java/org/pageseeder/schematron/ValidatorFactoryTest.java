package org.pageseeder.schematron;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public final class ValidatorFactoryTest {

  public ValidatorFactoryTest() {
    File dir = new File("src/test/resources/");
    if (!dir.exists()) {
      System.out.println("Unable to find: "+dir.getAbsolutePath());
    }
  }

  @Test
  public void testCompileStandaloneDefault() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-default.sch");
    factory.newValidator(schema);
  }

  @Test
  public void testCompileStandaloneXslt1() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-xslt1.sch");
    factory.newValidator(schema);
  }

  @Test
  public void testCompileStandaloneXslt2() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-xslt2.sch");
    factory.newValidator(schema);
  }

  @Test
  public void testCompileSplitXslt2() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/split-xslt2.sch");
    factory.newValidator(schema);
  }

  @Test
  public void testCompileStandaloneXslt3() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-xslt3.sch");
    factory.newValidator(schema);
  }

  @Test(expected = SchematronException.class)
  public void testCompileInvalid() throws Exception {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-invalid.sch");
    validateSchematron(new InputSource(new FileReader(schema)));
    factory.newValidator(schema);
  }

  @Test(expected = SchematronException.class)
  public void testCompileMalformed() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-malformed.sch");
    factory.newValidator(schema);
  }

  @Test(expected = SchematronException.class)
  public void testCompileUnknownBinding() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-unknown-binding.sch");
    factory.newValidator(schema);
  }

  @Test
  public void testCompileWithDebug() throws SchematronException {
    StringWriter debug = new StringWriter();
    ValidatorFactory factory = new ValidatorFactory().debug((systemId -> debug));
    File schema = new File("src/test/resources/sch/standalone-xslt2.sch");
    factory.newValidator(schema);
    Assert.assertTrue(debug.toString().length() > 0);
  }

  @Test
  public void testCompileWithDebugFile() throws SchematronException {
    AtomicReference<File> debugFile = new AtomicReference<>();
    ValidatorFactory factory = new ValidatorFactory()
        .debug((systemId) -> {
          File debug = Files.createTempFile("schematron-", ".xsl").toFile();
          debug.deleteOnExit();
          debugFile.set(debug);
          return new FileWriter(debug);
        });
    File schema = new File("src/test/resources/sch/standalone-xslt2.sch");
    factory.newValidator(schema);
    Assert.assertTrue(debugFile.get().exists());
    Assert.assertTrue(debugFile.get().length() > 0);
  }

  private static List<String> validateSchematron(InputSource source) throws IOException {
    List<String> errors = new ArrayList<>();
    try {
      XMLReader reader = XMLReaderFactory.createXMLReader();
      reader.setFeature("http://xml.org/sax/features/validation", true);
      reader.setFeature("http://apache.org/xml/features/validation/schema", true);
      reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
      String path = new File("src/test/resources/xsd/iso-schematron-2016.xsd").getAbsolutePath();
      reader.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", "http://purl.oclc.org/dsdl/schematron" + " " + path);
      reader.setErrorHandler(new DefaultHandler() {
        @Override
        public void error(SAXParseException ex) {
          String error = "[L" + ex.getLineNumber() + ":" + ex.getColumnNumber() + "] " + ex.getMessage();
          System.out.println(error);
          errors.add(error);
        }

        @Override
        public void fatalError(SAXParseException ex) {
          String error = "[" + ex.getLineNumber() + ":" + ex.getColumnNumber() + "] " + ex.getMessage();
          System.out.println(error);
          errors.add(error);
        }
      });
      reader.parse(source);
    } catch (SAXException ex) {
      // return false
      ex.printStackTrace();
    }
    return errors;
  }

}
