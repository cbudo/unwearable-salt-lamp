package tech.unwearable.magicsaltunwearable.contract;

/**
 * Created by budoc on 8/19/2017.
 */

public class ColorsData {
    private String color;
    private String hex;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public ColorsData(String color, String hex) {

        this.color = color;
        this.hex = hex;
    }
}
