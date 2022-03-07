package ma.octo.aop.repository.impl;

import ma.octo.aop.entity.Language;
import ma.octo.aop.repository.LanguageRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class LanguageRepositoryJDBCImpl implements LanguageRepository {

    private JdbcTemplate jdbcTemplate;
    public LanguageRepositoryJDBCImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate =jdbcTemplate;
    }



    @Override
    public Optional<Language> findByExtension(String extension) {
        String sql = "SELECT * FROM language WHERE fileExtension = ?";
        Language language = null;
        try {
            language = jdbcTemplate.queryForObject(sql, new Object[]{extension},   (resultSet, i) ->toLanguage(resultSet));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(language);
    }

    @Override
    public Optional<Language> findById(String id) {
        String sql = "SELECT * FROM language WHERE id = ?";
        Language language = null;
        try {
            language = jdbcTemplate.queryForObject(sql, new Object[]{id},   (resultSet, i) ->toLanguage(resultSet));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(language);
    }

    @Override
    public List<Language> findAll() {
        String sql = "SELECT * FROM language";
        return jdbcTemplate.query(sql,   (resultSet, i) ->toLanguage(resultSet));
    }

    @Override
    public void save(Language entity) {
        String sql = "INSERT INTO language (id, name, author, fileExtension) VALUES (?, ?, ?, ?)";
        int insert = jdbcTemplate.update(sql, entity.getId(), entity.getName(), entity.getAuthor(), entity.getFileExtension());
        System.out.println("saved" + insert);
    }
    private Language toLanguage(ResultSet resultSet) throws SQLException {
        return new Language(resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("author"), resultSet.getString("fileExtension"));

    }
}
