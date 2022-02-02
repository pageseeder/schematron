package org.pageseeder.schematron.svrl;

import org.junit.Test;
import org.pageseeder.schematron.SchematronException;

import org.junit.Assert;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class SVRLParserTest {

  @Test
  public void testParse() throws SchematronException, IOException {
    File svrl = new File("src/test/resources/svrl/sample1.svrl");
    SchematronOutput output = SVRLParser.parse(svrl);
    System.out.println(output.toXML());
    checkRoundTrip(output);
  }

  private void checkRoundTrip(SchematronOutput output) throws SchematronException {
    String exp = output.toXML();
    String got = SVRLParser.parse(new StringReader(exp)).toXML();
    Assert.assertEquals(exp, got);
  }

  @Test
  public void testListAsserts() throws SchematronException, IOException {
    File svrl = new File("src/test/resources/svrl/sample1.svrl");
    SchematronOutput output = SVRLParser.parse(svrl);
    List<AssertOrReport> asserts = output.getFailedAsserts();
    for (AssertOrReport failedAssert : asserts) {
      System.out.println(failedAssert.toMessageString(true));
    }
    checkRoundTrip(output);
  }

}
