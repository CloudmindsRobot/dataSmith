// 用户类，记录当前登录用户信息
class User {
  constructor (account, name, systems, staffId, tenantCode, tenantName, tenantId, branchId, vopId, logo, mobile, email, adminFlag) {
    this.account = account
    this.name = name
    this.systems = systems
    this.staffId = staffId
    this.tenantCode = tenantCode
    this.tenantName = tenantName
    this.tenantId = tenantId
    this.branchId = branchId
    this.vopId = vopId
    this.logo = logo
    this.mobile = mobile
    this.email = email
    this.adminFlag = adminFlag
  }
}

export default User
