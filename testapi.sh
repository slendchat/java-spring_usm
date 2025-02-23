#!/bin/bash

#sleep 10

API_URL="http://localhost:8080"
EXIT_CODE=0

echo "🚀 Запуск тестов API..."

# 📌 Функция для тестирования API-запросов с выводом респонса
test_api() {
    local method=$1
    local endpoint=$2
    local data=$3
    local expected_code=$4

    if [ "$method" == "POST" ]; then
        response=$(curl -s -w "%{http_code}" -X POST "$API_URL$endpoint" \
            -H "Content-Type: application/json" -d "$data")
    elif [ "$method" == "GET" ]; then
        response=$(curl -s -w "%{http_code}" -X GET "$API_URL$endpoint")
    elif [ "$method" == "PUT" ]; then
        response=$(curl -s -w "%{http_code}" -X PUT "$API_URL$endpoint" \
            -H "Content-Type: application/json" -d "$data")
    elif [ "$method" == "DELETE" ]; then
        response=$(curl -s -w "%{http_code}" -X DELETE "$API_URL$endpoint")
    fi

    http_code="${response: -3}"  # 📌 Последние 3 символа — это HTTP-код
    body="${response::-3}"  # 📌 Всё, кроме последних 3 символов — это JSON-ответ

    if [ "$http_code" -ne "$expected_code" ]; then
        echo "❌ Ошибка: $method $endpoint (Ожидался код: $expected_code, получили: $http_code)"
        echo "Ответ сервера:"
        echo "$body" | jq .  # 📌 Красиво форматируем JSON-ответ
        EXIT_CODE=1
    else
        echo "✅ Успешно: $method $endpoint (Код: $http_code)"
        echo "Ответ сервера:"
        echo "$body" | jq .
    fi
}

# 📌 Тесты

# подготовка
test_api "POST" "/books" '{
    "title": "testbook1",
    "authorName": "Aristotle",
    "publisherName": "Verso",
    "categories": ["Philosophy"]
}' 200
test_api "POST" "/books" '{
    "title": "testbook2",
    "authorName": "Lovecraft",
    "publisherName": "BVZ",
    "categories": ["Horror", "Sci-Fi"]
}' 200

# 🟢 **1. Тестируем авторов**
test_api "GET" "/authors" "" 200
test_api "POST" "/authors" '{"name": "John Doe"}' 200
test_api "GET" "/authors/1/books" "" 200

# 🟢 **2. Тестируем книги**
test_api "GET" "/books" "" 200
test_api "POST" "/books" '{
    "title": "Spring Boot in Action",
    "authorName": "John Doe",
    "publisherName": "Tech Books",
    "categories": ["Java", "Programming"]
}' 200
test_api "GET" "/books/1" "" 200
test_api "PUT" "/books/3" '{"title": "Updated Book"}' 200
test_api "DELETE" "/books/3" "" 200

# 🟢 **3. Тестируем категории**
test_api "GET" "/categories" "" 200
test_api "POST" "/categories" '{"name": "Java"}' 200
test_api "GET" "/categories/1/books" "" 200

# 🟢 **4. Тестируем издателей**
test_api "GET" "/publishers" "" 200
test_api "POST" "/publishers" '{"name": "Penguin Random House"}' 200
test_api "GET" "/publishers/1/books" "" 200

# 🟢 **5. Тестируем библиотеку**
test_api "GET" "/library" "" 200


# 📌 Завершаем тестирование
if [ "$EXIT_CODE" -ne 0 ]; then
    echo "❌ Ошибка: Один или несколько тестов не прошли!"
    exit 1
else
    echo "✅ Все тесты пройдены успешно!"
    exit 0
fi
