package com.eastnet.wechat.message.resp;
/**
 * 音乐消息中的Music定义
 * 音乐中的model
 * @author liangxy
 * @date 2017.06.06
 *
 */
public class Music {
	//音乐名称
	private String Title;
	//音乐描述
	private String Description;
	//音乐链接
	private String MusicUrl;
	//高质量音乐链接，WIFI环境下优先使用该链接播放音乐
	private String HQMusicUrl;
//	 // 缩略图的媒体id，通过上传多媒体文件得到的id,必须是通过微信认证的服务号才能得到，普通的服务号与订阅号可以忽略该参数
//    private String ThumbMediaId;

	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	
	public String getHQMusicUrl() {
		return HQMusicUrl;
	}
	public void setHQMusicUrl(String hQMusicUrl) {
		HQMusicUrl = hQMusicUrl;
	}

//    public String getThumbMediaId() {
//        return ThumbMediaId;
//    }
//
//    public void setThumbMediaId(String thumbMediaId) {
//        ThumbMediaId = thumbMediaId;
//    }
	
	public String getMusicUrl() {
		return MusicUrl;
	}
	public void setMusicUrl(String musicUrl) {
		MusicUrl = musicUrl;
	}
//	public String getThumbMediaId() {
//		return ThumbMediaId;
//	}
//	public void setThumbMediaId(String thumbMediaId) {
//		ThumbMediaId = thumbMediaId;
//	}
}
