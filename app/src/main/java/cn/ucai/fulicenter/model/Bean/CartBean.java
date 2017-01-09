package cn.ucai.fulicenter.model.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/9.
 */

public class CartBean {

    /**
     * id : 2016
     * userName : a123456
     * goodsId : 7672
     * goods : {"id":278,"goodsId":7672,"catId":291,"goodsName":"趣味煮蛋模具","goodsEnglishName":"Kotobuki","goodsBrief":"将煮好的鸡蛋放到模具中，扣好卡扣，把蛋模放冰水，耐心等上10分钟，就可以变化成各种各样的形状，宝宝看了说不定胃口大开！","shopPrice":"￥110","currencyPrice":"￥140","promotePrice":"￥0","rankPrice":"￥140","isPromote":false,"goodsThumb":"201509/thumb_img/7672_thumb_G_1442389445719.jpg","goodsImg":"201509/thumb_img/7672_thumb_G_1442389445719.jpg","addTime":1476820611547,"shareUrl":"http://m.fulishe.com/item/7672","properties":[{"id":9522,"goodsId":0,"colorId":4,"colorName":"绿色","colorCode":"#59d85c","colorImg":"201309/1380064997570506166.jpg","colorUrl":"1","albums":[{"pid":7672,"imgId":28283,"imgUrl":"201509/goods_img/7672_P_1442389445199.jpg","thumbUrl":"no_picture.gif"}]}],"promote":false}
     * count : 2
     * isChecked : false
     * checked : false
     */

    private int id;
    private String userName;
    private int goodsId;
    private GoodsDetailsBean goods;
    private int count;
    private boolean isChecked;
    private boolean checked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public void setGoods(GoodsDetailsBean goods) {
        this.goods = goods;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "CartBean{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", goodsId=" + goodsId +
                ", goods=" + goods +
                ", count=" + count +
                ", isChecked=" + isChecked +
                ", checked=" + checked +
                '}';
    }
}
