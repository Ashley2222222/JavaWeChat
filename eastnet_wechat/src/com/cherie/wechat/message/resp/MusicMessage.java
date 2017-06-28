package com.eastnet.wechat.message.resp;
/**
 * 响应的音乐消息
 * @author liangxy
 * @date 2017.06.06
 */
public class MusicMessage extends BaseMessage{
	//音乐
	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}
	
}
