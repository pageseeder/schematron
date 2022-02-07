<?xml version="1.0"?>
<!--
  Allows PageSeeder to identify the fragment and show in interface
-->
<sch:diagnostics xmlns:sch="http://purl.oclc.org/dsdl/schematron">

  <sch:diagnostic id="book">In <sch:value-of select="ancestor-or-self::book/title"/> by <sch:value-of select="ancestor-or-self::book/author"/></sch:diagnostic>

</sch:diagnostics>
