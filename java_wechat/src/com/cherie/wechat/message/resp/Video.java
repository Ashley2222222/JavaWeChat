package com.cherie.wechat.message.resp;


/**
* 类名: Video </br>
* 描述: 视频model </br>
* 开发人员： liangxy </br>
* 创建时间：  2017-6-5 </br>
* 发布版本：V1.0  </br>
 */
public class Video {
    // 媒体文件id
    private String MediaId;
    // 缩略图的媒体id
    private String ThumbMediaId;

    private String Title;
    private String Description;
    
	public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getThumbMediaId() {
        return ThumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        ThumbMediaId = thumbMediaId;
    }

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
}