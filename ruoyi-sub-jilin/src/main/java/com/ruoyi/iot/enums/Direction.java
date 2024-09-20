package com.ruoyi.iot.enums;

public enum Direction {
    NORTH("0", "北"),
    NORTH_NORTHEAST("22.5", "东北偏北"),
    NORTHEAST("45", "东北"),
    EAST_NORTHEAST("67.5", "东北偏东"),
    EAST("90", "东"),
    EAST_SOUTHEAST("112.5", "东南偏东"),
    SOUTHEAST("135", "东南"),
    SOUTH_SOUTHEAST("157.5", "东南偏南"),
    SOUTH("180", "南"),
    SOUTH_SOUTHWEST("202.5", "西南偏南"),
    SOUTHWEST("225", "西南"),
    WEST_SOUTHWEST("247.5", "西南偏西"),
    WEST("270", "西"),
    WEST_NORTHWEST("292.5", "西北偏西"),
    NORTHWEST("315", "西北"),
    NORTH_NORTHWEST("337.5", "西北偏北");

    private final String angle;
    private final String name;

    Direction(String angle, String name) {
        this.angle = angle;
        this.name = name;
    }

    public String getAngle() {
        return angle;
    }

    public String getName() {
        return name;
    }

    public static String fromAngle(String angle) {
        for (Direction direction : values()) {
            if (direction.angle.equals(angle)) {
                return direction.name;
            }
        }
        throw new IllegalArgumentException("不支持的角度: " + angle);
    }

    @Override
    public String toString() {
        return name;
    }

    public static void main(String[] args) {
        // 测试枚举类
        for (Direction direction : Direction.values()) {
            System.out.println(direction);
        }

        // 测试从角度获取方向名称
        System.out.println(Direction.fromAngle("45"));  // 输出: 东北
    }
}