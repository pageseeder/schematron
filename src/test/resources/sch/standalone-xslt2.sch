<?xml version="1.0"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Standalone schema for testing</sch:title>

  <sch:pattern>
    <sch:title>Elements</sch:title>

    <sch:rule context="/" flag="info" icon="info-circle" >
      <sch:report test="*" diagnostics="fragment" properties="element"
      >Document element <sch:emph><sch:value-of select="name(*)"/></sch:emph>.</sch:report>
    </sch:rule>

  </sch:pattern>

  <sch:diagnostics>
    <sch:diagnostic id="fragment">XXX</sch:diagnostic>
  </sch:diagnostics>

  <sch:properties>
    <sch:property id="element">YYY</sch:property>
  </sch:properties>
</sch:schema>
