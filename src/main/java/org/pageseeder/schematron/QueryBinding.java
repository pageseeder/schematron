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
 * Enum for supported query binding attributes
 *
 * @author Christophe Lauret
 * @version 2.0
 * @since 2.0
 */
public enum QueryBinding {

  /**
   * Default query binding (when unspecified in Schema)
   */
  DEFAULT("", "1.0"),

  /**
   * "xslt" query binding usnig XSLT 1.0 templates
   */
  XSLT1("xslt", "1.0"),

  /**
   * "xslt2" query binding usnig XSLT 2.0 version
   */
  XSLT2("xslt2", "2.0"),

  /**
   * "xslt3" query binding usnig XSLT 2.0 version
   */
  XSLT3("xslt3", "2.0");

  private final String _value;
  private final String _version;

  QueryBinding(String value, String version) {
    this._value = value;
    this._version = version;
  }

  public String version() {
    return this._version;
  }

  public static QueryBinding forValue(String value) throws SchematronException {
    for (QueryBinding binding : values()) {
      if (binding._value.equals(value)) return binding;
    }
    throw new SchematronException("Unsupported query binding attribute value: "+value);
  }
}
