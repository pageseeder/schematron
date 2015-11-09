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
 *
 * ---------- Original copyright notice for this portion of the code ----------
 *
 * Adapted from work by Christophe Lauret and Willy Ekasalim
 *
 * Open Source Initiative OSI - The MIT License:Licensing
 * [OSI Approved License]
 *
 * The MIT License
 *
 * Copyright (c) 2008 Rick Jelliffe, Topologi Pty. Ltd, Allette Systems
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.pageseeder.schematron;

/**
 * Classes of exceptions occurring while preparing or performing Schematron validation.
 *
 * <p>Note that failed Schematron assertions are not considered to be errors.
 *
 * @author Christophe Lauret
 * @version 8 August 2012
 */
public final class SchematronException extends Exception {

  /** As per requirements. */
  private static final long serialVersionUID = 2321079962121852344L;

  /**
   * Creates a new Schematron exception.
   */
  public SchematronException() {
  }

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
