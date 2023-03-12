package edu.oakland.arttour.service;

import edu.oakland.arttour.dao.UserDAO;
import edu.oakland.arttour.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired private UserDAO dao;

  @Value("${org.apereo.portal.soffit.jwt.signatureKey}")
  private String SECRET;

  public Map<String, String> registerUser(Map<String, String> user) {
    String email = user.get("email");
    String fname = user.get("fname");
    String lname = user.get("lname");
    String password = user.get("password");

    Map<String, String> userInfo = new HashMap<>();
    if (checkUser(email) == null) {
      dao.registerUser(email, fname, lname, password);
      dao.addConsumer(email);

      String jws =
          Jwts.builder()
              .setIssuer("Soffit")
              .claim("email", email)
              .signWith(SignatureAlgorithm.HS256, SECRET)
              .compact();
      userInfo.put("token", jws);
      userInfo.put("email", email);
      userInfo.put("fname", fname);
      userInfo.put("lname", lname);
      userInfo.put("password", password);
      return userInfo;
    }

    userInfo.put("token", null);
    userInfo.put("email", null);
    userInfo.put("fname", null);
    userInfo.put("lname", null);
    userInfo.put("password", null);
    return userInfo;
  }

  public void addConsumer(String email) {
    dao.addConsumer(email);
  }

  public void addAdmin(String email) {
    dao.addAdmin(email);
  }

  public void checkAdmin(String email) throws EmptyResultDataAccessException {
    dao.checkAdmin(email);
  }

  public String checkUser(String email) {
    try {
      return dao.userExists(email);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  public Map<String, String> login(String email) {
    Map<String, String> loginInfo = new HashMap<>();
    try {
      User user = dao.login(email);
      String jws =
          Jwts.builder()
              .setIssuer("Soffit")
              .claim("email", user.getEmail())
              .signWith(SignatureAlgorithm.HS256, SECRET)
              .compact();
      loginInfo.put("token", jws);
      loginInfo.put("email", user.getEmail());
      loginInfo.put("fname", user.getFname());
      loginInfo.put("lname", user.getLname());
      loginInfo.put("password", user.getPassword());
      return loginInfo;
    } catch (EmptyResultDataAccessException e) {
      return loginInfo;
    }
  }

  public List<String> getAdminEmails() {
    // todo: check permission
    return dao.getAdminEmails();
  }

  public void updateUser(String email, Map<String, String> user) {
    String fname = user.get("fname");
    String lname = user.get("lname");
    String password = user.get("password");

    dao.updateUser(email, fname, lname, password);
  }

  public void deleteUser(String email) {
    dao.deleteUser(email);
  }
}
