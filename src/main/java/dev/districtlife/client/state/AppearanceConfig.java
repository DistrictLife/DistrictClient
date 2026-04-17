package dev.districtlife.client.state;

public class AppearanceConfig {

    private static final AppearanceConfig INSTANCE = new AppearanceConfig();

    private int skinToneCount = 6;
    private int eyeColorCount = 5;
    private int hairStyleCount = 8;
    private int hairColorCount = 6;
    private int rpYear = 2026;

    private AppearanceConfig() {}

    public static AppearanceConfig getInstance() {
        return INSTANCE;
    }

    public void update(int skinToneCount, int eyeColorCount, int hairStyleCount, int hairColorCount, int rpYear) {
        this.skinToneCount = skinToneCount;
        this.eyeColorCount = eyeColorCount;
        this.hairStyleCount = hairStyleCount;
        this.hairColorCount = hairColorCount;
        this.rpYear = rpYear;
    }

    public int getSkinToneCount() { return skinToneCount; }
    public int getEyeColorCount() { return eyeColorCount; }
    public int getHairStyleCount() { return hairStyleCount; }
    public int getHairColorCount() { return hairColorCount; }
    public int getRpYear() { return rpYear; }
}
