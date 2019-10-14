package vc.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import vc.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserDao {
	public static final String USER_BY_EMAIL_SQL = "SELECT e FROM " + User.class.getName() + " e "
			+ " WHERE e.email = :email";

	@Autowired
	private EntityManager entityManager;

	public User getUserByEmail(String email) {
		try {
			Query query = entityManager.createQuery(USER_BY_EMAIL_SQL, User.class);
			query.setParameter("email", email);
			return (User) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public User getUserById(Long idUser) {
		try {
			return entityManager.find(User.class, idUser);
		} catch (NoResultException e) {
			return null;
		}
	}
}