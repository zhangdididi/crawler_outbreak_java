package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.Country;
import dao.ProjectDao;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//基于多态来实现的，servlet框架可以提供一些方法，让我们实现自己的一些逻辑
//我不需要去了解它内部具体是怎么实现的，只需要针对get/post请求方法来处理
public class AllRankServlet extends HttpServlet {

    private Gson gson = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //格式设置
        resp.setContentType("application/json; charset=utf8");
        //解析请求，获取到对应的日期请求
        String date = req.getParameter("date");
        if (date == null || "".equals(date)) {
            resp.setStatus(404);
            resp.getWriter().write("============= date 参数错误，找不到相关资源==============");
            return;
        }
//        resp.getWriter().write("===========================正在查找资源==========================\n");
        //从数据库中查找数据
        ProjectDao projectDao = new ProjectDao();

        List<Country> countries = projectDao.selectCountriesOfDate(date);

//        resp.getWriter().write("=================================已得到数据============================\n");
        //将得到的数据处理为json格式返回给客户端
        String respString = gson.toJson(countries);
//        resp.getWriter().write("==================================已转换为json格式=============================\n");
        resp.getWriter().write(respString);
//        resp.getWriter().write("\n");
//        resp.getWriter().write("=======================打印完毕===========================\n");
    }
}

















