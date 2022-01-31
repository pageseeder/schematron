<sch:pattern name="prices" xmlns:sch="http://purl.oclc.org/dsdl/schematron">

  <sch:title>Prices</sch:title>

  <sch:rule context="lib:book" flag="info" icon="info-circle" >

    <sch:let name="price-threshold" select="4"/>

    <sch:report test="lib:genre" diagnostics="fragment" properties="fragment-id"
    >Genre: <sch:value-of select="lib:genre"/>.</sch:report>

    <sch:report test="p:price gt $price-threshold">Price greater than <sch:value-of select="$price-threshold"/>.</sch:report>

  </sch:rule>

</sch:pattern>
