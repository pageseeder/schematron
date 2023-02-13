<?xml version="1.0"?>
<!DOCTYPE sch:schema [
    <!ELEMENT sch:schema ANY >
    <!ENTITY xxe SYSTEM "file:///etc/passwd" >
]><sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:pattern>
    <sch:rule context="/">
      <sch:assert test="true()">&xxe;</sch:assert>
    </sch:rule>
  </sch:pattern>

</sch:schema>