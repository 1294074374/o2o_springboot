package com.imooc.o2o.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.LocalAuthDao;
import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.exception.LocalAuthOperationException;
import com.imooc.o2o.service.LocalAuthService;
import com.imooc.o2o.util.MD5;
@Service
public class LocalAuthServiceImpl implements LocalAuthService {

	@Autowired
	private LocalAuthDao localAuthDao;

	@Override
	public LocalAuth getLocalAuthByUserNameAndPwd(String userName, String password) {
		return localAuthDao.queryLocalAuthByUserNameAndPwd(userName, MD5.getMd5(password));
		//return localAuthDao.queryLocalAuthByUserNameAndPwd(userName,password);
	}

	@Override
	public LocalAuth getLocalAuthByUserId(long userId) {
		return localAuthDao.queryLocalAuthById(userId);
	}

	@Override
	@Transactional
	public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
		// 空值判断 且传入的密码和userId 不能为空
		if (localAuth == null || localAuth.getPassword() == null || localAuth.getUserId() == null
				|| localAuth.getPersonInfo() == null || localAuth.getPersonInfo().getUserId() == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		// 查询此用户是否已经绑定过平台
		LocalAuth tempAuth = localAuthDao.queryLocalAuthById(localAuth.getUserId());
		if (tempAuth != null) {
			return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
		}
		try {
			localAuth.setCreateTime(new Date());
			localAuth.setLastEditTime(new Date());
			// 对密码进行MD5加密
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			int effectedNum = localAuthDao.insertLocalAuth(localAuth);
			if (effectedNum <= 0) {
				throw new LocalAuthOperationException("添加用户失败");
			} else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
			}
		} catch (Exception e) {
			throw new LocalAuthOperationException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public LocalAuthExecution modifyLocalAuth(Long userId, String userName, String password, String newPassword,
			Date lastEditTime) throws LocalAuthOperationException {
		// 非空判断
		if (userId != null || userName != null || newPassword != null && !password.equals(newPassword)) {
			try {
				int effectedNum = localAuthDao.updateLocalAuth(userId, userName, MD5.getMd5(password),
						MD5.getMd5(newPassword), lastEditTime);
				if (effectedNum <= 0) {
					throw new LocalAuthOperationException("账号密码修改失败");
				} else {
					return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
				}
			} catch (Exception e) {
				throw new LocalAuthOperationException("更新密码失败:" + e.toString());
			}
		}
		return null;
	}

}
