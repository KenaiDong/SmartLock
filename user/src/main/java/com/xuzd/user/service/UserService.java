package com.xuzd.user.service;

import com.xuzd.commons.db.Specifications;
import com.xuzd.commons.model.PageModel;
import com.xuzd.commons.utils.EncryptUtil;
import com.xuzd.commons.utils.StringUtils;
import com.xuzd.user.dao.IUserDao;
import com.xuzd.user.entity.User;
import com.xuzd.user.model.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private IUserDao userDao;

/**
 * 新增用户
 * @param user
 * @return
 * @throws Exception
 */
    @Transactional
    public User addUser(User user, Long[] roleIds) throws Exception {
        user.setPassword(EncryptUtil.shaAndMd5(user.getPassword()));
        // 保存用户
        User save_user = addUser(user);
        return save_user;
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     * @throws Exception
     */
    @Transactional
    public User addUser(User user) throws Exception {
        user = userDao.save(user);
        // 新增的用户信息发送到MQ
        /*JSONObject json = new JSONObject();
        json.put("user", user);
        UserRedisService.put(user.getId().toString(), user);
        outbox.sendStringOnTopic(EventTopicConstants.SIMO_SEND_USER, json.toJSONString());*/
        return user;
    }

    /**
 * 更新用户基本信息
 *
 * @param user
 * @return
 * @throws Exception
 */
    @Transactional
    public User updateUser(User user) throws Exception {
        user = userDao.update(user.getId(), user);
        /*// 修改的用户信息发送到MQ
        JSONObject json = new JSONObject();
        json.put("user", user);
        UserRedisService.put(user.getId().toString(), user);
        outbox.sendStringOnTopic(EventTopicConstants.SIMO_SEND_USER, json.toJSONString());*/
        return user;
    }

    /**
 * 更新用户密码
 *
 * @param user
 * @return
 * @throws Exception
 */
    @Transactional
    public User updatePwd(User user) throws Exception {
        return updateUser(user);
    }

    /**
 * 根据id查询用户
 *
 * @param id
 * @return
 */
    public User findById(Long id) {
        User user = userDao.findOne(id);
        return user;
    }

    /**
 * 查询所有用户
 *
 * @return 用户列表
 */
    public List<User> findAll() {
        return userDao.findAll();
    }

    /**
 * 查询所有正常状态用户
 *
 * @return 用户列表
 */
    public List<User> findAllEnable() {
        return userDao.findByStatus(UserStatus.OK);
    }

    public User findByName(String loginName) {
        return userDao.findByLoginName(loginName, UserStatus.OK);
    }

    public List<User> findByPhone(String phone){
        return userDao.findByPhone(phone, UserStatus.OK);
    }

    /**
            * 分页查询用户列表
     *
             * @param pageModel 分页参数
     * @param user 条件user
     * @return PageModel中的obj就是查询结果集
     */
    public PageModel findNotByPage(PageModel pageModel, User user) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createDate");
        Sort sort = Sort.by(order);
        // FIXME 需要对条件查询进行补充
        Specification<User> spec = new Specifications<User>()
                .eq(StringUtils.isStrictNotEmpty(user.getLoginName()), "loginName", user.getLoginName())
                .eq(StringUtils.isStrictNotEmpty(user.getUserName()), "userName", user.getUserName())
                .eq(StringUtils.isStrictNotEmpty(user.getPhone()), "phone", user.getPhone())
                .eq(StringUtils.isStrictNotEmpty(user.getEmail()), "email", user.getEmail())
                .ne(null != user.getStatus(), "status", user.getStatus()).ne(null != user.getId(), "id", user.getId())
                .build();
        return userDao.findAll(spec, pageModel, sort);
    }

}



 /**
     * 根据条件检索用户，并查询该用户下的所有域和角色信息
     * @param criteria  检索条件
     */
