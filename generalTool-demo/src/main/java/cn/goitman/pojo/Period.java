package cn.goitman.pojo;

/**
 * @author Nicky
 * @version 1.0
 * @className CrawlUtil
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 测试对象
 * @date 2022/4/25 16:00
 */
public class Period {

    private String date;

    private String issue;

    private String redNum;

    private String blueNum;

    private String money;

    private String firstNum;

    private String secondNum;

    private String flag;

    public Period() {
    }

    public Period(String date, String issue, String redNum, String blueNum) {
        this.date = date;
        this.issue = issue;
        this.redNum = redNum;
        this.blueNum = blueNum;
    }

    public Period(String date, String issue, String redNum, String blueNum, String money, String firstNum, String secondNum) {
        this.date = date;
        this.issue = issue;
        this.redNum = redNum;
        this.blueNum = blueNum;
        this.money = money;
        this.firstNum = firstNum;
        this.secondNum = secondNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getRedNum() {
        return redNum;
    }

    public void setRedNum(String redNum) {
        this.redNum = redNum;
    }

    public String getBlueNum() {
        return blueNum;
    }

    public void setBlueNum(String blueNum) {
        this.blueNum = blueNum;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getFirstNum() {
        return firstNum;
    }

    public void setFirstNum(String firstNum) {
        this.firstNum = firstNum;
    }

    public String getSecondNum() {
        return secondNum;
    }

    public void setSecondNum(String secondNum) {
        this.secondNum = secondNum;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Period{" +
                "date='" + date + '\'' +
                ", issue='" + issue + '\'' +
                ", redNum='" + redNum + '\'' +
                ", blueNum='" + blueNum + '\'' +
                ", money='" + money + '\'' +
                ", firstNum='" + firstNum + '\'' +
                ", secondNum='" + secondNum + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
