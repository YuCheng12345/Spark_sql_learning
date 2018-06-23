package com.imooc.log

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}

/**
  * 访问日志转换（输入-->输出）工具类
  */
object AccessConvertUtil {

  //定义输出的字段
  val struct = StructType(
    Array(
      StructField("url", StringType),
      StructField("cmsType", StringType),
      StructField("cmsId", LongType),
      StructField("traffic", LongType),
      StructField("ip", StringType),
      StructField("city", StringType),
      StructField("time", StringType),
      StructField("day", StringType)
    )
  )


  /**
    * 根据输入的每一行信息转换成输出的样式
    *
    * @param log 输出的每一行记录信息
    */
  def parseLog(log: String) = {


    try {
      val splits = log.split("\t")

      val url = splits(1)
      val traffic = splits(2).toLong
      val ip = splits(3)

      val domain = "http://www.imooc.com/"
      val cms = url.substring(url.indexOf(domain) + domain.length)
      val cmsTyped = cms.split("/")

      var cmsType = ""
      var cmsId = 0l
      if (cmsTyped.length > 1) {
        cmsType = cmsTyped(0)
        cmsId = cmsTyped(1).toLong
      }

      var city = IpUtils.getCity(ip)
      val time = splits(0)
      val day = time.substring(0, 10).replaceAll("-", "")


      //这个row里面的字段要和struct中的字段对应上
      Row(url, cmsType, cmsId, traffic, ip, city, time, day)
    }
    catch {
      case e: Exception => Row(0)
    }


  }

}