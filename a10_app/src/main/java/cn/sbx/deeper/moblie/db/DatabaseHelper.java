/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain	a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS,	WITHOUT	WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Android Development_
    https://commonsware.com/Android
 */

package cn.sbx.deeper.moblie.db;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;

public class DatabaseHelper extends SQLiteOpenHelper {
  private static final String DATABASE_NAME="A10_database1.db";
  private static final String LEGACY_DATABASE_NAME="A10_database-crypt.db";
  public static String PASSPHRASE="";
  private static final int SCHEMA=1;
    //LOGIN_USER数据库中的字段
 public static final String USERNAME="username";
 public static final String PASSWORD="password";
    //

 public static final String CUSTINFO="custInfo";
 public static final String ATTACHMENT="attachment";
 public static final String DICTIONARIES="dictionaries";
 public static final String LOGIN_USER="login_user";
 public static final String PERPHONE="perPhone";
 public static final String SCHEDINFO="schedInfo";
 public static final String UPLOADCUSTINFO="uploadcustInfo";
 public static final String USERLADDER="UserLadder";
 public static final String SCHEDINFO_AJ="schedInfo_aj";
 public static final String CUSTINFO_JU_AJ="custInfo_ju_aj";
 public static final String UPLOADCUSTINFO_AJ="uploadcustInfo_aj";
 public static final String PERSP_AJ="perSp_aj";
 public static final String PERFILE_AJ="perFile_aj";
 public static final String PERSH_AJ="perSh_aj";
 public static final String DIC_MODELINFO_AJ="dic_modelInfo_aj";
 public static final String DIC_CMSCSHITEM_AJ="dic_cmScShItem_aj";





  public static void encrypt(Context ctxt) {
    SQLiteDatabase.loadLibs(ctxt);

    File dbFile=ctxt.getDatabasePath(DATABASE_NAME);
    File legacyFile=ctxt.getDatabasePath(LEGACY_DATABASE_NAME);

    if (!dbFile.exists() && legacyFile.exists()) {
      SQLiteDatabase db=
          SQLiteDatabase.openOrCreateDatabase(legacyFile, "", null);

      db.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s';",
                                  dbFile.getAbsolutePath(), PASSPHRASE));
      db.rawExecSQL("SELECT sqlcipher_export('encrypted')");
      db.rawExecSQL("DETACH DATABASE encrypted;");

      int version=db.getVersion();

      db.close();

      db= SQLiteDatabase.openOrCreateDatabase(dbFile, PASSPHRASE, null);
      db.setVersion(version);
      db.close();

