/**
 * Logout Controller (Example)
 * @author generated by plugin script
 */
class LogoutController {

  def index = {
    /* ---- save your codes here ----  */
    
    redirect(uri:"/j_spring_security_logout")
    render(text:"")
  }
}
