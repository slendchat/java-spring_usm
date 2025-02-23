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


### **📌 Полный список API-запросов для тестирования**
Теперь, когда логика приложения исправлена, ты можешь протестировать его с помощью `curl` или Postman.

---

## **📌 1. Получить список книг в библиотеке (`GET /library`)**
📌 **Этот запрос вернёт список названий книг, которые находятся в библиотеке.**
```sh
curl -X GET http://localhost:8080/library
```
🔹 **Ожидаемый ответ (если в библиотеке есть книги):**
```json
{
  "bookTitles": ["Spring Boot in Action", "Java Fundamentals"]
}
```
🔹 **Если библиотека пустая:**
```json
{
  "bookTitles": []
}
```

---

## **📌 2. Добавить новую книгу в систему (`POST /books`)**
📌 **Этот запрос создаёт новую книгу в базе данных и автоматически добавляет её название в библиотеку.**
```sh
curl -X POST http://localhost:8080/books \
     -H "Content-Type: application/json" \
     -d '{
           "title": "Spring Boot in Action",
           "authorName": "John Doe",
           "publisherName": "Tech Books",
           "categories": ["Java", "Programming"]
         }'
```
🔹 **Ожидаемый ответ API (созданная книга):**
```json
{
  "id": 1,
  "title": "Spring Boot in Action",
  "authorName": "John Doe",
  "publisherName": "Tech Books",
  "categories": ["Java", "Programming"]
}
```
📌 **Теперь книга автоматически добавлена в библиотеку!**  
Ты можешь сделать `GET /library`, чтобы убедиться, что её название добавилось.

---

## **📌 3. Получить список всех книг (`GET /books`)**
📌 **Этот запрос вернёт список всех книг в системе с полной информацией.**
```sh
curl -X GET http://localhost:8080/books
```
🔹 **Ожидаемый ответ API:**
```json
[
  {
    "id": 1,
    "title": "Spring Boot in Action",
    "authorName": "John Doe",
    "publisherName": "Tech Books",
    "categories": ["Java", "Programming"]
  },
  {
    "id": 2,
    "title": "Java Fundamentals",
    "authorName": "Jane Smith",
    "publisherName": "O'Reilly",
    "categories": ["Java"]
  }
]
```

---

## **📌 4. Получить книгу по ID (`GET /books/{id}`)**
📌 **Этот запрос вернёт информацию о книге по её `ID`.**
```sh
curl -X GET http://localhost:8080/books/1
```
🔹 **Ожидаемый ответ API:**
```json
{
  "id": 1,
  "title": "Spring Boot in Action",
  "authorName": "John Doe",
  "publisherName": "Tech Books",
  "categories": ["Java", "Programming"]
}
```

---

## **📌 5. Удалить книгу (`DELETE /books/{id}`)**
📌 **Этот запрос удалит книгу из системы.**
```sh
curl -X DELETE http://localhost:8080/books/1
```
🔹 **Ожидаемый ответ API (если книга была найдена и удалена):**
```json
[HTTP 200 OK]
```
📌 **Но книга останется в списке `bookTitles` в библиотеке!**  
Если ты хочешь убрать её и оттуда, нужно вручную редактировать библиотеку.

---

## **📌 6. Получить всех авторов (`GET /authors`)**
📌 **Этот запрос вернёт список всех авторов.**
```sh
curl -X GET http://localhost:8080/authors
```
🔹 **Ожидаемый ответ API:**
```json
[
  {
    "id": 1,
    "name": "John Doe"
  },
  {
    "id": 2,
    "name": "Jane Smith"
  }
]
```

---

## **📌 7. Добавить нового автора (`POST /authors`)**
📌 **Этот запрос создаёт нового автора.**
```sh
curl -X POST http://localhost:8080/authors \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Robert Martin"
         }'
```
🔹 **Ожидаемый ответ API:**
```json
{
  "id": 3,
  "name": "Robert Martin"
}
```

---

## **📌 8. Получить всех издателей (`GET /publishers`)**
📌 **Этот запрос вернёт список всех издателей.**
```sh
curl -X GET http://localhost:8080/publishers
```
🔹 **Ожидаемый ответ API:**
```json
[
  {
    "id": 1,
    "name": "Tech Books"
  },
  {
    "id": 2,
    "name": "O'Reilly"
  }
]
```

---

## **📌 9. Добавить нового издателя (`POST /publishers`)**
📌 **Этот запрос создаёт нового издателя.**
```sh
curl -X POST http://localhost:8080/publishers \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Penguin Random House"
         }'
```
🔹 **Ожидаемый ответ API:**
```json
{
  "id": 3,
  "name": "Penguin Random House"
}
```

---

## **📌 10. Получить все книги в категории (`GET /categories/{categoryName}/books`)**
📌 **Этот запрос вернёт все книги, принадлежащие категории.**
```sh
curl -X GET http://localhost:8080/categories/Java/books
```
🔹 **Ожидаемый ответ API:**
```json
[
  {
    "id": 1,
    "title": "Spring Boot in Action",
    "authorName": "John Doe",
    "publisherName": "Tech Books",
    "categories": ["Java", "Programming"]
  },
  {
    "id": 2,
    "title": "Java Fundamentals",
    "authorName": "Jane Smith",
    "publisherName": "O'Reilly",
    "categories": ["Java"]
  }
]
```

---

## **📌 11. Получить все книги у издателя (`GET /publishers/{publisherName}/books`)**
📌 **Этот запрос вернёт все книги, изданные конкретным издателем.**
```sh
curl -X GET http://localhost:8080/publishers/Tech%20Books/books
```
🔹 **Ожидаемый ответ API:**
```json
[
  {
    "id": 1,
    "title": "Spring Boot in Action",
    "authorName": "John Doe",
    "publisherName": "Tech Books",
    "categories": ["Java", "Programming"]
  }
]
```
