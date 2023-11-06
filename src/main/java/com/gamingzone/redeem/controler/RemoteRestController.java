package com.gamingzone.redeem.controler;


import com.gamingzone.redeem.Field;
import com.gamingzone.redeem.HtmlUtil;
import com.gamingzone.redeem.services.PlaywrightHandler;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.Proxy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
//import com.jayway.jsonpath.JsonPath;

@RestController
public class RemoteRestController {


    @Autowired
    private PlaywrightHandler playwrightHandler;


    @GetMapping("/")
    public ResponseEntity<String> getPage(HttpServletRequest request, HttpServletResponse response,  HttpSession session) {
        return  playwrightHandler.getPage(request, response ,  session);
    }

    @PostMapping("/updateFieldData")
    public ResponseEntity<String> updateFieldData(HttpSession session, @RequestBody Field field) {
        return  playwrightHandler.updateFieldData(session, field);
    }

    @PostMapping("/click")
    public ResponseEntity<String> click(HttpServletRequest request, HttpSession session, @RequestBody Field field) {
        return  playwrightHandler.click(request, session ,field);

    }
    @GetMapping("/getTokenString")
    public ResponseEntity<String> getTokenString(HttpSession session) {
        return  playwrightHandler.getTokenString(session);

    }

    @PostMapping("/updateTokenString")
    public ResponseEntity<String> updateTokenString(HttpSession session, @RequestBody Field field) {
        return  playwrightHandler.updateTokenString(session ,field);

    }


    }






