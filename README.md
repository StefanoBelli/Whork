[![Whork build](https://github.com/StefanoBelli/Whork/actions/workflows/whork.yml/badge.svg)](https://github.com/StefanoBelli/Whork/actions/workflows/whork.yml)

[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=StefanoBelli_Whork&metric=sqale_index)](https://sonarcloud.io/dashboard?id=StefanoBelli_Whork)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=StefanoBelli_Whork&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=StefanoBelli_Whork)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=StefanoBelli_Whork&metric=code_smells)](https://sonarcloud.io/dashboard?id=StefanoBelli_Whork)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=StefanoBelli_Whork&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=StefanoBelli_Whork)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=StefanoBelli_Whork&metric=security_rating)](https://sonarcloud.io/dashboard?id=StefanoBelli_Whork)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=StefanoBelli_Whork&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=StefanoBelli_Whork)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=StefanoBelli_Whork&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=StefanoBelli_Whork)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=StefanoBelli_Whork&metric=coverage)](https://sonarcloud.io/dashboard?id=StefanoBelli_Whork)

# Whork
ISPW project a.y. 2020/2021

## Compatability notes
 * Browser automated testing is available on Windows and Linux only
 * Project will be compiled iff using JDK version >= 11
 * Project will be run iff JRE version >= 11
 * Database schema requires MySQL Server version >= 8
 * If jfx WebView cannot be shown, most likely it is a module importing error, add the following args to VM:
   ~~~
   --add-modules javafx.controls,javafx.fxml,javafx.web --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED
   ~~~
   (thanks Mike-98)
   
## Testing
Testing is achieved on two "levels":
 * Unit testing
 * "Browser automated" testing (via Selenium API and IDE)

### How to run tests manually
~~~
$ pwd # ensure we are inside root project directory (which includes pom.xml)
*/Whork
$ mvn test # runs unit tests
$ mvn test -Dtest=test.selenium.Selenium* # runs Selenium API tests
$ chromium . # from there open project (in side/whork-*.side) using Selenium IDE plugin
~~~
