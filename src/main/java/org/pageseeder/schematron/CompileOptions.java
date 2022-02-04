/*
 * Copyright 2015 Allette Systems (Australia)
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Available options for compiling Schematron.
 *
 * <p>Compile options must be supplied to the Schematron compiler and affect the generated validator.</p>
 *
 * <p>This class uses a fluent style and instances are immutable so that options can be reused without side-effects.
 *
 * <p>Unless specified, Schematron uses the {@link #defaults()} method.</p>
 *
 * <p>For backward-compatibility with the previous version of this library, the defaults can be overriden to
 * use behave like the previous version. To run in compatibility mode, set the system property
 * <code>org.pageseeder.schematron.compatibility</code> to "1.0".</p>
 *
 * @author Christophe Lauret
 * @version 2.0
 * @since 2.0
 */
public final class CompileOptions {

  private static final CompileOptions DEFAULT = new CompileOptions("xslt", false, false, false);

  private static final CompileOptions DEFAULT_COMPATIBILITY = new CompileOptions("xslt2", false, false, false);

  private final String defaultQueryBinding;
  private final boolean streamable;
  private final boolean metadata;
  private final boolean compact;

  /** Keep constructor private */
  private CompileOptions(String defaultQueryBinding, boolean metadata, boolean streamable, boolean compact) {
    this.defaultQueryBinding = defaultQueryBinding;
    this.metadata = metadata;
    this.streamable = streamable;
    this.compact = compact;
  }

  /**
   * The default compile options.
   *
   * <p>They are:</p>
   * <ul>
   *   <li><code>defaultQueryBinding = "xslt"</code></li>
   *   <li><code>metadata = false</code></li>
   *   <li><code>streamable = false</code></li>
   *   <li><code>compact = false</code></li>
   * </ul>
   *
   * <p>In compatibility mode, <code>defaultQueryBinding = "xslt2"</code></p>
   *
   * @return the default compile options for Schematron.
   */
  public static CompileOptions defaults() {
    if ("1.0".equals(System.getProperty("org.pageseeder.schematron.compatibility")))
      return DEFAULT_COMPATIBILITY;
    return DEFAULT;
  }

  /**
   * Schematron assumes `xslt` by default, but this method allows you to change this.
   *
   * @param defaultQueryBinding The query binding to use if not specified in the Schema.
   *
   * @return A new instance using the specified option.
   */
  public CompileOptions defaultQueryBinding(String defaultQueryBinding) {
    return new CompileOptions(defaultQueryBinding, this.metadata, this.streamable, this.compact);
  }

  /**
   * Whether to generate and include the `<sch:metadata>` element in the SVRL output.
   *
   * @implNote Used to set the `schxslt.compile.metadata` XSLT parameter.
   *
   * @param metadata The Schematron metadata.
   *
   * @return A new instance using the specified option.
   */
  public CompileOptions metadata(boolean metadata) {
    return new CompileOptions(this.defaultQueryBinding, metadata, this.streamable, this.compact);
  }

  /**
   * Set whether the source is streamble.
   *
   * @implNote Used to set the `schxslt.compile.streamable` XSLT parameter.
   *
   * @return A new instance using the specified option.
   */
  public CompileOptions streamable(boolean streamable) {
    return new CompileOptions(this.defaultQueryBinding, this.metadata, streamable, this.compact);
  }

  /**
   * <p>The latest version all.
   *
   * @implNote Used to set the `schxslt.svrl.compact` XSLT parameter.
   *
   * @return A new instance using the specified option.
   */
  public CompileOptions compact(boolean compact) {
    return new CompileOptions(this.defaultQueryBinding, this.metadata, this.streamable, compact);
  }

  /**
   * Indicates which query binding to use if not specified in the Schema.
   *
   * @return "xslt" by default, "xslt2" in compatibility mode.
   */
  public String defaultQueryBinding() {
    return this.defaultQueryBinding;
  }

  /**
   * Indicates whether to only generate the compact SVRL output
   *
   * @return <code>false</code> by default
   */
  public boolean isCompact() {
    return this.compact;
  }

  /**
   * Indicates whether to include the `<sch:metadata>` element in the SVRL output
   *
   * @return <code>false</code> by default
   */
  public boolean hasMetadata() {
    return this.metadata;
  }

  /**
   * @return <code>false</code> by default
   */
  public boolean isStreamable() {
    return this.streamable;
  }

  /**
   * @return the parameters sent to the preprocessors which are equivalent to these options.
   */
  public Map<String, Object> toParameters() {
    return toParameters(null);
  }

  /**
   * Returns a new mutable parameter map containing the parameters corresponding to these
   * options.
   *
   * @param phase The phase to use for validation
   * @return the parameters sent to the preprocessors which are equivalent to these options.
   */
  public Map<String, Object> toParameters(String phase) {
    Map<String, Object> parameters = new HashMap<>();
    // set the phase if specified
    if (phase != null) {
      parameters.put("phase", phase);
    }
    parameters.put("schxslt.compile.streamable", this.streamable);
    parameters.put("schxslt.compile.metadata", this.metadata);
    parameters.put("schxslt.svrl.compact", this.compact);
    return parameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CompileOptions that = (CompileOptions) o;

    if (streamable != that.streamable) return false;
    if (metadata != that.metadata) return false;
    if (compact != that.compact) return false;
    return Objects.equals(defaultQueryBinding, that.defaultQueryBinding);
  }

  @Override
  public int hashCode() {
    int result = defaultQueryBinding != null ? defaultQueryBinding.hashCode() : 0;
    result = 31 * result + (streamable ? 1 : 0);
    result = 31 * result + (metadata ? 1 : 0);
    result = 31 * result + (compact ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "CompileOptions{" +
        "defaultQueryBinding='" + defaultQueryBinding + '\'' +
        ", streamable=" + streamable +
        ", metadata=" + metadata +
        ", compact=" + compact +
        '}';
  }
}
