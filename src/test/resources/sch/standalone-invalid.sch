<?xml version="1.0"?>
<sch:schematron xmlns:sch="http://purl.oclc.org/dsdl/schematron">

  <!-- Deliberately invalid element below -->
  <sch:main-title>Standalone schema for testing</sch:main-title>

  <!-- Deliberately invalid element below -->
  <sch:something>
    <sch:title>Elements</sch:title>

    <sch:rule context="/">

      <sch:assert test="*">Document element</sch:assert>

    </sch:rule>

  </sch:something>

</sch:schematron>