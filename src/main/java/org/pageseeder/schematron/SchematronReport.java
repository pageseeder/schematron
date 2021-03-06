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

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A report aggregating a collection of Schematron validation results.
 *
 * @author Christophe lauret
 *
 * @version 11 March 2013
 */
public final class SchematronReport {

  /**
   * Maps XML files to results.
   */
  private final Map<String, SchematronResult> results = new HashMap<String, SchematronResult>();

  /**
   * Constructor of Schematron Report
   */
  public SchematronReport() {
  }

  /**
   * Add a new SchematronResult object to the collection
   *
   * @param result object to be added.
   */
  public void add(SchematronResult result) {
    this.results.put(result.getSystemID(), result);
  }

  /**
   * Returns the Schematron result instance corresponding to the given system ID.
   *
   * @param systemID The system ID of the requested schematron result.
   *
   * @return a SchematronResult object based on source filename/system ID given.
   */
  public SchematronResult get(String systemID) {
    return this.results.get(systemID);
  }

  /**
   * Saves this report as XML in the format:
   *
   * <pre>{@code
   * <fileset date="[yyyy-mm-dd]">
   *   <file name="xxxxx"> SRVL embedded here </file>
   *   <file name="xxxxx"> SRVL embedded here </file>
   * </fileset>
   * }</pre>
   *
   * @param file The file where the Schematron results should be saved.
   *
   * @throws IOException Should any I/O error occur.
   */
  public void saveAs(File file) throws IOException {
    // date of execution
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String date = sdf.format(new Date());

    // create the output stream
    PrintStream pout = new PrintStream(file, "UTF-8");

    // populate the file output stream with the svrl content
    pout.println("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
    pout.println("<fileset date=\"" + date + "\">");
    for (Entry<String, SchematronResult> entries : this.results.entrySet()) {
      String sourcefile = entries.getKey();
      String svrlfile = entries.getValue().getSVRLAsString();
      pout.println("<file name=\"" + removePath(sourcefile) + "\">");
      pout.println(svrlfile);
      pout.println("</file>");
    }
    pout.println("</fileset>");

    // close the stream
    pout.close();
  }

  /**
   * Print the SVRL result onto the console log.
   *
   * @param task The task which provides the Log to use.
   */
  public void printLog(PrintStream out) {
    for (SchematronResult result : this.results.values()) {
      result.printAllMessage(out);
    }
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
