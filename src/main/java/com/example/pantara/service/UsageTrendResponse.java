package com.example.pantara.service;

import com.example.pantara.dto.response.DailyUsageDto;
import com.example.pantara.dto.response.PopularMenuDto;

import java.util.List;

public class UsageTrendResponse {
    private List<DailyUsageDto> dailyUsage;
    private List<PopularMenuDto> popularMenus;

    public UsageTrendResponse(List<DailyUsageDto> dailyUsage, List<PopularMenuDto> popularMenus) {
        this.dailyUsage = dailyUsage;
        this.popularMenus = popularMenus;
    }

    public List<DailyUsageDto> getDailyUsage() { return dailyUsage; }
    public List<PopularMenuDto> getPopularMenus() { return popularMenus; }
}
