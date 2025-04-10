//package utils;
//
//import com.aventstack.extentreports.ExtentReports;
//import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class ExtentReportManager {
//
//    private static ExtentReports extent;
//    private static ExtentTest test;
//
//    public static ExtentReports createInstance() {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String reportPath = "Reports/CucumberReport/extent-report-" + timeStamp + ".html";
//
//        // ✅ 4.1.7 sürümünde doğru kullanım
//        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportPath);
//        htmlReporter.config().setDocumentTitle("Tiger3Automation Test Raporu");
//        htmlReporter.config().setReportName("Cucumber Test Sonuçları");
//        htmlReporter.config().setEncoding("UTF-8");
//
//        extent = new ExtentReports();
//        extent.attachReporter(htmlReporter);
//
//        extent.setSystemInfo("Proje", "Tiger3Automation");
//        extent.setSystemInfo("Ortam", "Test");
//        extent.setSystemInfo("Kullanıcı", System.getProperty("user.name"));
//
//        return extent;
//    }
//
//    public static ExtentReports getExtent() {
//        return extent;
//    }
//
//    public static ExtentTest getTest() {
//        return test;
//    }
//
//    public static void setTest(ExtentTest testInstance) {
//        test = testInstance;
//    }
//}
