// @SOURCE:B:/Documents/GitHub/AMMA/AutoCourseAssembly/conf/routes
// @HASH:18da69f7cfff74c60db39aeea55dc21cb84dc1c7
// @DATE:Tue Apr 19 22:01:26 CEST 2016

import Routes.{prefix => _prefix, defaultPrefix => _defaultPrefix}
import play.core._
import play.core.Router._
import play.core.Router.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._
import _root_.controllers.Assets.Asset
import _root_.play.libs.F

import Router.queryString


// @LINE:14
// @LINE:12
// @LINE:11
// @LINE:9
// @LINE:6
package controllers {

// @LINE:9
class ReverseAssets {


// @LINE:9
def at(file:String): Call = {
   implicit val _rrc = new ReverseRouteContext(Map(("path", "/public")))
   Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                        

}
                          

// @LINE:14
// @LINE:12
// @LINE:11
// @LINE:6
class ReverseApplication {


// @LINE:12
def checkTask(id:Long): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "check" + queryString(List(Some(implicitly[QueryStringBindable[Long]].unbind("id", id)))))
}
                        

// @LINE:11
def createTask(url:String, prefix:String = ""): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "start" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("url", url)), if(prefix == "") None else Some(implicitly[QueryStringBindable[String]].unbind("prefix", prefix)))))
}
                        

// @LINE:6
def index(): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix)
}
                        

// @LINE:14
def getTaskResult(id:Long): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "result" + queryString(List(Some(implicitly[QueryStringBindable[Long]].unbind("id", id)))))
}
                        

}
                          
}
                  


// @LINE:14
// @LINE:12
// @LINE:11
// @LINE:9
// @LINE:6
package controllers.javascript {
import ReverseRouteContext.empty

// @LINE:9
class ReverseAssets {


// @LINE:9
def at : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                        

}
              

// @LINE:14
// @LINE:12
// @LINE:11
// @LINE:6
class ReverseApplication {


// @LINE:12
def checkTask : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.checkTask",
   """
      function(id) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "check" + _qS([(""" + implicitly[QueryStringBindable[Long]].javascriptUnbind + """)("id", id)])})
      }
   """
)
                        

// @LINE:11
def createTask : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.createTask",
   """
      function(url,prefix) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "start" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("url", url), (prefix == null ? null : (""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("prefix", prefix))])})
      }
   """
)
                        

// @LINE:6
def index : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.index",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + """"})
      }
   """
)
                        

// @LINE:14
def getTaskResult : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.getTaskResult",
   """
      function(id) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "result" + _qS([(""" + implicitly[QueryStringBindable[Long]].javascriptUnbind + """)("id", id)])})
      }
   """
)
                        

}
              
}
        


// @LINE:14
// @LINE:12
// @LINE:11
// @LINE:9
// @LINE:6
package controllers.ref {


// @LINE:9
class ReverseAssets {


// @LINE:9
def at(path:String, file:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this.getClass.getClassLoader, "", "controllers.Assets", "at", Seq(classOf[String], classOf[String]), "GET", """ Map static resources from the /public folder to the /assets URL path""", _prefix + """assets/$file<.+>""")
)
                      

}
                          

// @LINE:14
// @LINE:12
// @LINE:11
// @LINE:6
class ReverseApplication {


// @LINE:12
def checkTask(id:Long): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.checkTask(id), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "checkTask", Seq(classOf[Long]), "GET", """""", _prefix + """check""")
)
                      

// @LINE:11
def createTask(url:String, prefix:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.createTask(url, prefix), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "createTask", Seq(classOf[String], classOf[String]), "GET", """""", _prefix + """start""")
)
                      

// @LINE:6
def index(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.index(), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "index", Seq(), "GET", """ Home page""", _prefix + """""")
)
                      

// @LINE:14
def getTaskResult(id:Long): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.getTaskResult(id), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "getTaskResult", Seq(classOf[Long]), "GET", """""", _prefix + """result""")
)
                      

}
                          
}
        
    