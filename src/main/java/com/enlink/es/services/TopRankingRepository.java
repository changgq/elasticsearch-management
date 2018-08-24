package com.enlink.es.services;

import com.enlink.es.base.CountCycle;
import com.enlink.es.dto.TopRankingDto;
import com.enlink.es.models.TopRanking;

import java.util.List;


public interface TopRankingRepository extends GeneralRepository<TopRanking> {

    /**
     * 通过统计周期对象获取top排行
     *
     * @param countCycle
     * @param top
     * @return
     * @throws Exception
     */
    List<TopRankingDto> findByCountCycle(CountCycle countCycle, int top) throws Exception;
}
