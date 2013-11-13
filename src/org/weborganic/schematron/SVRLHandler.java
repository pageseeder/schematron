/*
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
package org.weborganic.schematron;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A simple handler which parses the SRVL result and stores
 *
 * TEXT_ELT is not just under FAILED_ASSERT_ELT and SUCCESSFUL_REPORT_ELT.
 * <p> in schematron will also be copied to SVRL as TEXT_ELT
 *
 * @author Xin Chen
 * @author Christophe Lauret
 *
 * @version 11 March 2013
 */
public final class SVRLHandler extends DefaultHandler {

  /**
   * Static name for failed assertions.
   */
  private static final String FAILED_ASSERT_ELT = "svrl:failed-assert";

  /**
   * Static name for simple text
   */
  private static final String TEXT_ELT = "svrl:text";

  /**
   * Static name for successful report.
   */
  private static final String SUCCESSFUL_REPORT_ELT = "svrl:successful-report";

  /**
   * Static name for location attribute.
   */
  private static final String LOCATION_ATT = "location";

  // Class attributes
  // ----------------------------------------------------------------------------------------------

  /**
   * StringBuffer to collect text/character data received from characters() callback
   */
  private StringBuffer chars = new StringBuffer();

  /**
   * StringBuffer for constructing failed assertion/succesfull message
   */
  private StringBuffer message = new StringBuffer();

  /**
   * String to store the element name that are currently being produced
   */
  private String lastElement;

  /**
   * An ArrayList to store (String) message of failed assertion found.
   */
  private List<String> failedAssertions;

  /**
   * An ArrayList to store (String) message of successful reports found.
   */
  private List<String> successfulReports;

  /**
   * State variable to indicate that the current parsed element is
   * either FAILED_ASSERT_ELT or SUCCESSFUL_REPORT_ELT.
   */
  private boolean underAssertorReport = false;

  // Constructor
  // ----------------------------------------------------------------------------------------------

  /**
   * Constructor for SVRLHandler.
   */
  public SVRLHandler() {
  }

  /**
   * Constructor for SVRLHandler that require reference of failedAssertions and successfulReports.
   *
   * @param failedAssertions & successfulReports to store validation message result.
   *
   * @throws NullPointerException if either parameter is <code>null</code>.
   */
  public SVRLHandler(List<String> failedAssertions, List<String> successfulReports) {
    if (failedAssertions == null || successfulReports == null) throw new NullPointerException();
    this.failedAssertions = failedAssertions;
    this.successfulReports = successfulReports;
  }

  // Handler methods
  // ----------------------------------------------------------------------------------------------

  @Override
  public void startElement(String uri, String localName, String rawName, Attributes attributes) {
    // detect svrl:failed-assert and svrl:successful-report element
    if (rawName.equals(FAILED_ASSERT_ELT)) {
      this.message.append("[assert] " + attributes.getValue(LOCATION_ATT));
      this.lastElement = FAILED_ASSERT_ELT;
      this.underAssertorReport = true;
    } else if (rawName.equals(SUCCESSFUL_REPORT_ELT)) {
      this.message.append("[report] " + attributes.getValue(LOCATION_ATT));
      this.lastElement = SUCCESSFUL_REPORT_ELT;
      this.underAssertorReport = true;
    } else if (rawName.equals(TEXT_ELT) && this.underAssertorReport == true) {
      // clean the buffer to start collecting text of svrl:text
      getCharacters();
    }
  }

  @Override
  public void endElement(String namespaceURL, String localName, String rawName) {
    // reach the end of svrl:text and collect the text data
    if (rawName.equals(TEXT_ELT) && this.underAssertorReport == true) {
      this.message.append(" - " + getCharacters());
      //check the last element name to decide where to store the validation message
      if (this.lastElement.equals(FAILED_ASSERT_ELT)) {
        this.failedAssertions.add(getMessage());
      } else {
        this.successfulReports.add(getMessage());
      }
      this.underAssertorReport = false;
    }
    this.lastElement = "";
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    // print svrl:text text node if the lastElement is svrl:text
    this.chars.append (ch, start, length);
  }

  /**
   * Return the collected text data so far and clean the buffer
   * @return collected text data on the buffer
   */
  private String getCharacters() {
    String retstr = this.chars.toString();
    this.chars.setLength(0);
    return retstr;
  }

  /**
   * @return the constructed validation message
   */
  private String getMessage() {
    String retstr = this.message.toString();
    this.message.setLength(0);
    return retstr;
  }

}
