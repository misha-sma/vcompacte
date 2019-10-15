package vc.service;

import java.util.ArrayList;
import java.util.List;

import vc.dao.UserDao;
import vc.entity.User;
import vc.dao.RoleDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private UserDao userDAO;

	@Autowired
	private RoleDao roleDAO;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		userName = userName == null ? null : userName.toLowerCase();
		User user = userDAO.getUserByEmail(userName);
		if (user == null) {
			logger.error("User not found! " + userName);
			throw new UsernameNotFoundException("User " + userName + " was not found in the database");
		}
		logger.info("Found User by email: email=" + userName + " id_user=" + user.getIdUser());
		List<String> roleNames = roleDAO.getRoleNames(user.getIdUser());
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			for (String role : roleNames) {
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				grantList.add(authority);
			}
		}
		UserDetails userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(userName,
				user.getPassword(), grantList);
		return userDetails;
	}

}