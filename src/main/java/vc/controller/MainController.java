package vc.controller;

import java.security.Principal;

import vc.dao.RoleDao;
import vc.dao.UserDao;
import vc.entity.User;
import vc.model.OnlineUsers;
import vc.renderer.UserPageRenderer;
import vc.utils.DateUtil;
import vc.utils.EncrytedPasswordUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcomePage(Principal principal) {
		if (principal == null) {
			return "loginPage";
		}
		String userName = principal.getName();
		User user = userDao.getUserByEmail(userName);
		return "redirect:/user?id=" + user.getIdUser();
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		return "loginPage";
	}

	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logoutSuccessfulPage(Model model) {
		model.addAttribute("title", "Logout");
		return "logoutSuccessfulPage";
	}

	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public String userInfo(Principal principal) {
		// After user login successfully.
		String userName = principal.getName();
		User user = userDao.getUserByEmail(userName);
		logger.info("User Name: " + userName);
		return "redirect:/user?id=" + user.getIdUser();
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String createAccount(Principal principal) {
		if (principal == null) {
			return "register";
		}
		String userName = principal.getName();
		User user = userDao.getUserByEmail(userName);
		return "redirect:/user?id=" + user.getIdUser();
	}

	@RequestMapping(value = "/create_account", method = RequestMethod.POST)
	public ResponseEntity<String> createAccount2(@RequestParam String email, @RequestParam String password,
			@RequestParam String password2, Principal principal) {
		if (principal != null) {
			String userName = principal.getName();
			User user = userDao.getUserByEmail(userName);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", "/user?id=" + user.getIdUser());
			return new ResponseEntity<String>(headers, HttpStatus.FOUND);
		}
		if (password == null || password.length() < 8) {
			return new ResponseEntity<String>(
					"<html><body>Пароль должен состоять как минимум из 8 символов</body></html>", HttpStatus.OK);
		}
		if (!password.equals(password2)) {
			return new ResponseEntity<String>("<html><body>Пароли не совпадают</body></html>", HttpStatus.OK);
		}
		if (email == null || email.isEmpty()) {
			return new ResponseEntity<String>("<html><body>Email невалидный</body></html>", HttpStatus.OK);
		}
		email = email.toLowerCase();
		User user = userDao.getUserByEmail(email);
		if (user == null) {
			// создание юзера
			long idUser = userDao.addUser(email, EncrytedPasswordUtils.encrytePassword(password));
			logger.info("Create user idUser=" + idUser + " email=" + email);
			roleDao.addUserRole(idUser);
			return new ResponseEntity<String>(
					"<html><body>Вы успешно зарегистрировались!!!<br><a href=\"/login\">Войти</a></body></html>",
					HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("<html><body>Этот email уже занят</body></html>", HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ResponseEntity<String> userPage(@RequestParam Long id, Principal principal) {
		String userName = principal.getName();
		OnlineUsers.addUser(userName);
		User user = userDao.getUserByEmail(userName);
		if (user.getName() == null || user.getName().isEmpty() || user.getSurname() == null
				|| user.getSurname().isEmpty()) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Location", "/add_user_info");
			return new ResponseEntity<String>(headers, HttpStatus.FOUND);
		}
		User user2 = userDao.getUserById(id);
		String html = UserPageRenderer.renderUserPage(user2, user.getIdUser().equals(id), user.getIdUser());
		return new ResponseEntity<String>(html, HttpStatus.OK);
	}

	@RequestMapping(value = "/add_user_info", method = RequestMethod.GET)
	public String addUserInfo(Principal principal) {
		String userName = principal.getName();
		User user = userDao.getUserByEmail(userName);
		if (user.getName() != null && !user.getName().isEmpty() && user.getSurname() != null
				&& !user.getSurname().isEmpty()) {
			return "redirect:/user?id=" + user.getIdUser();
		}
		return "addUserInfo";
	}

	@RequestMapping(value = "/save_user_info", method = RequestMethod.POST)
	public String createAccount2(@RequestParam String name, @RequestParam String surname, @RequestParam String phone,
			@RequestParam String birthday, Principal principal) {
		String userName = principal.getName();
		User user = userDao.getUserByEmail(userName);
		user.setName(name.trim());
		user.setSurname(surname.trim());
		user.setPhone(Long.parseLong(phone.trim().replace("+", "")));
		user.setBirthday(DateUtil.string2Date(birthday));
		userDao.updateUser(user);
		return "redirect:/user?id=" + user.getIdUser();
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {
		if (principal != null) {
			String userName = principal.getName();
			model.addAttribute("userInfo", userName);
			String message = "Hi " + userName + "<br> You do not have permission to access this page!";
			model.addAttribute("message", message);
		}
		return "403Page";
	}

}