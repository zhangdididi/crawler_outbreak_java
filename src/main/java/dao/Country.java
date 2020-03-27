package dao;

import java.util.ArrayList;
import java.util.List;

public class Country {
    //国家名称
    private String countryName;
    //所属大洲
    private String continent;
    //累计确诊总人数
    private int allConfirmCount;
    //新增确诊病例
    private int confirmAddCount;
    //累计治愈总人数
    private int allHealCount;
    //新增治愈病例
    private int healAddCount;
    //累计死亡总人数
    private int allDeadCount;
    //新增死亡病例
    private int deadAddCount;
    //当前确诊人数
    private int nowConfirmCount;


    //该国家的城市列表
    private List<City> cities = new ArrayList<>();

    @Override
    public String toString() {
        return "Country{" +
                "countryName='" + countryName + '\'' +
                ", continent='" + continent + '\'' +
                ", allConfirmCount=" + allConfirmCount +
                ", confirmAddCount=" + confirmAddCount +
                ", allHealCount=" + allHealCount +
                ", healAddCount=" + healAddCount +
                ", allDeadCount=" + allDeadCount +
                ", deadAddCount=" + deadAddCount +
                ", nowConfirmCount=" + nowConfirmCount +
                ", cities=" + cities +
                '}';
    }

    public String getCountryName() {
        return countryName;
    }

    public String getContinent() {
        return continent;
    }

    public int getAllConfirmCount() {
        return allConfirmCount;
    }

    public int getConfirmAddCount() {
        return confirmAddCount;
    }

    public int getAllHealCount() {
        return allHealCount;
    }

    public int getHealAddCount() {
        return healAddCount;
    }

    public int getAllDeadCount() {
        return allDeadCount;
    }

    public int getDeadAddCount() {
        return deadAddCount;
    }

    public int getNowConfirmCount() {
        return nowConfirmCount;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }


    public void setContinent(String continent) {
        this.continent = continent;
    }


    public void setAllConfirmCount(int allConfirmCount) {
        this.allConfirmCount = allConfirmCount;
    }


    public void setConfirmAddCount(int confirmAddCount) {
        this.confirmAddCount = confirmAddCount;
    }


    public void setAllHealCount(int allHealCount) {
        this.allHealCount = allHealCount;
    }


    public void setHealAddCount(int healAddCount) {
        this.healAddCount = healAddCount;
    }


    public void setAllDeadCount(int allDeadCount) {
        this.allDeadCount = allDeadCount;
    }


    public void setDeadAddCount(int deadAddCount) {
        this.deadAddCount = deadAddCount;
    }


    public void setNowConfirmCount(int nowConfirmCount) {
        this.nowConfirmCount = nowConfirmCount;
    }


    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
