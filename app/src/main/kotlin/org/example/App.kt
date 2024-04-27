package org.example

import org.controller.SpaceController
import org.core.services.SpaceService
import org.json.JSONObject
import org.persistence.DatabaseCreator
import org.persistence.Databases
import org.persistence.repositories.SpaceRepository
import org.persistence.savers.SpaceSaver
import spark.Spark.*

fun main() {
  var main = Main().run()
}

class Main() {

  fun run() {
    var database = DatabaseCreator().createDatabase(Databases.H2, "/schemas.sql")

    var spaceController = SpaceController(SpaceService(SpaceRepository(SpaceSaver(database))))

    post("/spaces", spaceController::createSpace)

    after("/") { request, response -> response.type("application/json") }

    internalServerError(JSONObject().put("error", "internal server error").toString())
    notFound(JSONObject().put("error", "not found").toString())
  }
}
