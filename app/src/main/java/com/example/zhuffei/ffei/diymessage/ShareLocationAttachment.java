package com.example.zhuffei.ffei.diymessage;

import com.alibaba.fastjson.JSONObject;

import java.util.Random;

public class ShareLocationAttachment extends CustomAttachment{
    public enum Craps {
        one(1, "1"),
        ;
        private int value;
        private String desc;
        Craps(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }
        static Craps enumOfValue(int value) {
            for (Craps direction : values()){
                if (direction.getValue() == value) {
                    return direction;
                }
            }
            return one;
        }
        public int getValue() {
            return value;
        }
        public String getDesc() {
            return desc;
        }
    }
    private Craps value;
    public ShareLocationAttachment() {
        super(CustomAttachmentType.ShareLocation);
        random();
    }
    @Override
    protected void parseData(JSONObject data) {
        value = Craps.enumOfValue(data.getIntValue("value"));
    }
    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("value", value.getValue());
        return data;
    }
    private void random() {
        int value = new Random().nextInt(6) + 1;
        this.value = Craps.enumOfValue(value);
    }
    public Craps getValue() {
        return value;
    }
}