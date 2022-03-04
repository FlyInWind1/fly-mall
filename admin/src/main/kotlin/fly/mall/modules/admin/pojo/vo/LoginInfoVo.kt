package fly.mall.modules.admin.pojo.vo

import fly.mall.modules.admin.pojo.po.UmsMenuPo

class LoginInfoVo(
    var username: String
) {

    var menus: List<UmsMenuPo>? = null

    var icon: String? = null

    var roles: List<String>? = null

}