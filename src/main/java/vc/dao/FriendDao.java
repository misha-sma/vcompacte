package vc.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class FriendDao {
	public static final String FRIENDS_COUNT_SQL = "SELECT COUNT(1) FROM friends AS f1 INNER JOIN friends AS f2 ON "
			+ "f1.id_user_1=? AND f1.id_user_2=f2.id_user_1 AND f2.id_user_2=?";

	@Autowired
	private EntityManager entityManager;

	public int getFriendsCount(Long idUser) {
		Integer friendsCount = (Integer) entityManager.createNativeQuery(FRIENDS_COUNT_SQL).setParameter(1, idUser)
				.setParameter(2, idUser).getSingleResult();
		return friendsCount == null ? 0 : friendsCount;
	}
}
