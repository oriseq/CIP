package com.oriseq.common.utils;

import cn.hutool.core.lang.UUID;

import java.util.Random;

public class RandomCodeGenerator {

    public static void main(String[] args) {
        System.out.println(generateRandomCode(4));
    }

    /**
     * 数字位随机
     *
     * @param length
     * @return
     */
    public static String generateRandomCode(int length) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(10);
            code.append(number);
        }
        return code.toString();
    }


    /**
     * UUID生成随机值
     *
     * @return
     */
    public static String generateRandomCodeLong() {
        UUID uuid = UUID.randomUUID();
        String radStr = uuid.toString().replace("-", "");
        return radStr;
    }


}
