package tech.unwearable.magicsaltunwearable.contract;

import java.util.List;

/**
 * Created by budoc on 8/19/2017.
 */

public class ColorsList {
    private List<ColorsData> colorsList;

    public List<ColorsData> getColorsList() {
        return colorsList;
    }

    public void setColorsList(List<ColorsData> colorsList) {
        this.colorsList = colorsList;
    }

    public ColorsList(List<ColorsData> colorsList) {

        this.colorsList = colorsList;
    }
}
