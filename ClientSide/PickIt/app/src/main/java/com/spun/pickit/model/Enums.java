package com.spun.pickit.model;

public class Enums {
    public enum Toggles{
        TRENDING(0, "PickIt Toggle: Main Trending"),
        MOST_RECENT(1, "PickIt Toggle: Main Most Recent"),
        EXPIRING(2, "PickIt Toggle: Main Expiring"),
        UPLOADED(3, "PickIt Toggle: User Uploads"),
        RECENT_ACTIVITY(4, "PickIt Toggle: User Voted On");

        Toggles(int aStatus, String desc){
            this.status = aStatus;
            this.description = desc;
        }

        private final int status;
        private final String description;

        public int status(){
            return this.status;
        }
        public String description(){
            return this.description;
        }
    }
}