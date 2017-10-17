# Monthly Net Profit Calculator

Application calculates a monthly net profit from contract in a selected country.
The tax rate and fixed cost for available countries are stored in an inmemory database **h2**.

The Project is created in spring-boot (backend) and angular (ui) framework.
It is my first angular project.

Application uses the external api [NBP API](http://api.nbp.pl/) to read current exchange rate for currency.

## Application requirements

1. Java 8
2. Apache Maven 3.3.3 to compile a project
3. angular/cli: 1.4.7
4. node: 6.11.4

## Run application

To run backend application it is required to do:

In **kalkulator-sonalake-backend-api** directory execute:

First build backend project in maven:
``` bash
mvn clean install
```
To run backend application:
``` bash
mvn spring-boot:run
```
Backend application API application is running now.

To run frontend application it is required to do:

In **kalkulator-sonalake-ui** directory execute:
``` bash
ng serve
```

Frontend application is running now.

## How to use

Run [http://localhost:4200/](http://localhost:4200/) on your browser.

Select country and enter an appropriate net daily rate in selected country currency. 
On response application will calculate monthly net profit in selected country in PLN currency.