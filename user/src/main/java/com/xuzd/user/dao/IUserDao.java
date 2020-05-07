package com.xuzd.user.dao;

import com.xuzd.commons.db.repository.ICustomRepository;
import com.xuzd.user.entity.User;
import com.xuzd.user.model.UserStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IUserDao extends ICustomRepository<User, Long> {
    @Query("from User u where u.loginName = ?1 and u.status <> ?2")
    List<User> findByLoginNameAndNotStatus(String loginName, UserStatus userStatus);

    @Query("from User u where u.loginName = ?1 and u.status = ?2")
    User findByLoginName(String loginName, UserStatus userStatus);

    /*@Query("from User u where u.employeeCode = ?1 and u.status = ?2")
    List<User> findByCode(String employeeCode, UserStatus userStatus);

    @Query("from User u where u.employeeCode = ?1 and u.status != 'ToDeleted'")
    List<User> findByCodeNotDeleted(String employeeCode);*/

    @Query("from User u where u.phone = ?1 and u.status = ?2")
    List<User> findByPhone(String phone, UserStatus userStatus);

    @Query("from User u where u.phone = ?1 and u.status != 'ToDeleted'")
    List<User> findByPhoneNotDeleted(String phone);

    @Query("from User u where u.email = ?1 and u.status = ?2")
    List<User> findByEmail(String email, UserStatus userStatus);

    @Query("from User u where u.email = ?1 and u.status != 'ToDeleted'")
    List<User> findByEmailNotDeleted(String email);

    @Query("from User u where u.loginName = ?1 order by createDate desc")
    List<User> findByLoginName(String loginName);

    @Query("from User u where u.loginName = ?1 and u.status != 'ToDeleted'")
    List<User> findByLoginNameNotDeleted(String loginName);

    /*@Modifying
    @Query("update User u set u.limitDate = null where u.id = ?1")
    void updateUserLimitDateNull(Long userId);*/

    User findByIdAndStatus(Long Id, UserStatus status);

    Optional<User> findById(Long Id);

    List<User> findByStatus(UserStatus status);

    List<User> findByUserName(String userName);

    /*@Query("from User u where u.userName = ?1 and (u.limitDate is null or u.limitDate >= CURRENT_TIMESTAMP) and u.status != 'ToDeleted'")
    List<User> findByUserNameNonExpired(String userName);*/

    /*@Modifying
    @Query("update User u set u.departmentId = ?1 where u.id in ?2")
    void updateDepartmentIdByUserIds(Long departmentId, List<Long> userIds);*/

    /*List<User> findAllByOriginalAndStatusNot(Boolean original, UserStatus status);*/

    /*List<User> findAllByDepartmentIdInAndStatus(Set<Long> departmentIds, UserStatus status);*/

    /*@Query("from User u where u.id not in (?1) and u.status = ?2 and (u.limitDate is null or u.limitDate >= CURRENT_TIMESTAMP) ")
    List<User> findAllByIdNotInAndStatus(List<Long> ids, UserStatus status);*/

    List<User> findAllByStatusNot(UserStatus status);

    /*Long countAllByDepartmentIdInAndStatus(Set<Long> departmentId, UserStatus status);
    @Query("from User u where u.id in (?1) and u.status != 'ToDeleted'")
    List<User> findUsersByIds(Long[] ids);*/

    List<User> findAllByIdInAndStatus(List<Long> ids, UserStatus status);

}
