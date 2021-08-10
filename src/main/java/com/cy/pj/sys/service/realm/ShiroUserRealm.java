package com.cy.pj.sys.service.realm;

import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 借助此对象基于数据层对象"获取"认证信息,权限信息
 */
@Service
public class ShiroUserRealm extends AuthorizingRealm {//我们写的realm必须符合realm规范

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    @Autowired
    private SysMenuDao sysMenuDao;

    /**
     * 指定加密算法和次数(这里可以重写setCredentialsMatcher或getCredentialsMatcher)
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        //构建凭证匹配对象
        HashedCredentialsMatcher cMatcher = new HashedCredentialsMatcher("MD5");
        //设置加密次数 算法
        cMatcher.setHashIterations(1);
        super.setCredentialsMatcher(cMatcher);//注意此位置的值
    }

//    @Override
//    public CredentialsMatcher getCredentialsMatcher() {
//        //构建凭证匹配对象
//        HashedCredentialsMatcher cMatcher = new HashedCredentialsMatcher("MD5");
//        //设置加密次数 算法
//        cMatcher.setHashIterations(1);
//        return cMatcher;
//    }

    /**
     * 通过此方法完成认证数据的获取及封装,系统
     * 底层会将认证数据传递(SecurityManage)认证管理器，由认证
     * 管理器调用authenticate方法完成认证操作。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.获取用户名(用户页面输入)
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        //2.基于用户名查询用户信息
        SysUser user = sysUserDao.findUserByUserName(username);
        //3.判定用户是否存在
        if (user == null) {
            throw new UnknownAccountException();
        }
        //4.判定用户是否已经被禁用
        if (user.getValid() == 0) {
            throw new LockedAccountException();
        }
        //5.封装用户信息
        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getSalt());
        //记住,构建什么对象要看方法的返回值
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,//principal(身份)结合业务自己赋予具体对象
                user.getPassword(),//hashedCredentials 已加密的密码
                credentialsSalt,//credentialsSalt 对登录密码进行加密时使用的盐
                getName());//realmName
        //6.返回封装结果
        return info;//返回值会传递给认证管理器(后续)
        //认证管理器会通过此信息完成认证操作
    }

    /**
     * 基于此方法进行授权信息的获取和封装,会将封装好的数据传递给SecurityManage对象,由此对象
     * 进行用户权限检测和授权
     * 回顾:项目中业务的实现
     * 1)角色和菜单是多对多的关系(关系的维护方在sys_role_menus)
     * 2)用户和角色是多对多的关系(关系的维护在sys_user_roles)
     * 思考:业务中的资源分配过程
     * 1)将菜单资源授予角色
     * 2)用户授予角色对象
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("================doGetAuthorizationInfo=========");
        //1.获取登陆用户信息
        SysUser user = (SysUser) principals.getPrimaryPrincipal();
        //2.基于登陆的用户获取用户对应的角色id并校验
        List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(user.getId());
        if (roleIds == null || roleIds.size() == 0) {
            throw new AuthorizationException();
        }
        //3.基于角色id获取角色对应的菜单并校验
        List<Integer> menuIds
                = sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(new Integer[]{}));
        if (menuIds == null || menuIds.size() == 0) {
            throw new AuthorizationException();
        }
        //4.基于菜单id获取菜单模块的授权表示(sys:user:update)
        List<String> permissionsList = sysMenuDao.findPermissions(menuIds.toArray(new Integer[]{}));
        if (permissionsList == null || permissionsList.size() == 0) {//添加操作时设置有误
            throw new AuthorizationException();
        }
        //5.封装结果并返回
        Set<String> permissionsSet = new HashSet<>();
        for (String permissions : permissionsList) {
            if (!StringUtils.isEmpty(permissions)) {
                permissionsSet.add(permissions);
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permissionsSet);
        return info;//此对象会返回给securityManage对象
    }


}
