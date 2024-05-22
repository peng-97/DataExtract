package com.example.shape.Util.Send;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.text.SimpleDateFormat;
import java.util.Date;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DCMRequest", propOrder = { "clientID", "clientUID","corpPort",
        "clientPwd", "bizCode", "transID", "timeStamp", "actionCode",
        "dealkind", "svcCont" })
public class DCMRequest {

    @XmlElement(name = "ClientID")
    protected String clientID;
    @XmlElement(name = "ClientUID")
    protected String clientUID;
    @XmlElement(name = "CorpPort")
    protected String corpPort;
    @XmlElement(name = "ClientPwd")
    protected String clientPwd;
    @XmlElement(name = "BizCode")
    protected String bizCode;
    @XmlElement(name = "TransID")
    protected String transID;
    @XmlElement(name = "TimeStamp")
    protected String timeStamp;
    @XmlElement(name = "ActionCode")
    protected int actionCode;
    @XmlElement(name = "Dealkind")
    protected int dealkind;
    @XmlElement(name = "SvcCont")
    protected String svcCont;


    //给DCMRequest对象附值	值需由sini公司提供
    public void genRequest(String phone, String content) {
        this.clientID = "DCM00035";
        this.bizCode = "DCM102";
        this.clientUID = "zscsdn";
        this.clientPwd = "zscsdn";
        this.corpPort = "2";
        this.transID = "SU" + new SerialNumFactory().getNum();
        this.actionCode = 1;
        this.dealkind = 0;

        this.svcCont = this.genSvcCont(phone, content).toString();
    }

    //给DCMRequest对象附值	值需由sini公司提供
    public void genRequest(String corpport) {
        this.clientID = "";
        this.bizCode = "";
        this.clientUID = "";
        this.clientPwd = "";
        this.transID = "SU" + new SerialNumFactory().getNum();
        this.actionCode = 1;
        this.dealkind = 0;

        this.svcCont = this.genSvcCont(corpport).toString();
    }

    private StringBuilder genSvcCont(String corpport) {
        StringBuilder sb = new StringBuilder();
        sb.append("<NodeInceptRequest>");
        sb.append("	<Body>");
        sb.append("		<CorpPort>").append(corpport).append("</CorpPort>");//写入端口号
        sb.append("	</Body>");
        sb.append("</NodeInceptRequest>");
        return sb;
    }

    private StringBuilder genSvcCont(String phone, String content) {
        StringBuilder _sb = new StringBuilder();
        _sb.append("<NodeRequest>");

        StringBuilder sb = new StringBuilder();

        sb.append("<Body>");
        sb.append("<CorpID>");
        sb.append(this.clientUID);
        sb.append("</CorpID>");
        sb.append("<CorpPort>");
        sb.append(this.corpPort);
        sb.append("</CorpPort>");
        sb.append("<Node>");
        sb.append("<ID>").append(System.currentTimeMillis()).append("</ID>");		//sequence number
        sb.append("<Phone>").append(phone).append("</Phone>");
        sb.append("<Content>").append(content).append("</Content>");//短信接口测试，勿回复，谢谢！
        sb.append("<SendTime>").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("</SendTime>");	//发送时间，时间格式：yyyy-MM-dd HH:mm:ss
        sb.append("<ExtItem>");
        sb.append("<ItemName>type</ItemName>");
        sb.append("<ItemValue>123</ItemValue>");
        sb.append("</ExtItem>");
        sb.append("</Node>");
        sb.append("</Body>");

        String md5 = Encrypt.MD5(sb.toString()+clientUID).toUpperCase();
        _sb.append(sb);
        _sb.append("<MD5>");
        _sb.append(md5);
        _sb.append("</MD5>");
        _sb.append("</NodeRequest>");

        return _sb;
    }

    /**
     * Gets the value of the clientID property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getClientID() {
        return clientID;
    }

    /**
     * Sets the value of the clientID property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */


    public void setClientID(String value) {
        this.clientID = value;
    }

    /**
     * 获取 corpPort
     * @return the corpPort
     */
    public String getCorpPort() {
        return corpPort;
    }

    /**
     * 设置corpPort
     * @param corpPort the corpPort to set
     */
    public void setCorpPort(String corpPort) {
        this.corpPort = corpPort;
    }

    /**
     * Gets the value of the clientUID property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getClientUID() {
        return clientUID;
    }

    /**
     * Sets the value of the clientUID property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setClientUID(String value) {
        this.clientUID = value;
    }

    /**
     * Gets the value of the clientPwd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getClientPwd() {
        return clientPwd;
    }

    /**
     * Sets the value of the clientPwd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setClientPwd(String value) {
        this.clientPwd = value;
    }

    /**
     * Gets the value of the bizCode property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getBizCode() {
        return bizCode;
    }

    /**
     * Sets the value of the bizCode property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setBizCode(String value) {
        this.bizCode = value;
    }

    /**
     * Gets the value of the transID property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getTransID() {
        return transID;
    }

    /**
     * Sets the value of the transID property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setTransID(String value) {
        this.transID = value;
    }

    /**
     * Gets the value of the timeStamp property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the value of the timeStamp property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setTimeStamp(String value) {
        this.timeStamp = value;
    }

    /**
     * Gets the value of the actionCode property.
     *
     */
    public int getActionCode() {
        return actionCode;
    }

    /**
     * Sets the value of the actionCode property.
     *
     */
    public void setActionCode(int value) {
        this.actionCode = value;
    }

    /**
     * Gets the value of the dealkind property.
     *
     */
    public int getDealkind() {
        return dealkind;
    }

    /**
     * Sets the value of the dealkind property.
     *
     */
    public void setDealkind(int value) {
        this.dealkind = value;
    }

    /**
     * Gets the value of the svcCont property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getSvcCont() {
        return svcCont;
    }

    /**
     * Sets the value of the svcCont property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setSvcCont(String value) {
        this.svcCont = value;
    }
}
