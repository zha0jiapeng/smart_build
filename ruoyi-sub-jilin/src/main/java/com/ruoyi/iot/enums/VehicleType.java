package com.ruoyi.iot.enums;

public enum VehicleType {
    UNKNOWN("0", "未知"),
    SEDAN("1", "轿车"),
    SUV("2", "SUV"),
    MPV("3", "MPV"),
    MINIBUS("4", "小型客车"),
    COACH("5", "大型客车"),
    SMALL_TRUCK("6", "小型货车"),
    LARGE_TRUCK("7", "大型货车"),
    OTHER("hik0", "其他车"),
    SMALL_CAR("hik1", "小型车"),
    LARGE_CAR("hik2", "大型车"),
    MOTORCYCLE("hik3", "摩托车"),
    NOT_OPEN("hik10", "未开闸"),
    AUTO_OPEN("hik11", "自动开闸"),
    MANUAL_OPEN("hik12", "人工/人工开闸"),
    REMOTE_OPEN("hik13", "遥控器开闸");

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