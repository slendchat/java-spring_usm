package com.lab03.sport.repository;

import com.lab03.sport.entity.Publisher;
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
public class PublisherDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Publisher> publisherRowMapper = new RowMapper<>() {
        @Override
        public Publisher mapRow(ResultSet rs, int rowNum) throws SQLException {
            Publisher p = new Publisher();
            p.setPubId(rs.getLong("pub_id"));
            p.setPubName(rs.getString("pub_name"));
            return p;
        }
    };

    public PublisherDAO(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * POST — создание издателя. Возвращает сгенерированный pub_id.
     */
    public Long createPublisher(Publisher publisher) {
        String sql = """
            INSERT INTO PUBLISHER (pub_name)
            VALUES (:pubName)
            """;
        var params = new MapSqlParameterSource()
            .addValue("pubName", publisher.getPubName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        return keyHolder.getKey().longValue();
    }

    /**
     * PUT — обновление издателя по pub_id. Возвращает количество обновлённых строк.
     */
    public int updatePublisher(Publisher publisher) {
        String sql = """
            UPDATE PUBLISHER
               SET pub_name = :pubName
             WHERE pub_id   = :pubId
            """;
        var params = new MapSqlParameterSource()
            .addValue("pubName", publisher.getPubName())
            .addValue("pubId", publisher.getPubId());
        return jdbcTemplate.update(sql, params);
    }

    /**
     * GET — получение всех издателей.
     */
    public List<Publisher> getAllPublishers() {
        String sql = "SELECT * FROM PUBLISHER";
        return jdbcTemplate.query(sql, publisherRowMapper);
    }

    /**
     * GET — получение издателя по ID.
     */
    public Optional<Publisher> getPublisherById(Long id) {
        String sql = "SELECT * FROM PUBLISHER WHERE pub_id = :pubId";
        var params = new MapSqlParameterSource().addValue("pubId", id);
        List<Publisher> list = jdbcTemplate.query(sql, params, publisherRowMapper);
        return list.stream().findFirst();
    }

    /**
     * DELETE — удаление издателя по ID. Возвращает количество удалённых строк.
     */
    public int deletePublisherById(Long id) {
        String sql = "DELETE FROM PUBLISHER WHERE pub_id = :pubId";
        var params = new MapSqlParameterSource().addValue("pubId", id);
        return jdbcTemplate.update(sql, params);
    }
}
