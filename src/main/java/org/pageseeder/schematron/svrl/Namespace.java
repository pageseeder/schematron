package org.pageseeder.schematron.svrl;

/**
 * <pre>
 *  # only namespaces from sch:ns need to be reported
 *  ns-prefix-in-attribute-values =
 *     element ns-prefix-in-attribute-values {
 *         attribute prefix { xsd:NMTOKEN },
 *         attribute uri { text },
 *         empty
 *     }
 * </pre>
 *
 */
public final class Namespace {

  private String prefix;
  private String uri;

  public String getPrefix() {
    return prefix;
  }

  public String getUri() {
    return uri;
  }

  void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  void setUri(String uri) {
    this.uri = uri;
  }
}
