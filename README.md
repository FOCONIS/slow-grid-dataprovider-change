slow-grid-dataprovider-change
=============================

A sample project to demonstrate the slowness of the grid fully rendered compared to being generated in a defered run by changing DataProvider.

[Vaadin framework 8 issue #11683 on GitHub:](https://github.com/vaadin/framework/issues/11683) - 
Vaadin 8 grid rendering time slower than rendering it twice (empty, then full) switching DataProvider 

Performance Results:
====================

- Run the application, run "mvn jetty:run" and open http://localhost:8080/ for manual tests.

- click the "clear UI" button and compare rendering times between:
- clicking "build full Grid" button just loads the full grid with 200 rows by updating the empty DataProvider with a full one
- clicking "build full Grid" button just loads an empty grid first and then defers the loading of the full grid with the 200 rows DataProvider shortly using polling
- clicking "build empty Grid" button for comparison just loads an empty grid


Project Usage
=============

To compile the entire project, run "mvn install" using Maven > 3.

To run the application, run "mvn jetty:run" and open http://localhost:8080/ 
- then change the columns, hidden columns and rows to your liking and generate the grid after hiding it

To produce a deployable production mode WAR:
- change productionMode to true in the servlet class configuration (nested in the UI class)
- run "mvn clean package"
- test the war file with "mvn jetty:run-war"
