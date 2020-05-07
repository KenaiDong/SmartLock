package com.xuzd.commons.utils;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.xuzd.commons.baseclass.Constant;

/**
 * 从session中获取数据
 *
 *
 */
public class SessionUtils {

    // private final static Logger logger = LoggerFactory.getLogger(SessionUtils.class);

    /**
     * 在session中获取当前登录用户Id
     * @param session
     * @return
     */
    public static String getCurrentUserIdBySession(HttpSession session) {
        JSONObject userAccountObj = getCurrentUserFromSession(session);
        if (userAccountObj == null) {
            return null;
        }
        return userAccountObj.getString("id");
    }

    /**
     * 在session中获取当前登录用户Id
     * @param session
     * @return
     */
    public static Long getCurrentUserIdFromSession(HttpSession session) {
        JSONObject userAccountObj = getCurrentUserFromSession(session);
        if (userAccountObj == null) {
            return null;
        }
        return userAccountObj.getLong("id");
    }

    /**
     * 在session中获取当前登录用户
     * @param session
     * @return
     */
    public static JSONObject getCurrentUserFromSession(HttpSession session) {
        JSONObject json = (JSONObject) session.getAttribute(Constant.SESSION_USER);
        // logger.info("user in session: " + json);
        return json;
    }

    /*
     * 获取当前session中的SiteRole信息
     * @param session
     * @return
    public static JSONArray getSessionUserRoles(HttpSession session) {
        return (JSONArray) session.getAttribute(Constant.SITE_USER_ROLES);
    }*/

    /**
     * 获取当前session中的SiteRoleId
     * @param session
     * @return
     */
    /*public static String getSessionUserRoleIds(HttpSession session) {
        String roleIds = "";
        JSONArray roles = getSessionUserRoles(session);
        if (roles == null){
            return roleIds;
        }
        for (int i = 0; i < roles.size(); i++) {
            JSONObject role = roles.getJSONObject(i);
            roleIds += "," + role.getLong("id");
        }
        if (!roleIds.isEmpty()) {
            return roleIds.substring(1);
        }
        return null;
    }*/

    /**
     * 获取当前session中的SiteRoleId
     * @param session
     * @return
     */
    /*public static JSONArray getSessionUserRoleIdArr(HttpSession session) {
        JSONArray roleIds = new JSONArray();
        JSONArray roles = getSessionUserRoles(session);
        for (int i = 0; i < roles.size(); i++) {
            JSONObject role = roles.getJSONObject(i);
            roleIds.add(role.getString("id"));
        }
        return roleIds;
    }*/

//    /**
//     * 获取当前session中的SiteDomain信息
//     * @param session
//     * @return
//     */
//    public static JSONArray getSessionUserDomain(HttpSession session) {
//        return (JSONArray) session.getAttribute(Constant.SITE_USER_DOMAINS);
//    }

//    /**
//     * 获取当前session中的domain ids 信息
//     * @param session
//     * @return
//     */
//    public static Long[] getSessionUserDomainIds(HttpSession session) {
//        return (Long[]) session.getAttribute(Constant.SITE_USER_DOMAIN_IDS);
//    }

//    /**
//     * 获取默认域的id
//     * @param session
//     * @return
//     */
//    public static Long getSessionDefaultDomainId(HttpSession session) {
//        return (Long) session.getAttribute(Constant.SITE_DEFAULT_DOMAIN_ID);
//    }

    /*public static boolean isSuperAdmin(HttpSession session) {
        Object isSuperAdmin = session.getAttribute(Constant.IS_SUPER_ADMIN);
        if (isSuperAdmin == null) {
            return false;
        }
        return (boolean) isSuperAdmin;
    }

    public static boolean isSystemAdmin(HttpSession session) {
        Object isSystemAdmin = session.getAttribute(Constant.IS_SYSTEM_ADMIN);
        if (isSystemAdmin == null) {
            return false;
        }
        return (boolean) isSystemAdmin;
    }

    public static boolean isAuditAdmin(HttpSession session) {
        Object isAuditAdmin = session.getAttribute(Constant.IS_AUDIT_ADMIN);
        if (isAuditAdmin == null) {
            return false;
        }
        return (boolean) isAuditAdmin;
    }

    public static boolean isPolicyAdmin(HttpSession session) {
        Object isPoliceAdmin = session.getAttribute(Constant.IS_POLICY_ADMIN);
        if (isPoliceAdmin == null) {
            return false;
        }
        return (boolean) isPoliceAdmin;
    }*/

    /**
     * 获取session中的license信息
     * @param session
     * @return
     */
    // public static JSONObject getSessionLicense(HttpSession session) {
    // return (JSONObject) session.getAttribute(Constant.LICENSE);
    // }

}
