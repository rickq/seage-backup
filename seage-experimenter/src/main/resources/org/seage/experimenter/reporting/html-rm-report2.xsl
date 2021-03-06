<?xml version="1.0" encoding="utf-8"?>

<!--
    Document   : html-rm-report2.xsl
    Created on : 15. duben 2012, 22:50
    Author     : zmatlja1
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    
<xsl:output method="html" doctype-system="http://www.w3.org/TR/html4/loose.dtd" doctype-public="-//W3C//DTD HTML 4.01//EN" indent="yes" />

    <xsl:template match="/">
        <html>
            <head>
                <title>Report 2</title>
            </head>
            <body>
                <xsl:apply-templates /> 
            </body>
        </html>         
    </xsl:template>

    <xsl:template match="Report">
        <h1>Report 2</h1>
        
        <xsl:import href="../seage-experimenter/src/main/resources/org/seage/experimenter/reporting/algorithm-parameters-names.xsl"/>
        <xsl:import href="../seage-experimenter/src/main/resources/org/seage/experimenter/reporting/algorithm-parameters-values.xsl"/>
        <xsl:import href="../seage-experimenter/src/main/resources/org/seage/experimenter/reporting/milis-to-sec.xsl"/>
        
        <table border="1">
        <xsl:for-each select="ExampleSet[1]/Examples/Example">
            <xsl:variable name="Algorithm" select="Result[2]/@value" />
            <xsl:variable name="ProblemID" select="Result/@value" />            
            
            <xsl:choose>
                <xsl:when test="preceding-sibling::Example/Result[1]/@value=$ProblemID">
                    <tr><td></td></tr>
                </xsl:when>
                <xsl:otherwise>
                    <tr><td><h2><xsl:value-of select="Result/@value" /></h2></td></tr>
                </xsl:otherwise>
            </xsl:choose>            

            <tr>
                <td></td><td><h2><xsl:value-of select="$Algorithm" /></h2></td>
                <td>
                    Time <xsl:call-template name="MillisecondsToSeconds"><xsl:with-param name="miliseconds" select="Result[3]/@value" /></xsl:call-template> s - 
                    <xsl:call-template name="MillisecondsToSeconds"><xsl:with-param name="miliseconds" select="Result[4]/@value" /></xsl:call-template> s
                </td>
            </tr>
            
            <xsl:call-template name="GetParametersValues">
                <xsl:with-param name="Algorithm" select="$Algorithm" />
                <xsl:with-param name="FirstParameterStartPosition" select="5" />
            </xsl:call-template>
            
            <tr><td colspan="4"> </td></tr>
            <xsl:for-each select="/Report/ExampleSet[2]/Examples/Example[Result[1]/@value=$ProblemID and Result[2]/@value=$Algorithm]">
                <xsl:variable name="InstanceID" select="Result[3]/@value" />  
                <tr><td></td><td></td>
                    <td align="center"><h3><xsl:value-of select="Result[3]/@value" /></h3></td>
                    <td>Average Solution Value: <strong><xsl:value-of select="Result[4]/@value" /></strong></td>
                    <xsl:call-template name="GetParametersValues">
                        <xsl:with-param name="Algorithm" select="$Algorithm" />
                        <xsl:with-param name="FirstParameterStartPosition" select="5" />
                    </xsl:call-template>
                </tr>                
                
                <tr>
                    <td></td><td></td><td></td>
                    <th>Minimal SolutionValue</th>
                    <th>Time</th>
                    
                    <xsl:variable name="position" select="0" />
                    <xsl:for-each select="/Report/Collection//ExampleSet/Examples/Example[Result[1]/@value=$ProblemID and Result[2]/@value=$Algorithm and Result[3]/@value=$InstanceID][1]/Result">
                                
                        <xsl:choose>
                            <xsl:when test="position()&lt;7"></xsl:when>
                            <xsl:when test="@value=-1.0"></xsl:when>
                            <xsl:when test="position()=17"></xsl:when>
                            <xsl:otherwise>
                                
                                <xsl:variable name="position">
                                    <xsl:choose>
                                        <xsl:when test="position() = 7"><xsl:value-of select="position()-1" /></xsl:when>
                                        <xsl:when test="position() = 8"><xsl:value-of select="position()" /></xsl:when>
                                        <xsl:when test="position() = 9"><xsl:value-of select="position()+1" /></xsl:when>
                                        <xsl:when test="position() = 10"><xsl:value-of select="position()+2" /></xsl:when>
                                        <xsl:when test="position() = 11"><xsl:value-of select="position()+3" /></xsl:when>
                                        <xsl:when test="position() = 12"><xsl:value-of select="position()+4" /></xsl:when>
                                        <xsl:when test="position() = 13"><xsl:value-of select="position()+5" /></xsl:when>
                                        <xsl:when test="position() = 14"><xsl:value-of select="position()+6" /></xsl:when>
                                        <xsl:when test="position() = 15"><xsl:value-of select="position()+7" /></xsl:when>
                                        <xsl:when test="position() = 16"><xsl:value-of select="position()+8" /></xsl:when>
                                    </xsl:choose>
                                </xsl:variable>
                                
                                <th>
                                    <xsl:call-template name="GetParametresByAlgorithmAndPosition">
                                        <xsl:with-param name="algorithm" select="$Algorithm" />
                                        <xsl:with-param name="position" select="$position" />
                                    </xsl:call-template>
                                </th>

                            </xsl:otherwise>
                        </xsl:choose>
                        
                    </xsl:for-each>

                </tr>
                <xsl:for-each select="/Report/Collection//ExampleSet/Examples/Example[Result[1]/@value=$ProblemID and Result[2]/@value=$Algorithm and Result[3]/@value=$InstanceID]">
                    <tr><td></td><td></td>
                    <td><xsl:value-of select="position()" /></td>
                    <td><xsl:value-of select="Result[5]/@value" /></td>
                    <td><xsl:call-template name="MillisecondsToSeconds"><xsl:with-param name="miliseconds" select="Result[6]/@value" /></xsl:call-template> s</td>
                    <xsl:for-each select="Result">
                        <xsl:choose>
                            <xsl:when test="position()&lt;7"></xsl:when>
                            <xsl:when test="position()=17"></xsl:when>
                            <xsl:when test="@value=-1.0"></xsl:when>
                            <xsl:otherwise>
                                <td><xsl:value-of select="@value" /></td>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:for-each>
                    </tr>
                </xsl:for-each>

                <tr><td colspan="4"> </td></tr>
            </xsl:for-each> 
        </xsl:for-each>
        </table>
    </xsl:template>
</xsl:stylesheet>
