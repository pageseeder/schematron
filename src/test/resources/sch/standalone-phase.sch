<?xml version="1.0"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron"
            queryBinding="xslt2" defaultPhase="authoring">

  <sch:title>Standalone schema for testing</sch:title>

  <sch:phase id="authoring">
    <sch:active pattern="doc-element"/>
  </sch:phase>

  <sch:phase id="quality">
    <sch:active pattern="is-catalog"/>
  </sch:phase>

  <sch:pattern id="doc-element">
    <sch:rule context="/">
      <sch:report test="*">Document element is <sch:value-of select="name(*)"/>.</sch:report>
    </sch:rule>
  </sch:pattern>

  <sch:pattern id="is-catalog">
    <sch:rule context="/">
      <sch:assert test="catalog">Document element must be `catalog`.</sch:assert>
    </sch:rule>
  </sch:pattern>

</sch:schema>
