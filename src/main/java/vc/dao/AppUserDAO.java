package vc.dao;
 
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
 
import vc.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
 
@Repository
@Transactional
public class AppUserDAO {
 
    @Autowired
    private EntityManager entityManager;
 
    public AppUser findUserAccount(String userName) {
        try {
            String sql = "Select e from " + AppUser.class.getName() + " e " //
                    + " Where e.userName = :userName ";
 
            Query query = entityManager.createQuery(sql, AppUser.class);
            query.setParameter("userName", userName);
 
            return (AppUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
 
    public AppUser findUserById(Long id) {
        try {
        	
        	return entityManager.find(AppUser.class, id);
        	
//            String sql = "Select e from " + AppUser.class.getName() + " e " //
//                    + " Where e.userName = :userName ";
// 
//            Query query = entityManager.createQuery(sql, AppUser.class);
//            query.setParameter("userName", userName);
// 
//            return (AppUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}