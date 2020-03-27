package dao;

public class City {
    //城市名称
    private String cityName;
    //累计确诊总人数
    private int allConfirmCountOfCity;
    //新增确诊任务
    private int confirmAddCountOfCity;
    //累计死亡人数
    private int allDeadCountOfCity;
    //累计治愈人数
    private int allHealCountOfCity;


    @Override
    public String toString() {
        return  "\n" + cityName + ": \n{" +
                ",\n 累计确诊人数=" + allConfirmCountOfCity +
                ",\n 新增确诊人数=" + confirmAddCountOfCity +
                ",\n 累计死亡人数=" + allDeadCountOfCity +
                ",\n 累计治愈人数=" + allHealCountOfCity +
                '}';
    }


    public String getCityName() {
        return cityName;
    }

    public int getAllConfirmCountOfCity() {
        return allConfirmCountOfCity;
    }

    public int getConfirmAddCountOfCity() {
        return confirmAddCountOfCity;
    }

    public int getAllDeadCountOfCity() {
        return allDeadCountOfCity;
    }

    public int getAllHealCountOfCity() {
        return allHealCountOfCity;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }


    public void setAllConfirmCountOfCity(int allConfirmCountOfCity) {
        this.allConfirmCountOfCity = allConfirmCountOfCity;
    }


    public void setConfirmAddCountOfCity(int confirmAddCountOfCity) {
        this.confirmAddCountOfCity = confirmAddCountOfCity;
    }


    public void setAllDeadCountOfCity(int allDeadCountOfCity) {
        this.allDeadCountOfCity = allDeadCountOfCity;
    }


    public void setAllHealCountOfCity(int allHealCountOfCity) {
        this.allHealCountOfCity = allHealCountOfCity;
    }
}
