<?xml version="1.0"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt">

  <sch:title>Standalone schema for testing</sch:title>

  <sch:pattern>
    <sch:title>Elements</sch:title>

    <sch:rule context="/">

      <sch:report test="*">Document element <sch:name /></sch:report>

    </sch:rule>

  </sch:pattern>

</sch:schema>