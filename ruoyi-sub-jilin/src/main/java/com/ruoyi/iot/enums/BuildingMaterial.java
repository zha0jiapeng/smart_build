package com.ruoyi.iot.enums;

import java.util.*;
import java.util.stream.*;

/**
 * 建筑材料枚举（支持多中文关键词反向查找）
 */
public enum BuildingMaterial {
    // 枚举值定义（英文常量，可变长中文关键词数组）
    MAR_FIN_NAT_SAND("砂", "天然砂", "骨料1"),
    MAR_COAR_5_10("5-10mm碎石", "骨料2"),
    MAR_COAR_5_20("5-20mm碎石", "骨料3"),
    MAR_COAR_20_40("20-40mm碎石", "骨料4"),
    MAR_BON_FLYASH("F类Ⅱ级", "粉煤灰1", "粉煤灰2"),
    MAR_CEM_NOR_POR("普通硅酸盐水泥", "水泥1", "水泥2", "水泥3"),
    MAR_ADD_TATER_REDUCER("减水剂", "外加剂1");

    private final Set<String> keywords;
    private static final Map<String, BuildingMaterial> keywordMap = new HashMap<>();

    static {
        // 初始化关键词映射表
        for (BuildingMaterial material : values()) {
            for (String keyword : material.keywords) {
                keywordMap.put(keyword, material);
            }
        }
    }

    BuildingMaterial(String... keywords) {
        this.keywords = Arrays.stream(keywords).collect(Collectors.toSet());
    }

    /**
     * 通过任意中文关键词获取枚举常量
     * @param keyword 中文关键词（如"砂"或"骨料1"）
     * @return 对应的枚举常量
     * @throws IllegalArgumentException 如果找不到对应的材料
     */
    public static BuildingMaterial findByKeyword(String keyword) {
        BuildingMaterial material = keywordMap.get(keyword);
        if (material == null) {
            throw new IllegalArgumentException("未知的建筑材料关键词: " + keyword);
        }
        return material;
    }

    /**
     * 获取该材料的所有中文关键词
     */
    public Set<String> getKeywords() {
        return Collections.unmodifiableSet(keywords);
    }
}