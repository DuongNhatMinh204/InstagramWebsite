package com.nminh.websiteinstagram.controller;

import com.nminh.websiteinstagram.model.response.ApiResponse;
import com.nminh.websiteinstagram.model.response.SearchUserDTOResponse;
import com.nminh.websiteinstagram.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/v1/user/search")
public class SearchController {
    @Autowired
    private SearchService searchService;
    @GetMapping
    public ApiResponse searchUser(@RequestParam String query) {
        log.info("Searching user with query {}", query);
        ApiResponse apiResponse = new ApiResponse(searchService.searchUsers(query));
        log.info("Searching user response");
        return apiResponse;
    }
}
