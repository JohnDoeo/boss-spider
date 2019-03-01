package com.johndoeo.bean;

import java.io.Serializable;
import java.util.Date;

public class Job implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String jobTitle;
    private String salary;
    private String company;
    private String companyUrl;
    private String hrName;
    private String address;
    private String profession;
    private String publishDate;
    private Date createTime;

    public Job(){}

    public Job(String jobTitle, String salary, String company, String companyUrl, String hrName, String address, String profession, String publishDate) {
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.company = company;
        this.companyUrl = companyUrl;
        this.hrName = hrName;
        this.address = address;
        this.profession = profession;
        this.publishDate = publishDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public String getHrName() {
        return hrName;
    }

    public void setHrName(String hrName) {
        this.hrName = hrName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}
