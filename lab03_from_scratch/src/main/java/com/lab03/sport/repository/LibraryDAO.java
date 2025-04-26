// src/main/java/com/lab03/sport/repository/LibraryDAO.java
package com.lab03.sport.repository;

import com.lab03.sport.entity.Library;
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
public class LibraryDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Library> libraryRowMapper = (ResultSet rs, int rowNum) -> {
        Library lib = new Library();
        lib.setLibId(rs.getLong("lib_id"));
        lib.setLibName(rs.getString("lib_name"));
        return lib;
    };

    public LibraryDAO(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long createLibrary(Library library) {
        String sql = "INSERT INTO LIBRARY (lib_name) VALUES (:libName)";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("libName", library.getLibName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public int updateLibrary(Library library) {
        String sql = "UPDATE LIBRARY SET lib_name = :libName WHERE lib_id = :libId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("libName", library.getLibName())
            .addValue("libId", library.getLibId());
        return jdbcTemplate.update(sql, params);
    }

    public List<Library> getAllLibraries() {
        String sql = "SELECT lib_id, lib_name FROM LIBRARY";
        return jdbcTemplate.query(sql, libraryRowMapper);
    }

    public Optional<Library> getLibraryById(Long id) {
        String sql = "SELECT lib_id, lib_name FROM LIBRARY WHERE lib_id = :libId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("libId", id);
        List<Library> list = jdbcTemplate.query(sql, params, libraryRowMapper);
        return list.stream().findFirst();
    }

    public int deleteLibraryById(Long id) {
        String sql = "DELETE FROM LIBRARY WHERE lib_id = :libId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("libId", id);
        return jdbcTemplate.update(sql, params);
    }
}
