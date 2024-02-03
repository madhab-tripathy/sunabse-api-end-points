package com.sunbase.sunbase.user.management.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sunbase.sunbase.user.management.dto.CustomerResponse;
import com.sunbase.sunbase.user.management.dto.SyncResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ApiSyncService {
    String authenticateAndGetToken();
    List<Map<String,Object>> fetchCustomerData() throws JsonProcessingException;
}
