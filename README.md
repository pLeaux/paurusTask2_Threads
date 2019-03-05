Internal java project, prepared by me for Paurus. 

THIS IS JUST AN ALTERNATIVE BAD+SLOW+NOT FINISHED SOLUTION FOR TASK2, THE ONE WHICH USES THREADS. 
USE paurusTask2_LoadData SOLUTION INSTEAD.

Application "FileToDbImporter" creates one FileReaderThread, one SharedQueue for data and multiple DbWriterThreads. 
Reader thread reads data from file and pushes them to the Queue. Simultaneously, Writer theads pull data from the Queue and insert them to the database.  

To be able the follow the processing of data records, debug messages are displayed in console and FileReader is artificially slowed down.
See debug config parameters in resource file config.properties for ifurther instructions.

Instructions for testing:

* download ZIP and unpack in an empty project folder
* use mySQL server for testing, create only database schema, table is created by this application;
* Open project's resource file "config.properties" and adjust settings for database connection and file path; read further instructions there
* start application (class FileToDbImporter): table foo_random is created and data imported; follow Console output for processing details 
* check imported data in table foo_random

Leo P.
