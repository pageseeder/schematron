<?xml version="1.0"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:ns prefix="lib" uri="https://example.org/library"/>
  <sch:ns prefix="p" uri="https://example.org/pricing"/>

  <sch:title>Testing Namespaces</sch:title>

  <sch:pattern name="prices" xmlns:sch="http://purl.oclc.org/dsdl/schematron">

    <sch:title>Prices</sch:title>

    <sch:rule context="lib:book" flag="info" icon="info-circle" >

      <sch:let name="price-threshold" value="4"/>
      <sch:let name="min-price" value="2"/>
      <sch:let name="max-price" value="10"/>

      <sch:report test="p:price &gt; $price-threshold" diagnostics="book"
      >Price of $<sch:value-of select="p:price"/> greater than $<sch:value-of select="$price-threshold"/>.</sch:report>

      <sch:assert test="p:price &gt; $min-price" diagnostics="book"
      >Price of $<sch:value-of select="p:price"/> lower than minimum ($<sch:value-of select="$min-price"/>).</sch:assert>

      <sch:assert test="p:price &lt; $max-price" diagnostics="book"
      >Price of $<sch:value-of select="p:price"/> higher than maximum ($<sch:value-of select="$max-price"/>).</sch:assert>

    </sch:rule>

  </sch:pattern>

  <sch:diagnostics>
    <sch:diagnostic id="book">In "<sch:value-of select="ancestor-or-self::lib:book/lib:title"/>" by <sch:emph><sch:value-of select="ancestor-or-self::lib:book/lib:author"/></sch:emph>, </sch:diagnostic>
  </sch:diagnostics>

  <sch:properties>
    <sch:property id="book-id"><sch:value-of select="ancestor-or-self::lib:book/@id"/></sch:property>
  </sch:properties>

</sch:schema>