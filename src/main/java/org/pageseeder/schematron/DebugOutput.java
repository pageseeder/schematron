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

import java.io.IOException;
import java.io.Writer;

/**
 * Implementations can specify where the compiled stylesheet should be saved.
 *
 * @author Christophe Lauret
 *
 * @version 2.0
 * @since 2.0
 */
@FunctionalInterface
public interface DebugOutput {

  /**
   * Return a writer for the specified schema system Id.
   *
   * @param systemId The system ID of the schema being compiled.
   *
   * @return The corresponding writer or <code>null</code> for no debug.
   *
   * @throws IOException should an IOException occur
   */
  Writer getWriter(String systemId) throws IOException;

  /**
   * Indicate whether the debug output should be indented.
   */
  default boolean indent() {
    return true;
  }
}
