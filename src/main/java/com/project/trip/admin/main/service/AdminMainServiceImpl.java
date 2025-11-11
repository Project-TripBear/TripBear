package com.project.trip.admin.main.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.project.trip.admin.main.mapper.AdminMainMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMainServiceImpl implements AdminMainService {

    private final AdminMainMapper mainMapper;

    @Override
    public Map<String, Integer> getDashboardStats() {
        
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("totalMembers", mainMapper.getTotalMembers());
        stats.put("todayReservations", mainMapper.getTodayReservations());
        stats.put("pendingReports", mainMapper.getPendingReports());
        
        return stats;
    }
}