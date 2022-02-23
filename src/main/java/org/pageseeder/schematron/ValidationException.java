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
 * An error that occurred during validation.
 *
 * @author Christophe Lauret
 * @version 2.0
 * @since 1.0
 */
public final class ValidationException extends SchematronException {

  private final String failingExpression;

  private final String errorCode;

  private final String matchPattern;

  public ValidationException(TransformerException cause) {
    super("Unable to validate: "+cause.getMessage(), cause);
    this.failingExpression = loadFailingExpression(cause);
    this.errorCode = loadErrorCode(cause);
    this.matchPattern = loadMatchPattern(cause);
  }

  @Override
  public String getMessage() {
    StringBuilder out = new StringBuilder();
    if (this.errorCode != null) {
      out.append('[').append(this.errorCode).append("] ");
    }
    out.append(super.getMessage());
    if (this.failingExpression != null) {
      out.append(" (expression=").append(this.failingExpression).append(")");
    }
    if (this.matchPattern != null) {
      out.append(" (matchPattern=").append(this.matchPattern).append(")");
    }
    return out.toString();
  }

  public String getErrorCode() {
    return this.errorCode;
  }

  public String getFailingExpression() {
    return this.failingExpression;
  }

  public String getMatchPattern() {
    return this.matchPattern;
  }

  private static String loadMatchPattern(TransformerException cause) {
    try {
      Object locator = cause.getClass().getMethod("getLocator").invoke(cause);
      if (locator != null) {
        Object matchPattern = locator.getClass().getMethod("getMatchPattern").invoke(locator);
        if (matchPattern != null) return matchPattern.toString();
      }
    } catch (Exception ex) {
      // Ignore
    }
    return null;
  }

  private static String loadFailingExpression(TransformerException cause) {
    try {
      Object expression = cause.getClass().getMethod("getFailingExpression").invoke(cause);
      return expression != null ? expression.toString() : null;
    } catch (Exception ex) {
      return null;
    }
  }

  private static String loadErrorCode(TransformerException cause) {
    try {
      Object errorCode = cause.getClass().getMethod("getErrorCodeLocalPart").invoke(cause);
      return errorCode != null ? errorCode.toString() : null;
    } catch (Exception ex) {
      return null;
    }
  }
}
