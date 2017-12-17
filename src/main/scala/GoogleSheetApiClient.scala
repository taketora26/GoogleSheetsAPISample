import java.io.{File, IOException, InputStreamReader}
import java.util

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.{GoogleAuthorizationCodeFlow, GoogleClientSecrets}
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.{Sheets, SheetsScopes}
import com.google.api.services.sheets.v4.model.{UpdateValuesResponse, ValueRange}
import com.google.inject.{Inject, Singleton}
import com.google.inject.name.Named

import scala.util.Try
import scala.collection.JavaConverters._
import scala.collection.mutable

@Singleton
class GoogleSheetApiClient @Inject()(
                                      @Named("applicationName") applicationName: String,
                                      @Named("storeDirName") storeDirName: String,
                                      @Named("spreadsheetId") spreadsheetId: String,
                                      @Named("sheet") sheet: String,
                                      @Named("rangeOfReadCell") rangeOfReadCell: String,
                                      @Named("rangeOfWriteCell") rangeOfWriteCell: String
                                    ) {

  private val dataStoreDir = new File(System.getProperty("user.home"), storeDirName)
  private val dataStoreFactory = new FileDataStoreFactory(dataStoreDir)
  private val jsonFactory = JacksonFactory.getDefaultInstance
  private val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

  /** アプリケーションに必要な権限のスコープ。アプリケーション実行後実行後、
    * スコープを変更する際には,dataStoreDirのcredentialsを削除する必要がある。
    */
  private val scopes = util.Arrays.asList(SheetsScopes.SPREADSHEETS)

  /** 認証されたクレデンシャルを作成 */
  @throws[IOException]
  private def authorize: Credential = {
    val in = classOf[Nothing] getResourceAsStream "/your_client_secret.json"
    val clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in))
    val flow = new GoogleAuthorizationCodeFlow.Builder(
      httpTransport,
      jsonFactory,
      clientSecrets,
      scopes
    ).setDataStoreFactory(dataStoreFactory).setAccessType("offline").build
    new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver).authorize("user")
  }

  /**
    * APIクライアントサービスをビルドする
    */
  @throws[IOException]
  private def getSheetsService: Sheets = {
    val credential = authorize
    new Sheets.Builder(httpTransport, jsonFactory, credential).setApplicationName(applicationName).build
  }


  private def fetch = {
    val service = getSheetsService
    val range = s"${sheet}!${rangeOfReadCell}"
    val response = service.spreadsheets.values.get(spreadsheetId, range).execute
    val values = response.getValues
  }


  @throws[IOException]
  def read(): mutable.Seq[Unit] = {
    val service = getSheetsService
    //rangeはシート名とセルの範囲を指定します。
    val range = s"${sheet}!A1:E"
    val response = service.spreadsheets.values.get(spreadsheetId, range).execute
    val values = response.getValues
    //javaのList型で返ってくるので、scalaのコレクションに変換します。
    for {row <- values.asScala
    } yield {
      for {
        columnA <- Try(row.get(0)).toOption
        columnB <- Try(row.get(1)).toOption
        columnC <- Try(row.get(2)).toOption
        columnD <- Try(row.get(3)).toOption
        columnE <- Try(row.get(4)).toOption
      }
        println(s"$columnA $columnB $columnC $columnD $columnE")
    }
  }

  @throws[IOException]
  def write(): UpdateValuesResponse = {
    val service = getSheetsService
    val range = s"${sheet}!${rangeOfWriteCell}"
    val values = List(
      List("人口密度(人/k㎡)").asJava,
      List("4,400").asJava,
      List("9,500").asJava,
      List("12,100").asJava,
      List("15,300").asJava
    ).asJava
    val body = new ValueRange
    val requestBody = body.set("values", values)
    val request =
      service.spreadsheets.values
        .update(spreadsheetId, range, requestBody)
        .setValueInputOption("USER_ENTERED")
    request.execute
  }
}
