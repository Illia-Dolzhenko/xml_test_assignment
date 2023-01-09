# README #

### XML Service test assignemnt ###

	/src/main/resources/application.properties contains db connection settings
	
### Exposed endpoints:
>GET http://localhost:8080/files/{id}?page=0[&sort=field]

>GET http://localhost:8080/files?page=0[&sort=field&fileName=fileNameFilter]
  
>POST http://localhost:8080/upload
	
### To run: ###

	mvn spring-boot:run
