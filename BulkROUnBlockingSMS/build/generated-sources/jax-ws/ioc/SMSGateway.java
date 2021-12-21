
package ioc;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebService(name = "SMSGateway", targetNamespace = "http://ioc/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SMSGateway {


    /**
     * 
     * @param password
     * @param smsContent
     * @param smsType
     * @param mobileNo
     * @param userID
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "OutGoingSMS")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "OutGoingSMS", targetNamespace = "http://ioc/", className = "ioc.OutGoingSMS")
    @ResponseWrapper(localName = "OutGoingSMSResponse", targetNamespace = "http://ioc/", className = "ioc.OutGoingSMSResponse")
    @Action(input = "http://ioc/SMSGateway/OutGoingSMSRequest", output = "http://ioc/SMSGateway/OutGoingSMSResponse")
    public String outGoingSMS(
        @WebParam(name = "mobile_no", targetNamespace = "")
        String mobileNo,
        @WebParam(name = "sms_content", targetNamespace = "")
        String smsContent,
        @WebParam(name = "sms_type", targetNamespace = "")
        String smsType,
        @WebParam(name = "userID", targetNamespace = "")
        String userID,
        @WebParam(name = "password", targetNamespace = "")
        String password);

    /**
     * 
     * @param smsSTATUSVENDOR
     * @param smsFAILEDREASONVENDOR
     * @param password
     * @param smsDELIVDTTIMEVENDOR
     * @param userID
     * @param uniqueID
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "DeliveredSMS_Update")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "DeliveredSMS_Update", targetNamespace = "http://ioc/", className = "ioc.DeliveredSMSUpdate")
    @ResponseWrapper(localName = "DeliveredSMS_UpdateResponse", targetNamespace = "http://ioc/", className = "ioc.DeliveredSMSUpdateResponse")
    @Action(input = "http://ioc/SMSGateway/DeliveredSMS_UpdateRequest", output = "http://ioc/SMSGateway/DeliveredSMS_UpdateResponse")
    public String deliveredSMSUpdate(
        @WebParam(name = "uniqueID", targetNamespace = "")
        String uniqueID,
        @WebParam(name = "SMS_STATUS_VENDOR", targetNamespace = "")
        String smsSTATUSVENDOR,
        @WebParam(name = "SMS_DELIV_DTTIME_VENDOR", targetNamespace = "")
        String smsDELIVDTTIMEVENDOR,
        @WebParam(name = "SMS_FAILED_REASON_VENDOR", targetNamespace = "")
        String smsFAILEDREASONVENDOR,
        @WebParam(name = "userID", targetNamespace = "")
        String userID,
        @WebParam(name = "password", targetNamespace = "")
        String password);

    /**
     * 
     * @param password
     * @param smsReceiveUniqueID
     * @param smsContent
     * @param mobileNo
     * @param smsReceiveDatetime
     * @param userID
     * @return
     *     returns java.lang.String
     * @throws JSONException_Exception
     */
    @WebMethod(operationName = "InComingSMS")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "InComingSMS", targetNamespace = "http://ioc/", className = "ioc.InComingSMS")
    @ResponseWrapper(localName = "InComingSMSResponse", targetNamespace = "http://ioc/", className = "ioc.InComingSMSResponse")
    @Action(input = "http://ioc/SMSGateway/InComingSMSRequest", output = "http://ioc/SMSGateway/InComingSMSResponse", fault = {
        @FaultAction(className = JSONException_Exception.class, value = "http://ioc/SMSGateway/InComingSMS/Fault/JSONException")
    })
    public String inComingSMS(
        @WebParam(name = "mobile_no", targetNamespace = "")
        String mobileNo,
        @WebParam(name = "sms_content", targetNamespace = "")
        String smsContent,
        @WebParam(name = "sms_receive_datetime", targetNamespace = "")
        String smsReceiveDatetime,
        @WebParam(name = "sms_receive_uniqueID", targetNamespace = "")
        String smsReceiveUniqueID,
        @WebParam(name = "userID", targetNamespace = "")
        String userID,
        @WebParam(name = "password", targetNamespace = "")
        String password)
        throws JSONException_Exception
    ;

}