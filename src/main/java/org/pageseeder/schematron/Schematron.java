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
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

/**
 * An Ant task that allows user to validate a fileset of XML files with a Schematron schema.
 *
 * Functional specifications
 *
 * The schematron ant task should process the file(s) specified by the user either directly or using
 * <ul>
 *   <li>the fileset Ant concept for a better integration with ant.</li>
 *   <li>The ant task must specify the ISO Schematron schema file to use.</li>
 *   <li>Optionally, the user can specify a phase.</li>
 * </ul>
 *
 * All arguments should be properly parsed using Ant methods so that Ant arguments and concept can be specified.
 *
 * The configuration of the XSLT transformer should be inherited from the JAXP properties.
 *
 * @deprecated
 *
 * @author Christophe lauret
 * @author Willy Ekasalim
 * @author Rick Jelliffe
 *
 * @version  11 March 2013
 */
@Deprecated
public final class Schematron {

  /**
   * Specify the query language binding of schematron to use "xslt1" "xslt2" "auto"
   */
  private QueryBinding queryLanguageBinding = QueryBinding.DEFAULT;

  /**
   * Specify the format  (metastylesheet), "svrl" (default), "message" or "terminate"
   */
  private String format ="svrl";

  /**
   * The Schematron schema file.
   */
  private File schema;

  /**
   * The path to the directory where the output file should be stored.
   */
  private String outputDir;

  /**
   * The name of the file where the results will be stored.
   */
  private String outputFilename = "result.xml";

  /**
   * List of files to be Schematron validated.
   */
  private final List<File> files = new ArrayList<File>();

  /**
   * The Schematron validator.
   */
  private Validator validator;

  /**
   * If set to <code>true</code> (default), throws a buildException if the parser yields an error.
   */
  private boolean failOnError = true;

  /**
   * If set to <code>true</code> (default is <code>false</code>), then preprocessing stylesheet will be outputted to file debug.xslt
   */
  private boolean debugMode = false;

  /**
   * A collection of options to pass to the factory.
   */
  private CompileOptions options = new CompileOptions();

  /** name for XSL parameter containing the filename */
  private String archiveNameParameter = null;

  /** name for XSL parameter containing the file directory */
  private String archiveDirParameter = null;

  /** name for XSL parameter containing the filename */
  private String fileNameParameter = null;

  /** name for XSL parameter containing the file directory */
  private String fileDirParameter = null;

  /** Encoding for output */
  private String encoding = null;

  // public methods
  // ----------------------------------------------------------------------------------------------

  /**
   * Specify how parser error are to be handled.
   *
   * Optional, default is <code>true</code>.
   *
   * If set to <code>true</code> (default), throws a buildException if the parser yields an error.
   *
   * @param fail if set to <code>false</code> do not fail on error
   */
  public void setFailOnError(boolean fail) {
    this.failOnError = fail;
  }

  /**
   * Specify which binding to use "xslt1", "xslt2"
   * @param binding
   */
  public void setQueryLanguageBinding(String binding) {
    this.queryLanguageBinding = QueryBinding.valueOf(binding.toUpperCase());
  }

  /**
   * Specify whether preprocessor stylesheet are to be print into file for debugging.
   *
   * <p>Optional, default is <code>false</code>.
   *
   * <p>If set to <code>true</code> output preprocessing stylesheet into file debug.xslt
   *
   * @param debug if set to <code>false</code> do not output preprocessing stylesheet to file.
   */
  public void setDebugMode(boolean debug) {
    this.debugMode = debug;
  }

  /**
   * Specify the file to be checked; optional.
   *
   * @param file The file to check
   */
  public void setSchema(File file) {
    this.schema = file;
  }

  /**
   * Specify the file to be checked; optional.
   *
   * @param file The file to check
   */
  public void setFile(File file) {
    this.files.clear();
    this.files.add(file);
  }

  /**
   * Specify the path to the directory where the output file should be stored.
   *
   * Optional, default is the current directory.
   *
   * @param outputDir the path to the directory where the output file should be stored.
   */
  public void setOutputDir(String outputDir) {
    this.outputDir = outputDir;
  }

  /**
   * Specify the name of the file where the results will be stored.
   *
   * Optional, default is <code>result.xml</code>.
   *
   * @param outputFilename the name of the file where the results will be stored.
   */
  public void setOutputFilename(String outputFilename) {
    this.outputFilename = outputFilename;
  }

  /**
   * Specifies a semicolon separated list of catalog files
   */
  public void setCatalog(String data) {
    System.setProperty("xml.catalog.files", data);
  }

  /**
   * Pass the filename of the current processed file as a xsl parameter
   * to the transformation. This value sets the name of that xsl parameter.
   *
   * @param value    fileNameParameter name of the xsl parameter retrieving the
   *                          current file name
   */
  public void setFileNameParameter(String value) {
      this.fileNameParameter = value;
  }

  /**
   * Pass the directory name of the current processed file as a xsl parameter
   * to the transformation. This value sets the name of that xsl parameter.
   *
   * @param value fileDirParameter name of the xsl parameter retrieving the
   *                         current file directory
   */
  public void setFileDirParameter(String value) {
    this.fileDirParameter = value;
  }

