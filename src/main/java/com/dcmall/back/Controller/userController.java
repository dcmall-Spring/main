package com.dcmall.back.Controller;

import com.dcmall.back.model.UserDAO;
import com.dcmall.back.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class userController {

    @Autowired
    UserDAO dao;

    List<UserDTO> userList;

    @GetMapping("/")
    public String mainPage(Model model){
        userList = this.dao.selectUser();

        model.addAttribute("List", userList);
        return "user.html";
    }
}
