package kr.go.molit.nhsnes.model;

/**
 * Created by jongrakmoon on 2017. 4. 6..
 */

public class NhsDestinationSearchMapModel extends NhsBaseModel {
    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
