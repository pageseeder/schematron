package org.pageseeder.schematron;

import org.junit.Assert;
import org.junit.Test;

public final class CompileOptionsTest {

  @Test
  public void testDefault() {
    Assert.assertEquals("xslt", CompileOptions.defaults().defaultQueryBinding());
    Assert.assertFalse(CompileOptions.defaults().hasMetadata());
    Assert.assertFalse(CompileOptions.defaults().isStreamable());
    Assert.assertFalse(CompileOptions.defaults().isCompact());
  }

  @Test
  public void testCompatibility() {
    System.setProperty("org.pageseeder.schematron.compatibility", "1.0");
    Assert.assertEquals("xslt2", CompileOptions.defaults().defaultQueryBinding());
    Assert.assertFalse(CompileOptions.defaults().hasMetadata());
    Assert.assertFalse(CompileOptions.defaults().isStreamable());
    Assert.assertFalse(CompileOptions.defaults().isCompact());
    System.clearProperty("org.pageseeder.schematron.compatibility");
    Assert.assertEquals("xslt", CompileOptions.defaults().defaultQueryBinding());
    Assert.assertFalse(CompileOptions.defaults().hasMetadata());
    Assert.assertFalse(CompileOptions.defaults().isStreamable());
    Assert.assertFalse(CompileOptions.defaults().isCompact());
  }
}
