package com.eastnet.wechat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.eastnet.wechat.main.MenuManager;
import com.eastnet.wechat.message.resp.Article;
import com.eastnet.wechat.message.resp.Image;
import com.eastnet.wechat.message.resp.ImageMessage;
import com.eastnet.wechat.message.resp.Music;
import com.eastnet.wechat.message.resp.MusicMessage;
import com.eastnet.wechat.message.resp.NewsMessage;
import com.eastnet.wechat.message.resp.TextMessage;
import com.eastnet.wechat.message.resp.Video;
import com.eastnet.wechat.message.resp.VideoMessage;
import com.eastnet.wechat.message.resp.Voice;
import com.eastnet.wechat.message.resp.VoiceMessage;
import com.eastnet.wechat.pojo.AccessToken;
import com.eastnet.wechat.utils.MessageUtil;
import com.eastnet.wechat.utils.OperatorUtil;
import com.eastnet.wechat.utils.WeixinUtil;
import java.util.List;

/**
 * 解耦，使控制层与业务逻辑层分离开来，主要处理请求，响应
 * 
 * @author liangxy
 * @date 2017.06.05
 */
public class CoreService {
	private static OperatorUtil operatorUtil;
	static TextMessage textMessage = new TextMessage();
	static ImageMessage img = new ImageMessage();
	static VoiceMessage voice = new VoiceMessage();
	static VideoMessage video = new VideoMessage();
	static NewsMessage newmsg = new NewsMessage();
	static MusicMessage musicmsg = new MusicMessage();

	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		// 默认返回的文本消息类容
		String respContent = "请求处理异常，请稍后尝试！";
		String fromUserName = "";
		String toUserName = "";
		String msgType = "";
		try {
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.pareXml(request);
			System.out.println("\nrequestMap:" + requestMap.toString());
			// 发送方账号（open_id）
			fromUserName = requestMap.get("FromUserName");
			// 公众账号
			toUserName = requestMap.get("ToUserName");
			// 消息类型
			msgType = requestMap.get("MsgType");
			System.out.println("MsgType:" + msgType);
			// String eventType = requestMap.get("Event");
			String fromContent = "";

			String userName = "";
			if ((MessageUtil.REQ_MESSSAGE_TYPE_EVENT).equals(msgType)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = requestMap.get("EventKey");
					if ("stuInfoEdit".equals(eventKey)) {// 个人信息修改
						respContent = new OperatorUtil().editStuInfo(fromUserName);
					} else if ("stuInfoView".equals(eventKey)) {
						respContent = new OperatorUtil().viewStuInfo(fromUserName);
					} else if ("stuTravelView".equals(eventKey)) {// 行程查看
						respContent = new OperatorUtil().viewTravel(fromUserName);
					} else if ("stuTravelAdd".equals(eventKey)) {// 行程添加
						respContent = new OperatorUtil().addTravel(fromUserName);
					} else if ("stuTravelEdit".equals(eventKey)) {// 行程修改
						respContent = new OperatorUtil().editTravel(fromUserName);
					} else if ("help".equals(eventKey)) {// 操作说明
						respContent = "绑定账号:请回复  用户名绑定+用户名,例:用户名绑定fangw";
					} else if ("callAdmin".equals(eventKey)) {// 呼叫管理员
						respContent = "已通知管理员，稍后管理员会与您联系";
					} else if ("suggestions".equals(eventKey)) {// 意见反馈
						respContent = "意见反馈被点击";
					} else {
						respContent = "请求失败";
					}
				}
			}
			// 订阅

