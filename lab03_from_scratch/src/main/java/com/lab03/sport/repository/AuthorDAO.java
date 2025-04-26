package com.lab03.sport.repository;

import com.lab03.sport.entity.Author;
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
public class AuthorDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Author> authorRowMapper = new RowMapper<>() {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author a = new Author();
            a.setAuthId(rs.getLong("auth_id"));
            a.setAuthName(rs.getString("auth_name"));
            return a;
        }
    };

    public AuthorDAO(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * POST — создание автора. Возвращает сгенерированный auth_id.
     */
    public Long createAuthor(Author author) {
        String sql = """
            INSERT INTO AUTHOR (auth_name)
            VALUES (:authName)
            """;
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("authName", author.getAuthName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        return keyHolder.getKey().longValue();
    }

    /**
     * PUT — обновление автора по auth_id. Возвращает количество обновлённых строк.
     */
    public int updateAuthor(Author author) {
        String sql = """
            UPDATE AUTHOR
               SET auth_name = :authName
             WHERE auth_id   = :authId
            """;
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("authName", author.getAuthName())
            .addValue("authId",   author.getAuthId());
        return jdbcTemplate.update(sql, params);
    }

    /**
     * GET — получение всех авторов.
     */
    public List<Author> getAllAuthors() {
        String sql = "SELECT * FROM AUTHOR";
        return jdbcTemplate.query(sql, authorRowMapper);
    }

    /**
     * GET — получение автора по ID.
     */
    public Optional<Author> getAuthorById(Long id) {
        String sql = "SELECT * FROM AUTHOR WHERE auth_id = :authId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("authId", id);
        List<Author> list = jdbcTemplate.query(sql, params, authorRowMapper);
        return list.stream().findFirst();
    }

    /**
     * DELETE — удаление автора по ID. Возвращает количество удалённых строк.
     */
    public int deleteAuthorById(Long id) {
        String sql = "DELETE FROM AUTHOR WHERE auth_id = :authId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("authId", id);
        return jdbcTemplate.update(sql, params);
    }
}
