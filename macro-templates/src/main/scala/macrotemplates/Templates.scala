package macrotemplates

import scala.language.experimental.macros

import scala.reflect.macros.Context
import scala.reflect.macros.compat.QuasiquoteCompat

object Templates {

  def byName(rootPackage: String) = macro Macros.byName

  object Macros {

    def byName(c: Context)(rootPackage: c.Expr[String]) = c.Expr[Any](C[c.type](c).byName(rootPackage))

    case class C[C <: Context](c: C) extends QuasiquoteCompat {
      import c.universe._

      def byName(rootPackage: c.Expr[String]) = {

        val pkg = rootPackage.tree match {
          case Literal(Constant(name: String)) => c.mirror.staticPackage(name)
        }

        val templates = for {
          member <- pkg.typeSignature.members
          if member.typeSignature <:< typeOf[play.templates.BaseScalaTemplate[_, _]]
        } yield q"${member.name.decoded} -> $member"

        // TODO return a value which has page names as members
        q"Map(..$templates)"
      }

    }

  }

}