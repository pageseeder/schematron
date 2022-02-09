<?xml version="1.0"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:let name="gotit" value="'Nope.'"/>

  <sch:pattern>
    <sch:title>Elements</sch:title>

    <sch:rule context="/">
      <sch:report test="true()">Got parameter? <sch:value-of select="$gotit"/></sch:report>
    </sch:rule>

  </sch:pattern>

</sch:schema>
