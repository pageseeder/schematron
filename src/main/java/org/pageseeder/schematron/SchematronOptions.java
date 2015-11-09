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
 * @author Christophe Lauret
 * @version 11 March 2013
 */
public class SchematronOptions {

  /**
   * Specifies a Schematron phase to use.
   */
  private String phase;

  /**
   * command-line parameters of the skeleton
   *
    phase           NMTOKEN | "#ALL" (default) Select the phase for validation
    allow-foreign   "true" | "false" (default)   Pass non-Schematron elements to the generated stylesheet
    sch.exslt.imports semi-colon delimited string of filenames for some EXSLT implementations
    message-newline "true" (default) | "false"   Generate an extra newline at the end of messages
    debug           "true" | "false" (default)  Debug mode lets compilation continue despite problems
    attributes "true" | "false"  (Autodetecting) Use only when the schema has no attributes as the context nodes
    only-child-elements "true" | "false" (Autodetecting) Use only when the schema has no comments
    or PI  as the context nodes

 Experimental: USE AT YOUR OWN RISK
    visit-text "true" "false"   Also visist text nodes for context. WARNING: NON_STARDARD.
    select-contents '' | 'key' | '//'   Select different implementation strategies

 Conventions: Meta-stylesheets that override this may use the following parameters
    generate-paths=true|false   generate the @location attribute with XPaths
    diagnose= yes | no    Add the diagnostics to the assertion test in reports
    terminate= yes | no   Terminate on the first failed assertion or successful report
   */
  private String allow_foreign;
  private String sch_exlst_imports;
  private String message_newline;
  private String attributes;
  private String only_child_elements;
  private String visit_text;
  private String select_contents;
  private String generate_paths;
  private String diagnose;
  private String terminate;


  private String langCode;

  /*
   * Parameters from include and abstract pre-processors
   * Schema-id is in iso_abstract_expand and is used for selecting a particular schema
   * from when there are multiple schemas embedded in, say, an NVRL file
   */
  private String schema_id;

  /**
   *
   */
  public SchematronOptions() {
  }

  /**
   * Specifies the phase Schematron should use.
   *
   * <p>If this parameter is set the schematron processor will match this parameter value with
   * the 'id' attribute of a phase element.
   *
   * @param phase The ID of the phase to use.
   */
  public void setPhase(String phase) {
    this.phase = phase;
  }


  // allow-foreign   "true" | "false" (default)   Pass non-Schematron elements to the generated stylesheet
  public void setAllow_foreign( String value) {
    this.allow_foreign = value;
  }

  //sch.exslt.imports semi-colon delimited string of filenames for some EXSLT implementations
  public void setSch_exlst_imports ( String value) {
    this.sch_exlst_imports  = value;
  }

  //message-newline "true" (default) | "false"   Generate an extra newline at the end of messages
  public void setMessage_newline ( String value) {
    this.message_newline  = value;
  }

  //attributes "true" | "false"  (Autodetecting) Use only when the schema has no attributes as the context nodes
  public void setAttributes (String value) {
    this.attributes  = value;
  }

  //only-child-elements "true" | "false" (Autodetecting) Use only when the schema has no comments or PI  as the context nodes
  public void setOnly_child_elements(String value) {
    this.only_child_elements  = value;
  }

  //visit-text "true" "false"   Also visist text nodes for context. WARNING: NON_STARDARD.
  public void setVisit_text(String value) {
    this.visit_text  = value;
  }

  //select-contents '' | 'key' | '//'   Select different implementation strategies
  public void setSelect_contents (String value) {
    this.select_contents = value;
  }

  //generate-paths=true|false   generate the @location attribute with XPaths
  public void setGenerate_paths(String value) {
    this.generate_paths = value;
  }

  //diagnose= yes | no    Add the diagnostics to the assertion test in reports
  public void setDiagnose ( String value) {
    this.diagnose  = value;
  }

  //terminate= yes | no   Terminate on the first failed assertion or successful report
  public void setTerminate ( String value) {
    this.terminate = value;
  }

  //schema-id = NMTOKEN   the id of the Schematron schema to use (when there are multiples)
  public void setSchema_id( String value) {
    this.schema_id = value;
  }

  //langCode = NMTOKEN   the language of compiler messages
  public void setLangCode(String value) {
    this.langCode = value;
  }


  /** Encoding for output */
  private String encoding = null;


  public void configure(ValidatorFactory factory) {

    // set the phase if specified
    if (this.phase != null) {
      factory.setParameter("phase", this.phase);
    }

    // Other command line options available
    if (this.allow_foreign !=null) {
      factory.setParameter("allow-foreign", this.allow_foreign );
    }

    if (this.sch_exlst_imports !=null) {
      factory.setParameter("sch.exslt.imports", this.sch_exlst_imports );
    }
    if (this.message_newline !=null) {
      factory.setParameter("message-newline", this.message_newline  );
    }
    if (this.attributes !=null) {
      factory.setParameter("attributes", this.attributes  );
    }
    if (this.only_child_elements !=null) {
      factory.setParameter("only-child-elements", this.only_child_elements   );
    }
    if (this.visit_text !=null) {
      factory.setParameter("visit-text", this.visit_text  );
    }
    if (this.select_contents !=null) {
      factory.setParameter("select-contents", this.select_contents    );
    }
    if (this.generate_paths !=null) {
      factory.setParameter("generate-paths", this.generate_paths   );
    }
    if (this.diagnose !=null) {
      factory.setParameter("diagnose", this.diagnose);
    }
    if (this.terminate !=null) {
      factory.setParameter("terminate", this.terminate);
    }
    if (this.schema_id !=null) {
      factory.setParameter("schema-id", this.schema_id);
    }
    if (this.encoding !=null) {
      factory.setParameter("output-encoding", this.encoding);
    }
    if (this.langCode !=null) {
      factory.setParameter("langCode", this.langCode);
    }
  }
}
