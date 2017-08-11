package com.chinamobo.ue.course.entity;

import java.util.Date;

public class TomTest {
    private Integer testId;

    private String testName;

    private Integer courseId;

    private String testUserId;

    private String testUserName;

    private Integer testQuestionId;

    private String testQuestionName;

    private Date testStartTime;

    private Date testEndTime;

    private String testUserAnswer;

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName == null ? null : testName.trim();
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getTestUserId() {
        return testUserId;
    }

    public void setTestUserId(String testUserId) {
        this.testUserId = testUserId == null ? null : testUserId.trim();
    }

    public String getTestUserName() {
        return testUserName;
    }

    public void setTestUserName(String testUserName) {
        this.testUserName = testUserName == null ? null : testUserName.trim();
    }

    public Integer getTestQuestionId() {
        return testQuestionId;
    }

    public void setTestQuestionId(Integer testQuestionId) {
        this.testQuestionId = testQuestionId;
    }

    public String getTestQuestionName() {
        return testQuestionName;
    }

    public void setTestQuestionName(String testQuestionName) {
        this.testQuestionName = testQuestionName == null ? null : testQuestionName.trim();
    }

    public Date getTestStartTime() {
        return testStartTime;
    }

    public void setTestStartTime(Date testStartTime) {
        this.testStartTime = testStartTime;
    }

    public Date getTestEndTime() {
        return testEndTime;
    }

    public void setTestEndTime(Date testEndTime) {
        this.testEndTime = testEndTime;
    }

    public String getTestUserAnswer() {
        return testUserAnswer;
    }

    public void setTestUserAnswer(String testUserAnswer) {
        this.testUserAnswer = testUserAnswer == null ? null : testUserAnswer.trim();
    }
}