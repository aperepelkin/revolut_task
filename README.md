# RESTful API for Account Service

Building service
----------------

`./gradlew assembleDist`

after building you could find followings 
- build/distributions - archives with binaries and scripts
- build/reports/tests/test/index.html - test report

 
Running service
----------------

Unzip distribution archive and run bin/account-service or bin/account-service.bat (for Windows).
By default, service will run on 8080 port
You could build distribution or download it from https://github.com/aperepelkin/revolut_task/releases/latest

API
---
- GET http://localhost:8080/accounts - list avaliable accounts
- GET http://localhost:8080/accounts/{id} - get information about account with {id}
- GET http://localhost:8080/accounts/create - generate new account with unique id and number and zero balance
- DELETE http://localhost:8080/accounts/{id} - delete account with non zero balance
- PUT http://localhost:8080/accounts/{id}/enroll - enrollment, add to account with {id} amount that will send as text/plain body 
- PUT http://localhost:8080/accounts/{id}/withdraw - subtract from account with {id} amount that will send as text/plain body
- PUT http://localhost:8080/accounts/{id}/transfer/{to} - transfer from account with {id} to account with {to} amount that will send as text/plain body

CURL examples
-------------
- `curl -X GET http://localhost:8080/accounts`
- `curl -X GET http://localhost:8080/accounts/1`
- `curl -X GET http://localhost:8080/accounts/create`
- `curl -X DELETE http://localhost:8080/accounts/3`
- `curl -X PUT -H "Content-Type: text/plain" --data "200.00" http://localhost:8080/accounts/1/enroll`
- `curl -X PUT -H "Content-Type: text/plain" --data "200.00" http://localhost:8080/accounts/1/withdraw`
- `curl -X PUT -H "Content-Type: text/plain" --data "200.00" http://localhost:8080/accounts/1/transfer/2`
 
