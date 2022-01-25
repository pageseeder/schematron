<?xml version="1.0"?>
<!--
  Allows PageSeeder to identify the fragment and show in interface
-->
<sch:properties xmlns:sch="http://purl.oclc.org/dsdl/schematron">

  <sch:property id="fragment-id"><sch:value-of select="ancestor-or-self::fragment/@id"/></sch:property>

</sch:properties>
