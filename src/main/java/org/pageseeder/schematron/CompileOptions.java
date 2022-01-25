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

/**
 * Available options for compiling Schematron.
 *
 * <p>This class uses a fluent style.
 *
 * @author Christophe Lauret
 * @version 2.0
 * @since 2.0
 */
public final class CompileOptions {

  private static final CompileOptions DEFAULT = new CompileOptions("xslt", false, false, false);

  private final String defaultQueryBinding;
  private final boolean streamable;
  private final boolean metadata;
  private final boolean compact;

  private CompileOptions(String defaultQueryBinding, boolean metadata, boolean streamable, boolean compact) {
    this.defaultQueryBinding = defaultQueryBinding;
    this.metadata = metadata;
    this.streamable = streamable;
    this.compact = compact;
  }

  /**
   * @return the default compile options for Schematron.
   */
  public static CompileOptions defaults() {
    return DEFAULT;
  }

  public CompileOptions defaultQueryBinding(String defaultQueryBinding) {
    return new CompileOptions(defaultQueryBinding, this.metadata, this.streamable, this.compact);
  }

  public CompileOptions metadata(boolean metadata) {
    return new CompileOptions(this.defaultQueryBinding, metadata, this.streamable, this.compact);
  }

  public CompileOptions streamable(boolean streamable) {
    return new CompileOptions(this.defaultQueryBinding, this.metadata, streamable, this.compact);
  }

  public CompileOptions compact(boolean compact) {
    return new CompileOptions(this.defaultQueryBinding, this.metadata, this.streamable, compact);
  }

  public String defaultQueryBinding() {
    return this.defaultQueryBinding;
  }

  public boolean isCompact() {
    return this.compact;
  }

  public boolean hasMetadata() {
    return this.metadata;
  }

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

}
