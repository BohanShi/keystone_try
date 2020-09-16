package com.example.keystone_try.ui.OpenData;

public class Fetch_data {
        private String age_group;
        private Long male;
        private Long female;
        private Long total;

    public Fetch_data (String age_group, Long male, Long female, Long total){
        this.age_group = age_group;
        this.male = male;
        this.female = female;
        this.total = total;
    }

        public String getAge_group() {
            return age_group;
        }

        public void setAge_group(String age_group) {
            this.age_group = age_group;
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

