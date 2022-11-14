package com.jwt.jwtauthentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {

    @RequestMapping("/welcome")
    public String welcome()
    {
      String text="This is a Private Page....";
      text+= "This page is not allowed to Unauthenticated  User";
      return text;
    }
}
