UniDue-FallStudie-CEP
=====================


How to Build
-----------------------
To build the war you need to execute the package goal.

<code>mvn clean package</code>


Instant execution | Tomcat7 Maven Plugin
-----------------------
To run the project without a local installed tomcat,
you are able to run it with the tomcat7 maven plugin directly from the shell.

<code>mvn tomcat7:run</code>

Tomcat starts at configured port ( Default 8080 ) 
and you can reach it over the URL [http://localhost:8080](http://localhost:8080)