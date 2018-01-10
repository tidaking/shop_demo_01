package com.robin.service.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.robin.utils.MailUtils;
import com.robin.bean.User;
import com.robin.constant.Constant;
import com.robin.dao.UserDao;
import com.robin.dao.impl.UserDaoImpl;
import com.robin.service.UserService;

public class UserServiceImpl implements UserService {
	
	@Override
	public User login(String username, String password) throws SQLException {
		UserDao dao = new UserDaoImpl();
		User user = dao.findUser(username, password);
		return user;
	}

	@Override
	public boolean regist(User user) throws SQLException{
		UserDao dao = new UserDaoImpl();
		
		
		
		
		//int update = dao.saveUser(user);
		int update = dao.saveUserInTransAction(user);
		if(update != 1)
		{
			//注册失败
			System.out.println("[UserService][regist]:regist faild!ret="+update);
			dao.rollback();
			return false;
		}
		else
		{
			//注册成功
			System.out.println("[UserService][regist]:regist success");
			//发一封激活邮件给用户
			System.out.println("[UserService][regist]:Sending Email");
			try {
				MailUtils.sendMail(user.getEmail(),"尊敬的"+user.getName()+":欢迎注册黑马商城!请点击下面的链接,进行激活.<a href='http://localhost/shop_project_1/userServlet?method=active&code="+user.getCode()+"'>点击激活</a>");
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				dao.rollback();
				System.out.println("[UserService][regist]:Email send faild!RollBack!");
				return false;
			}
			dao.commit();
			System.out.println("[UserService][regist]:Email send success!");
			return true;
		}
	}

	@Override
	public boolean active(String code) throws SQLException {
		UserDao dao = new UserDaoImpl();
		User user = dao.findUser(code);
		if(user != null)
		{
			user.setCode(null);
			user.setState(Constant.USER_ACTIVED);
			int update = dao.updateUser(user);
			if(update != 1)
			{
				return false;
			}else {
				return true;
			}
		}
		return false;
	}
}
