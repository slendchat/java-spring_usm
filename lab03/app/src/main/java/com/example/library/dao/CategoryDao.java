package com.example.library.repository;

import com.example.library.entity.Category;
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
public class CategoryDao {

    private final NamedParameterJdbcTemplate npJdbc;

    public CategoryDao(NamedParameterJdbcTemplate npJdbc) {
        this.npJdbc = npJdbc;
    }

    // Маппит id и name
    private final RowMapper<Category> categoryRowMapper = new RowMapper<>() {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            Category category = new Category();
            category.setId(rs.getLong("id"));
            category.setName(rs.getString("name"));
            return category;
        }
    };

    // Получить все bookIds для категории
    private List<Long> findBookIdsByCategoryId(Long categoryId) {
        String sql = "SELECT book_id FROM book_category WHERE category_id = :categoryId";
        return npJdbc.queryForList(sql,
            new MapSqlParameterSource("categoryId", categoryId),
            Long.class
        );
    }

    public Optional<Category> findById(Long id) {
        String sql = "SELECT id, name FROM category WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        List<Category> list = npJdbc.query(sql, params, categoryRowMapper);
        if (list.isEmpty()) return Optional.empty();
        Category category = list.get(0);
        category.setBookIds(findBookIdsByCategoryId(id));
        return Optional.of(category);
    }

    public List<Category> findAll() {
        String sql = "SELECT id, name FROM category";
        List<Category> list = npJdbc.query(sql, new MapSqlParameterSource(), categoryRowMapper);
        for (Category category : list) {
            category.setBookIds(findBookIdsByCategoryId(category.getId()));
        }
        return list;
    }

    public Optional<Category> findByName(String name) {
        String sql = "SELECT id, name FROM category WHERE name = :name";
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        List<Category> list = npJdbc.query(sql, params, categoryRowMapper);
        if (list.isEmpty()) return Optional.empty();
        Category category = list.get(0);
        category.setBookIds(findBookIdsByCategoryId(category.getId()));
        return Optional.of(category);
    }

    public Category save(Category category) {
        if (category.getId() == null) {
            // INSERT новой категории
            String sql = "INSERT INTO category(name) VALUES(:name)";
            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", category.getName());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            npJdbc.update(sql, params, keyHolder, new String[]{"id"});
            category.setId(keyHolder.getKey().longValue());
        } else {
            // UPDATE существующей категории
            String sql = "UPDATE category SET name = :name WHERE id = :id";
            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", category.getName())
                .addValue("id", category.getId());
            npJdbc.update(sql, params);
        }
        return category;
    }

    public int deleteById(Long id) {
        // Сначала удалить связи в промежуточной таблице
        String relSql = "DELETE FROM book_category WHERE category_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        npJdbc.update(relSql, params);
        // Затем удалить саму категорию
        String sql = "DELETE FROM category WHERE id = :id";
        return npJdbc.update(sql, params);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM category WHERE id = :id";
        Integer count = npJdbc.queryForObject(
            sql,
            new MapSqlParameterSource("id", id),
            Integer.class
        );
        return count != null && count > 0;
    }
}