      legacyFile.delete();
    }
  }

  public DatabaseHelper(Context ctxt) {
    super(ctxt, DATABASE_NAME, null, SCHEMA);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // 客户信息表
    db.execSQL("create table custInfo(schedInfoID varchar(128),"
            + " meterReadCycleRouteSequence varchar(128),"
            + "accountId varchar(128),"
            + "entityName varchar(128),"
            + "customerClass varchar(128),"
            + "cmCustClDescr varchar(128),"
            + "cmMrAddress varchar(128),"
            + "cmMrDistrict varchar(128),"
            + "cmMrStreet varchar(128),"
            + "cmMrCommunity varchar(128),"
            + "cmMrBuilding varchar(128),"
            + "cmMrUnit varchar(128),"
            + "cmMrRoomNum varchar(128),"
            + "spMeterHistoryId varchar(128),"
            + "meterConfigurationId varchar(128),"
            + "cmMrMtrBarCode varchar(128),"
            + "fullScale varchar(128),"
            + "cmMrAvgVol varchar(128),"
            + "rateSchedule varchar(128),"
            + "cmRsDescr varchar(128),"
            + "cmMrLastBal varchar(128),"
            + "cmMrOverdueAmt varchar(128),"
            + "cmMrDebtStatDt varchar(128),"
            + "cmMrLastMrDttm varchar(128),"
            + "readType varchar(128),"
            + "cmMrLastMr varchar(128),"
            + "cmMrLastVol varchar(128),"
            + "cmMrLastDebt varchar(128),"
            + "cmMrLastSecchkDt varchar(128),"
            + "cmMrRemark varchar(128),"
            + "cmMrState varchar(128)," + "cmMrDate varchar(128))");

    db.execSQL("create table attachment(bzId varchar(128),"

            + "bzType varchar(128),"
            + "userID varchar(128),"
            + "attachmentType varchar(128),"
            + "attachmenturl varchar(128),"
            + "status varchar(128))");

    db.execSQL("create table dictionaries(dictionaryDescr varchar(128),"

            + "dictionaryCode varchar(128),"
            + "parentID varchar(128),"
            + "dictsequence varchar(128) default '0')");


    db.execSQL("create table login_user(userID varchar(128),"

            + "username varchar(128),"
            + "password varchar(128),"
            + "deviceId varchar(128))");


    db.execSQL("create table perPhone(accountId varchar(128),"

            + "sequence varchar(128),"
            + "phoneType varchar(128),"
            + "phone varchar(128),"
            + "extension varchar(128),"
            + "cmPhoneOprtType varchar(128))");

    db.execSQL("create table schedInfo(userID varchar(128),"

            + "meterReadRoute varchar(128),"
            + "cmMrRteDescr varchar(128),"
            + "meterReadCycle varchar(128),"
            + "cmMrCycDescr varchar(128),"
            + "scheduledSelectionDate varchar(128),"
            + "scheduledReadDate varchar(128),"
            + "cmMrDate varchar(128) default '0')");

    db.execSQL("create table uploadcustInfo(spMeterHistoryId varchar(128),"

            + "meterConfigurationId varchar(128),"
            + "cmMr varchar(128),"
            + "readType varchar(128),"
            + "cmMrDttm varchar(128),"
            + "cmMrRefVol varchar(128),"
            + "cmMrRefDebt varchar(128),"
            + "cmMrNotiPrtd varchar(128),"
            + "cmMrCommCd varchar(128),"
            + "cmMrRemark varchar(128),"
            + "cmMrState varchar(128)," + "cmMrDate varchar(128))");


    db.execSQL("create table UserLadder(firstGasPrice varchar(128),"

            + "firstTolerance varchar(128),"
            + "firstGasFee varchar(128),"
            + "secondGasPrice varchar(128),"
            + "secondTolerance varchar(128),"
            + "secondGasFee varchar(128),"
            + "thirdGasPrice varchar(128),"
            + "thirdTolerance varchar(128),"
            + "thirdGasFee varchar(128)," + "UserNo varchar(128))");

    // 安检数据表

    // 任务表(schedInfo_aj)
    db.execSQL("create table schedInfo_aj (userID varchar(128),"

            + "cmSchedId varchar(128),"
            + "description varchar(128),"
            + "cmScTypeCd varchar(128),"
            + "spType   varchar(128),"
            + "scheduleDateTimeStart varchar(128),"
            + "cmMrDate varchar(128) default '0' ,"
            + "Reserve_space1 varchar(128),"
            + "Reserve_space2 varchar(128),"
            + "Reserve_space3 varchar(128),"
            + "Reserve_space4 varchar(128),"
            + "Reserve_space5 varchar(128))");

    // 下载客户信息表(custInfo_ju_aj)
    db.execSQL("create table custInfo_ju_aj ("
            + "cmSchedId varchar(128),"
            + "fieldActivityId varchar(128),"
            + "servicePointId varchar(128),"
            + "spType varchar(128)," + "badgeNumber varchar(128),"
            + "meterConfigurationId varchar(128),"
            + "accountId varchar(128),"
            + "entityName varchar(128),"
            + "customerClass varchar(128),"
            + "cmCustClDescr varchar(128),"
            + "cmMrAddress varchar(128),"
            + "cmMrDistrict varchar(128),"
            + "cmMrStreet varchar(128),"
            + "cmMrCommunity varchar(128),"
            + "cmMrBuilding varchar(128),"
            + "cmMrUnit varchar(128),"
            + "cmMrRoomNum varchar(128),"
            + "cmScOpenDttm varchar(128),"
            + "cmScResType varchar(128),"
            + "cmScUserType varchar(128),"
            + "meterType varchar(128),"
            + "manufacturer varchar(128)," + "model varchar(128),"
            + "serialNumber varchar(128),"
            + "cmMrMtrBarCode varchar(128),"
            + "cmMlr varchar(128)," + "cmScLgfmGj varchar(128),"
            + "cmScLgfmWz varchar(128),"
            + "cmScLgfmCz varchar(128)," + "cmScZjPp varchar(128),"
            + "cmScZjYs varchar(128)," + "cmScZjXhbh varchar(128),"
            + "cmScZjSyrq varchar(128),"
            + "cmScLjgCz varchar(128)," + "cmScCnlPp varchar(128),"
            + "cmScCnlPffs varchar(128),"
            + "cmScCnlSyrq varchar(128),"
            + "cmScRsqPp varchar(128),"
            + "cmScRsqPffs varchar(128),"
            + "cmScRsqSyrq varchar(128),"
            + "cmScBjqPp varchar(128),"
            + "cmScBjqSyrq varchar(128),"
            + "cmMrLastSecchkDt varchar(128),"
            + "cmScIntvl varchar(128)," + "cmScAqyh varchar(128),"
            + "cmScYhzg varchar(128)," + "cmScRemark varchar(128),"
            + "cmMrState varchar(128)," + "cmMrDate varchar(128),"
            + "Reserve_space1 varchar(128),"
            + "Reserve_space2 varchar(128),"
            + "Reserve_space3 varchar(128),"
            + "Reserve_space4 varchar(128),"
            + "Reserve_space5 varchar(128))");

    // 上传表
    db.execSQL("create table uploadcustInfo_aj ("
            + "cmSchedId varchar(128)," + "accountId varchar(128),"
            + "cmScResType varchar(128),"
            + "cmScUserType varchar(128),"
            + "cmScDttm varchar(128)," + "cmScAjrh varchar(128),"
            + "cmScYhzg varchar(128)," + "cmScZgtzd varchar(128),"
            + "cmScZtjs varchar(128)," + "cmMr varchar(128),"
            + "readType varchar(128)," + "cmScSyql varchar(128),"
            + "meterType varchar(128),"
            + "manufacturer varchar(128)," + "model varchar(128),"
            + "serialNumber varchar(128),"
            + "cmMrMtrBarCode varchar(128),"
            + "cmMlr varchar(128)," + "cmScLgfmGj varchar(128),"
            + "cmScLgfmWz varchar(128),"
            + "cmScLgfmCz varchar(128)," + "cmScZjPp varchar(128),"
            + "cmScZjYs varchar(128)," + "cmScZjXhbh varchar(128),"
            + "cmScZjSyrq varchar(128),"
            + "cmScLjgCz varchar(128)," + "cmScCnlPp varchar(128),"
            + "cmScCnlPffs varchar(128),"
            + "cmScCnlSyrq varchar(128),"
            + "cmScRsqPp varchar(128),"
            + "cmScRsqPffs varchar(128),"
            + "cmScRsqSyrq varchar(128),"
            + "cmScBjqPp varchar(128),"
            + "cmScBjqSyrq varchar(128),"
            + "cmScNotiPrtd varchar(128),"
            + "cmMrCommCd varchar(128),"
            + "cmScRemark varchar(128),"
            + "cmMrState varchar(128)," + "cmMrDate varchar(128),"
            + "Reserve_space1 varchar(128),"
            + "Reserve_space2 varchar(128),"
            + "Reserve_space3 varchar(128),"
            + "Reserve_space4 varchar(128),"
            + "Reserve_space5 varchar(128))");


    // 安全讲解(perSp_aj)
    db.execSQL("create table perSp_aj ("
            + "cmSchedId varchar(128)," + "accountId varchar(128),"
            + "cmScSpItem varchar(128),"
            + "cmScSpCheck varchar(128),"
            + "Reserve_space1 varchar(128),"
            + "Reserve_space2 varchar(128),"
            + "Reserve_space3 varchar(128),"
            + "Reserve_space4 varchar(128),"
            + "Reserve_space5 varchar(128))");

    // 图片文件(perFile_aj)
    db.execSQL("create table perFile_aj ("
            + "cmSchedId varchar(128)," + "accountId varchar(128),"
            + "cmScFileName varchar(128),"
            + "cmScFileTitle varchar(128),"
            + "cmScFileForm varchar(128),"
            + "cmScBusiType varchar(128),"
            + "cmScFileRoute varchar(128),"
            + "cmScFileSize varchar(128),"
            + "cmScFileDttm varchar(128),"
            + "cmScFileVar1 varchar(128),"
            + "cmScFileVar2 varchar(128),"
            + "cmScdate varchar(128) ,"
            + "cmMrState varchar(128) default 'N' ,"
            + "Reserve_space1 varchar(128),"
            + "Reserve_space2 varchar(128),"
            + "Reserve_space3 varchar(128),"
            + "Reserve_space4 varchar(128),"
            + "Reserve_space5 varchar(128))");


    // 隐患信息表(perSh_aj)
    db.execSQL("create table perSh_aj ("
            + "cmSchedId varchar(128)," + "accountId varchar(128),"
            + "cmScShType varchar(128),"
            + "cmScShTypeDescr varchar(128),"
            + "cmScShItem varchar(128),"
            + "cmScShItemDescr varchar(128),"
            + "cmScShCheck varchar(128),"
            + "cmScShIsOld varchar(128),"
            + "Reserve_space1 varchar(128),"
            + "Reserve_space2 varchar(128),"
            + "Reserve_space3 varchar(128),"
            + "Reserve_space4 varchar(128),"
            + "Reserve_space5 varchar(128))");



      // 增加一个隐患信息表(perSh_aj1)
      db.execSQL("create table perSh_aj1 ("
              + "cmSchedId varchar(128)," + "accountId varchar(128),"
              + "cmScShType varchar(128),"
              + "cmScShTypeDescr varchar(128),"
              + "cmScShItem varchar(128),"
              + "cmScShItemDescr varchar(128),"
              + "cmScShCheck varchar(128),"
              + "cmScShIsOld varchar(128),"
              + "Reserve_space1 varchar(128),"
              + "Reserve_space2 varchar(128),"
              + "Reserve_space3 varchar(128),"
              + "Reserve_space4 varchar(128),"
              + "Reserve_space5 varchar(128))");


      // 数据字典_表具-型号(dic_modelInfo_aj)
    db.execSQL("create table dic_modelInfo_aj ("
            + "manufacturer varchar(128),"
            + "manufacturerDescr varchar(128),"
            + "model varchar(128)," + "modelDescr varchar(128),"
            + "Reserve_space1 varchar(128),"
            + "Reserve_space2 varchar(128),"
            + "Reserve_space3 varchar(128),"
            + "Reserve_space4 varchar(128),"
            + "Reserve_space5 varchar(128))");


    // 数据字典_隐患选项(dic_cmScShItem_aj)
    db.execSQL("create table dic_cmScShItem_aj ("
            + "cmScShType varchar(128),"
            + "cmScShTypeDescr varchar(128),"
            + "cmScShNO varchar(128)," + "cmScShItem varchar(128),"
            + "cmScShItemDescr varchar(128),"
            + "Reserve_space1 varchar(128),"
            + "Reserve_space2 varchar(128),"
            + "Reserve_space3 varchar(128),"
            + "Reserve_space4 varchar(128),"
            + "Reserve_space5 varchar(128))");


//    try {
//      db.beginTransaction();
//      db.execSQL("CREATE TABLE constants (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, value REAL);");
//
//      ContentValues cv=new ContentValues();
//
//      cv.put(TITLE, "Gravity, Death Star I");
//      cv.put(VALUE, SensorManager.GRAVITY_DEATH_STAR_I);
//      db.insert("constants", TITLE, cv);
//
//      cv.put(TITLE, "Gravity, Earth");
//      cv.put(VALUE, SensorManager.GRAVITY_EARTH);
//      db.insert("constants", TITLE, cv);
//
//      cv.put(TITLE, "Gravity, Jupiter");
//      cv.put(VALUE, SensorManager.GRAVITY_JUPITER);
//      db.insert("constants", TITLE, cv);
//
//      cv.put(TITLE, "Gravity, Mars");
//      cv.put(VALUE, SensorManager.GRAVITY_MARS);
//      db.insert("constants", TITLE, cv);
//
//      cv.put(TITLE, "Gravity, Mercury");
//      cv.put(VALUE, SensorManager.GRAVITY_MERCURY);
//      db.insert("constants", TITLE, cv);
//
//      cv.put(TITLE, "Gravity, Moon");
//      cv.put(VALUE, SensorManager.GRAVITY_MOON);
//      db.insert("constants", TITLE, cv);
//
//      cv.put(TITLE, "Gravity, Neptune");
//      cv.put(VALUE, SensorManager.GRAVITY_NEPTUNE);
//      db.insert("constants", TITLE, cv);
//
//      cv.put(TITLE, "Gravity, Pluto");
//      cv.put(VALUE, SensorManager.GRAVITY_PLUTO);
//      db.insert("constants", TITLE, cv);
//
//      cv.put(TITLE, "Gravity, Saturn");
//      cv.put(VALUE, SensorManager.GRAVITY_SATURN);
//      db.insert("constants", TITLE, cv);
//
//      cv.put(TITLE, "Gravity, Sun");
//      cv.put(VALUE, SensorManager.GRAVITY_SUN);
//      db.insert("constants", TITLE, cv);
//
//      cv.put(TITLE, "Gravity, The Island");
//      cv.put(VALUE, SensorManager.GRAVITY_THE_ISLAND);
//      db.insert("constants", TITLE, cv);
//
//      cv.put(TITLE, "Gravity, Uranus");
//      cv.put(VALUE, SensorManager.GRAVITY_URANUS);
//      db.insert("constants", TITLE, cv);
//
//      cv.put(TITLE, "Gravity, Venus");
//      cv.put(VALUE, SensorManager.GRAVITY_VENUS);
//      db.insert("constants", TITLE, cv);
//
//      db.setTransactionSuccessful();
//    }
//    finally {
//      db.endTransaction();
//    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion,
                        int newVersion) {
    throw new RuntimeException("How did we get here?");
  }

  SQLiteDatabase getReadableDatabase() {

          return(super.getReadableDatabase(PASSPHRASE));

  }

  public SQLiteDatabase getWritableDatabase() {
          return(super.getWritableDatabase(PASSPHRASE));
  }
}
