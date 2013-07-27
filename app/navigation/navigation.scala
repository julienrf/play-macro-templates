package navigation

import macrotemplates.Templates
import scala.util.Try
import play.api.i18n.Messages

object `package` {

  val pages = Templates.byName("views.html.pages")

}

case class Item(path: String, title: String, pageName: String)

case class Menu(items: Seq[Item]) {
  def get(path: String) =
    for {
      item <- items.find(_.path == path)
      page <- pages.get(item.pageName)
    } yield (page, item)
}

object Menu {

  def fromPageKeys(keys: Iterable[String]) = Menu((for {
    name <- keys
    m <- "_(\\d+)_".r.findFirstMatchIn(name)
    position <- Try(m.group(1).toInt).toOption
    path <- Option(m.after).map(_.toString)
  } yield (position, Item(path, Messages(s"$path.title"), name))).to[Seq].sortBy(_._1).map(_._2))

}
