# java-spring_usm

### ИСПОЛЬЗОВАТЬ АККУРАТНО build.sh ВЫПОЛНЯЕТ docker system prune -af


### To install the programm:
 1. Install docker on linux system
 2. run build.sh

### Test if it is working
check if it is working, the result should be the same:
```bash
curl -X POST http://localhost:8080/books      -H "Content-Type: application/json"      -d '{
           "title": "Spring Boot in Action",
           "authorName": "John Doe",
           "publisherName": "Tech Books",
           "categories": ["Java", "Programming"]
         }'
```

result - `{"id":1,"title":"Spring Boot in Action","authorName":"John Doe","publisherName":"Tech Books","categories":["Java","Programming"]}`

```bash
curl -X GET http://localhost:8080/books
```
result - `[{"id":1,"title":"Spring Boot in Action","authorName":"John Doe","publisherName":"Tech Books","categories":["Java","Programming"]}]`