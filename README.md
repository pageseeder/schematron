[![Maven Central](https://img.shields.io/maven-central/v/org.pageseeder.schematron/pso-schematron.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.pageseeder.schematron%22%20AND%20a:%22pso-schematron%22)

# Schematron Validator

## About this library

This library provides a simple Schematron validator in Java.

This project was initially forked from Google Code <https://code.google.com/p/schematron/> and 
retains large portions of the code licenced under MIT and developed by Rick Jelliffe and others.

We simply wanted a different and simpler Java API.

## Dependencies

This library requires an XSLT processor at runtime.

## Testing

If you want to test the code, ensure that you add the following to the ANT classpath:
- Saxon (available in text/lib)
- Either a version of the jar or both the /src and /classes folders
