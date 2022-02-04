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
package org.pageseeder.schematron;

/**
 * Output options for the validator.
 *
 * <p>Output options must be supplied to the Schematron validator and affect the generated SVRL output.</p>
 *
 * <p>This class uses a fluent style and instances are immutable so that options can be reused without side-effects.  *
 * <p>Unless specified, Schematron uses the {@link #defaults()} method.</p>
 *
 * <p>For backward-compatibility with the previous version of this library, the defaults can be overriden to
 * use behave like the previous version. To run in compatibility mode, set the system property
 * <code>org.pageseeder.schematron.compatibility</code> to "1.0".</p>
 *
 * @author Christophe Lauret
 *
 * @version 2.0
 * @since 2.0
 */
public final class OutputOptions {

  private static final OutputOptions DEFAULT = new OutputOptions("utf-8", false, true, false);

  private static final OutputOptions DEFAULT_COMPATIBILITY = new OutputOptions("utf-8", true, true, true);

  private final String encoding;

  private final boolean indent;

  private final boolean omitXmlDeclaration;

  private final boolean usePrefixInLocation;

  /** Keep constructor private */
  private OutputOptions(String encoding, boolean indent, boolean omitXmlDeclaration, boolean usePrefixInLocation) {
    this.encoding = encoding;
    this.indent = indent;
    this.omitXmlDeclaration = omitXmlDeclaration;
    this.usePrefixInLocation = usePrefixInLocation;
  }

  /**
   * The default output options use UTF-8 encoding, do not indent the results and omit the XML declaration.
   *
   * <ul>
   *   <li><code>encoding = "utf-8"</code></li>
   *   <li><code>indent = false</code></li>
   *   <li><code>omitXmlDeclaration = true</code></li>
   *   <li><code>usePrefixInLocation = false</code></li>
   * </ul>
   *
   * <p>In compatibility mode, <code>indent = false</code> and <code>usePrefixInLocation = true</code></p>
   *
   * @return The default output options
   */
  public static OutputOptions defaults() {
    if ("1.0".equals(System.getProperty("org.pageseeder.schematron.compatibility")))
      return DEFAULT_COMPATIBILITY;
    return DEFAULT;
  }

  public OutputOptions encoding(String encoding) {
    return new OutputOptions(encoding, this.indent, this.omitXmlDeclaration, this.usePrefixInLocation);
  }

  public OutputOptions indent(boolean indent) {
    return new OutputOptions(this.encoding, indent, this.omitXmlDeclaration, this.usePrefixInLocation);
  }

  public OutputOptions omitXmlDeclaration(boolean omitXmlDeclaration) {
    return new OutputOptions(this.encoding, this.indent, omitXmlDeclaration, this.usePrefixInLocation);
  }

  public OutputOptions usePrefixInLocation(boolean usePrefixInLocation) {
    return new OutputOptions(this.encoding, this.indent, this.omitXmlDeclaration, usePrefixInLocation);
  }

  public String encoding() {
    return this.encoding;
  }

  public boolean isIndent() {
    return this.indent;
  }

  public boolean isOmitXmlDeclaration() {
    return this.omitXmlDeclaration;
  }

  public boolean usePrefixInLocation() {
    return this.usePrefixInLocation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OutputOptions that = (OutputOptions) o;
    if (indent != that.indent) return false;
    if (omitXmlDeclaration != that.omitXmlDeclaration) return false;
    if (usePrefixInLocation != that.usePrefixInLocation) return false;
    return encoding.equals(that.encoding);
  }

  @Override
  public int hashCode() {
    int result = encoding.hashCode();
    result = 31 * result + (indent ? 1 : 0);
    result = 31 * result + (omitXmlDeclaration ? 1 : 0);
    result = 31 * result + (usePrefixInLocation ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "OutputOptions{" +
        "encoding='" + encoding + '\'' +
        ", indent=" + indent +
        ", omitXmlDeclaration=" + omitXmlDeclaration +
        ", usePrefixInLocation=" + usePrefixInLocation +
        '}';
  }
}
