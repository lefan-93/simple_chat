# Simple_chat

## Description
## Getting started and initial settings

For the service to work correctly, you must first configure the additional software

#### 1.PostgreSQL 12;

- Download and install postgres with default settings (*port: 5432*)

- Execute in local Postgres:
```postgresql
DROP DATABASE IF EXISTS simplechat;
DROP SCHEMA IF EXISTS simplechat;
DROP USER IF EXISTS simplechat;
DROP ROLE IF EXISTS simplechat;
CREATE USER simplechat WITH LOGIN SUPERUSER PASSWORD 'simplechat';
CREATE DATABASE simplechat OWNER simplechat;
CREATE SCHEMA simplechat AUTHORIZATION simplechat;
```
For Keycloak:
```postgresql
DROP SCHEMA IF EXISTS keycloak;
DROP USER IF EXISTS keycloak;
DROP ROLE IF EXISTS keycloak;
CREATE USER keycloak WITH LOGIN SUPERUSER PASSWORD 'keycloak';
CREATE SCHEMA keycloak AUTHORIZATION keycloak;
```


#### 2. Keycloak
   
   ##### 2.1 Download and unzip keycloak server

   [Download Keycloak](https://www.keycloak.org/downloads)  ( Downloads "Distribution powered by WildFly")

   ##### 2.2 Relational Database Setup

   - You can follow the instructions on the official website [(6.1-6.5)](https://www.keycloak.org/docs/4.8/server_installation/#_database)

   - Or you can follow these steps:

   1. Download postgres driver from official page [download](https://jdbc.postgresql.org/download.html) 

   2. Create directory ***...\postgresql\main*** in ***keycloak_path\modules\system\layers\keycloak\org***

   3. Move the loaded driver in this directory

   4. Create module.xml file in this directory and create the following XML:

```xml 
<?xml version="1.0" ?>
<module xmlns="urn:jboss:module:1.3" name="org.postgresql">

    <resources>
        <resource-root path="name_postgres_driver.jar"/>
    </resources>

    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```
   5. Declare and Load JDBC Driver 
      
      The next thing you have to do is declare your newly packaged JDBC driver into your deployment profile so that it loads and becomes available when the server boots up.
   
      Edit ***keycloak_path/standalone/configuration/standalone.xml:***

      Within the profile, search for the drivers XML block within the datasources subsystem. You should see a pre-defined driver declared for the H2 JDBC driver. This is where you’ll declare the JDBC driver for your external database.

      JDBC Drivers:
      
```xml
<subsystem xmlns="urn:jboss:domain:datasources:6.0">
   <datasources>
      ...
      <drivers>
         <driver name="h2" module="com.h2database.h2">
             <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
         </driver>
      </drivers>
    </datasources>
</subsystem>
```

Within the drivers XML block you’ll need to declare an additional JDBC driver. It needs to have a name which you can choose to be anything you want. You specify the module attribute which points to the module package you created earlier for the driver JAR. Finally you have to specify the driver’s Java class. Here’s an example of installing PostgreSQL driver that lives in the module example defined earlier in this chapter.

Declare Your JDBC Drivers
```xml
<subsystem xmlns="urn:jboss:domain:datasources:5.0">
    <datasources>
      ...
      <drivers>
         <driver name="postgresql" module="org.postgresql">
             <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
         </driver>
         <driver name="h2" module="com.h2database.h2">
             <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
         </driver>
      </drivers>
    </datasources>
 </subsystem>
```
   6. Modify the Keycloak Datasource
      
      After declaring your JDBC driver, you have to modify the existing datasource configuration that Keycloak uses to connect it to your new external database. You’ll do this within the same configuration file and XML block that you registered your JDBC driver in. Here’s an example that sets up the connection to your new database:

   Declare Your JDBC Drivers
   ```xml
<subsystem xmlns="urn:jboss:domain:datasources:6.0">
    <datasources>
       ...
       <datasource jndi-name="java:jboss/datasources/KeycloakDS" pool-name="KeycloakDS" enabled="true" use-java-context="true">
          <connection-url>jdbc:postgresql://localhost:5432/simplechat?currentSchema=keyckloak</connection-url>
          <driver>postgresql</driver>
          <pool>
             <max-pool-size>20</max-pool-size>
          </pool>
          <security>
             <user-name>keycloak</user-name>
             <password>keycloak</password
          </security>
       </datasource>
       ...
    </datasources>
</subsystem>
   ```

   7. Database Configuration 

```xml
<subsystem xmlns="urn:jboss:domain:keycloak-server:1.1">
    ...
    <spi name="connectionsJpa">
     <provider name="default" enabled="true">
         <properties>
             <property name="dataSource" value="java:jboss/datasources/KeycloakDS"/>
             <property name="initializeEmpty" value="false"/>
             <property name="migrationStrategy" value="update"/>
             <property name="migrationExport" value="${jboss.home.dir}/keycloak-database-update.sql"/>
         </properties>
     </provider>
    </spi>
    ...
</subsystem>
```
   ##### 2.3 Start Keycloak

   All these steps are described in the official [documentation](https://www.keycloak.org/getting-started/getting-started-zip)

   From a terminal open the directory ***keycloak_path/bin***, then to start Keycloak run the following command.

   On Linux run:
   >standalone.sh -Djboss.socket.binding.port-offset=100

   On Windows run:
   >standalone.bat -Djboss.socket.binding.port-offset=100

   ***Create an admin user:***

   Keycloak does not come with a default admin user, which means before you can start using Keycloak you need to create an admin user.

   To do this open http://localhost:8180/auth, then fill in the form with your preferred username and password (we recommend admin admin).

   ***Login to the admin console:***

   Go to the Keycloak Admin Console (http://localhost:8180/auth/admin) and login with the username and password you created earlier.
   
   ##### 2.4 Create a realm 

   - Open the Keycloak Admin Console

   - Hover the mouse over the dropdown in the top-left corner where it says Master, then click on Add realm

   - Fill in the form with the following values:

      ***Name***: Simple-chat 
     
   - Click Create

   ##### 2.5 Create a client 

   - Open the Keycloak Admin Console

   - Hover the mouse over the dropdown in the top-left corner where it says Master, then click on Semple-chat realm

   - Click 'Clients'

   - Fill in the form with the following values:

     ***Client ID:*** simplechat

     ***Client Protocol:*** openid-connect

     ***Root URL:*** http://localhost:8080

   - Click Save

   ##### 2.6 Setup a client for secure app

   - Click 'Clients' 

   - In the table with clients, click on client id 'simplechat'

   - On the **Settings** tab:

      + set Access Type *confidential*
         
      + turn on *Authorization Enabled*
   
      + turn on *Service Accounts Enabled*
   
   - On the **Roles** tab:

      + In the table with roles, click on role name 'uma_protection'
   
      + turn on  *Composite Roles*
   
      + In item *Client Roles* select *realm-management* 
   
      + Select from list Available Roles following roles and click **Add selected**:
         + *manage-clients*
         + *manage-users*
         + *view-clients*
         + *view-users*
   
   - Execute in local Postgres: 

```postgresql
UPDATE keyckloak.client SET secret='5cc51cb9-84be-4c03-ba9d-05b9a069f33d' 
WHERE client_id = 'simplechat'
```

####  3. Start Simple-chat app 

Go to in terminal ***app_path\build\libs***

 run: >java simple_chat_server-1.0-SNAPSHOT.jar


