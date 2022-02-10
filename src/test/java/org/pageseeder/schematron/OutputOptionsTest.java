package org.pageseeder.schematron;

import org.junit.Assert;
import org.junit.Test;

public final class OutputOptionsTest {

  @Test
  public void testDefault() {
    Assert.assertEquals("utf-8", OutputOptions.defaults().encoding());
    Assert.assertFalse(OutputOptions.defaults().isIndent());
    Assert.assertFalse(OutputOptions.defaults().isOmitXmlDeclaration());
    Assert.assertFalse(OutputOptions.defaults().usePrefixInLocation());
  }

  @Test
  public void testCompatibility() {
    System.setProperty("org.pageseeder.schematron.compatibility", "1.0");
    Assert.assertEquals("utf-8", OutputOptions.defaults().encoding());
    Assert.assertTrue(OutputOptions.defaults().isIndent());
    Assert.assertTrue(OutputOptions.defaults().isOmitXmlDeclaration());
    Assert.assertTrue(OutputOptions.defaults().usePrefixInLocation());
    System.clearProperty("org.pageseeder.schematron.compatibility");
  }
}
