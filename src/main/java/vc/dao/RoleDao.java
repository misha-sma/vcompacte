package vc.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import vc.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RoleDao {
	public static final String ROLE_NAMES_SQL = "SELECT ur.role.roleName FROM " + UserRole.class.getName() + " ur "
			+ " WHERE ur.user.idUser = :idUser";

	@Autowired
	private EntityManager entityManager;

	public List<String> getRoleNames(Long idUser) {
		Query query = entityManager.createQuery(ROLE_NAMES_SQL, String.class);
		query.setParameter("idUser", idUser);
		return query.getResultList();
	}

}