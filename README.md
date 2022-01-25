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

This library requires an XSLT processor at runtime.

## Testing

If you want to test the code, ensure that you add the following to the ANT classpath:
- Saxon (available in text/lib)
- Either a version of the jar or both the /src and /classes folders
