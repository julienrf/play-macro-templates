package controllers

import play.api.mvc.{Action, Controller}
import play.api.i18n.Messages
import navigation.{pages, Item, Menu}
import play.api.cache.Cached
import play.api.Play.current

case class PageContext(menu: Menu, activeItemOrTitle: Either[Item, String])

object Application extends Controller {

  implicit val menu = Menu.fromPageKeys(pages.keys)

  val index = Cached("page_index") {
    Action {
      Ok(views.html.index())
    }
  }

  def page(path: String) = Cached(s"page_$path") {
    Action {
      (menu.get(path) map { case (page, item) => (page, Left(item)) }) orElse (pages.get(path) map ((_, Right(Messages(s"$path.title"))))) match {
        case Some((page, itemOrTitle)) => Ok(page.render(PageContext(menu, itemOrTitle)))
        case None => notFound(path)
      }
    }
  }

  def notFound(path: String) = NotFound(views.html.notFound(path))

}