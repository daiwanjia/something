package com.marcdai.leetcode;

import org.junit.Test;

/**
 * @author：marcdai
 * @date：2024/11/18
 */
public class Case1 {

    //https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/
    @Test
    public void bestSellStock() {
        int[] prices = new int[]{1, 2, 3, 4, 3};

        int max = 0;
        int min = prices[0];
        for (int i = 1; i < prices.length; i++) {
            int p = prices[i];
            if (p < min){
                min = p;
            }else {
                int diff = p - min;
                if (diff > max){
                    max = diff;
                }
            }
        }
        System.out.println(max);
    }
}
