package com.cherie.wechat.message.req;
/**
 * 文本消息
 * @author liangxy
 * @date 2017.06.06
 */
public class TextMessage extends BaseMessage{
	//消息内容
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
