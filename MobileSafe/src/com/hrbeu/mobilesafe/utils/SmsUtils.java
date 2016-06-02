package com.hrbeu.mobilesafe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Hankai Xia on 2016-05-30.
 */
public class SmsUtils {
	/**
	 * 备份短信的接口
	 *
	 * @author Hankai Xia
	 */
	public interface BackUpSms {
		public void befor(int count);

		public void onBackUpSms(int process);
	}

	public static boolean backUP(Context context, BackUpSms callBack) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			ContentResolver resolver = context.getContentResolver();
			//获取短信的路径
			Uri uri = Uri.parse("content://sms/");
			//type=1接收，type=2发送
			Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);
			//短信的总量
			int count = cursor.getCount();
			//pd的最大值
			//pd.setMax(count);
			callBack.befor(count);

			int process = 0;

			try {
				File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
				FileOutputStream fos = new FileOutputStream(file);
				//序列化器,pull解析
				XmlSerializer serializer = Xml.newSerializer();
				//短信序列化到SD卡，utf-8编码格式
				serializer.setOutput(fos, "utf-8");
				//standalone是否是独立文件,true文件独立
				serializer.startDocument("utf-8", true);
				serializer.startTag(null, "smss");
				//smss节点的属性值size
				serializer.attribute(null, "size", String.valueOf(count));

				while (cursor.moveToNext()) {
					serializer.startTag(null, "sms");
					serializer.startTag(null, "address");
					serializer.text(cursor.getString(0));
					serializer.endTag(null, "address");
					serializer.startTag(null, "date");
					serializer.text(cursor.getString(1));
					serializer.endTag(null, "date");
					serializer.startTag(null, "type");
					serializer.text(cursor.getString(2));
					serializer.endTag(null, "type");
					serializer.startTag(null, "body");

					serializer.text(Crypto.encrypt("xhkSun", cursor.getString(3)));

					serializer.endTag(null, "body");
					serializer.endTag(null, "sms");

					process++;
					//pd.setProgress(process);
					callBack.onBackUpSms(process);
					SystemClock.sleep(100);
				}

				cursor.close();

				serializer.endTag(null, "smss");
				serializer.endDocument();

				fos.flush();
				fos.close();
				return true;

			} catch (Exception e) {
				e.printStackTrace();
			}


		}
		return false;
	}
}
