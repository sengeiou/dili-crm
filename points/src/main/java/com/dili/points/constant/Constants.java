package com.dili.points.constant;

public class Constants {


    //权重类型
    public enum WeightType {
        NUMBER(10, "交易量"),
        MONEY(20, "交易额"),
        CATEGORY(30, "商品"),
        PAYMETHOD(40, "支付方式");

        private int code;
        private String name;

        WeightType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        /**
         * 根据类型编码获取名字
         *
         * @param code
         * @return
         */
        public static String getNameByCode(int code) {
            for (WeightType weightType : WeightType.values()) {
                if (weightType.getCode() == code) {
                    return weightType.getName();
                }
            }

            return null;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

}
