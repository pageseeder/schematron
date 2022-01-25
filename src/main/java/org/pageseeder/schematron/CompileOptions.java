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
 *
 *
 * @author Christophe Lauret
 * @version 2.0
 * @since 1.0
 */
public class CompileOptions {

  /**
   * Specifies a Schematron phase to use.
   */
  private String phase;

  public String defaultQueryBinding = "xslt";

  private final Map<String, Object> options = new HashMap<>();

  /**
   *
   */
  public CompileOptions() {
  }

  /**
   * Specifies the phase Schematron should use.
   *
   * <p>If this parameter is set the schematron processor will match this parameter value with
   * the 'id' attribute of a phase element.
   *
   * @param phase The ID of the phase to use.
   */
  public void setPhase(String phase) {
    this.phase = phase;
  }

  public void setStreamable(boolean yes) {
    this.options.put("schxslt.compile.streamable", yes);
  }

  public void setMetadata(boolean yes) {
    this.options.put("schxslt.compile.metadata", yes);
  }

  public void setCompact(boolean yes) {
    this.options.put("schxslt.svrl.compact", yes);
  }

  public void setDefaultQueryBinding(String value) {
    this.defaultQueryBinding = value;
  }

  public String getDefaultQueryBinding() {
    return defaultQueryBinding;
  }

  public void configure(ValidatorFactory factory) {

    // set the phase if specified
    if (this.phase != null) {
      factory.setParameter("phase", this.phase);
    }

    // Set any other option
    for (Map.Entry<String, Object> option: this.options.entrySet()) {
      factory.setParameter(option.getKey(), option.getValue());
    }

  }
}
