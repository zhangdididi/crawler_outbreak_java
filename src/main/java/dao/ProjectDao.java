package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao {

    public static void main(String[] args) {
        ProjectDao projectDao = new ProjectDao();
        List<Country> countries = projectDao.selectCountriesOfDate("20200325");
        System.out.println(countries.toString());

    }

    //保存 Country 对象到数据库中
    public void saveToCountryTable(Country country) {
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = null;
        String sql = "insert into country values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, country.getCountryName());
            statement.setString(2, country.getContinent());
            statement.setInt(3, country.getAllConfirmCount());
            statement.setInt(4, country.getConfirmAddCount());
            statement.setInt(5, country.getAllHealCount());
            statement.setInt(6, country.getHealAddCount());
            statement.setInt(7, country.getAllDeadCount());
            statement.setInt(8, country.getDeadAddCount());
            statement.setInt(9, country.getNowConfirmCount());

            //获取当前日期 20200324 根据当前系统时间 + SimpleDateFormat类
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String nowTime = simpleDateFormat.format(System.currentTimeMillis());
            statement.setString(10, nowTime);

            //更新
            int ret = statement.executeUpdate();
            if (ret != 1) {
                System.out.println("当前数据库插入country " + country.getCountryName() + " 失败！！！");
                return;
            }
            System.out.println("插入country " + country.getCountryName() + " 数据成功！！！");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }

    public void saveToCityTable(City city, String nameOfCountry) {
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = null;
        String sql = "insert into city values(?, ?, ?, ?, ?, ?, ?)";
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, city.getCityName());
            statement.setString(2, nameOfCountry);
            statement.setInt(3, city.getAllConfirmCountOfCity());
            statement.setInt(4, city.getConfirmAddCountOfCity());
            statement.setInt(5, city.getAllHealCountOfCity());
            statement.setInt(6, city.getAllDeadCountOfCity());

            //获取当前日期 20200324 根据当前系统时间 + SimpleDateFormat类
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String nowTime = simpleDateFormat.format(System.currentTimeMillis());
            statement.setString(7, nowTime);

            //更新
            int ret = statement.executeUpdate();
            if (ret != 1) {
                System.out.println("当前数据库插入city " + city.getCityName() + " 失败！！！");
            }
            System.out.println("插入city " + city.getCityName() + " 成功！！！");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }

    //根据日期获取数据库中的相应信息
    public List<Country> selectCountriesOfDate(String date) {
        List<Country> countries = new ArrayList<>();
        //先获取数据库连接
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "select name,continent_name,country_all_confirm_count,country_confirm_add_count," +
                "country_all_heal_count,country_heal_add_count,country_all_dead_count," +
                "country_dead_add_count,now_confirm_count " +
                "from country where date=? order by country_all_confirm_count desc";
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, date);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Country country = new Country();
                country.setCountryName(resultSet.getString("name"));
                country.setContinent(resultSet.getString("continent_name"));
                country.setAllConfirmCount(resultSet.getInt("country_all_confirm_count"));
                country.setConfirmAddCount(resultSet.getInt("country_confirm_add_count"));
                country.setAllHealCount(resultSet.getInt("country_all_heal_count"));
                country.setHealAddCount(resultSet.getInt("country_heal_add_count"));
                country.setAllDeadCount(resultSet.getInt("country_all_dead_count"));
                country.setDeadAddCount(resultSet.getInt("country_dead_add_count"));
                country.setNowConfirmCount(resultSet.getInt("now_confirm_count"));

                //根据国家名称和日期找到对应时间对应国家的城市信息
                country.setCities(getCityListOfCountry(country.getCountryName(), date));
                countries.add(country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return countries;
    }

    //获取到该国家的城市列表
    public List<City> getCityListOfCountry(String countryName, String date) {
        List<City> cites = new ArrayList<>();
        //先获取数据库连接
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "select * from city where country_name = ? and date = ? " +
                "order by city_all_confirm_count desc";
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, countryName);
            statement.setString(2, date);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                City city = new City();
                city.setCityName(resultSet.getString("city_name"));
                city.setAllConfirmCountOfCity(resultSet.getInt("city_all_confirm_count"));
                city.setConfirmAddCountOfCity(resultSet.getInt("city_confirm_add_count"));
                city.setAllHealCountOfCity(resultSet.getInt("city_all_heal_count"));
                city.setAllHealCountOfCity(resultSet.getInt("city_all_dead_count"));

                cites.add(city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return cites;
    }
}