/*public UserCriteria search(UserCriteria criteria) {
    criteria.doSearch(userDao);
    if (Objects.isNull(criteria.getObject())) {
        return criteria;
    }
    List<User> users = (List<User>) criteria.getObject();
    if (CollectionUtils.isEmpty(users)) {
        return criteria;
    }
    List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
    // 根据用户ids查询所有的角色
    List<UserRoleDto> roles = roleService.findByUserIds(userIds, STATUS_NORMAL);
    JSONArray userArray = new JSONArray();
    for (User user : users) {
        JSONObject userJson = JSON.parseObject(JSON.toJSONString(user));
        if (!roles.isEmpty()) {
            userJson.put("roles",
                    roles.stream().filter(e -> e.getUserId().equals(user.getId())).collect(Collectors.toList()));
        }
        SiteDepartment department = departmentService.findOne(user.getDepartmentId());
        if (department != null) {
            userJson.put("departmentName", department.getName());
        }

        userArray.add(userJson);
    }
    criteria.setObject(userArray);
    return criteria;
}

    *//**
     * 超级管理员/普通用户查询用户列表
     *
     * @param pageModel 分页参数
     * @return PageModel中的obj就是查询结果集
     *//*
    @SuppressWarnings("unchecked")
    public PageModel findAllWithDomain(PageModel pageModel, UserQuery userQuery) {
        // 构造JPQL查询语句
        Map<String, Object> params = userQuery.createParams();
        List<User> users = (List<User>) userDao.findByJPQL(userQuery.createJpql(), pageModel, params,
                User.class);
        List<Long> userIds = users.stream().map(user -> user.getId()).collect(Collectors.toList());

        *//**
         * 添加角色信息
         *
         *//*
        List<UserRole> userRoles = userRoleDao.findByUserIdIn(userIds);
        List<Long> roleIds = userRoles.stream().map(userRole -> userRole.getRoleId()).collect(Collectors.toList());
        List<SiteRole> roles = iSiteRoleDao.findByIdIn(roleIds);
        JSONArray usersJsonArray = (JSONArray) JSON.toJSON(users);
        for (int i = 0; i < usersJsonArray.size(); i++) {
            JSONObject userJsonObject = usersJsonArray.getJSONObject(i);
            for (UserRole UserRole : userRoles) {
                if (UserRole.getUserId() == userJsonObject.get("id")) {
                    for (SiteRole siteRole : roles) {
                        if (siteRole.getId() == UserRole.getRoleId()) {
                            String role = userJsonObject.getString("role");
                            userJsonObject.put("role", null == role || ("").equals(role) ? siteRole.getName()
                                    : role + "," + siteRole.getName());
                        }
                    }
                }
            }
        }

        Long count = userDao.countByJPQL(userQuery.createCountJpql(), params);
        pageModel.setCount(count);
        pageModel.setObject(usersJsonArray);
        return pageModel;
    }

    *//**
     * 分页查询用户列表
     *
     * @param pageModel 分页参数
     * @param user 条件user
     * @return PageModel中的obj就是查询结果集
     *//*
    public PageModel findAllByPage(PageModel pageModel, User user) {
        Order order = new Order(Direction.DESC, "createDate");
        Sort sort = Sort.by(order);
        // FIXME 需要对条件查询进行补充
        Specification<User> spec = new Specifications<User>()
                .eq(StringUtils.isStrictNotEmpty(user.getLoginName()), "loginName", user.getLoginName())
                .eq(StringUtils.isStrictNotEmpty(user.getUserName()), "userName", user.getUserName())
                .eq(StringUtils.isStrictNotEmpty(user.getPhone()), "phone", user.getPhone())
                .eq(StringUtils.isStrictNotEmpty(user.getEmail()), "email", user.getEmail())
                .eq(StringUtils.isStrictNotEmpty(user.getEmployeeCode()), "employeeCode", user.getEmployeeCode())
                .eq(null != user.getStatus(), "status", user.getStatus()).build();
        return userDao.findAll(spec, pageModel, sort);
    }

    *//**
     * 分页查询用户列表
     *
     * @param pageModel 分页参数
     * @param user 条件user
     * @return PageModel中的obj就是查询结果集
     *//*
    public PageModel findNotByPage(PageModel pageModel, User user) {
        Order order = new Order(Direction.DESC, "createDate");
        Sort sort = Sort.by(order);
        // FIXME 需要对条件查询进行补充
        Specification<User> spec = new Specifications<User>()
                .eq(StringUtils.isStrictNotEmpty(user.getLoginName()), "loginName", user.getLoginName())
                .eq(StringUtils.isStrictNotEmpty(user.getUserName()), "userName", user.getUserName())
                .eq(StringUtils.isStrictNotEmpty(user.getPhone()), "phone", user.getPhone())
                .eq(StringUtils.isStrictNotEmpty(user.getEmail()), "email", user.getEmail())
                .eq(StringUtils.isStrictNotEmpty(user.getEmployeeCode()), "employeeCode", user.getEmployeeCode())
                .ne(null != user.getStatus(), "status", user.getStatus()).ne(null != user.getId(), "id", user.getId())
                .build();
        return userDao.findAll(spec, pageModel, sort);
    }

    @Transactional
    public User createOrGet(String loginName, String name, String concat, String email) {
        synchronized (UserService.class) {
            User user = null;
            List<User> users = userDao.findByLoginName(loginName);
            if (users.size() > 1) {
                return null;
            }
            if (users.size() == 1) {
                user = users.get(0);
            }
            if (user == null) {
                user = new User();
                user.setLoginName(loginName);
                user.setEmail(email);
                user.setPhone(concat);
                String uuid = UUID.randomUUID().toString().replaceAll("\\-", "");
                user.setEmployeeCode(loginName + "_" + uuid);
                user.setPassword(UUID.randomUUID().toString());
                user.setStatus(UserStatus.OK);
                user.setUserName(Strings.isNullOrEmpty(name) ? loginName : name);
            }
            try {
                if (user.getId() == null) {
                    SiteRole role = roleService.getOrNewEmptyRole();
                    return addUser(user, new Long[] { role.getId() });
                } else {
                    return addUser(user, new Long[0]);
                }
            } catch (Exception e) {
            }

            return null;
        }
    }

    *//**
     * 新增用户
     *
     * @param user
     * @return
     * @throws Exception
     *//*
    @Transactional
    public User addUser(User user) throws Exception {
        user = userDao.save(user);
        // 新增的用户信息发送到MQ
        JSONObject json = new JSONObject();
        json.put("user", user);
        UserRedisService.put(user.getId().toString(), user);
        outbox.sendStringOnTopic(EventTopicConstants.SIMO_SEND_USER, json.toJSONString());
        return user;
    }

    *//**
     * 新增用户
     *
     * @param user
     * @return
     * @throws Exception
     *//*
    @Transactional
    public User addUser(User user, Long[] roleIds) throws Exception {
        user.setPassword(EncryptUtil.shaAndMd5(user.getPassword()));
        // 保存用户
        User save_user = addUser(user);
        // 保存角色关系
        List<UserRole> userRoles = new ArrayList<UserRole>();
        for (int i = 0; i < roleIds.length; i++) {
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleIds[i]);
            userRole.setUserId(save_user.getId());
            userRoles.add(userRole);
        }
        userRoleDao.saveAll(userRoles);
        return save_user;
    }

    *//**
     * 更新用户基本信息
     *
     * @param user
     * @return
     * @throws Exception
     *//*
    @Transactional
    public User updateUser(User user) throws Exception {
        user = userDao.update(user.getId(), user);
        // 修改的用户信息发送到MQ
        JSONObject json = new JSONObject();
        json.put("user", user);
        UserRedisService.put(user.getId().toString(), user);
        outbox.sendStringOnTopic(EventTopicConstants.SIMO_SEND_USER, json.toJSONString());
        return user;
    }

    *//**
     * 更新用户密码
     *
     * @param user
     * @return
     * @throws Exception
     *//*
    @Transactional
    public User updatePwd(User user) throws Exception {
        return updateUser(user);
    }

    *//**
     * 根据id查询用户
     *
     * @param id
     * @return
     *//*
    public User findById(Long id) {
        User user = userDao.findOne(id);
        return user;
    }

    *//**
     * 查询所有用户
     *
     * @return 用户列表
     *//*
    public List<User> findAll() {
        return userDao.findAll();
    }

    *//**
     * 查询所有正常状态用户
     *
     * @return 用户列表
     *//*
    public List<User> findAllEnable() {
        return userDao.findByStatus(UserStatus.OK);
    }

    *//**
     * 查询所有正常的非管理员用户
     *//*
    public List<User> findAllEnableNotAdmin(UserCriteria criteria) {
        criteria.setRoleEnumsIn(Lists.newArrayList(RoleEnum.Custom));
        criteria.setStatusIn(Lists.newArrayList(UserStatus.OK));
        UserCriteria result = this.search(criteria);
        if (Objects.isNull(result) || Objects.isNull(result.getObject())) {
            return new ArrayList<>();
        }
        return JSONArray.parseArray(JSON.toJSONString(result.getObject()), User.class);
    }

    *//**
     * 检查工号是否已经被占用
     *
     * @param employeeId
     * @param employeeCode
     * @return
     *//*
    public boolean uniqueEmployeeCode(Long employeeId, String employeeCode, UserStatus userStatus) {
        List<User> users = userDao.findByCodeNotDeleted(employeeCode);
        if (users != null && users.size() > 0) {
            if (employeeId != 0 && users.get(0).getId().equals(employeeId)) {
                return true;
            }
            return false;
        }
        return true;
    }

    *//**
     * 检查登录名是否已经被占用
     *
     * @param employeeId
     * @param loginName
     * @return
     *//*
    public boolean uniqueLoginName(Long employeeId, String loginName) {
        List<User> users = userDao.findByLoginNameNotDeleted(loginName);
        if (users != null && users.size() > 0) {
            if (employeeId != 0 && users.get(0).getId().equals(employeeId)) {
                return true;
            }
            return false;
        }
        return true;
    }

    *//**
     * 检查Email是否已经被占用
     *
     * @param employeeId
     * @param email
     * @return
     *//*
    public boolean uniqueEmail(Long employeeId, String email, UserStatus userStatus) {
        List<User> users = userDao.findByEmailNotDeleted(email);
        if (users != null && users.size() > 0) {
            if (employeeId != 0 && users.get(0).getId().equals(employeeId)) {
                return true;
            }
            return false;
        }
        return true;
    }

    *//**
     * 检查电话号码是否已经被占用
     *
     * @param employeeId
     * @param telephone
     * @return
     *//*
    public boolean uniquePhone(Long employeeId, String telephone, UserStatus userStatus) {
        List<User> users = userDao.findByPhoneNotDeleted(telephone);
        if (users != null && users.size() > 0) {
            if (employeeId != 0 && users.get(0).getId().equals(employeeId)) {
                return true;
            }
            return false;
        }
        return true;
    }

    *//**
     * 将用户过期时间设置为null，表示永不过期
     *
     * @param userId
     *//*
    @Transactional
    public void updateUserLimitDateNull(Long userId) {
        userDao.updateUserLimitDateNull(userId);
    }

    *//**
     * 根据roleId查询用户
     *
     * @param roleId
     * @return
     *//*
    @SuppressWarnings("unchecked")
    public List<User> findUserByRoleId(Long roleId, UserStatus status) {
        String jpql = "select su from User su, UserRole sur "
                + "where su.id = sur.userId and su.status = :status " + "and sur.roleId = :roleId";
        Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("roleId", roleId);
        List<User> users = (List<User>) userDao.findByJPQL(jpql, params, User.class);
        Date now = new Date();
        for (int i = 0; i < users.size(); i++) {
            Date limitDate = users.get(i).getLimitDate();
            if (limitDate != null && limitDate.before(now)) {
                users.remove(i);
            }
        }
        return users;
    }

    *//**
     * 根据ID查询User信息
     *
     * @param userId
     * @return
     *//*
    public User findUserById(Long userId, UserStatus status) {
        return userDao.findByIdAndStatus(userId, status);
    }

    *//**
     * 根据ID查询User信息
     *
     * @param userId
     * @return
     *//*
    public User findUserByIdIngoreStatus(Long userId) {
        return userDao.findOne(userId);
    }

    *//**
     * 分页获取域下的用户
     *
     * @param domainId
     * @return
     *//*
    @SuppressWarnings("unchecked")
    public PageModel findDomainUser(PageModel pageModel, Long domainId) {
        String jpql = "select su from User su, SiteDomainUser sdu where sdu.domainId = :domainId "
                + "and sdu.userId = su.id and su.status != 'ToDeleted' order by su.status asc, su.id asc";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("domainId", domainId);
        List<User> users = (List<User>) userDao.findByJPQL(jpql, pageModel, params, User.class);
        jpql = "select count(su.id) from User su, SiteDomainUser sdu where sdu.domainId = :domainId "
                + "and sdu.userId = su.id and su.status != 'ToDeleted'";
        Long count = userDao.countByJPQL(jpql, params);
        pageModel.setCount(count);
        pageModel.setObject(users);
        return pageModel;
    }

    *//**
     * 根据域ID查询正常用户
     *
     * @param domainId 域ID
     * @return
     *//*
    @SuppressWarnings("unchecked")
    public List<User> findUserByDomainId(Long domainId) {
        String jpql = "select su from User su, SiteDomainUser sdu where sdu.userId = su.id and su.status = '"
                + UserStatus.OK.toString() + "' and sdu.domainId = :domainId";
        Map<String, Object> params = new HashMap<>();
        params.put("domainId", domainId);
        return (List<User>) userDao.findByJPQL(jpql, params, User.class);
    }

    *//**
     * 查询系统中所有的超级管理员
     *//*
    @SuppressWarnings({ "unchecked" })
    public List<User> findSuperAdmins() {
        String jpql = "select su from User su, UserRole sur, SiteRole sr"
                + " where su.id = sur.userId and sur.roleId = sr.id and su.status = '" + UserStatus.OK.toString()
                + "' and sr.roleEnum = '" + RoleEnum.Super_Admin.toString() + "'"
                + " order by su.id";
        return (List<User>) userDao.findByJPQL(jpql, User.class);
    }


    *//**
     * 查询初始化的超级管理员，得到系统注册的唯一用户
     *//*
    public User findOriginalAdmin() {
        List<User> users = userDao.findAllByOriginalAndStatusNot(true, UserStatus.ToDeleted);
        return CollectionUtils.isEmpty(users) ? null : users.get(0);
    }

    public List<User> findByName(String loginName) {
        return userDao.findByLoginName(loginName);
    }

    public List<User> findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

    public List<User> findByUserNameNonExpired(String userName) {
        return userDao.findByUserNameNonExpired(userName);
    }

    *//**
     * 查询所有符合条件的用户
     *
     * @param userQuery
     * @return
     *//*
    @SuppressWarnings("unchecked")
    public List<User> findUserList(UserQuery userQuery) {
        Map<String, Object> params = userQuery.createParams();
        List<User> users = (List<User>) userDao.findByJPQL(userQuery.createJpql(), params, User.class);
        return users;
    }

    *//**
     * 根据角色Id获取拥有工单菜单权限的用户
     *
     * @param roleId
     * @param ok
     * @return
     *//*
    @SuppressWarnings("unchecked")
    public List<User> findWorkflowUserByRoleId(Long roleId, UserStatus ok) {
        List<User> workFlowUsers = new ArrayList<>();
        List<User> users = findUserByRoleId(roleId, ok);
        List<User> superAdmins = findSuperAdmins();
        Set<Long> adminIds = null;
        if (CollectionUtils.isNotEmpty(superAdmins)) {
            adminIds = superAdmins.stream().map(User::getId).collect(Collectors.toSet());
        }
        // 查询工单父菜单
        String menuJpql = "from SiteMenu m where m.parentId = 'SIMO' and m.modelName = 'workflow'";
        SiteMenu workFlowMenu = (SiteMenu) menuDao.findByJPQL(menuJpql, SiteMenu.class).get(0);
        for (User user : users) {
            // 如果是超级管理员
            if (adminIds != null && adminIds.contains(user.getId())) {
                return users;
            }
            String jpql = "select distinct(m) from UserRole ur, SiteRoleMenu rm, SiteMenu m "
                    + "where ur.userId = :userId and rm.roleId = ur.roleId and m.id = rm.menuId";
            Map<String, Object> params = new HashMap<>();
            params.put("userId", user.getId());
            List<SiteMenu> menus = (List<SiteMenu>) userDao.findByJPQL(jpql, params, SiteMenu.class);
            if (workFlowMenu != null && menus.contains(workFlowMenu)) {
                workFlowUsers.add(user);
            }
        }
        return workFlowUsers;
    }

    *//**
     * 根据域ID查询正常用户ID
     *
     * @param domainIds 多个域ID用,分隔
     * @return
     *//*
    public List<Long> findUserIdByDomainId(String domainIds) {
        if (StringUtils.isEmpty(domainIds) || "null".equals(domainIds)) {
            return null;
        }
        List<Long> domainIdList = new ArrayList<Long>();
        for (String domainId : domainIds.split(",")) {
            domainIdList.add(Long.valueOf(domainId));
        }
        String jpql = "select distinct su.id from User su, SiteDomainUser sdu where sdu.userId = su.id and su.status = '"
                + UserStatus.OK.toString() + "' and sdu.domainId in (:domainIds)";
        Map<String, Object> params = new HashMap<>();
        params.put("domainIds", domainIdList);
        @SuppressWarnings("unchecked")
        List<Long> userIds = (List<Long>) userDao.findByJPQL(jpql, params, Long.class);
        return userIds;
    }

    public List<User> searchUsers(UserCriteria criteria) {
        List<User> result = new ArrayList<>();
        criteria.doSearch(userDao);
        if (Objects.isNull(criteria.getObject())) {
            return result;
        }
        List<User> users = (List<User>) criteria.getObject();
        if (CollectionUtils.isEmpty(users)) {
            return result;
        }
        return users;
    }

    @Transactional
    public void updateDepartmentIdByIds(List<Long> userIds, Long departmentId) {
        userDao.updateDepartmentIdByUserIds(departmentId, userIds);
    }

    public List<User> findAllOnline() {
        return userDao.findAllByStatusNot(UserStatus.ToDeleted);
    }

    public Long countByDepartmentIdIn(Set<Long> departmentId) {
        return userDao.countAllByDepartmentIdInAndStatus(departmentId, UserStatus.OK);
    }


    public List<User> findAllByDepartmentIdIn(Set<Long> departmentIds) {
        List<User> result = new ArrayList<>();
        if (departmentIds != null && !departmentIds.isEmpty()) {
            result = userDao.findAllByDepartmentIdInAndStatus(departmentIds, UserStatus.OK);
        }
        return result;
    }

    *//**
     * 根据一组id值查询用户
     * @param ids
     * @return
     *//*
    public List<User> findUsersByIds(Long[] ids){
        return userDao.findUsersByIds(ids);
    }

    public List<User> findEnableByIds(List<Long> ids) {
        return userDao.findAllByIdInAndStatus(ids, UserStatus.OK);
    }

    public List<User> findUsersByIdNotIn(List<Long> uids){
        if (uids.isEmpty()) {
            uids.add(0L);
        }
        return userDao.findAllByIdNotInAndStatus(uids, UserStatus.OK);
    }

    *//**
     * 根据一组id值查询用户分页
     * @param ids
     * @return
     *//*
    public PageModel findUsersByIdsPaged(PageModel pageModel,Long[] ids){
        Order order = new Order(Direction.DESC, "createDate");
        Sort sort = Sort.by(order);
        Specification<User> spec = new Specifications<User>().in("id", ids.length>0?ids:new Long[]{-1L}).build();
        PageModel pg = userDao.findAll(spec, pageModel, sort);
        return pg;
    }

    *//**
     * 根据姓名关键字分页查询在用用户
     * @param keyword
     * @param pageModel
     * @return
     *//*
    public PageModel findUsersByKeywordPaged(String keyword, PageModel pageModel){
        Order order = new Order(Direction.DESC, "createDate");
        Sort sort = Sort.by(order);
        Specification<User> spec = new Specifications<User>()
                .like("userName", true, Criteria.escapeLike(keyword == null ? keyword : keyword.toLowerCase()))
                .ne("status", UserStatus.ToDeleted)
                .build();
        PageModel pg = userDao.findAll(spec, pageModel, sort);
        return pg;
    }*/
