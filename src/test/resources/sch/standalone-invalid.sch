<?xml version="1.0"?>
<sch:schematron xmlns:sch="http://purl.oclc.org/dsdl/schematron">

  <sch:main-title>Standlone schema for testing</sch:main-title>

  <sch:something>
    <sch:title>Elements</sch:title>

    <sch:rule context="/">

      <sch:assert test="*">Document element</sch:assert>

    </sch:rule>

  </sch:something>

</sch:schematron>