<?xml version="1.0"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt3">

  <sch:pattern>

    <sch:rule context="/*">

      <sch:report test="secret"><sch:value-of select="secret" /></sch:report>

    </sch:rule>

  </sch:pattern>

</sch:schema>