package crawler;

import dao.City;
import dao.Country;
import dao.ProjectDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadCrawler extends Crawler {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        ThreadCrawler crawler = new ThreadCrawler();
        String dataString = crawler.getPage("https://view.inews.qq.com/g2/getOnsInfo?name=disease_foreign&callback=jQuery34108868805550922418_1584942276685&_=1584942276686");
        String jsonString = crawler.getJsonString(dataString);
        ArrayList<Object> allCounties = crawler.getAllCountriesArray(jsonString);
        ArrayList<Country> countries = crawler.parseCountryList(allCounties);

        long startSaveTime = System.currentTimeMillis();

        //获取到了所有国家的信息，使用线程池进行存储数据库
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        List<Future<?>> taskResults = new ArrayList<>();
        ProjectDao projectDao = new ProjectDao();
        
        //先删除当天数据库的内容，然后再进行保存，以保证实时数据的更新
        projectDao.deleteCityTable();
        projectDao.deleteCountryTable();

        for (Country country : countries) {
            //保存一个线程的工作进度
            //submit()传入的参数为实现Runnable/Callable接口的类
            Future<?> taskResult = executorService.submit(new CrawlerTask(country, projectDao));
            taskResults.add(taskResult);
        }
        //等待所有线程池中的任务执行结束，在进行下一步操作
        for (Future<?> taskResult : taskResults) {
            //检查每个任务是否都完成了
            try {
                taskResult.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        //所有任务执行完之后，要关闭线程池，避免占用资源
        executorService.shutdown();

        long finishTime = System.currentTimeMillis();
        System.out.println("保存数据用时：" + (finishTime - startSaveTime));
        System.out.println("整个程序执行时间：" + (System.currentTimeMillis() - startTime));
    }

    static class CrawlerTask implements Runnable {
        private Country country;
        private ProjectDao projectDao;

        public CrawlerTask(Country country, ProjectDao projectDao) {
            this.country = country;
            this.projectDao = projectDao;
        }

        @Override
        public void run() {
            //知道Country对象以及对象
            //保存到数据库中
            projectDao.saveToCountryTable(country);
            for (City city : country.getCities()) {
                projectDao.saveToCityTable(city, country.getCountryName());
            }
        }
    }
}
