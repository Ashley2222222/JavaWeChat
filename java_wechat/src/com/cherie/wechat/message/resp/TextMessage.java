package com.cherie.wechat.message.resp;
/**
 * 响应文本消息
 * @author liangxy
 * @date 2017.06.06
 */
public class TextMessage extends BaseMessage{
	//回复的消息类容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

}
