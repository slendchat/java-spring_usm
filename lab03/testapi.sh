#!/usr/bin/env bash
# testapi.sh — автоматические тесты для Library API
# Зависимости: curl, jq

API_URL="http://localhost:8080"
EXIT_CODE=0

echo "🚀 Запуск тестов API..."

# Функция-тестер:
# $1 = METHOD, $2 = endpoint, $3 = data (JSON string or empty), $4 = expected HTTP code
test_api() {
    local method=$1 endpoint=$2 data=$3 expected=$4
    local response http_code body

    if [[ "$method" == "GET" || "$method" == "DELETE" ]]; then
        response=$(curl -s -w "%{http_code}" -X $method "$API_URL$endpoint")
    else
        response=$(curl -s -w "%{http_code}" -X $method "$API_URL$endpoint" \
            -H "Content-Type: application/json" -d "$data")
    fi

    http_code="${response: -3}"
    body="${response::-3}"

    if [[ "$http_code" -ne "$expected" ]]; then
        echo "❌ $method $endpoint: ожидали $expected, получили $http_code"
        echo "Ответ:"
        echo "$body" | jq .
        EXIT_CODE=1
    else
        echo "✅ $method $endpoint ($http_code)"
        echo "$body" | jq .
    fi
    echo
}

#───────────────

#───────────────
# 2) CRUD для Books
test_api POST   "/books" '{"title":"Book 1","authorId":1,"publisherId":1,"categoryIds":[1]}' 201
test_api GET    "/books"                              ""                     200
test_api GET    "/books/1"                            ""                     200
test_api PUT    "/books/1" '{"title":"Book 1 Updated","authorId":1,"publisherId":1,"categoryIds":[1]}' 200
test_api DELETE "/books/1"                            ""                     204

# 1) Создаём Author, Publisher, Category (POST → 201)
test_api POST   "/books" '{"title":"Book 1","authorId":1,"publisherId":1,"categoryIds":[1]}' 201
test_api POST "/authors"    '{"name":"Author One"}'               201
test_api POST "/publishers" '{"name":"Publisher One","bookIds":[]}' 201
test_api POST "/categories" '{"name":"Category A","bookIds":[]}'   201
#───────────────
# 3) CRUD для Authors
test_api GET    "/authors"                            ""                     200
test_api GET    "/authors/1"                          ""                     200
test_api GET    "/authors/1/books"                    ""                     200
test_api PUT    "/authors/1" '{"name":"Author One Edited"}'      200
test_api DELETE "/authors/1"                          ""                     204

#───────────────
# 4) CRUD для Publishers
test_api GET    "/publishers"                         ""                     200
test_api GET    "/publishers/1"                       ""                     200
test_api GET    "/publishers/1/books"                 ""                     200
test_api PUT    "/publishers/1" '{"name":"Publisher One Edited","bookIds":[]}' 200
test_api DELETE "/publishers/1"                       ""                     204

#───────────────
# 5) CRUD для Categories
test_api GET    "/categories"                         ""                     200
test_api GET    "/categories/1"                       ""                     200
test_api GET    "/categories/1/books"                 ""                     200
test_api PUT    "/categories/1" '{"name":"Category A Edited","bookIds":[]}'   200
test_api DELETE "/categories/1"                       ""                     204

#───────────────
# 6) CRUD для Library
test_api GET    "/library"                            ""                     200
test_api GET    "/library/1"                          ""                     200
test_api PUT    "/library/1" '{"id":1,"name":"Main Branch","bookTitles":["Book 1 Updated"]}' 200
test_api POST   "/library"                            '{"name":"Branch 2","bookTitles":[]}' 201
test_api GET    "/library"                            ""                     200
test_api DELETE "/library/2"                          ""                     204

# Финал
if [[ $EXIT_CODE -ne 0 ]]; then
    echo "❌ Некоторые тесты не прошли."
    exit 1
else
    echo "✅ Все тесты пройдены!"
    exit 0
fi
