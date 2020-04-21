package com.example.zhuffei.ffei.tool;

/**
 * Created by zhuffei on 2020/3/15.
 */

public class UrlTool {
    //    public static final String host = "192.168.1.6";
    public static final String host = "192.168.1.6";
    public static final String prefix = "http://" + host + ":8080/ffei/";
    public static final String AVATOR = prefix + "avator/";
    public static final String GOODSIMG = prefix + "goodsImg/";
    public static final String LOGIN = prefix + "user/login";
    public static final String REGISTER = prefix + "user/register";
    public static final String ADDGOODS = prefix + "goods/add";
    public static final String LISTRECENTGOODS = prefix + "goods/listRecentGoods";
    public static final String GETBANNER = prefix + "goods/getBanner";
    public static final String GETROLLTEXT = prefix + "goods/getRollText";
    public static final String GETGOODSBYID = prefix + "goods/getGoodsById";
    public static final String VIEWGOODS = prefix + "goods/viewGoods";
    public static final String LISTCOMMENTBYGID = prefix + "goods/listCommentByGid";
    public static final String CHECKCOLLECT = prefix + "relation/checkCollect";
    public static final String COLLECT = prefix + "relation/collect";
    public static final String CANCELCOLLECT = prefix + "relation/cancelCollect";
    public static final String REPORRT = prefix + "relation/report";
    public static final String COMMENT = prefix + "relation/comment";
    public static final String DELETECOMMENT = prefix + "relation/deleteComment";
    public static final String SEARCHGOODS = prefix + "goods/searchGoods";
    public static final String LISTFOCUSGOODS = prefix + "goods/listFocusGoods";
    public static final String ADDBUY = prefix + "goods/addBuy";
    public static final String LISTCOLLECTGOODS = prefix + "relation/listCollectGoods";
    public static final String LISTMYGOODS = prefix + "relation/listMyGoods";
    public static final String LISTMYSELL = prefix + "relation/listMySell";
    public static final String LISTMYBUY = prefix + "relation/listMyBuy";
}
