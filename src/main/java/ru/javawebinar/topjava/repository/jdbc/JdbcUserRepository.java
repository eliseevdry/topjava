package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final RowMapper<Role> ROLE_ROW_MAPPER = (rs, rowNum) -> {
        String roleName = rs.getString("role");
        return roleName == null ? null : Role.valueOf(roleName);
    };
    public static final ResultSetExtractor<User> RESULT_SET_EXTRACTOR_WITH_ROLES = rs -> {
        User user = null;
        int row = 0;
        while (rs.next()) {
            if (user == null) {
                user = ROW_MAPPER.mapRow(rs, row);
            }
            Role role = ROLE_ROW_MAPPER.mapRow(rs, row);
            if (role != null && user != null) {
                user.addRoles(role);
            }
            row++;
        }
        return user;
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        Integer id = user.getId();

        if (user.isNew()) {
            id = insertUser.executeAndReturnKey(parameterSource).intValue();
            user.setId(id);
            saveRoles(new ArrayList<>(user.getRoles()), id);
            return user;
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        deleteRoles(id);
        saveRoles(new ArrayList<>(user.getRoles()), id);

        return user;
    }

    private void saveRoles(List<Role> roleList, Integer id) {
        if (roleList.isEmpty()) {
            return;
        }
        jdbcTemplate.batchUpdate("INSERT INTO user_roles VALUES (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, id);
                ps.setString(2, roleList.get(i).name());
            }

            @Override
            public int getBatchSize() {
                return roleList.size();
            }
        });
    }

    private void deleteRoles(int id) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur ON u.id = ur.user_id WHERE id=?", RESULT_SET_EXTRACTOR_WITH_ROLES, id);
    }

    @Override
    public User getByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur ON u.id = ur.user_id WHERE email=?", RESULT_SET_EXTRACTOR_WITH_ROLES, email);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur ON u.id = ur.user_id ORDER BY u.name, u.email", rs -> {
            List<User> users = new ArrayList<>();
            Long userId = null;
            User currentUser = null;
            int userIdx = 0, roleIdx = 0;
            while (rs.next()) {
                if (currentUser == null || !userId.equals(rs.getLong("id"))) {
                    userId = rs.getLong("id");
                    currentUser = ROW_MAPPER.mapRow(rs, userIdx++);
                    roleIdx = 0;
                    users.add(currentUser);
                }
                Role role = ROLE_ROW_MAPPER.mapRow(rs, roleIdx++);
                if (role != null && currentUser != null) {
                    currentUser.addRoles(role);
                }
            }
            return users;
        });
    }
}
