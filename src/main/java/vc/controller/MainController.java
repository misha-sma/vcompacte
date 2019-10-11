package vc.controller;
 
import java.security.Principal;

import javax.persistence.EntityManager;

import vc.dao.AppUserDAO;
import vc.entity.AppUser;
import vc.utils.WebUtils;

import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
 
@Controller
public class MainController {
 
	@Autowired
    private AppUserDAO appUserDao;
	
    @RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
    public String welcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
//        return "welcomePage";
        return "loginPage";
    }
 
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
         
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
         
        return "adminPage";
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
        AppUser user=appUserDao.findUserAccount(userName);
        System.out.println("User Name: " + userName);
 
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        return "redirect:/user?id="+user.getUserId();
    }
 
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<String> userPage(@RequestParam Long id,  Principal principal) {
//    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    	if (principal instanceof UserDetails) {
//    	  String username = ((UserDetails)principal).getUsername();
//    	} else {
//    	  String username = principal.toString();
//    	}
    	String userName = principal.getName();
        AppUser user=appUserDao.findUserAccount(userName);
        if(user.getUserId().equals(id)) {
            return new ResponseEntity<String>("<html><body>"+user.getUserName()+" owner Page</body></html>", HttpStatus.OK);
        }
        AppUser user2=appUserDao.findUserById(id);
        return new ResponseEntity<String>("<html><body>"+user2.getUserName()+" Page</body></html>", HttpStatus.OK);
    }
    
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {
 
        if (principal != null) {
            User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
            String userInfo = WebUtils.toString(loginedUser);
 
            model.addAttribute("userInfo", userInfo);
 
            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);
 
        }
 
        return "403Page";
    }
 
}