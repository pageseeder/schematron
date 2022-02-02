package org.pageseeder.schematron.svrl;

import javax.xml.namespace.QName;
import java.util.Objects;

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

  public Namespace() {
  }

  public Namespace(String prefix, String uri) {
    this.prefix = Objects.toString(prefix, "");
    this.uri = Objects.toString(uri, "");
  }

  Namespace(QName name) {
    this(name.getPrefix(), name.getNamespaceURI());
  }

  public String getPrefix() {
    return prefix;
  }

  public String getUri() {
    return uri;
  }

  void setPrefix(String prefix) {
    this.prefix = Objects.toString(prefix, "");
  }

  void setUri(String uri) {
    this.uri = Objects.toString(uri, "");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Namespace namespace = (Namespace) o;
    return getPrefix().equals(namespace.getPrefix()) && getUri().equals(namespace.getUri());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPrefix(), getUri());
  }

  @Override
  public String toString() {
    return "Namespace{" +
        "prefix='" + prefix + '\'' +
        ", uri='" + uri + '\'' +
        '}';
  }
}
