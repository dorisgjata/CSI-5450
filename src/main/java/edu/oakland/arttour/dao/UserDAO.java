package edu.oakland.arttour.dao;

import edu.oakland.arttour.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public void registerUser(String email, String fname, String lname, String password) {
    jdbcTemplate.update(Constants.REGISTER_USER, new Object[] { email, fname, lname, password });
  }

  public void addConsumer(String email) {
    jdbcTemplate.update(Constants.ADD_CONSUMER, email);
  }

  public void addAdmin(String email) {
    jdbcTemplate.update(Constants.ADD_ADMIN, email);
  }

  // todo: should probably return a boolean
  public void checkAdmin(String email) throws DataAccessException {
    jdbcTemplate.queryForObject(Constants.CHECK_ADMIN, String.class, email);
  }

  public String userExists(String email) throws DataAccessException {
    return jdbcTemplate.queryForObject(Constants.USER_EXISTS, String.class, email);
  }

  public User login(String email) throws DataAccessException {
    return jdbcTemplate.queryForObject(Constants.LOGIN, User.mapper, email);
  }

  public List<String> getAdminEmails() throws DataAccessException {
    return jdbcTemplate.queryForList(Constants.GET_ADMIN_EMAILS, String.class);
  }

  public void updateUser(String email, String fname, String lname, String password) {
    jdbcTemplate.update(Constants.UPDATE_USER, new Object[] { email, fname, lname, password });
  }

  public void deleteUser(String email) {
    jdbcTemplate.update(Constants.DELETE_USER, email);
  }

  // todo: add delete admin

}
