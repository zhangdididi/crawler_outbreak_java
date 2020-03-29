package crawler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import dao.City;
import dao.Country;
import dao.ProjectDao;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Crawler {

    //创建一个OkHttp客户端，用来发送请求接收请求
    private OkHttpClient okHttpClient = new OkHttpClient();

    //创建GSON对象
    private Gson gson = new GsonBuilder().create();

    //抓包的脚本文件地址
    private static final String URL = "https://view.inews.qq.com/g2/getOnsInfo?name=disease_foreign&callback=jQuery34108868805550922418_1584942276685&_=1584942276686";

    public static void main(String[] args) throws IOException {
        Crawler crawler = new Crawler();
        long startTime = System.currentTimeMillis();

        //得到疫情的数据字符串
        String dataString = crawler.getPage(URL);

        //248ms
        long finishTime = System.currentTimeMillis();
        System.out.println("获取页面时间：" + (finishTime - startTime));

        String jsonString = crawler.getJsonString(dataString);

        //再次根据key为"foreignList"取到所有国家的信息
        ArrayList<Object> allCounties = crawler.getAllCountriesArray(jsonString);
//        System.out.println(allCounties);

        //将每个国家的数据填入到countries数组列表中保存
        ArrayList<Country> countries = crawler.parseCountryList(allCounties);

        //33ms
        System.out.println("解析国家数组列表时间：" + (System.currentTimeMillis() - finishTime));
        finishTime = System.currentTimeMillis();

        ProjectDao projectDao = new ProjectDao();

        //遍历所有国家信息,并保存到数据库中去
        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(i);
            //保存到数据库中
            projectDao.saveToCountryTable(country);
            for (City city : country.getCities()) {
                projectDao.saveToCityTable(city, country.getCountryName());
            }
        }

        //1404ms
        System.out.println("存储数据库时间：" + (System.currentTimeMillis() - finishTime));

        System.out.println("总时间：" + (System.currentTimeMillis() - startTime));
    }


    /**
     * 获取页面信息
     * @param url
     * @return
     * @throws IOException
     */
    public String getPage(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if (!response.isSuccessful()) {
            System.out.println("获取响应失败！！！");
            return null;
        }
        return response.body().string();
    }


    //将获取到的页面内容转换成JSON格式的字符串
    public String getJsonString(String dataString) {
        //去除前面多余的内容
        int startIndex = dataString.indexOf('(');
        System.out.println(startIndex);
        dataString = dataString.substring(startIndex + 1, dataString.length() - 1);

        //解析JSON格式，取到key为"date"的数据
        Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        HashMap<String, Object> hashMap1 = gson.fromJson(dataString, type);
        String jsonString = (String)hashMap1.get("data");

        return jsonString;
    }


    //解析字符串，得到包含所有境外国家信息的数组列表
    public ArrayList<Object> getAllCountriesArray(String jsonString) {
        Type type = new TypeToken<HashMap<String, ?>>(){}.getType();
        HashMap<String, ?> map = gson.fromJson(jsonString, type);
        Object allCountries = map.get("foreignList");
        //取到所有国家的信息
        ArrayList<Object> arrayOfAllCountries = (ArrayList<Object>)allCountries;
        return arrayOfAllCountries;
    }


    //解析数据到国家的数组列表中
    public ArrayList<Country> parseCountryList(ArrayList<Object>  allCountries)  {
        ArrayList<Country> result = new ArrayList<>();

        for (Object obj : allCountries) {
            //取到每一个国家的信息
            LinkedTreeMap<String, Object> aCountry = (LinkedTreeMap<String, Object>)obj;
//            System.out.println(aCountry);

            //创建国家对象，准备填入字段属性
            Country country = new Country();
            //设置国家名称
            String countryName = (String)aCountry.get("name");
            country.setCountryName(countryName);
            //设置所属大洲
            String continent = (String)aCountry.get("continent");
            country.setContinent(continent);
            //设置累计总确诊
            Double allConfirmCount = (Double)aCountry.get("confirm");
            country.setAllConfirmCount(allConfirmCount.intValue());
            //设置新增确诊病例
            Double confirmAddCount = (Double)aCountry.get("confirmAdd");
            country.setConfirmAddCount(confirmAddCount.intValue());
            //设置累计治愈病例
            Double allHealCount = (Double)aCountry.get("heal");
            country.setAllHealCount(allHealCount.intValue());
            //设置新增治愈病例
            Double healAddCount = (Double)aCountry.get("healCompare");
            country.setHealAddCount(healAddCount.intValue());
            //设置累计死亡人数
            Double allDeadCount = (Double)aCountry.get("dead");
            country.setAllDeadCount(allDeadCount.intValue());
            //设置新增死亡人数
            Double deadAddCount = (Double)aCountry.get("deadCompare");
            country.setDeadAddCount(deadAddCount.intValue());
            //设置当前确诊人数
            Double nowConfirmCount = (Double)aCountry.get("nowConfirm");
            country.setNowConfirmCount(nowConfirmCount.intValue());

            //如果map长度为16，说明该国家还包含城市信息
            if (aCountry.size() > 15) {
                ArrayList<Object> citiesArray = (ArrayList<Object>)aCountry.get("children");
                ArrayList<City> cities = parseCityList(citiesArray);
                country.setCities(cities);
            }

            result.add(country);
        }
        return result;
    }

    /**
     * 解析数据到城市的数组列表中
     * @param citiesArray
     * @return cities
     */
    public ArrayList<City> parseCityList(ArrayList<Object> citiesArray) {

        ArrayList<City> cities = new ArrayList<>();

        for (Object obj : citiesArray) {
            LinkedTreeMap<String, Object> aCity = (LinkedTreeMap<String, Object>)obj;

            City city = new City();

            //设置城市名称
            String cityName = (String)aCity.get("name");
            city.setCityName(cityName);
            //设置累计确诊总人数
            Double allConfirmCountOfCity = (Double)aCity.get("confirm");
            city.setAllConfirmCountOfCity(allConfirmCountOfCity.intValue());
            //设置新增确诊病例
            Double confirmAddCountOfCity = (Double)aCity.get("confirmAdd");
            city.setConfirmAddCountOfCity(confirmAddCountOfCity.intValue());
            //设置累计治愈病例
            Double allHealCountOfCity = (Double)aCity.get("heal");
            city.setAllHealCountOfCity(allHealCountOfCity.intValue());
            //设置累计死亡人数
            Double  allDeadCountOfCity = (Double)aCity.get("dead");
            city.setAllDeadCountOfCity(allDeadCountOfCity.intValue());

            cities.add(city);
        }
        return cities;
    }
}
