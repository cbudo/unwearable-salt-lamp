package tech.unwearable.magicsaltunwearable.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by budoc on 8/19/2017.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class ColorsData {
    @JsonProperty("name")
    private String name;
    @JsonProperty("hex")
    private String hex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    @JsonCreator

    public ColorsData(@JsonProperty("name") String color, @JsonProperty("hex") String hex) {

        this.name = color;
        this.hex = hex;
    }
}
