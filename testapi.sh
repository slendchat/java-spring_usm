#!/bin/bash

API_URL="http://localhost:8080"
EXIT_CODE=0

echo "🚀 Запуск тестов API..."

# 📌 Функция для тестирования API-запросов
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
    elif [ "$method" == "DELETE" ]; then
        response=$(curl -s -w "%{http_code}" -X DELETE "$API_URL$endpoint")
    fi

    http_code="${response: -3}"  # 📌 Последние 3 символа — это HTTP-код
    body="${response::-3}"  # 📌 Всё, кроме последних 3 символов — это JSON-ответ

    if [ "$http_code" -ne "$expected_code" ]; then
        echo "❌ Ошибка: $method $endpoint (Ожидался код: $expected_code, получили: $http_code)"
        echo "Ответ сервера:"
        echo "$body" | jq .  # 📌 Красиво выводим JSON-ответ
        EXIT_CODE=1
    else
        echo "✅ Успешно: $method $endpoint (Код: $http_code)"
        echo "Ответ сервера:"
        echo "$body" | jq .  # 📌 Выводим JSON-ответ
    fi
}

# 📌 Тесты

# 1. Получить список книг в библиотеке (должен быть 200 OK)
test_api "GET" "/library" "" 200

# 2. Добавить новую книгу
test_api "POST" "/books" '{
    "title": "Spring Boot in Action",
    "authorName": "John Doe",
    "publisherName": "Tech Books",
    "categories": ["Java", "Programming"]
}' 200

test_api "POST" "/books" '{
    "title": "Test book 2",
    "authorName": "Hovard Lovecraft",
    "publisherName": "pocketclassic",
    "categories": ["Sci-Fi", "Horror"]
}' 200

# 3. Получить список всех книг
test_api "GET" "/books" "" 200

# 4. Получить книгу по ID (ID = 1)
test_api "GET" "/books/1" "" 200

# 5. Удалить книгу (ID = 1)
test_api "DELETE" "/books/1" "" 200

# 6. Получить всех авторов
test_api "GET" "/authors" "" 200

# 7. Добавить нового автора
test_api "POST" "/authors" '{
    "name": "Robert Martin"
}' 200

# 8. Получить всех издателей
test_api "GET" "/publishers" "" 200

# 9. Добавить нового издателя
test_api "POST" "/publishers" '{
    "name": "Penguin Random House"
}' 200

# 📌 Завершаем тестирование
if [ "$EXIT_CODE" -ne 0 ]; then
    echo "❌ Ошибка: Один или несколько тестов не прошли!"
    exit 1
else
    echo "✅ Все тесты пройдены успешно!"
    exit 0
fi
