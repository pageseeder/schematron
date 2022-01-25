package org.pageseeder.schematron;

import org.junit.Test;

import java.io.File;

public class ValidatorTest {

  @Test
  public void testValidate0() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-default.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult result = validator.validate(sample);
    System.out.println(result.isValid());
    System.out.println(result.getSVRLAsString());
  }

  @Test
  public void testValidate1() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-xslt1.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult result = validator.validate(sample);
    System.out.println(result.isValid());
    System.out.println(result.getSVRLAsString());
  }

  @Test
  public void testValidate2() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-xslt2.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult result = validator.validate(sample);
    System.out.println(result.isValid());
    System.out.println(result.getSVRLAsString());
  }

  @Test
  public void testValidateWithMetadata() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    factory.setOptions(CompileOptions.defaults().metadata(true));
    File schema = new File("src/test/resources/sch/standalone-xslt2.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult result = validator.validate(sample);
    System.out.println(result.isValid());
    System.out.println(result.getSVRLAsString());
  }

  @Test
  public void testValidateSplitNoMetadata2() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    factory.setOptions(CompileOptions.defaults().metadata(false));
    File schema = new File("src/test/resources/sch/split-xslt2.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult result = validator.validate(sample);
    System.out.println(result.isValid());
    System.out.println(result.getSVRLAsString());
  }

  @Test
  public void testValidateOptions() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    factory.setOptions(CompileOptions.defaults().metadata(true));
    File schema = new File("src/test/resources/sch/split-xslt2.sch");
    Validator validator = factory.newValidator(schema);
    File sample = new File("src/test/resources/xml/books.xml");
    SchematronResult result = validator.validate(sample, OutputOptions.defaults().indent(true));
    System.out.println(result.isValid());
    System.out.println(result.getSVRLAsString());
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

}
