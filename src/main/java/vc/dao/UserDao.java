package vc.dao;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import vc.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserDao {
	private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

	public static final String USER_BY_EMAIL_SQL = "SELECT e FROM " + User.class.getName() + " e "
			+ " WHERE e.email = :email";
	public static final String INSERT_USER_SQL = "INSERT INTO users(email, password) VALUES(?, ?) RETURNING id_user";
	public static final String UPDATE_USER_SQL = "UPDATE users SET name=?, surname=?, birthday=?, phone=? WHERE id_user=?";

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
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public long addUser(String email, String password) {
		return ((BigInteger) entityManager.createNativeQuery(INSERT_USER_SQL).setParameter(1, email)
				.setParameter(2, password).getSingleResult()).longValueExact();
	}

	public void updateUser(User user) {
		entityManager.createNativeQuery(UPDATE_USER_SQL).setParameter(1, user.getName())
				.setParameter(2, user.getSurname()).setParameter(3, user.getBirthday()).setParameter(4, user.getPhone())
				.setParameter(5, user.getIdUser()).executeUpdate();
	}

}