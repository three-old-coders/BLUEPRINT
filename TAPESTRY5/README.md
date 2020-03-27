This sample project demonstrates how to use Tapestry 5 + SpringBoot + Bootstrap 3 / 4 and Java 11

After cloning you will find the following directory structure:

- TAPESTRY5 (root directory for all Tapestry related applications, more to come)
  - T55_BS4_SB_Java11 (root directory to organize all required Tapestry, Bootstrap, Springboot artifacts)
    - BOOTSTRAP (all Bootstrap related artifacts)
      - BOOTSTRAP COMMON (Tapestry components shared between all Bootstrap versions)
      - BOOTSTRAP 3 (Bootstrap 3 specializations, currently empty / unused)
      - BOOTSTRAP 4 (Bootstrap 4 specializations, currently supporting css grid and alert components)
    - T55_BS_SB_Java11-APPLICATION (Springboot application using Bootstrap 4 and a sample page to show Alerts)
    
The tapestry application integrates a library from https://github.com/code8/tapestry-boot, but updated to use more or less resent versions of all dependend jars. Due to the fact we are currently not pushing artifacts to a central maven repo and we want to avoid you need to deal with gradle to build the tapestry-boot artifact, it's contained as jar. To copy it to your local M2 repo just navigate to
```
BLUEPRINT/TAPESTRY5/T55_BS4_SB_Java11
```
and type
```
mvn initialize
```
you will see something similar to this:
```
[INFO] Installing <YOUR_DIR>/BLUEPRINT/TAPESTRY5/T55_BS4_SB_Java11/T55_BS_SB_Java11-APPLICATION/LIB/SpringBoot_Tapestry55_Java11-0.1.0-SNAPSHOT.jar to <YOUR_DIR>/.m2/repository/de/3OC/blueprint/t55bs/SpringBoot_Tapestry55_Java11/0.1.0/SpringBoot_Tapestry55_Java11-0.1.0.jar
[INFO] Installing <YOUR_DIR>/BLUEPRINT.GIT/BLUEPRINT/TAPESTRY5/T55_BS4_SB_Java11/T55_BS_SB_Java11-APPLICATION/LIB/SpringBoot_Tapestry55_Java11-0.1.0-SNAPSHOT.pom to <YOUR_DIR>//.m2/repository/de/3OC/blueprint/t55bs/SpringBoot_Tapestry55_Java11/0.1.0/SpringBoot_Tapestry55_Java11-0.1.0.pom
```
you only need to do this once (to be more precise, once after pulling). Afterwards compile the entire project by
```
mvn clean install
```
Now, navigate to
```
BLUEPRINT/TAPESTRY5/T55_BS4_SB_Java11/T55_BS_SB_Java11-APPLICATION
```
and type 
```
mvn spring-boot:run
```
As soon you see something similar to "Started Tapestry55SBLauncher in 4.494 seconds (JVM running for 5.265)" everything is fine. Fire up your favorite browser and type
```
http://localhost:8080
```
that's it. You see a Tapestry 5 generated test page showing some alerts rendered as components. Let's treat this a starting point to see how Tapestry 5, Bootstrap 4 works together. 
First of all, Tapestry 5 is a component based, convention over configuration, voodoo magic inversion of control framework which confuses novices but makes you happy when you got familiar with all concepts and headache subsides. It supports inheritance, packaging your components in libraries and surprisingly we will make use of all features. 

Tapestry orginizes all application relevant parts in subdirectories. So your application looks like:
- YOUR_APP_DIR/
  - pages (all pages, a page assembles components)
    - Index (a Tapestry page you can access with your browser by localhost:8080/index or localhost:8080 (defaults to index)
  - components (reuseable components)
    - BS4Layout (a Layout class)
  - services (all interfaces and service implementations, Tapestry initialization classes a.s.o)
    - AppModule (base configuration of the Tapestry application)
  
TBC
