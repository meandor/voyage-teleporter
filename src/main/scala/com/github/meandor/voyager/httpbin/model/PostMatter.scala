package com.github.meandor.voyager.httpbin.model

import com.github.meandor.transporter.Matter

case class PostMatter(id: String, owner: String, posts: Seq[PostsMatter]) extends Matter