package com.cherie.wechat.message.resp;

/**
 * 类名: Image </br>
 * 描述: 图片 </br>
 * 开发人员：liangxy </br>
 * 创建时间： 2017-6-5 </br>
 * 发布版本：V1.0 </br>
 */
public class Image {

	private String MediaId;

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public Image(String mediaId) {
		super();
		MediaId = mediaId;
	}

}
