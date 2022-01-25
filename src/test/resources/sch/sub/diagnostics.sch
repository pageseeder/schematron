<?xml version="1.0"?>
<!--
  Allows PageSeeder to identify the fragment and show in interface
-->
<sch:diagnostics xmlns:sch="http://purl.oclc.org/dsdl/schematron">

  <sch:diagnostic id="fragment"><sch:value-of select="ancestor-or-self::fragment/@id"/></sch:diagnostic>

</sch:diagnostics>
