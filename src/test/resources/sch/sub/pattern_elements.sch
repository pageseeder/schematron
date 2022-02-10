<sch:pattern name="elements" xmlns:sch="http://purl.oclc.org/dsdl/schematron">

  <sch:title>Elements</sch:title>

  <sch:rule context="/" flag="info" icon="info-circle" >
    <sch:report test="*" diagnostics="fragment" properties="book-id"
    >Document element <sch:emph><sch:value-of select="name(*)"/></sch:emph>.</sch:report>
  </sch:rule>

</sch:pattern>
