====

     Copyright (C) 2010 SYSNET International, Inc. <support@sysnetint.com>

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
     implied. See the License for the specific language governing
     permissions and limitations under the License.

====
Adding missing libraries to maven repository:

mvn -X install:install-file -Dfile=simmetrics-1.6.2.jar -DgroupId=simmetrics -DartifactId=simmetrics -Dversion=1.6.2 -Dpackaging=jar

Installing JBoss's own jbossall-client.jar into our local maven repo, so JUnit tests will use the same as deployment

mvn install:install-file -DgroupId=org.jboss.client -DartifactId=jbossall-client -Dversion=4.2.3.GA -Dfile=C:/jboss/client/jbossall-client.jar -Dpackaging=jar -DgeneratePom=true

