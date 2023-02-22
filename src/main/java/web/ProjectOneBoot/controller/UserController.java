package web.ProjectOneBoot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.ProjectOneBoot.model.User;
import web.ProjectOneBoot.service.UserService;

import java.util.List;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        List<User> listUsers = userService.getListUsers("-1");
        model.addAttribute("listUsers", listUsers);
        return "index";
    }

    @RequestMapping("/new")
    public String newUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "new_user";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") User user) {
        if (user.getEmail().isEmpty() && user.getName().isEmpty() && user.getLastname().isEmpty()) {
            return "redirect:/";
        }
        userService.add(user);
        return "redirect:/";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteUserForm(@RequestParam(name="id") long id) {
        userService.remove(id);
        return "redirect:/";
    }
    @RequestMapping("/edit")
    public String editUserForm(@RequestParam(name="id") long id, Model model) {
        User user = userService.findUser(id);
        model.addAttribute("user", user);
        return "edit";
    }

    @PatchMapping(value = "/{id}")
    public String updateUserForm(@ModelAttribute("user") User user, @PathVariable("id") long id) {
        userService.update(user, id);
        return "redirect:/";
    }

    @RequestMapping("/search")
    public String getListUsers(@RequestParam(defaultValue = "0") String keyword, Model model) {
        if (keyword.equals("0")) {
            return "redirect:/";
        }
        model.addAttribute("users", userService.getListUsers(keyword));
        return "users";
    }
}
