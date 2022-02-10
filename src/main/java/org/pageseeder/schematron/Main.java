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

import org.pageseeder.schematron.svrl.AssertOrReport;
import org.pageseeder.schematron.svrl.SVRLParser;
import org.pageseeder.schematron.svrl.SchematronOutput;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Main class to invoke schematrn ono the command-line
 */
public final class Main {

  private static final List<Option> AVAILABLE_OPTIONS = Arrays.asList(
      new Option('i', "input", true, "Path to XML file to validate (required)"),
      new Option('s', "schematron", true, "Path to Schematron file to use (required)"),
      new Option('o', "output", true, "Path to output file"),
      new Option('d', "detail", false, "Flag to include diagnotics and properties in text output"),
      new Option('v', "svrl", false, "Flag to return the results as SVRL instead of text"),
      new Option('m', "metadata", false, "Flag to include the metadata in SVRL"),
      new Option('p', "prefix-in-location", false, "Flag to use prefix in locations"),
      new Option('c', "compact", false, "Flag to only return asserts and reports in SVRL"),
      new Option('t', "indent", false, "Flag to indent the SVRL output")
  );

  private boolean details = false;

  private boolean svrl = false;

  private boolean metadata = false;

  private boolean compact = false;

  private boolean prefixInLocation = false;

  private boolean indent = false;

  private File input;

  private File schema;

  private File output;

  private Main(){
  }

  private boolean checkReady() {
    if (this.input == null) {
      System.err.println("No input specified, use -i option");
      return false;
    }
    if (!input.isFile()) {
      System.err.println("Input must be an existing file");
      return false;
    }
    if (this.schema == null) {
      System.err.println("No schema specified, use -s option");
      return false;
    }
    if (!this.schema.isFile()) {
      System.err.println("Schema must be an existing file");
      return false;
    }
    return true;
  }

  public void validate() throws SchematronException, IOException {
    ValidatorFactory factory = new ValidatorFactory();
    CompileOptions compileOptions = CompileOptions.defaults()
        .metadata(this.metadata)
        .compact(this.compact);
    factory.setOptions(compileOptions);
    Validator validator = factory.newValidator(this.schema);
    OutputOptions outputOptions = OutputOptions.defaults()
        .indent(this.indent)
        .usePrefixInLocation(this.prefixInLocation);
    SchematronResult result = validator.options(outputOptions).validate(this.input);

    if (this.output != null) {
      FileOutputStream out = new FileOutputStream(this.output);
      OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
      if (this.svrl) {
        writer.write(result.getSVRLAsString());
      } else {
        SchematronOutput output = SVRLParser.parse(new StringReader(result.getSVRLAsString()));
        for (AssertOrReport assertion : output.getAllAssertsOrReports()) {
          writer.write(assertion.toMessageString());
        }
      }
      writer.flush();
      writer.close();
    } else {
      if (this.svrl) {
        System.out.println(result.getSVRLAsString());
      } else {
        SchematronOutput output = SVRLParser.parse(new StringReader(result.getSVRLAsString()));
        for (AssertOrReport assertion : output.getAllAssertsOrReports()) {
          System.out.println(assertion.toMessageString(this.details));
        }
      }
    }
  }

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      printUsage();
      return;
    }
    ArgumentList argumentList = parse(args);
    Main main = new Main();
    main.details = argumentList.hasOption('d');
    main.svrl = argumentList.hasOption('v');
    main.compact = argumentList.hasOption('c');
    main.metadata = argumentList.hasOption('m');
    main.prefixInLocation = argumentList.hasOption('p');
    main.indent = argumentList.hasOption('t');
    if (argumentList.hasOption('i')) {
      main.input = new File(argumentList.getValue('i'));
    }
    if (argumentList.hasOption('o')) {
      main.output = new File(argumentList.getValue('o'));
    }
    if (argumentList.hasOption('s')) {
      main.schema = new File(argumentList.getValue('s'));
    }

    // Check
    if (!main.checkReady()) {
      printUsage();
      return;
    }

    // Validate
    try {
      main.validate();
    } catch (SchematronException ex) {
      System.err.println(ex.getMessage());
      if (ex.getCause() != null) {
        System.err.println(ex.getCause().getMessage());
      }
    }

  }

  /**
   * Prints usage on the console.
   */
  public static void printUsage() {
    System.err.println("Schematron");
    System.err.println("java -cp pso-schematron.jar:Saxon.jar org.pageseeder.schematron.Main -i [filename] -s [filename]");
    System.err.println("usage:");
    for (Option option : AVAILABLE_OPTIONS) {
      System.err.print(" -" + option.letter + " or --" + padded(option.word, option.arg, 20));
      System.err.println(option.description);
    }
  }

  private static String padded(String word, boolean hasValue, int max) {
    StringBuilder padding = new StringBuilder();
    padding.append(word);
    if (hasValue) {
      padding.append(" [path]");
      for (int i=0; i < max - word.length() - 7; i++) padding.append(' ');
    } else {
      for (int i=0; i < max - word.length(); i++) padding.append(' ');
    }
    return padding.toString();
  }

  private static ArgumentList parse(String[] args) {
    ArgumentList arguments = new ArgumentList();
    List<String> options = Arrays.asList(args);
    for (Iterator<String> i = options.iterator(); i.hasNext();) {
      String arg = i.next();
      for (Option option : AVAILABLE_OPTIONS) {
        if (option.match(arg)) {
          if (option.arg) {
            arguments.add(option, i.hasNext() ? i.next() : "");
          } else {
            arguments.add(option, "");
          }
        }
      }
    }
    return arguments;
  }

  /**
   * Option available for command-line arguments.
   */
  private final static class Option {
    private final char letter;
    private final String word;
    private final boolean arg;
    private final String description;
    Option(char letter, String word, boolean arg, String description) {
      this.letter = letter;
      this.word = word;
      this.arg = arg;
      this.description = description;
    }

    boolean match(String arg) {
      return arg.equals("--"+this.word) || arg.equals("-"+this.letter);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Option option = (Option) o;
      return letter == option.letter;
    }

    @Override
    public int hashCode() {
      return letter;
    }
  }

  /**
   * Convenience class to check the command-line arguments.
   */
  private final static class ArgumentList {

    List<Argument> arguments = new ArrayList<>();

    Argument get(char c) {
      for (Argument argument : this.arguments) {
        if (argument.option.letter == c) return argument;
      }
      return null;
    }

    boolean hasOption(char c) {
      return get(c) != null;
    }

    String getValue(char c) {
      for (Argument argument : this.arguments) {
        if (argument.option.letter == c) return argument.value;
      }
      return "";
    }

    void add(Option option, String value) {
      this.arguments.add(new Argument(option, value));
    }

  }

  /**
   * Command-line argument with optional value
   */
  private final static class Argument {
    private final Option option;
    private final String value;

    public Argument(Option option, String value) {
      this.option = option;
      this.value = value;
    }
  }

}
