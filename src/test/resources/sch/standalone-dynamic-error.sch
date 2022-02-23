<?xml version="1.0"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:pattern>
    <sch:let name="names" value="//website/name/text()" as="xs:string*"/>

    <sch:rule context="/*">
      <sch:report test="$names">Document element <sch:name /></sch:report>
    </sch:rule>

  </sch:pattern>

</sch:schema>
