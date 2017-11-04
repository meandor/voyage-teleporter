package com.github.meandor.voyager.example

import com.github.meandor.transporter.{Location, Matter}

case class ExampleMatter(id: String, owner: String) extends Matter

case class ExampleLocation(id: String) extends Location

case class ExampleRequest(location: ExampleLocation, matter: ExampleMatter)
