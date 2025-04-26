package com.lab03.sport.repository;

import com.lab03.sport.entity.Category;
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
public class CategoryDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Category> categoryRowMapper = new RowMapper<>() {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            Category c = new Category();
            c.setCatId(rs.getLong("cat_id"));
            c.setCatName(rs.getString("cat_name"));
            return c;
        }
    };

    public CategoryDAO(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * POST — создание категории. Возвращает сгенерированный cat_id.
     */
    public Long createCategory(Category category) {
        String sql = """
            INSERT INTO CATEGORY (cat_name)
            VALUES (:catName)
            """;
        var params = new MapSqlParameterSource()
            .addValue("catName", category.getCatName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        return keyHolder.getKey().longValue();
    }

    /**
     * PUT — обновление категории по cat_id. Возвращает количество обновлённых строк.
     */
    public int updateCategory(Category category) {
        String sql = """
            UPDATE CATEGORY
               SET cat_name = :catName
             WHERE cat_id   = :catId
            """;
        var params = new MapSqlParameterSource()
            .addValue("catName", category.getCatName())
            .addValue("catId",   category.getCatId());
        return jdbcTemplate.update(sql, params);
    }

    /**
     * GET — получение всех категорий.
     */
    public List<Category> getAllCategories() {
        String sql = "SELECT * FROM CATEGORY";
        return jdbcTemplate.query(sql, categoryRowMapper);
    }

    /**
     * GET — получение категории по ID.
     */
    public Optional<Category> getCategoryById(Long id) {
        String sql = "SELECT * FROM CATEGORY WHERE cat_id = :catId";
        var params = new MapSqlParameterSource().addValue("catId", id);
        List<Category> list = jdbcTemplate.query(sql, params, categoryRowMapper);
        return list.stream().findFirst();
    }

    /**
     * DELETE — удаление категории по ID. Возвращает количество удалённых строк.
     */
    public int deleteCategoryById(Long id) {
        String sql = "DELETE FROM CATEGORY WHERE cat_id = :catId";
        var params = new MapSqlParameterSource().addValue("catId", id);
        return jdbcTemplate.update(sql, params);
    }
}
