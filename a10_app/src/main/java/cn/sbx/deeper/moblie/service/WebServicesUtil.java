package cn.sbx.deeper.moblie.service;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import cn.sbx.deeper.moblie.domian.PhotoAudio;
import cn.sbx.deeper.moblie.util.WebUtils;

/**
 * author : sheng
 * date :   On 2016/11/23
 * <p/>
 * 连接webservices
 */
public class WebServicesUtil {
    private static org.ksoap2.transport.HttpTransportSE transport;
    private static String soapAction;
    private static SoapSerializationEnvelope envelope;
    static Object obj = new Object();

    public static boolean connectWebServiceTest(String UserId, String Date, PhotoAudio photoAudio, String ImageBase64, boolean isLast, UploadTask uploadTask) {
        String nameSpace = "http://tempuri.org/";
        String methodName = "SaveImages";
        String soapAction = "http://tempuri.org/SaveImages";
//        String url = "http://10.120.131.248/DCWebTest/ATenTest/SaveImgService.asmx";// 后面加不加那个?wsdl参数影响都不大
        String url = WebUtils.webservicesUrl;// 后面加不加那个?wsdl参数影响都不大

        org.ksoap2.transport.HttpTransportSE transport;
        transport = new HttpTransportSE(url);
        transport.debug = true;// 是否是调试模式

        // 设置连接参数
        SoapObject soapObject = new SoapObject(nameSpace, methodName);
        soapObject.addProperty("userId", UserId);  //用户id
        soapObject.addProperty("date", Date);   //日期
        soapObject.addProperty("imageName", photoAudio.getCmScFileName());//图片名称
        soapObject.addProperty("imageByte", ImageBase64);//图片
//        System.out.println("UserId :" + UserId + "====Date:" + Date + "====ImageName:" + ImageName + "====ImageBase64:" + ImageBase64);

        SoapSerializationEnvelope envelope;
        envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;// 注意：这个属性是对dotnetwebservice协议的支持,如果dotnet的webservice
        // 不指定rpc方式则用true否则要用false
        envelope.bodyOut = transport;
        envelope.setOutputSoapObject(soapObject);// 设置请求参数
        // 调用WebService
        try {
            transport.call(soapAction, envelope);
            if (envelope.getResponse() != null) {
                // 获取返回的数据
                SoapObject object = (SoapObject) envelope.bodyIn;
                // 获取返回的结果
                String result = object.getProperty(0).toString();
                Log.d("WebService", "图片返回结果：" + result + "==当前线程:" + Thread.currentThread().getName().toString());
                transport = null;
                envelope = null;
                if (result.equals("true")) {
                    uploadTask.uploadSuccess(photoAudio);
                    ImageBase64 = null;
                } else {
                    uploadTask.uploadFailed(photoAudio);
                }
                //如果是最后一张, 调用该方法
                if (isLast) {
                    uploadTask.uploadFinish();
                }
                return result.equals("true");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //上传异常,
            uploadTask.uploadFailed(photoAudio);
            //如果是最后一张, 调用该方法
            if (isLast) {
                uploadTask.uploadFinish();
            }
            Log.d("WebService", "图片返回false=====当前线程:" + Thread.currentThread().getName().toString() + "==e:" + e.toString());
            return false;
        }
        return true;
    }


    public static String connectWebServiceTest1(String UserId, String pwd) {
        String nameSpace = "http://tempuri.org/";
        String methodName = "AddUserOffLine";
        String soapAction = "http://tempuri.org/AddUserOffLine";
//        String url = "http://10.120.131.248/DCWebTest/ATenTest/SaveImgService.asmx";// 后面加不加那个?wsdl参数影响都不大
        String url = WebUtils.webservicesUrl1;// 后面加不加那个?wsdl参数影响都不大

        org.ksoap2.transport.HttpTransportSE transport;
        transport = new HttpTransportSE(url);
        transport.debug = true;// 是否是调试模式

        // 设置连接参数
        SoapObject soapObject = new SoapObject(nameSpace, methodName);
        soapObject.addProperty("userId", UserId);  //用户id
        soapObject.addProperty("date", pwd);   //日期
        soapObject.addProperty("cookie",WebUtils.cookie);
//        System.out.println("UserId :" + UserId + "====Date:" + Date + "====ImageName:" + ImageName + "====ImageBase64:" + ImageBase64);

        SoapSerializationEnvelope envelope;
        envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;// 注意：这个属性是对dotnetwebservice协议的支持,如果dotnet的webservice
        // 不指定rpc方式则用true否则要用false
        envelope.bodyOut = transport;
        envelope.setOutputSoapObject(soapObject);// 设置请求参数
        // 调用WebService
        try {
            transport.call(soapAction, envelope);
            if (envelope.getResponse() != null) {
                // 获取返回的数据
                SoapObject object = (SoapObject) envelope.bodyIn;
                // 获取返回的结果
                String result = object.getProperty(0).toString();
                Log.d("WebService", "图片返回结果：" + result + "==当前线程:" + Thread.currentThread().getName().toString());

                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //上传异常,
            //如果是最后一张, 调用该方法
            return "";
        }
        return "";
    }

    public static String connectWebServiceTest2(String UserId) {
        String nameSpace = "http://tempuri.org/";
        String methodName = "GetUserOffLinePassword";
        String soapAction = "http://tempuri.org/GetUserOffLinePassword";
//        String url = "http://10.120.131.248/DCWebTest/ATenTest/SaveImgService.asmx";// 后面加不加那个?wsdl参数影响都不大
        String url = WebUtils.webservicesUrl1;// 后面加不加那个?wsdl参数影响都不大

        org.ksoap2.transport.HttpTransportSE transport;
        transport = new HttpTransportSE(url);
        transport.debug = true;// 是否是调试模式

        // 设置连接参数
        SoapObject soapObject = new SoapObject(nameSpace, methodName);
        soapObject.addProperty("userId", UserId);  //用户id
        soapObject.addProperty("cookie",WebUtils.cookie);
//        System.out.println("UserId :" + UserId + "====Date:" + Date + "====ImageName:" + ImageName + "====ImageBase64:" + ImageBase64);

        SoapSerializationEnvelope envelope;
        envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;// 注意：这个属性是对dotnetwebservice协议的支持,如果dotnet的webservice
        // 不指定rpc方式则用true否则要用false
        envelope.bodyOut = transport;
        envelope.setOutputSoapObject(soapObject);// 设置请求参数
        // 调用WebService
        try {
            transport.call(soapAction, envelope);
            if (envelope.getResponse() != null) {
                // 获取返回的数据
                SoapObject object = (SoapObject) envelope.bodyIn;
                // 获取返回的结果
                String result = object.getProperty(0).toString();
                Log.d("WebService", "图片返回结果：" + result + "==当前线程:" + Thread.currentThread().getName().toString());

                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //上传异常,
            //如果是最后一张, 调用该方法
            return "";
        }
        return "";
    }


    /**
     * 上传接口
     */
    public interface UploadTask {
        //成功
        void uploadSuccess(PhotoAudio photoAudio);

        //失败
        void uploadFailed(PhotoAudio photoAudio);

        //所有图片上传完成
        void uploadFinish();
    }


    public static boolean connectWebService(String UserId, String Date, String ImageName, String ImageBase64) {
        String nameSpace = "http://tempuri.org/";
        String methodName = "SaveImages";
        soapAction = "http://tempuri.org/SaveImages";
//        String url = "http://10.29.32.78/ATenInterfaceTest/ATenService.asmx";// 后面加不加那个?wsdl参数影响都不大
        String url = "http://10.120.131.248/DCWebTest/ATenTest/SaveImgService.asmx";// 后面加不加那个?wsdl参数影响都不大
//        http://10.120.133.17/DMCWeb/Integrated/SaveImgService.asmx
//		String url = "http://10.29.32.78/ATenInterfaceTest/ATenService.asmx?wsdl";// 后面加不加那个?wsdl参数影响都不大
        transport = new HttpTransportSE(url);
        transport.debug = true;// 是否是调试模式

        // 设置连接参数
        SoapObject soapObject = new SoapObject(nameSpace, methodName);
        soapObject.addProperty("userId", UserId);  //用户id
        soapObject.addProperty("date", Date);   //日期
        soapObject.addProperty("imageName", ImageName + ".jpg");//图片名称
        soapObject.addProperty("imageByte", ImageBase64);//图片
        System.out.println("UserId :" + UserId + "====Date:" + Date + "====ImageName:" + ImageName + "====ImageBase64:" + ImageBase64);
        envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // V1.1）
        envelope.dotNet = true;// 注意：这个属性是对dotnetwebservice协议的支持,如果dotnet的webservice
        // 不指定rpc方式则用true否则要用false
        envelope.bodyOut = transport;
        envelope.setOutputSoapObject(soapObject);// 设置请求参数

        FutureTask<String> futureTask = new FutureTask<String>(
                new Callable<String>() {


                    @Override
                    public String call() throws Exception {
                        // 调用WebService
//                        transport.call(soapAction, envelope);
                        try {
                            // 调用WebService
                            transport.call(soapAction, envelope);
                            if (envelope.getResponse() != null) {
                                // 获取返回的数据
                                SoapObject object = (SoapObject) envelope.bodyIn;
                                // 获取返回的结果
//                                String result = object.getProperty(0)
//                                        .toString();
                                Object obj = envelope.getResponse();
                                Log.d("WebService", "返回结果：" + obj.toString());
                                System.out.println("当前线程:" + Thread.currentThread().getName().toString());
                                return object.getProperty(0).toString();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("当前线程:" + Thread.currentThread().getName().toString());
                            Log.d("WebService", "返回结果false");
                        }
                        return null;
                    }

                });
        new Thread(futureTask).start();
        return true;
    }
}


