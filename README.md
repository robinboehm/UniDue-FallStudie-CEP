UniDue-FallStudie-CEP
=====================

Application Files are in the subfolder <b>application</b>.

Following Commands you can use to get startet with the project.

If you need some dependencies you can search them on [http://search.maven.org/](http://search.maven.org/)


How to Build
-----------------------
To build the war you need to execute the package goal.

<code>mvn clean package</code>

A new folder named target will be generated where you can find your artifact as .war file.

Instant execution | Tomcat7 Maven Plugin
-----------------------
To run the project without a local installed tomcat,
you are able to run it with the tomcat7 maven plugin directly from the shell.

<code>mvn tomcat7:run</code>

Tomcat starts at configured port ( Default 8080 ) 
and you can reach it over the URL [http://localhost:8080](http://localhost:8080)