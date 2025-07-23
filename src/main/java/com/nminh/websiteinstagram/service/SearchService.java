package com.nminh.websiteinstagram.service;

import com.nminh.websiteinstagram.model.response.SearchUserDTOResponse;

import java.util.List;

public interface SearchService {
    List<SearchUserDTOResponse> searchUsers(String searchQuery);
}
