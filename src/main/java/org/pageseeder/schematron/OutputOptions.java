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
 * <p>This class uses a fluent style.
 *
 * @author Christophe Lauret
 *
 * @version 2.0
 * @since 2.0
 */
public final class OutputOptions {

  private static final OutputOptions DEFAULT =  new OutputOptions("utf-8", false, true);

  private final String encoding;

  private final boolean indent;

  private final boolean omitXmlDeclaration;

  private OutputOptions(String encoding, boolean indent, boolean omitXmlDeclaration) {
    this.encoding = encoding;
    this.indent = indent;
    this.omitXmlDeclaration = omitXmlDeclaration;
  }

  /**
   * The default output options use UTF-8 encoding, do not indent the results and omit the XML declaration.
   *
   * @return The default output options
   */
  public static OutputOptions defaults() {
    return DEFAULT;
  }

  public OutputOptions encoding(String encoding) {
    return new OutputOptions(encoding, this.indent, this.omitXmlDeclaration);
  }

  public OutputOptions indent(boolean indent) {
    return new OutputOptions(this.encoding, indent, this.omitXmlDeclaration);
  }

  public OutputOptions omitXmlDeclaration(boolean omitXmlDeclaration) {
    return new OutputOptions(this.encoding, this.indent, omitXmlDeclaration);
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
}
