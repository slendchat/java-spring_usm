#!/usr/bin/env python3
import requests

BASE_URL = 'http://localhost:8080'  # поменяй, если приложение на другом хосте/порту

def print_step(step):
    print(f'\n=== {step} ===')

def test_libraries():
    print_step('Libraries')
    # 1. создать библиотеку lib1
    r = requests.post(f'{BASE_URL}/libraries', params={'libName': 'lib1'})
    lib1 = r.json(); print('Created lib1:', lib1)
    lib1_id = lib1['libId']

    # 2. создать библиотеку lib2
    r = requests.post(f'{BASE_URL}/libraries', params={'libName': 'lib2'})
    lib2 = r.json(); print('Created lib2:', lib2)
    lib2_id = lib2['libId']

    # 3. переименовать lib2 → lib_test
    r = requests.put(f'{BASE_URL}/libraries/{lib2_id}', params={'libName': 'lib_test'})
    lib_test = r.json(); print('Renamed lib2 → lib_test:', lib_test)
    lib_test_id = lib_test['libId']

    # 4. получить все библиотеки
    r = requests.get(f'{BASE_URL}/libraries')
    print('All libraries:', r.json())

    # 5. получить библиотеку по айди
    r = requests.get(f'{BASE_URL}/libraries/{lib_test_id}')
    print(f'Library {lib_test_id}:', r.json())

    # 6. удалить библиотеку lib_test
    r = requests.delete(f'{BASE_URL}/libraries/{lib_test_id}')
    print(f'Deleted library {lib_test_id}, status:', r.status_code)


def test_authors():
    print_step('Authors')
    # POST
    r = requests.post(f'{BASE_URL}/authors', params={'authName': 'author1'})
    a1 = r.json(); print('Created author1:', a1)
    a1_id = a1['authId']

    r = requests.post(f'{BASE_URL}/authors', params={'authName': 'author2'})
    a2 = r.json(); print('Created author2:', a2)
    a2_id = a2['authId']

    # PUT
    r = requests.put(f'{BASE_URL}/authors/{a2_id}', params={'authName': 'author_test'})
    at2 = r.json(); print('Renamed author2 → author_test:', at2)
    at2_id = at2['authId']

    # GET all
    r = requests.get(f'{BASE_URL}/authors')
    print('All authors:', r.json())

    # GET by id
    r = requests.get(f'{BASE_URL}/authors/{at2_id}')
    print(f'Author {at2_id}:', r.json())

    # DELETE
    r = requests.delete(f'{BASE_URL}/authors/{at2_id}')
    print(f'Deleted author {at2_id}, status:', r.status_code)


def test_categories():
    print_step('Categories')
    r = requests.post(f'{BASE_URL}/categories', params={'catName': 'cat1'})
    c1 = r.json(); print('Created cat1:', c1)
    c1_id = c1['catId']

    r = requests.post(f'{BASE_URL}/categories', params={'catName': 'cat2'})
    c2 = r.json(); print('Created cat2:', c2)
    c2_id = c2['catId']

    r = requests.put(f'{BASE_URL}/categories/{c2_id}', params={'catName': 'cat_test'})
    ct2 = r.json(); print('Renamed cat2 → cat_test:', ct2)
    ct2_id = ct2['catId']

    r = requests.get(f'{BASE_URL}/categories')
    print('All categories:', r.json())

    r = requests.get(f'{BASE_URL}/categories/{ct2_id}')
    print(f'Category {ct2_id}:', r.json())

    r = requests.delete(f'{BASE_URL}/categories/{ct2_id}')
    print(f'Deleted category {ct2_id}, status:', r.status_code)


def test_publishers():
    print_step('Publishers')
    r = requests.post(f'{BASE_URL}/publishers', params={'pubName': 'pub1'})
    p1 = r.json(); print('Created pub1:', p1)
    p1_id = p1['pubId']

    r = requests.post(f'{BASE_URL}/publishers', params={'pubName': 'pub2'})
    p2 = r.json(); print('Created pub2:', p2)
    p2_id = p2['pubId']

    r = requests.put(f'{BASE_URL}/publishers/{p2_id}', params={'pubName': 'pub_test'})
    pt2 = r.json(); print('Renamed pub2 → pub_test:', pt2)
    pt2_id = pt2['pubId']

    r = requests.get(f'{BASE_URL}/publishers')
    print('All publishers:', r.json())

    r = requests.get(f'{BASE_URL}/publishers/{pt2_id}')
    print(f'Publisher {pt2_id}:', r.json())

    r = requests.delete(f'{BASE_URL}/publishers/{pt2_id}')
    print(f'Deleted publisher {pt2_id}, status:', r.status_code)


def test_books():
    print_step('Books')
    # Для книг нужны существующие author, publisher, category, library
    # создаём по одному каждого
    a = requests.post(f'{BASE_URL}/authors', params={'authName': 'b_author'}).json(); a_id = a['authId']
    p = requests.post(f'{BASE_URL}/publishers', params={'pubName': 'b_publisher'}).json(); p_id = p['pubId']
    c = requests.post(f'{BASE_URL}/categories', params={'catName': 'b_category'}).json(); c_id = c['catId']
    l = requests.post(f'{BASE_URL}/libraries', params={'libName': 'b_library'}).json(); l_id = l['libId']

    # POST книги
    r = requests.post(
        f'{BASE_URL}/books',
        params={
            'authorId': a_id,
            'publisherId': p_id,
            'categoryId': c_id,
            'libraryId': l_id,
            'bookName': 'book1'
        }
    )
    b1 = r.json(); print('Created book1:', b1)
    b1_id = b1['bookId']

    r = requests.post(
        f'{BASE_URL}/books',
        params={
            'authorId': a_id,
            'publisherId': p_id,
            'categoryId': c_id,
            'libraryId': l_id,
            'bookName': 'book2'
        }
    )
    b2 = r.json(); print('Created book2:', b2)
    b2_id = b2['bookId']

    # PUT book2 → book_test
    r = requests.put(
        f'{BASE_URL}/books/{b2_id}',
        params={
            'authorId': a_id,
            'publisherId': p_id,
            'categoryId': c_id,
            'libraryId': l_id,
            'bookName': 'book_test'
        }
    )
    bt2 = r.json(); print('Renamed book2 → book_test:', bt2)

    # GET all books
    r = requests.get(f'{BASE_URL}/books')
    print('All books:', r.json())

    # GET by ID
    r = requests.get(f'{BASE_URL}/books/{b2_id}')
    print(f'Book {b2_id}:', r.json())

    # DELETE book_test
    r = requests.delete(f'{BASE_URL}/books/{b2_id}')
    print(f'Deleted book {b2_id}, status:', r.status_code)


if __name__ == '__main__':
    test_libraries()
    test_authors()
    test_categories()
    test_publishers()
    test_books()
