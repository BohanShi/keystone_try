package com.example.keystone_try.ui.OpenData;

public class YearlyData {
    private Long year;
    private Long male;
    private Long female;
    private Long total;

    public YearlyData (Long year, Long male, Long female, Long total){
        this.year = year;
        this.male = male;
        this.female = female;
        this.total = total;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getMale() {
        return male;
    }

    public void setMale(Long male) {
        this.male = male;
    }

    public Long getFemale() {
        return female;
    }

    public void setFemale(Long female) {
        this.female = female;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
