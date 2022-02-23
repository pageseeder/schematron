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

import javax.xml.transform.TransformerException;

/**
 * Classes of exceptions occurring while preparing or performing Schematron validation.
 *
 * <p>Note that failed Schematron assertions are not considered to be errors.
 *
 * @author Christophe Lauret
 * @version 2.0
 * @since 1.0
 */
public class SchematronException extends Exception {

  /** As per requirements. */
  private static final long serialVersionUID = 7541115878853544269L;

  /**
   * Creates a new Schematron exception.
   *
   * @param message A message explaining the cause of the error.
   */
  public SchematronException(String message) {
    super(message);
  }

  /**
   * Creates a new Schematron exception.
   *
   * @param cause   The original error causing the exception.
   */
  public SchematronException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new Schematron exception.
   *
   * @param message A message explaining the cause of the error.
   * @param cause   The original error causing the exception.
   */
  public SchematronException(String message, Throwable cause) {
    super(message, cause);
  }

}
