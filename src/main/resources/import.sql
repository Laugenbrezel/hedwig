-- You can use this file to load seed data into the database using SQL statements
insert into Member (id, name, email, phone_number) values (-1, 'John Smith', 'john.smith@mailinator.com', '2125551212') 

insert into Target (id, name, host, port, registryName, objectName, accessMethod, status) values (-1, 'My 1st Sample Bean','localhost', 8888, 'jmxrmi', 'bean:name=sampleBean', 'checkMeOut', 'UNKNOWN') 