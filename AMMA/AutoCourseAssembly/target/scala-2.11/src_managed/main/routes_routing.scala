// @SOURCE:B:/Documents/GitHub/AMMA/AutoCourseAssembly/conf/routes
// @HASH:18da69f7cfff74c60db39aeea55dc21cb84dc1c7
// @DATE:Tue Apr 19 22:01:26 CEST 2016


import play.core._
import play.core.Router._
import play.core.Router.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._
import _root_.controllers.Assets.Asset
import _root_.play.libs.F

import Router.queryString

object Routes extends Router.Routes {

import ReverseRouteContext.empty

private var _prefix = "/"

def setPrefix(prefix: String): Unit = {
  _prefix = prefix
  List[(String,Routes)]().foreach {
    case (p, router) => router.setPrefix(prefix + (if(prefix.endsWith("/")) "" else "/") + p)
  }
}

def prefix = _prefix

lazy val defaultPrefix = { if(Routes.prefix.endsWith("/")) "" else "/" }


// @LINE:6
private[this] lazy val controllers_Application_index0_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix))))
private[this] lazy val controllers_Application_index0_invoker = createInvoker(
controllers.Application.index,
HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "index", Nil,"GET", """ Home page""", Routes.prefix + """"""))
        

// @LINE:9
private[this] lazy val controllers_Assets_at1_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("assets/"),DynamicPart("file", """.+""",false))))
private[this] lazy val controllers_Assets_at1_invoker = createInvoker(
controllers.Assets.at(fakeValue[String], fakeValue[String]),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Assets", "at", Seq(classOf[String], classOf[String]),"GET", """ Map static resources from the /public folder to the /assets URL path""", Routes.prefix + """assets/$file<.+>"""))
        

// @LINE:11
private[this] lazy val controllers_Application_createTask2_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("start"))))
private[this] lazy val controllers_Application_createTask2_invoker = createInvoker(
controllers.Application.createTask(fakeValue[String], fakeValue[String]),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "createTask", Seq(classOf[String], classOf[String]),"GET", """""", Routes.prefix + """start"""))
        

// @LINE:12
private[this] lazy val controllers_Application_checkTask3_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("check"))))
private[this] lazy val controllers_Application_checkTask3_invoker = createInvoker(
controllers.Application.checkTask(fakeValue[Long]),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "checkTask", Seq(classOf[Long]),"GET", """""", Routes.prefix + """check"""))
        

// @LINE:14
private[this] lazy val controllers_Application_getTaskResult4_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("result"))))
private[this] lazy val controllers_Application_getTaskResult4_invoker = createInvoker(
controllers.Application.getTaskResult(fakeValue[Long]),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "getTaskResult", Seq(classOf[Long]),"GET", """""", Routes.prefix + """result"""))
        
def documentation = List(("""GET""", prefix,"""controllers.Application.index"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """start""","""controllers.Application.createTask(url:String, prefix:String ?= "")"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """check""","""controllers.Application.checkTask(id:Long)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """result""","""controllers.Application.getTaskResult(id:Long)""")).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
  case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
  case l => s ++ l.asInstanceOf[List[(String,String,String)]]
}}
      

def routes:PartialFunction[RequestHeader,Handler] = {

// @LINE:6
case controllers_Application_index0_route(params) => {
   call { 
        controllers_Application_index0_invoker.call(controllers.Application.index)
   }
}
        

// @LINE:9
case controllers_Assets_at1_route(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        controllers_Assets_at1_invoker.call(controllers.Assets.at(path, file))
   }
}
        

// @LINE:11
case controllers_Application_createTask2_route(params) => {
   call(params.fromQuery[String]("url", None), params.fromQuery[String]("prefix", Some(""))) { (url, prefix) =>
        controllers_Application_createTask2_invoker.call(controllers.Application.createTask(url, prefix))
   }
}
        

// @LINE:12
case controllers_Application_checkTask3_route(params) => {
   call(params.fromQuery[Long]("id", None)) { (id) =>
        controllers_Application_checkTask3_invoker.call(controllers.Application.checkTask(id))
   }
}
        

// @LINE:14
case controllers_Application_getTaskResult4_route(params) => {
   call(params.fromQuery[Long]("id", None)) { (id) =>
        controllers_Application_getTaskResult4_invoker.call(controllers.Application.getTaskResult(id))
   }
}
        
}

}
     