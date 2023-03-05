package com.example.helloworld.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * data set, 범위 기반 조회, 특정 유저 순위조회
 */
@Service
public class RankingService {

    public static final String LEADER_BOARD_KEY = "leaderBoard";
    private final StringRedisTemplate redisTemplate;

    public RankingService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 특정 사용자 순위 조회
     * @param userId
     * @param score
     * @return
     */
    public boolean setUserScore(String userId, int score) {
        // 레디스의 sorted-set 타입 사용
        ZSetOperations zSetOps = redisTemplate.opsForZSet();
        zSetOps.add(LEADER_BOARD_KEY, userId, score);
        return true;
    }

    /**
     * 특정 유저의 랭킹 조회
     * @param userId
     * @return
     */
    public Long getUserRanking(String userId) {
        ZSetOperations zSetOps = redisTemplate.opsForZSet();
        Long rank = zSetOps.reverseRank(LEADER_BOARD_KEY, userId);

        return rank;
    }

    /**
     * 범위기반 조회 함수
     * @param limit
     * @return
     */
    public List<String> getTopRanking(int limit) {
        ZSetOperations zSetOps = redisTemplate.opsForZSet();
        // 범위기반 조회 결과값은 Set으로 오게 된다.
        // 상위 1개 리턴 : limit이 1이 들어왔을 떄 0~0까지 보여줄 수 있게 한다.
        Set<String> rangeSet = zSetOps.reverseRange(LEADER_BOARD_KEY, 0, limit - 1);

        return new ArrayList<>(rangeSet);
    }
}
