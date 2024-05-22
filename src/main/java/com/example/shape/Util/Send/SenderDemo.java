package com.example.shape.Util.Send;


public class SenderDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("手机号： ");
		String phone = "15364081672";
		System.out.println(phone);
		System.out.println("短信内容： 您好！世界！");
		String content = "您好！世界！";
		System.out.println(content);
		
		ObjectFactory factory = new ObjectFactory();		//工厂类  用于生成WebService输入参数
		Service service = new Service();					//建立服务
		DCMRequest request = factory.createDCMRequest();	//建立请求
		request.genRequest(phone,content);					//为请求赋值(实例化参数DCMRequest)
		
		ServiceSoap serviceSoap = service.getServiceSoap();	//获得SOAP实例
		DCMResponse response = serviceSoap.clientForDCM(request); //方法调用
		
		System.out.println("ResultCode: "+response.getResultCode());
		System.out.println("ResultMsg: "+response.getResultMsg());
		System.out.println("svcCont: "+response.getSvcCont());
	}
}
