package dev.districtlife.client.gui.creation;

public class CharacterCreationFlow {

    private static final CharacterCreationFlow INSTANCE = new CharacterCreationFlow();

    private String firstName = "";
    private String lastName = "";
    private String birthDate = "";  // Format ISO YYYY-MM-DD
    private int skinTone = 1;
    private int eyeColor = 1;
    private int hairStyle = 1;
    private int hairColor = 1;
    private String lastError = "";  // Erreur serveur à afficher au retour sur un Screen

    private CharacterCreationFlow() {}

    public static CharacterCreationFlow getInstance() {
        return INSTANCE;
    }

    public void reset() {
        firstName = "";
        lastName = "";
        birthDate = "";
        skinTone = 1;
        eyeColor = 1;
        hairStyle = 1;
        hairColor = 1;
        lastError = "";
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public int getSkinTone() { return skinTone; }
    public void setSkinTone(int skinTone) { this.skinTone = skinTone; }

    public int getEyeColor() { return eyeColor; }
    public void setEyeColor(int eyeColor) { this.eyeColor = eyeColor; }

    public int getHairStyle() { return hairStyle; }
    public void setHairStyle(int hairStyle) { this.hairStyle = hairStyle; }

    public int getHairColor() { return hairColor; }
    public void setHairColor(int hairColor) { this.hairColor = hairColor; }

    public String getLastError() { return lastError; }
    public void setLastError(String lastError) { this.lastError = lastError != null ? lastError : ""; }
}
