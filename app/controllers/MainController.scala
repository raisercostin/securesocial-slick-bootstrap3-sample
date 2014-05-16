package controllers

import play.api.Play

object MainController{
    import play.api.Play.current

    def ???!(msg: String): Nothing = throw new RuntimeException(msg)
  
	def property(name:String) = 
			Play.application.configuration.getString(name).orElse(???!(s"Tried to find property [$name]")).get
	def property(name:String, defaultValue: =>String):String = 
			Play.application.configuration.getString(name).getOrElse(defaultValue)
}