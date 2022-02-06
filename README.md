[![Maven Central](https://img.shields.io/maven-central/v/org.pageseeder.schematron/pso-schematron.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.pageseeder.schematron%22%20AND%20a:%22pso-schematron%22)

# Schematron Validator

## About this library

This library provides a simple Schematron validator in Java.

This project was initially forked from Google Code <https://code.google.com/p/schematron/> licenced 
under MIT and developed by Rick Jelliffe and others.

Most of the API has been refactored to provide a simple Java API.

## SchXslt

This library includes the XSLT-based Schematron processor from SchXslt
  https://github.com/schxslt/schxslt

SchXslt is copyright (c) 2018–2021 by David Maus <dmaus@dmaus.name> and 
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

To specify a different phase:
```java
  SchematronResult result = validator.validate(sample, "test");
```

### SRVL

The `SchematronResult` object simply holds the SVRL output, whether the 
document is valid, and how many failed assertions or successful reports in
includes.

To simply print the SVRL
```java
  String svrl = result.getSVRLAsString();
```

To get the SVRL as an object model:
```java
  SchematronOutput output = results.getSchematronOutput()
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

NB. Using the `-jar` option with `java` takes precedence over the classpath
`-cp` or `-classpath` and does not work.

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
```

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

