package com.example.helloworld;

import com.example.helloworld.service.RankingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class RankingTest {

    @Test
    void getRanks() {
        // 네트워크 떄문에 시간이 오래 걸릴 수 있어서 한번 의미 없이 조회
        rankingService.getTopRanking(1);

        // 1)
        Instant before = Instant.now();
        Long userRank = rankingService.getUserRanking("user_100");
        Duration elapsed = Duration.between(before, Instant.now());

        System.out.println(String.format("Rank(%d)  - Took %d ms", userRank, elapsed.getNano() / 1000000));

        // 2)
        before = Instant.now();
        List<String> topRankers  = rankingService.getTopRanking(10);
        elapsed = Duration.between(before, Instant.now());

        System.out.println(String.format("Range  - Took %d ms", elapsed.getNano() / 1000000));

         // Rank 보다 Range 가 더 빠른이유는 끝(head, tail)에서 가져오면 되기 떄문에 더 빠르다.
    }

    @Autowired
    private RankingService rankingService;

    @Test
    void insertScore() {
        for(int i=0; i<1000000; i++) {
            int score = (int)(Math.random() * 1000000); // 0~ 99999
            String userId = "user_" + i;
            rankingService.setUserScore(userId, score);
        }
    }

    @DisplayName("Redis를 사용하지 않았을 때 성능 측정")
    @Test
    void inMemorySortPerformance() {
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=0; i<1000000; i++) {
            int score = (int)(Math.random() * 1000000); // 0~ 99999
            list.add(score);
        }

        Instant before = Instant.now();
        Collections.sort(list); // nlogn -> 가장 빠른 sort 알고리즘
        Duration elapsed = Duration.between(before, Instant.now());
        System.out.println((elapsed.getNano() / 1000000) + "ms");
    }
}
