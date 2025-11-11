package com.project.trip.admin.main.service;

import java.util.Map;

public interface AdminMainService {

    // 3가지 통계 데이터를 Map에 담아 반환
    Map<String, Integer> getDashboardStats();
    
}