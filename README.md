[![Maven Central](https://img.shields.io/maven-central/v/org.pageseeder.schematron/pso-schematron.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.pageseeder.schematron%22%20AND%20a:%22pso-schematron%22)

# Schematron Validator

## About this library

This library provides a simple Schematron validator in Java.

This project was initially forked from Google Code <https://code.google.com/p/schematron/> licenced 
under MIT and developed by Rick Jelliffe and others.

Most of the API has been refactored to provide a simple fluent-style Java API.
The core classes are designed to create immutable objects to help with thread-safety.

## SchXslt

This library includes the XSLT-based Schematron processor from SchXslt
  https://github.com/schxslt/schxslt

SchXslt is copyright (c) 2018–2022 by David Maus <dmaus@dmaus.name> and 
released under the terms of the MIT license.

## Dependencies

This library requires an XSLT 2.0 or 3.0 processor at runtime such as Saxon.

## Usage

### Validating

Basic example, using defaults settings returned by
`CompileOptions.defaults()` and `OutputOptions.defaults()`

```java
  // Create a validator factory
  ValidatorFactory factory = new ValidatorFactory();

  // Create a validator for the schema
  File schema = new File("schema.sch");
  Validator validator = factory.newValidator(schema);

  // Validate your source document
  File sample = new File("source.xml");
  SchematronResult result = validator.validate(sample);
```

### Phases

Validators are specific to a phase, use the factory to generate a validator
with a different phase:
```java
  Validator validator = factory.newValidator(sample, "test");
```

### Parameters

To send parameters to your schema, ensure that your schema defines global parameters:
```xml
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron">
  <sch:let name="max" value="100" />
  ...
</sch:schematron>
```

Use a `Map<String, Object>` when validating:
```java
  Map<String,Object> parameters = Collection.singletonMap("max", 200);
  SchematronResult result = validator.validate(sample, parameters);
```

### SRVL

The `SchematronResult` object simply holds the SVRL output, whether the document
is valid, and how many failed assertions or successful reports in includes.

To simply print the SVRL
```java
  String svrl = result.getSVRLAsString();
```

To get the SVRL as an object model:
```java
  SchematronOutput output = results.toSchematronOutput();
```

### Validator instances

A `Validator` is thread-safe. Internally, it only holds a copy of the `Templates` 
used for validation and the configuration. A new `Transformer` is created for each
validation.

To speed things up, you can reuse the `Transformer` by creating a `Validator.Instance`.
Instances are not thread-safe but when used serially, for example when validating
files in a folder or a collection of files, they are significantly faster:

```java
  // Create a reusable instance wrapping a transformer 
  Validator.Instance instance = validator.newInstance();

  // Reuse the same instance, for a collection of files
  for (File file : files) {
    instance.validate(file);
  }
```

### Debugging

To help with debugging, you can set up the `ValidatorFactory` to save a copy of
the generated XSLT stylesheet used for validation. This can be useful to interpret
errors thrown during compilation or understand who the stylesheet is generated.

You can use the default debug option which save files in the current directory:
```java
  // Saves 
  ValidatorFactory factory = new ValidatorFactory().enableDebug();
```

Or specify your own:
```java
  // Debug method takes a DebugOutput object
  ValidatorFactory factory = new ValidatorFactory()
      .debug((systemId) -> {
          File debug = Files.createTempFile("schematron-", ".xsl").toFile();
          System.err.println("debug file: "+debug.getAbsolutePath());
          return new FileWriter(debug);
      });
```

## CLI

You need to include an XSLT 2.0 (or later) processor such as Saxon in your
classpath to use Schematron:

```shell
java -cp pso-schematron-2.0.0.jar:Saxon-HE-10.6.jar \
      org.pageseeder.schematron.Main \
      -i example/source.xml \
      -s example/schema.sch
```

Command-line options:

```
-i or --input [path]        Path to XML file to validate (required)
-s or --schematron [path]   Path to Schematron file to use (required)
-o or --output [path]       Path to output file
-d or --detail              Flag to include diagnotics and properties in text output
-v or --svrl                Flag to return the results as SVRL instead of text
-m or --metadata            Flag to include the metadata in SVRL
-p or --prefix-in-location  Flag to use prefix in locations
-c or --compact             Flag to only return asserts and reports in SVRL
-t or --indent              Flag to indent the SVRL output
```

NB. Using the `-jar` option with `java` takes precedence over the classpath
`-cp` or `-classpath` and **does not work**.

## Compile options

Compile options must be supplied to the Schematron compiler and affect the generated validator.

Default options are:

| Compile option        | Value    |
|-----------------------|----------|
| `defaultQueryBinding` | `"xslt"` |
| `metadata`            | `false`  |
| `streamable`          | `false`  |
| `compact`             | `false`  |

You can specify custom compiler options when configuring the factory:

```java
  CompileOptions options = CompileOptions.defaults().compact(true);
  ValidatorFactory factory = new ValidatorFactory();
  factory.setOptions(options)
```

Options are immutable and therefore thread-safe.

### DefaultQueryBinding

As specified by ISO Schematron, this library assumes that the default query binding is `xslt`
when it is not specified in your schema. You can override this to be `xslt2`.

Default value: `xslt`

### Metadata

This option tell Schematron to include the `<sch:metadata>` element in the SVRL output.
It is used to set the `schxslt.compile.metadata` XSLT parameter when compiling with SchXslt.

Default: `false`

### Streamable

It is used to set the `schxslt.compile.streamable` XSLT parameter when compiling with SchXslt.

Default: `false`

### Compact

It is used to set the `schxslt.svrl.compact` XSLT parameter when compiling with SchXslt.

## Output options

Output options must be supplied to the Schematron validator and affect the generated SVRL output.

Default options are:

| Compile option        | Value     |
|-----------------------|-----------|
| `encoding`            | `"utf-8"` |
| `indent`              | `false`   |
| `omitXmlDeclaration`  | `false`   |
| `usePrefixInLocation` | `false`   |

You can specify custom output options when validating:

```java
  OutputOptions options = OutputOptions.defaults().indent(true);
  validator.validate(source, options);
```

Options are immutable and therefore thread-safe.

### Encoding

To specify character encoding of the SVRL output

Default: `utf-8`

### Indent

To indent the SVRL output.

Default: `false`

### Omit XML declaration

To omit the XML declaration from the output.

Default: `true`

### Use prefix in location

By default, SchXSlt generates the location using the namespace URI.
YOu can use this option to use the namespace prefix instead.

Default: `false`

## Backward compatibility

Version 2.0 uses different defaults to version 1.0.

For backward-compatibility with the previous version of this library, 
the defaults can be overriden to use behave like the previous version. 

To run in compatibility mode, set the system property `org.pageseeder.schematron.compatibility` to `"1.0"` 
with either
```java
  System.setProperty("org.pageseeder.schematron.compatibility", "1.0");
```
Or launching it with 
```shell
  java -Dorg.pageseeder.schematron.compatibility=1.0
```

Running in compatiblity mode, only affects the defaults:
```java
  CompileOptions.defaults();
  OutputOptions.defaults();
```

| Compile option        | Value    |
|-----------------------|----------|
| `defaultQueryBinding` | `"xslt"` |
| `metadata`            | `false`  |
| `streamable`          | `false`  |
| `compact`             | `false`  |

