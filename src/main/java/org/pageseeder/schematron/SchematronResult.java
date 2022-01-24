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

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Stores the results of the schematron validation.
 *
 * @author Christophe lauret
 * @author Willy Ekasalim
 *
 * @version 14 February 2007
 */
public final class SchematronResult {

  /** Store the source file name or systemID */
  private String systemID;

  /** Store the SVRL content */
  private String svrl;

  /**
   * An ArrayList to store (String) message of failed assertion found.
   */
  private final List<String> failedAssertions = new ArrayList<String>();

  /**
   * An ArrayList to store (String) message of successful report found.
   */
  private final List<String> successfulReports = new ArrayList<String>();

  /**
   * Constructor of SchematronResult that accept the source file name (or systemID)
   *
   * @param systemID The system ID of the XML for which this result instance is built.
   */
  public SchematronResult(String systemID) {
    this.systemID = systemID;
  }

  /**
   * @return this object's systemID.
   */
  public String getSystemID() {
    return this.systemID;
  }

  /**
   * @return <code>true</code> if there's no failed assertion;
   *         <code>false</code> if there is failed assertion
   */
  public boolean isValid() {
    return this.failedAssertions.size() == 0;
  }

  /**
   * Setter for SVRL content/file, also parse the SVRL content to extract the failed assertion and the
   * successful report.
   *
   * @param svrl The corresponding generated by SVRL.
   *
   * @throws SchematronException Should an error occur whilst parsing.
   */
  public void setSVRL(String svrl) throws SchematronException {
    this.svrl = removeXMLheader(svrl);
    try {
      parseSVRL();
    } catch (Exception ex) {
      // if there is a parse error then it is probably a different metastylesheet
      this.svrl = svrl;
      // was throw new BuildException("Error on parsing SVRL content: " + ex.getMessage());
    }
  }

  /**
   * @return SVRL content as String representation.
   */
  public String getSVRLAsString() {
    return this.svrl;
  }

  /**
   * Print the failed assertion messages only on the specified stream.
   *
   * <p>This method is used only when "failOnError" is set to true.
   *
   * @param out SchematronTask object for message logging
   */
  public void printFailedMessage(PrintStream out) {
    if (this.failedAssertions.size() > 0) {
      out.println("Source file: " + removePath(this.systemID));
      for (int i = 0; i < this.failedAssertions.size(); i++) {
        out.println(this.failedAssertions.get(i));
      }
    }
  }

  /**
   * Return the failed assertion message only.
   *
   * <p>This method is used for Pageseeder Schematron Validation error output.
   *
   * @return a concatenation of all failed messages.
   */
  public String getFailedMessage() {
    String erroutput = "";
    if (this.failedAssertions.size() > 0) {
      for (int i = 0; i < this.failedAssertions.size(); i++) {
        erroutput += this.failedAssertions.get(i);
      }
    }
    return erroutput;
  }

  /**
   * Print both failed assertion and successful report message to the console.
   */
  public void printAllMessage(PrintStream out) {
    if (this.failedAssertions.size() > 0 || this.successfulReports.size() > 0) {

      if (this.systemID != null)
        out.println(("Source file: " + removePath(this.systemID)));

      // FIXME: TRYING TO ACCESS STRING AS A STRING RESULTS IN ERROR

      for (String assertion : this.failedAssertions) {
        out.println(assertion);
      }
      for (String report : this.successfulReports) {
        out.println(report);
      }
    }

  }

  /**
   * @return the list of failed assertions
   */
  public List<String> getFailedAssertions() {
    return this.failedAssertions;
  }

  /**
   * @return the list of successful reports
   */
  public List<String> getSuccessfulReports() {
    return this.successfulReports;
  }

  // private helper
  // ----------------------------------------------------------------------------------------------

  /**
   * Parse the SVRL content to extract any failed or success message.
   *
   * <p>The message will be stored in failedAssertions and successfulReports by SVRL handler.
   */
  private void parseSVRL() throws IOException, SAXException, ParserConfigurationException {
    SVRLHandler handler = new SVRLHandler(this.failedAssertions, this.successfulReports);
    InputSource is = new InputSource(new StringReader(this.svrl));
    is.setEncoding("UTF-16");
    SAXParserFactory.newInstance().newSAXParser().parse(is, handler);
  }

  /**
   * Given an XML content, remove the XML header (first line if starts with <?xml)
   *
   * @param svrl The XML content of the SVRL including the XML declaration.
   *
   * @return the SVRL content without the XML header(first line) */
  private String removeXMLheader(String svrl) {
    int firstLineEnd = svrl.indexOf("\n");
    // Handle Unicode BOM
    if (svrl.startsWith("<?xml ")
     || svrl.startsWith("<?xml ", 1)
     || svrl.startsWith("<?xml ", 2)
     || svrl.startsWith("<?xml ", 3)) return svrl.substring(firstLineEnd + 1);
    else return svrl;
  }

  /**
   * Given full path to the path and return file name only
   *
   * @param filename full path to a file
   * @return name of the file only without any of the path
   */
  private String removePath(String filename) {
    String[] splitted = filename.split("/");
    return splitted[splitted.length - 1];
  }

}
