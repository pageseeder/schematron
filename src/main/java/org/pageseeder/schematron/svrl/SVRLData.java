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

import java.io.IOException;
import java.io.Reader;

/**
 * Implementations hold SVRL data.
 *
 * @author Christophe Lauret
 *
 * @version 2.1.1
 * @since 2.1.1
 */
public interface SVRLData {

  /**
   * @return The SVRL data as a String
   */
  String asString();

  /**
   * @return The SVRL data as a byte array
   */
  byte[] asByteArray();

  /**
   * @return A reader to the SVRL data
   */
  Reader getReader() throws IOException;
}