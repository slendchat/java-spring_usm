# java-spring_usm
## ИСПОЛЬЗОВАТЬ АККУРАТНО build.sh ВЫПОЛНЯЕТ docker system prune -af

check if it is working, the result should be the same:
curl -X POST http://localhost:8080/books      -H "Content-Type: application/json"      -d '{
           "title": "Spring Boot in Action",
           "authorName": "John Doe",
           "publisherName": "Tech Books",
           "categories": ["Java", "Programming"]
         }'

{"id":1,"title":"Spring Boot in Action","authorName":"John Doe","publisherName":"Tech Books","categories":["Java","Programming"]}


curl -X GET http://localhost:8080/books
[{"id":1,"title":"Spring Boot in Action","authorName":"John Doe","publisherName":"Tech Books","categories":["Java","Programming"]}]