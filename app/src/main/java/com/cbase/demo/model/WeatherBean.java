package com.cbase.demo.model;

import java.util.List;

/**
 * @author : zhouyx
 * @date : 2017/10/22
 * @description :
 */
public class WeatherBean {

    /**
     * shidu : 27%
     * pm25 : 58
     * pm10 : 101
     * quality : 良
     * wendu : -2
     * ganmao : 极少数敏感人群应减少户外活动
     * yesterday : {"date":"17","sunrise":"07:34","high":"高温 5.0℃","low":"低温 -7.0℃","sunset":"17:14","aqi":53,"ymd":"2019-01-17","week":"星期四","fx":"北风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"}
     * forecast : [{"date":"18","sunrise":"07:34","high":"高温 6.0℃","low":"低温 -6.0℃","sunset":"17:16","aqi":80,"ymd":"2019-01-18","week":"星期五","fx":"西南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"19","sunrise":"07:33","high":"高温 3.0℃","low":"低温 -6.0℃","sunset":"17:17","aqi":40,"ymd":"2019-01-19","week":"星期六","fx":"北风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"20","sunrise":"07:33","high":"高温 5.0℃","low":"低温 -7.0℃","sunset":"17:18","aqi":32,"ymd":"2019-01-20","week":"星期日","fx":"北风","fl":"3-4级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"21","sunrise":"07:32","high":"高温 8.0℃","low":"低温 -5.0℃","sunset":"17:19","aqi":48,"ymd":"2019-01-21","week":"星期一","fx":"西风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"22","sunrise":"07:32","high":"高温 9.0℃","low":"低温 -6.0℃","sunset":"17:20","aqi":33,"ymd":"2019-01-22","week":"星期二","fx":"西北风","fl":"3-4级","type":"晴","notice":"愿你拥有比阳光明媚的心情"}]
     */

    private String shidu;
    private int pm25;
    private int pm10;
    private String quality;
    private String wendu;
    private String ganmao;
    private YesterdayBean yesterday;
    private List<ForecastBean> forecast;

    public String getShidu() {
        return shidu;
    }

    public void setShidu(String shidu) {
        this.shidu = shidu;
    }

    public int getPm25() {
        return pm25;
    }

    public void setPm25(int pm25) {
        this.pm25 = pm25;
    }

    public int getPm10() {
        return pm10;
    }

    public void setPm10(int pm10) {
        this.pm10 = pm10;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public String getGanmao() {
        return ganmao;
    }

    public void setGanmao(String ganmao) {
        this.ganmao = ganmao;
    }

    public YesterdayBean getYesterday() {
        return yesterday;
    }

    public void setYesterday(YesterdayBean yesterday) {
        this.yesterday = yesterday;
    }

    public List<ForecastBean> getForecast() {
        return forecast;
    }

    public void setForecast(List<ForecastBean> forecast) {
        this.forecast = forecast;
    }

    public static class YesterdayBean {
        /**
         * date : 17
         * sunrise : 07:34
         * high : 高温 5.0℃
         * low : 低温 -7.0℃
         * sunset : 17:14
         * aqi : 53
         * ymd : 2019-01-17
         * week : 星期四
         * fx : 北风
         * fl : <3级
         * type : 晴
         * notice : 愿你拥有比阳光明媚的心情
         */

        private String date;
        private String sunrise;
        private String high;
        private String low;
        private String sunset;
        private int aqi;
        private String ymd;
        private String week;
        private String fx;
        private String fl;
        private String type;
        private String notice;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public int getAqi() {
            return aqi;
        }

        public void setAqi(int aqi) {
            this.aqi = aqi;
        }

        public String getYmd() {
            return ymd;
        }

        public void setYmd(String ymd) {
            this.ymd = ymd;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getFx() {
            return fx;
        }

        public void setFx(String fx) {
            this.fx = fx;
        }

        public String getFl() {
            return fl;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }
    }

    public static class ForecastBean {
        /**
         * date : 18
         * sunrise : 07:34
         * high : 高温 6.0℃
         * low : 低温 -6.0℃
         * sunset : 17:16
         * aqi : 80
         * ymd : 2019-01-18
         * week : 星期五
         * fx : 西南风
         * fl : <3级
         * type : 多云
         * notice : 阴晴之间，谨防紫外线侵扰
         */

        private String date;
        private String sunrise;
        private String high;
        private String low;
        private String sunset;
        private int aqi;
        private String ymd;
        private String week;
        private String fx;
        private String fl;
        private String type;
        private String notice;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public int getAqi() {
            return aqi;
        }

        public void setAqi(int aqi) {
            this.aqi = aqi;
        }

        public String getYmd() {
            return ymd;
        }

        public void setYmd(String ymd) {
            this.ymd = ymd;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getFx() {
            return fx;
        }

        public void setFx(String fx) {
            this.fx = fx;
        }

        public String getFl() {
            return fl;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }
    }
}
