package com.example.library.repository;

import com.example.library.entity.Publisher;
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
public class PublisherDao {

    private final NamedParameterJdbcTemplate npJdbc;

    public PublisherDao(NamedParameterJdbcTemplate npJdbc) {
        this.npJdbc = npJdbc;
    }

    // RowMapper для Publisher
    private final RowMapper<Publisher> publisherRowMapper = new RowMapper<>() {
        @Override
        public Publisher mapRow(ResultSet rs, int rowNum) throws SQLException {
            Publisher publisher = new Publisher();
            publisher.setId(rs.getLong("id"));
            publisher.setName(rs.getString("name"));
            return publisher;
        }
    };

    // Получить список bookIds для издателя
    private List<Long> findBookIdsByPublisherId(Long publisherId) {
        String sql = "SELECT id FROM book WHERE publisher_id = :publisherId";
        MapSqlParameterSource params = new MapSqlParameterSource("publisherId", publisherId);
        return npJdbc.queryForList(sql, params, Long.class);
    }

    public Optional<Publisher> findById(Long id) {
        String sql = "SELECT id, name FROM publisher WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        List<Publisher> list = npJdbc.query(sql, params, publisherRowMapper);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        Publisher publisher = list.get(0);
        publisher.setBookIds(findBookIdsByPublisherId(id));
        return Optional.of(publisher);
    }

    public List<Publisher> findAll() {
        String sql = "SELECT id, name FROM publisher";
        List<Publisher> list = npJdbc.query(sql, new MapSqlParameterSource(), publisherRowMapper);
        for (Publisher publisher : list) {
            publisher.setBookIds(findBookIdsByPublisherId(publisher.getId()));
        }
        return list;
    }

    public Optional<Publisher> findByName(String name) {
        String sql = "SELECT id, name FROM publisher WHERE name = :name";
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        List<Publisher> list = npJdbc.query(sql, params, publisherRowMapper);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        Publisher publisher = list.get(0);
        publisher.setBookIds(findBookIdsByPublisherId(publisher.getId()));
        return Optional.of(publisher);
    }

    public Publisher save(Publisher publisher) {
        if (publisher.getId() == null) {
            // INSERT
            String sql = "INSERT INTO publisher (name) VALUES (:name)";
            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", publisher.getName());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            npJdbc.update(sql, params, keyHolder, new String[]{"id"});
            publisher.setId(keyHolder.getKey().longValue());
        } else {
            // UPDATE
            String sql = "UPDATE publisher SET name = :name WHERE id = :id";
            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", publisher.getName())
                .addValue("id", publisher.getId());
            npJdbc.update(sql, params);
        }
        return publisher;
    }

    public int deleteById(Long id) {
        // Сначала удалить книги издателя, если нужно
        String deleteBooks = "DELETE FROM book WHERE publisher_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        npJdbc.update(deleteBooks, params);
        // Затем удалить самого издателя
        String sql = "DELETE FROM publisher WHERE id = :id";
        return npJdbc.update(sql, params);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM publisher WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        Integer count = npJdbc.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
}