			String eventTypeSub = requestMap.get("Event");
			if ((MessageUtil.EVENT_TYPE_SUBSCRIBE).equals(eventTypeSub)) {
				// respContent = "景程教育欢迎您的到来！ \n 回复\"用户名绑定\"+登录用户名 如:用户名绑定fangw
				// 可完成账号绑定！\n 只有绑定账号后才可以实现接下来的操作";
				respContent = "谢谢关注！(づ￣ 3￣)づ";
			}
			// event
			// if(eventType.equals(MessageUtil.EVENT_TYPE_CLICK)){
			// String EventKey=requestMap.get("EventKey");
			// if("stuInfoEdit".equals(EventKey)){
			// respContent=new OperatorUtil().editTravel(fromUserName);
			//// respMessage=("<xml><ToUserName><![CDATA["+requestMap.get("FromUserName")+
			//// "]]></ToUserName>"+"<FromUserName><![CDATA["+requestMap.get("ToUserName")
			//// +"]]></FromUserName><CreateTime>"+System.currentTimeMillis()+"</CreateTime><MsgType><![CDATA[event]]></MsgType><Content><![CDATA["+respContent+"]]></Content></xml>");
			//// return respMessage;
			// }
			// }
			if (fromContent.contains("用户名绑定")) {
				userName = fromContent.substring(5).trim();
				respContent = new OperatorUtil().bindAccount(fromUserName, userName);
			}
			if (fromContent.contains("解除绑定")) {
				userName = fromContent.substring(4).trim();
				if ("oS-GywW5Aljk6V5v1JGDiUAOMdX0".equals(fromUserName)) {
					respContent = new OperatorUtil().unBindAccount(userName);
				} else {
					respContent = "您不具备管理员权限";
				}

			}
			if ("行程查看".equals(fromContent)) {
				respContent = new OperatorUtil().viewTravel(fromUserName);
			}
			if ("行程添加".equals(fromContent)) {
				respContent = new OperatorUtil().addTravel(fromUserName);
			}
			if ("行程修改".equals(fromContent)) {
				respContent = new OperatorUtil().editTravel(fromUserName);
			}
			if ("帮助".equals(fromContent)) {
				respContent = "绑定账号:请回复  用户名绑定+用户名,例:用户名绑定fangw\n行程查看:请回复  行程查看\n行程添加:请回复  行程添加\n行程修改:请回复  行程修改\n";
			}
			// 回复文本消息
			textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);// 接收方帐号（open_id）
			textMessage.setFromUserName(toUserName);// 公众账号
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSSAGE_TYPE_TEXT);
			// textMessage.setFuncFlag(0);
			textMessage.setMsgId(new Date().getTime());
			// System.out.println("id:"+textMessage.getMsgId()+"");

			StringBuffer sb = new StringBuffer();
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSSAGE_TYPE_TEXT)) {
				// respContent="你发的是文本信息:"+fromContent;
				fromContent = requestMap.get("Content");
				respContent = fromContent;
				if (fromContent.contains("Ionic")) {// 根据特定字符串返回相应图文消息
					msgType = MessageUtil.REQ_MESSSAGE_TYPE_LINK;
					Article article = new Article();
					List<Article> list = new ArrayList<Article>();
					article = new Article();
					article.setDescription("Ionic中会大量使用AngularJS基础知识，所以先来了解下AngularJS"); // 图文消息的描述
					article.setPicUrl(
							"http://mmbiz.qpic.cn/mmbiz_jpg/xUqw2AeQjMHEbRaKzGeqkzic0keHWB5JajuL5asRs0bL7aWqUsnEdYSlNePHhtuGHwQlWN23S4PnNZt3S8eIwicQ/640?wx_fmt=jpeg&wxfrom=5&wx_lazy=1"); // 图文消息图片地址
					article.setTitle("Ionic开发准备：AngularJs介绍 "); // 图文消息标题
					article.setUrl("http://mp.weixin.qq.com/s/rtQu0C9aEb1_3wypdqZYNQ"); // 图文url链接
					list.add(article); // 这里发送的是单图文，如果需要发送多图文则在这里list中加入多个Article即可！

					newmsg.setArticleCount(list.size());
					newmsg.setArticles(list);
					newmsg.setCreateTime(new Date().getTime());
					newmsg.setToUserName(fromUserName);// 接收方帐号（open_id）
					newmsg.setFromUserName(toUserName);// 公众账号
					newmsg.setMsgId(new Date().getTime());
					newmsg.setMsgType(MessageUtil.RESP_MESSSAGE_TYPE_NEWS);
					//
				}		else	if (fromContent.contains("杨千嬅")) {// 根据特定字符串返回相应歌曲
				} /*
					 * Connection conn=new DBCPConnection().getConnection();
					 * if(conn==null){ respContent="连接数据库失败"; }else{ int count
					 * =0; String sql="select * from crm_student_info";
					 * PreparedStatement ps=conn.prepareStatement(sql);
					 * ResultSet rs=ps.executeQuery(); while (rs.next()) {
					 * if(count>6){ break; } String
					 * name=rs.getString("exam_time");
					 * sb.append(name).append("/n"); count++; } }
					 * respContent=sb.toString();
					 */
				// respContent = new OpenDBConnection().selectData("select *
				// from crm_student_info");
			}
			// 图片消息

			else if (msgType.equals(MessageUtil.REQ_MESSSAGE_TYPE_IMAGE)) {
				String picUrl = requestMap.get("PicUrl");
				System.out.println(picUrl);
				String MediaId = requestMap.get("MediaId");
				System.out.println(MediaId);
				Image im = new Image(MediaId);
		
					msgType = MessageUtil.RESP_MESSSAGE_TYPE_MUSIC;
					Music music = new Music();
					music.setMusicUrl("http://119.23.20.192/eastnet_wechat/song1.mp3");
					music.setHQMusicUrl("http://119.23.20.192/eastnet_wechat/song1.mp3");
//					music.setThumbMediaId(
//							"https://mmbiz.qlogo.cn/mmbiz_jpg/xUqw2AeQjME0PhtgT7u2ctmqKpyk9iaKoKrxLAjn74yw675upjxmtmgIbCvgmCScpGaSyMRzAQWBarFxCgpK3lg/0?wx_fmt=jpeg");
					music.setTitle("少女的祈祷");
					music.setDescription("杨千嬅");
					music.setThumbMediaId(MediaId);
					musicmsg.setMusic(music);
					musicmsg.setToUserName(fromUserName);// 接收方帐号（open_id）
					musicmsg.setFromUserName(toUserName);// 公众账号
					musicmsg.setCreateTime(new Date().getTime());
					musicmsg.setMsgType(MessageUtil.RESP_MESSSAGE_TYPE_MUSIC);
					musicmsg.setFuncFlag(0);
					musicmsg.setMsgId(new Date().getTime());

			
				
				
/*				img.setImage(im);
				img.setToUserName(fromUserName);// 接收方帐号（open_id）
				img.setFromUserName(toUserName);// 公众账号
				img.setCreateTime(new Date().getTime());
				img.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_IMAGE);
				// img.setFuncFlag(0);
				img.setMsgId(new Date().getTime());
				// respContent = "您发送的是图片消息！"+picUrl;
				System.out.println("你发的是图片信息picUrl=" + picUrl);
*/			}
			// 地理位置
			else if (msgType.equals(MessageUtil.REQ_MESSSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
				String Label = requestMap.get("Label");
				String Location_X = requestMap.get("Location_X");
				String Location_Y = requestMap.get("Location_Y");
				respContent = respContent + "当前位置：" + Label + "，纬度：" + Location_X + "，经度：" + Location_Y;
				System.out.println("你发的是地理位置信息" + respContent);
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSSAGE_TYPE_LINK)) {
				// respContent = "您发送的是链接消息！";
				// System.out.println("你发的是链接信息");
				// news1
				Article article = new Article();
				List<Article> list = new ArrayList<Article>();
				article.setDescription("gitHub使用的3种方法"); // 图文消息的描述
				article.setPicUrl(
						"http://mmbiz.qpic.cn/mmbiz_png/xUqw2AeQjMG9uTUB0ia0pVBQBkVrWpR46gHyTZIEsicGVZm97KI3o0En5WxQBIOzlliafBvHUpNmEmvrb0QPBgib3g/640?wx_fmt=png&wxfrom=5&wx_lazy=1"); // 图文消息图片地址
				article.setTitle("收到《Ionic in Action》作者的email"); // 图文消息标题
				article.setUrl("http://mp.weixin.qq.com/s/DmesYW4lC7hyHi4EIlYwgQ"); // 图文url链接
				list.add(article); // 这里发送的是单图文，如果需要发送多图文则在这里list中加入多个Article即可！
				// news2
				article = new Article();
				article.setDescription("Ionic中会大量使用AngularJS基础知识，所以先来了解下AngularJS"); // 图文消息的描述
				article.setPicUrl(
						"http://mmbiz.qpic.cn/mmbiz_jpg/xUqw2AeQjMHEbRaKzGeqkzic0keHWB5JajuL5asRs0bL7aWqUsnEdYSlNePHhtuGHwQlWN23S4PnNZt3S8eIwicQ/640?wx_fmt=jpeg&wxfrom=5&wx_lazy=1"); // 图文消息图片地址
				article.setTitle("Ionic开发准备：AngularJs介绍 "); // 图文消息标题
				article.setUrl("http://mp.weixin.qq.com/s/rtQu0C9aEb1_3wypdqZYNQ"); // 图文url链接
				list.add(article); // 这里发送的是单图文，如果需要发送多图文则在这里list中加入多个Article即可！

				newmsg.setArticleCount(list.size());
				newmsg.setArticles(list);
				newmsg.setCreateTime(new Date().getTime());
				newmsg.setToUserName(fromUserName);// 接收方帐号（open_id）
				newmsg.setFromUserName(toUserName);// 公众账号
				newmsg.setMsgId(new Date().getTime());
				newmsg.setMsgType(MessageUtil.RESP_MESSSAGE_TYPE_NEWS);
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSSAGE_TYPE_VOICE)) {
				String mediaId = requestMap.get("MediaId");
				System.out.println("mediaId:" + mediaId);
				Voice vo = new Voice();
				vo.setMediaId(mediaId);
				voice.setVoice(vo);
				voice.setToUserName(fromUserName);// 接收方帐号（open_id）
				voice.setFromUserName(toUserName);// 公众账号
				voice.setCreateTime(new Date().getTime());
				voice.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_VOICE);
				// img.setFuncFlag(0);
				voice.setMsgId(new Date().getTime());
				respContent = "您发送的是音频消息！" + mediaId;
				System.out.println("你发的是音频信息" + mediaId);
			}
			// 视频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) {
				String mediaId = requestMap.get("MediaId");
				System.out.println("mediaId:" + mediaId);

				Video vo = new Video();
				vo.setMediaId(mediaId);
				vo.setThumbMediaId(mediaId);
				video.setVideo(vo);
				video.setToUserName(fromUserName);// 接收方帐号（open_id）
				video.setFromUserName(toUserName);// 公众账号
				video.setCreateTime(new Date().getTime());
				video.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_VIDEO);
				// img.setFuncFlag(0);
				video.setMsgId(new Date().getTime());
				respContent = "您发送的是视频消息！" + mediaId;
				System.out.println("你发的是视频信息" + mediaId);
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "谢谢关注！";
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					//
					System.out.println("取消订阅");
				} else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// 自定义菜单消息处理
					System.out.println("自定义菜单消息处理");
				}
			}
			System.out.println("respContent:" + respContent);
			textMessage.setContent(respContent);
			// respMessage=("\n<xml><ToUserName><![CDATA["+requestMap.get("FromUserName")+
			// "]]></ToUserName>"+"<FromUserName><![CDATA["+requestMap.get("ToUserName")
			// +"]]></FromUserName><CreateTime>"
			// +"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA["
			// +respContent+"]]>"
			// +"<MsgId>"+textMessage.getMsgId()+
			// "</MsgId>"+"</Content></xml>");
			// if("".equals(respContent)||(respContent==null)){
			// respMessage=("<xml><ToUserName><![CDATA["+requestMap.get("FromUserName")+
			// "]]></ToUserName>"+"<FromUserName><![CDATA["+requestMap.get("ToUserName")
			// +"]]></FromUserName><CreateTime>"+System.currentTimeMillis()+"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[查无此人]]></Content></xml>");
			// }else{
			// respMessage=("<xml><ToUserName><![CDATA["+requestMap.get("FromUserName")+
			// "]]></ToUserName>"+"<FromUserName><![CDATA["+requestMap.get("ToUserName")
			// +"]]></FromUserName><CreateTime>"+System.currentTimeMillis()+"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA["+respContent+"]]></Content></xml>");
			// }

			respMessage = MessageUtil.messageToXml(textMessage);
			if (msgType.equals(MessageUtil.REQ_MESSSAGE_TYPE_IMAGE))
				respMessage = MessageUtil.messageToXml(img);// 回复发送给公众号的图片给用户
			else if (msgType.equals(MessageUtil.RESP_MESSAGE_TYPE_VOICE)) {
				respMessage = MessageUtil.messageToXml(voice);// 回复发送给公众号的音频给用户
			} else if (msgType.equals(MessageUtil.RESP_MESSAGE_TYPE_VIDEO)) {
				System.out.println("video:" + respContent);
				respMessage = MessageUtil.messageToXml(video);// 回复发送给公众号的视频给用户
			} else if (msgType.equals(MessageUtil.REQ_MESSSAGE_TYPE_LINK)) {
				respMessage = MessageUtil.messageToXml(newmsg);// 回复发送给公众号的视频给用户
			} else if (msgType.equals(MessageUtil.RESP_MESSSAGE_TYPE_MUSIC)) {
				respMessage = MessageUtil.messageToXml(musicmsg);// 回复发送给公众号的视频给用户
				respMessage.replace("<CreateTime><![CDATA[", "<CreateTime>");
				respMessage = respMessage.replace("]]></CreateTime>", "</CreateTime>");
			}
		} catch (Exception e) {
			respMessage = "err:" + MessageUtil.messageToXml(textMessage);
			// if(msgType.equals(MessageUtil.REQ_MESSSAGE_TYPE_IMAGE))
			// respMessage = "err:"+MessageUtil.messageToXml(img);
			// respMessage=("\n<xml><ToUserName><![CDATA["+fromUserName+
			// "]]></ToUserName>"+"<FromUserName><![CDATA["+toUserName
			// +"]]></FromUserName><CreateTime>"+System.currentTimeMillis()
			// +"<MsgId>"+System.currentTimeMillis()+ "123456"+"</MsgId>"
			// +"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA["+respContent+"]]></Content></xml>");
		}
		return respMessage;
	}
}
