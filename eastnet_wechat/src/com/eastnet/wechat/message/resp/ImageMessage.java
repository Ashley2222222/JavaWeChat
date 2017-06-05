package com.eastnet.wechat.message.resp;
/**
* 类名: Image </br>
* 描述: 图片 </br>
* 开发人员：liangxy </br>
* 创建时间：  2017-6-5 </br>
* 发布版本：V1.0  </br>
 */
public class ImageMessage extends BaseMessage{
	   
    private Image Image;

    public Image getImage() {
        return Image;
    }

    public void setImage(Image image) {
        Image = image;
    }
}
