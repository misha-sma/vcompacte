package vc.controller;

import java.security.Principal;

import vc.dao.UserDao;
import vc.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public String welcomePage(Model model) {
		model.addAttribute("title", "Welcome");
		model.addAttribute("message", "This is welcome page!");
		return "loginPage";
	}

	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public ResponseEntity<String> errorPage(Model model) {
		return new ResponseEntity<String>("<html><body>Error page 500</body></html>", HttpStatus.OK);
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(Model model) {
		return "loginPage";
	}

	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logoutSuccessfulPage(Model model) {
		model.addAttribute("title", "Logout");
		return "logoutSuccessfulPage";
	}

	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {
		// After user login successfully.
		String userName = principal.getName();
		User user = userDao.getUserByEmail(userName);
		logger.info("User Name: " + userName);
		return "redirect:/user?id=" + user.getIdUser();
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ResponseEntity<String> userPage(@RequestParam Long id, Principal principal) {
		String userName = principal.getName();
		User user = userDao.getUserByEmail(userName);
		if (user.getIdUser().equals(id)) {
			return new ResponseEntity<String>("<html><body>" + user.getEmail() + " owner Page</body></html>",
					HttpStatus.OK);
		}
		User user2 = userDao.getUserById(id);
		return new ResponseEntity<String>("<html><body>" + user2.getEmail() + " Page</body></html>", HttpStatus.OK);
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