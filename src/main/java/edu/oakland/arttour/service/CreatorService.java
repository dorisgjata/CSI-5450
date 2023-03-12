package edu.oakland.arttour.service;

import edu.oakland.arttour.dao.CreatorDAO;
import edu.oakland.arttour.model.Creator;
import edu.oakland.arttour.util.UniqueIdGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CreatorService {

    @Autowired
    private CreatorDAO dao;

    public void addCreator(Creator creator) {
        creator.setCreatorId(UniqueIdGenerator.generateUniqueId("CR-"));
        dao.addCreator(creator);
    }

    public void updateCreator(Creator updatedCreator) throws Exception {
        Creator currentCreator = getCreatorById(updatedCreator.getCreatorId());
        if (currentCreator == null) {
            throw new Exception("Creator with ID " + updatedCreator.getCreatorId() + " not found");
        }

        currentCreator.setCreatorId(updatedCreator.getCreatorId());
        currentCreator.setFullName(updatedCreator.getFullName());
        currentCreator.setCitedName(updatedCreator.getCitedName());
        currentCreator.setRole(updatedCreator.getRole());
        currentCreator.setNationality(updatedCreator.getNationality());
        currentCreator.setBirthDate(updatedCreator.getBirthDate());
        currentCreator.setDeathDate(updatedCreator.getDeathDate());
        currentCreator.setBirthPlace(updatedCreator.getBirthPlace());
        currentCreator.setDeathPlace(updatedCreator.getDeathPlace());

        try {
            dao.updateCreator(currentCreator);
        } catch (DataAccessException e) {
            e.printStackTrace();
            System.out.println("Creator with ID " + currentCreator.getCreatorId() + " failed to update");
        }
    }

    public Creator getCreatorById(String creatorId) {
        try {
            return dao.getCreatorById(creatorId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            System.out.println("Creator with ID " + creatorId + " not found");
            return null;
        }
    }

    public void deleteCreator(String creatorId) {
        dao.deleteCreator(creatorId);
    }

}
