package com.victor2022.seckill;

import com.victor2022.seckill.common.util.MD5Util;
import com.victor2022.seckill.entity.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SecKillTest {

	@Test
	public void test()throws Exception {
		createUser(5000);
	}

	private static void createUser(int count) throws Exception{
		List<User> users = new ArrayList<User>(count);
		//生成用户
		for(int i=0;i<count;i++) {
			User user = new User();
			user.setId((int)10000000000L+i);
			user.setLoginCount(1);
			user.setUserName("user"+i);
			user.setRegisterDate(new Date());
			user.setPhone((18077200000L+i)+"");
			user.setLastLoginDate(new Date());
			user.setSalt("9d5b364d");
			user.setHead("");
			user.setPassword(MD5Util.inputPassToDbPass("123456", user.getSalt()));
			users.add(user);
		}
//		System.out.println("create user");
//		//插入数据库
//		Connection conn = DBUtil.getConn();
//		String sql = "INSERT INTO `seckill`.`user` (`user_name`, `phone`, `password`, `salt`, `head`, `login_count`," +
//				" `register_date`, `last_login_date`)values(?,?,?,?,?,?,?,?)";
//		PreparedStatement pstmt = conn.prepareStatement(sql);
//		for(int i=0;i<users.size();i++) {
//			User user = users.get(i);
//			//pstmt.setLong(1, user.getId());
//			pstmt.setString(1, user.getUserName());
//			pstmt.setString(2, user.getPhone());
//			pstmt.setString(3, user.getPassword());
//			pstmt.setString(4, user.getSalt());
//			pstmt.setString(5, user.getHead());
//			pstmt.setInt(6, user.getLoginCount());
//			pstmt.setTimestamp(7, new Timestamp(user.getRegisterDate().getTime()));
//			pstmt.setTimestamp(8, new Timestamp(user.getRegisterDate().getTime()));
//			pstmt.addBatch();
//		}
//		pstmt.executeBatch();
//		pstmt.close();
//		conn.close();
//		System.out.println("insert to db");

		//登录，生成token
		String urlString = "http://localhost:8888/user/login";
		File file = new File("./res/tokens.txt");
		if(file.exists()) {
			file.delete();
		}
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		file.createNewFile();
		raf.seek(0);
		for(int i=0;i<users.size();i++) {
			// 获取用户信息
			User user = users.get(i);
			// 创建网络连接
			URL url = new URL(urlString);
			HttpURLConnection co = (HttpURLConnection)url.openConnection();
			co.setRequestMethod("POST");
			co.setDoOutput(true);
			OutputStream out = co.getOutputStream();
			// 设置请求参数
			String params = "mobile="+user.getPhone()+"&password="+MD5Util.inputPassToFormPass("123456");
			out.write(params.getBytes());
			// 提交请求
			out.flush();
			// 获取返回体
			InputStream inputStream = co.getInputStream();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte buff[] = new byte[1024];
			int len = 0;
			while((len = inputStream.read(buff)) >= 0) {
				bout.write(buff, 0 ,len);
			}
			inputStream.close();
			bout.close();
			// 解析返回体
			String response = new String(bout.toByteArray());
			JSONObject jo = JSON.parseObject(response);
			String token = Integer.toString(jo.getString("data").hashCode());
			System.out.println("create token : " + user.getId());

			String row = user.getId()+","+token;
			raf.seek(raf.length());
			raf.write(row.getBytes());
			raf.write("\r\n".getBytes());
			System.out.println("write to file : " + user.getId());
		}
		raf.close();
		
		System.out.println("over");
	}

}
