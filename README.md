# BLUEPRINT
Blueprints and Samples (mainly using the JAVA language / stack)

We are a team of passionate software developers and love what we do. And we are old, really old, but just let's say "experienced" instead. Whenever we start new projects, start to learn something new, Google, Stackoverflow is our main starting point. Unfortunately most of the projects / samples were a little outdated or do not work seamlessly with current versions of libraries / frameworks or build environments. The basic idea behind this repositiory is to share samples of different technolgies, ready to use, with up-to-date software.

So, in detail we are talking about:

1. Infrastructure
   1. Eureka + SpringBoot + Rest Interface
   2. Zuul + SpringBoot (making use of Eureka)
   3. SpringBoot Sample Application (just connecting to Eureka)
2. ClamAV
   1. SpringBoot + Eureka + Zuul + Rest Interface (virus scanning)
   2. Docker container assembling ClamAV(d) and SpringBoot application (Rest Interface)
   3. TODO: Docker container network settings prevent Eureka client to connect to external Eureka Server  
3. NoSQL
   1. Hazelcast + SpringBoot + Rest Interface + Integration of Eureka and Zuul
   2. TODO: ElasticSearch + SpringBoot + Rest Interface + Integration of Eureka and Zuul
4. RUNDECK
   1. Rundeck Plugins to bridge JDK8 to JDK11 (by JSON file generated on compile time via maven)
   2. Rundeck Plugins to bridge JDK8 to JDK11 (multi-release jar using maven-compile-switch-plugin)   
   3. Picoli CommandLine to Rundeck WorkflowSteps including Rundeck UI
5. Tapestry 5.5 (https://github.com/three-old-coders/BLUEPRINT/tree/master/TAPESTRY5)
   1. SpringBoot + Tapestry 5.5 App + SpringBoot, which does ... well ... "nothing, but starting"
   2. SpringBoot + Tapestry 5.5 App + SpringBoot + Bootstrap 4 (bundle version) + some BS4 demo components 
   3. TODO: SpringBoot + Tapestry 5.5 App + SpringBoot + Bootstrap 4 + Jquery 3.x via RequireJS
   
   
More to come (like AWS support, CAMEL, maven, gradle, Docker a.s.o). 


Jens / Robert / Andi
