package org.pageseeder.schematron;

import org.junit.Test;

import java.io.File;

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
  public void testCompileInvalid() throws SchematronException {
    ValidatorFactory factory = new ValidatorFactory();
    File schema = new File("src/test/resources/sch/standalone-invalid.sch");
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

}
