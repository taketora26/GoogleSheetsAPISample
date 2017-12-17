import com.google.inject.AbstractModule
import com.google.inject.name.Names
import com.typesafe.config.ConfigFactory
import play.api.{Configuration, Environment}

class GoogleSheetApiModule(environment: Environment, configuration: Configuration) extends AbstractModule {

  @Override def configure(): Unit = {
    bind(classOf[String])
      .annotatedWith(Names.named("applicationName"))
      .toInstance(configuration.get[String]
    "api.google.sheets.applicationName"
    )

    bind(classOf[String])
      .annotatedWith(Names.named("storeDirName"))
      .toInstance(configuration.get[String]
    "api.google.sheets.storeDirName"
    )

    bind(classOf[String])
      .annotatedWith(Names.named("spreadsheetId"))
      .toInstance(configuration.get[String]
    "api.google.sheets.spreadsheetId"
    )

    bind(classOf[String])
      .annotatedWith(Names.named("sheet"))
      .toInstance(configuration.get[String]("api.google.sheets.sheet"))

    bind(classOf[String])
      .annotatedWith(Names.named("rangeOfReadCell"))
      .toInstance(configuration.get[String]
    "api.google.sheets.rangeOfReadCell"
    )

    bind(classOf[String])
      .annotatedWith(Names.named("rangeOfWriteCell"))
      .toInstance(configuration.get[String]("api.google.sheets.rangeOfWriteCell"))
  }
}