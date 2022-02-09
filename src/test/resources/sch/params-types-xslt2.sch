<?xml version="1.0"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:let name="name" value="''"/>
  <sch:let name="age" value="0"/>
  <sch:let name="dob" value="''"/>
  <sch:let name="id" value="0"/>
  <sch:let name="greeting" value="()"/>

  <sch:pattern>
    <sch:title>Elements</sch:title>

    <sch:rule context="/" >
      <sch:report test="true()" properties="name age dob id lang greeting">Testing parameter types</sch:report>
    </sch:rule>

  </sch:pattern>

  <sch:properties>
    <sch:property id="name"><sch:value-of select="$name"/></sch:property>
    <sch:property id="age"><sch:value-of select="$age"/></sch:property>
    <sch:property id="dob"><sch:value-of select="$dob"/></sch:property>
    <sch:property id="id"><sch:value-of select="$id"/></sch:property>
    <sch:property id="lang"><sch:value-of select="$greeting/@lang"/></sch:property>
    <sch:property id="greeting"><sch:value-of select="$greeting"/></sch:property>
  </sch:properties>

</sch:schema>
