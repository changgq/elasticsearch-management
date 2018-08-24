package com.enlink.es.controllers;


import com.enlink.es.base.BaseAction;
import com.enlink.es.base.CountCycle;
import com.enlink.es.controllers.responses.AjaxResults;
import com.enlink.es.controllers.responses.ResultCode;
import com.enlink.es.controllers.responses.Results;
import com.enlink.es.dto.TopRankingDto;
import com.enlink.es.services.TopRankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/topRanking")
public class TopRankingController extends BaseAction {

    @Autowired
    private TopRankingRepository topRankingRepository;

    /**
     * 获取排行榜top100
     *
     * @param countCycle
     * @return
     * @throws Exception
     */
    @PostMapping("/top/{topValue}")
    public AjaxResults top(@RequestBody CountCycle countCycle, @PathVariable int topValue) throws Exception {
        if (topValue < 0) {
            topValue = 5;
        }
        List<TopRankingDto> topRanks = topRankingRepository.findByCountCycle(countCycle, topValue);
        return Results.resultOf(ResultCode.OK, topRanks);
    }

}
