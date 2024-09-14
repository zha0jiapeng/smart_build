package com.ruoyi.iot.enums;

public enum VehicleType {
    UNKNOWN("0", "未知"),
    SEDAN("1", "轿车"),
    SUV("2", "SUV"),
    MPV("3", "MPV"),
    MINIBUS("4", "小型客车"),
    COACH("5", "大型客车"),
    SMALL_TRUCK("6", "小型货车"),
    LARGE_TRUCK("7", "大型货车");

    private final String code;
    private final String remark;

    // 构造函数
    VehicleType(String code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    // 获取编码
    public String getCode() {
        return code;
    }

    // 获取备注
    public String getRemark() {
        return remark;
    }

    // 根据编码获取备注
    public static String getRemarkByCode(String code) {
        for (VehicleType type : VehicleType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type.getRemark();
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

    public static void main(String[] args) {
        // 示例用法
        String remark = VehicleType.getRemarkByCode("1");
        System.out.println(remark);  // 输出：轿车

        String anotherRemark = VehicleType.getRemarkByCode("2");
        System.out.println(anotherRemark);  // 输出：SUV
    }
}