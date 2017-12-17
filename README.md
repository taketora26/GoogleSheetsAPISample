## This is a sample code about Google Sheets API by Scala.

In this repository, you can read and write your Spreadsheet by using Google Sheets API.

## Setting

##### 1.please put your Google OAuth client key in `src/main/resouces/your_client_secret.json`

if you don't have client key  yet, please check this official document.

`Java Quickstart`
https://developers.google.com/sheets/quickstart/java?hl=ja


##### 2.please set application.conf about your Google sheets information
```text
      applicationName = "Google Sheets API Scala Sample"
      storeDirName = ".credentials/sheets.googleapis.com-scala-quickstart"
      spreadsheetId = "your spreadsheetId"
      sheet = "target sheet"
      rangeOfReadCell = "A1:E"
      rangeOfWriteCell = "F1:F5"
```
