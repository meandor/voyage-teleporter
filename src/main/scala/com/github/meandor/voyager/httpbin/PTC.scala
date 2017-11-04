package com.github.meandor.voyager.httpbin

import com.github.meandor.transporter.Matter
import com.github.meandor.transporter.platform.{Energy, PhaseTransitionCoil}
import com.github.meandor.voyager.httpbin.model.{PostMatter, PostsMatter}
import com.typesafe.scalalogging.LazyLogging

object PTC extends PhaseTransitionCoil with LazyLogging {

  case class PostsEnergy(subject: PostsMatter) extends Energy {
    override def toJson: String = s"{'id': '${subject.id}', 'owner': '${subject.owner}'}"
  }

  case class PostEnergy(subject: PostMatter) extends Energy {
    override def toJson: String = {
      val postsEnergy = subject.posts.par.map(PostsEnergy(_).toJson).mkString("[", ",", "]")
      s"{'id':'${subject.id}','owner':'${subject.owner}','posts':$postsEnergy}"
    }
  }

  override def energize(subject: Matter): Energy = {
    logger.info(s"Energizing PhaseTransitionCoil with: ${subject.id}, ${subject.owner}")
    PostEnergy(subject.asInstanceOf[PostMatter])
  }
}
