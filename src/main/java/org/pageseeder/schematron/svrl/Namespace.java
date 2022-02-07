/*
 * Copyright 2022 Allette Systems (Australia)
 * http://www.allette.com.au
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pageseeder.schematron.svrl;

import javax.xml.namespace.QName;
import java.util.Objects;

/**
 * A namespace prefix mapping.
 *
 * @author Christophe Lauret
 *
 * @version 2.0
 * @since 2.0
 */
public final class Namespace {

  private final String prefix;
  private final String uri;

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
