package edu.oakland.arttour.dao;

import edu.oakland.arttour.model.Creator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CreatorDAO {

  @Autowired private JdbcTemplate jdbcTemplate;

  public void addCreator(Creator creator) {
    jdbcTemplate.update(
        Constants.ADD_CREATOR,
        creator.getCreatorId(),
        creator.getFullName(),
        creator.getCitedName(),
        creator.getRole(),
        creator.getNationality(),
        creator.getBirthDate(),
        creator.getDeathDate(),
        creator.getBirthPlace(),
        creator.getDeathPlace());
  }

  public void updateCreator(Creator creator) throws DataAccessException {
    jdbcTemplate.update(
        Constants.UPDATE_CREATOR,
        creator.getCreatorId(),
        creator.getFullName(),
        creator.getCitedName(),
        creator.getRole(),
        creator.getNationality(),
        creator.getBirthDate(),
        creator.getDeathDate(),
        creator.getBirthPlace(),
        creator.getDeathPlace());
  }

  public Creator getCreatorById(String creatorId) throws EmptyResultDataAccessException {
    return jdbcTemplate.queryForObject(Constants.GET_CREATOR, Creator.mapper, creatorId);
  }

  public void deleteCreator(String creatorId) throws DataIntegrityViolationException {
    jdbcTemplate.update(Constants.DELETE_CREATOR, creatorId);
  }
}
