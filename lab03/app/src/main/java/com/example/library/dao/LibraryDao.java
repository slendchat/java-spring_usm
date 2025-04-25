package com.example.library.repository;

import com.example.library.entity.Library;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class LibraryDao {

    private final NamedParameterJdbcTemplate npJdbc;

    public LibraryDao(NamedParameterJdbcTemplate npJdbc) {
        this.npJdbc = npJdbc;
    }

    // Маппер для Library (id, name)
    private final RowMapper<Library> libraryRowMapper = new RowMapper<>() {
        @Override
        public Library mapRow(ResultSet rs, int rowNum) throws SQLException {
            Library lib = new Library();
            lib.setId(rs.getLong("id"));
            lib.setName(rs.getString("name"));
            return lib;
        }
    };

    // Вспомогательный метод: получить список заголовков книг из book.library_id
    private List<String> findBookTitlesByLibraryId(Long libraryId) {
        String sql = "SELECT title FROM book WHERE library_id = :libraryId";
        return npJdbc.queryForList(sql,
                new MapSqlParameterSource("libraryId", libraryId),
                String.class);
    }

    /** Найти библиотеку по ID, вместе с заголовками книг */
    public Optional<Library> findById(Long id) {
        String sql = "SELECT id, name FROM library WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        List<Library> list = npJdbc.query(sql, params, libraryRowMapper);
        if (list.isEmpty()) return Optional.empty();
        Library lib = list.get(0);
        lib.setBookTitles(findBookTitlesByLibraryId(id));
        return Optional.of(lib);
    }

    /** Вернуть все библиотеки, каждая с заголовками своих книг */
    public List<Library> findAll() {
        String sql = "SELECT id, name FROM library";
        List<Library> list = npJdbc.query(sql, new MapSqlParameterSource(), libraryRowMapper);
        for (Library lib : list) {
            lib.setBookTitles(findBookTitlesByLibraryId(lib.getId()));
        }
        return list;
    }

    /** Сохранить или обновить библиотеку (имя). Книги привязываются в BookService. */
    public Library save(Library library) {
        if (library.getId() == null) {
            // INSERT новой библиотеки
            String sql = "INSERT INTO library (name) VALUES (:name)";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("name", library.getName());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            npJdbc.update(sql, params, keyHolder, new String[]{"id"});
            library.setId(keyHolder.getKey().longValue());
        } else {
            // UPDATE существующей библиотеки
            String sql = "UPDATE library SET name = :name WHERE id = :id";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("name", library.getName())
                    .addValue("id", library.getId());
            npJdbc.update(sql, params);
        }
        return library;
    }

    /** Удалить библиотеку по ID и обнулить поле library_id у связанных книг */
    public int deleteById(Long id) {
        // Снять привязку у книг
        String clearBooks = "UPDATE book SET library_id = NULL WHERE library_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        npJdbc.update(clearBooks, params);
        // Удалить саму библиотеку
        String sql = "DELETE FROM library WHERE id = :id";
        return npJdbc.update(sql, params);
    }
}