  /**
   * Pass the filename of the current processed file as a xsl parameter
   * to the transformation. This value sets the name of that xsl parameter.
   *
   * @param value archiveNameParameter name of the xsl parameter retrieving the
   *                          current file name
   */
  public void setArchiveNameParameter(String value) {
    this.archiveNameParameter = value;
  }

  /**
   * Pass the directory name of the current processed file as a xsl parameter
   * to the transformation. This value sets the name of that xsl parameter.
   *
   * @param value  archiveDirParameter name of the xsl parameter retrieving the
   *                         current file directory
   */
  public void setarchiveDirParameter(String value) {
    this.archiveDirParameter = value;
  }

  /**
   * Set the encoding to be used.
   *
   * This is hardcoded into the output element of the final XSLT, and passed to the
   * metastylesheet using the output-encoding parameter.
   */
  public void setOutputEncoding(String value) {
    this.encoding=value;
  }

  /* ====================================================================================*/

  /**
   * Executes this task.
   *
   * This method will:
   *
   * 1. Produce the Templates instances
   *   a. Locate the schema file in file system
   *   b. Locate the skeleton file in classpath
   *   c. Transform the source schema with the skeleton
   *   d. Parse the output of the transformation as Template instances
   *   e. Store for reuse
   *   f. Optionally, save as file in file system for debugging
   *
   * 2. Process the XML files
   *   a. Locate each source XML file to validate
   *   b. Transform each file with the generated templates
   *   c. Capture the output and messages in the Ant/standard output
   *
   * Note: 'Locate' means that the value and fileset from Ant may need
   * to be parsed/processed so that each file can be found an checked prior to processing.
   */
  public void execute() throws SchematronException {
    // the number of file processed
    int fileProcessed = 0;

    // verify that we have at least one file to validate
    if (this.files.isEmpty()) throw new SchematronException("Specify at least one source - a file or a fileset.");

    // verify that we have at least one schema specified
    if (this.schema == null) throw new SchematronException("Specify at least one schema.");
    else if (!(this.schema.exists() && this.schema.canRead() && this.schema.isFile())) throw new SchematronException("Schema "+this.schema+" cannot be read");

    // initialises the validator
    initValidator();

    // initialises Schematron Report to store the validation result
    SchematronReport report = new SchematronReport();

    // when filesets are specified
    for (File f : this.files) {
      SchematronResult result = doValidate(f);

      //Check if failOnError is set to true, then validation stop if a file is invalid
      if (this.failOnError) {
        if (!result.isValid()) {
          result.printFailedMessage(System.err);
          return;
        }
      }
      report.add(result);
      fileProcessed++;
      Thread.yield();  // Slows down this, but maybe better citizen with other threads
    }
    try {
      File resultFile = new File(this.outputDir, this.outputFilename);
      report.saveAs(resultFile);
    } catch (IOException ex) {
      throw new SchematronException("Unable to write to file: " + ex.getMessage());
    }
    try {
      report.printLog(System.out);
    } catch (Exception ex) {
      throw new SchematronException(ex.getMessage());
    }
    System.out.println(fileProcessed + " file(s) have been successfully validated.");
  }

// Private helpers --------------------------------------------------------------------------------

  /**
   * Initialise the validator.
   *
   * Loads the parser class, and set features if necessary
   */
  private void initValidator() throws SchematronException {
    try {
      ValidatorFactory factory = new ValidatorFactory();
      factory.setDebugMode(this.debugMode);
      factory.setErrorListener(new Listener());
      this.options.configure(factory);

      System.out.println("Generating validator for schema " + this.schema + "... ");
      this.validator = factory.newValidator(new StreamSource(this.schema));
      System.out.println("Validator ready to process");
//
//    } catch (TransformerException ex) {
//      System.out.println(ex.getMessage());
//      SourceLocator locator = ex.getLocator();
//      if (locator != null)
//        System.out.println("SystemID: "+locator.getSystemId()+"; Line#: "+locator.getLineNumber()+"; Column#: "+locator.getColumnNumber());
//      throw new SchematronException("The validator could not be initialised", ex);
//    // exception thrown when try to print preprocessing stylesheet to output file
//    } catch (IOException io) {
//      System.out.println("Error when outputting preprocessor stylesheet: " + io.getMessage());
    } catch (Exception e) {
        System.out.println("Error with initializing validator: " + e.getMessage());
        e.printStackTrace();
    }

  }

  /**
   * Performs the validation for an individual file.
   *
   * @param afile  The file to validate.
   */
  private SchematronResult doValidate(File afile) throws SchematronException {
    System.out.println("Validating " + afile.getName() + "... ");
    StreamSource xml = new StreamSource(afile);
    OutputOptions options = OutputOptions.defaults().encoding(this.encoding);
    SchematronResult result = this.validator.validate(xml, options);

    // Usual return
    return result;
  }

  /**
   * The Listener class which catches xsl:messages during the transformation of the Schematron schema
   */
  private class Listener implements ErrorListener {

    @Override
    public void warning(TransformerException ex) {
      System.err.println("[warning] "+ex.getMessage());
    }

    @Override
    public void error(TransformerException ex) throws TransformerException {
      throw ex;
    }

    @Override
    public void fatalError(TransformerException ex) throws TransformerException {
      throw ex;
    }
  }

}
