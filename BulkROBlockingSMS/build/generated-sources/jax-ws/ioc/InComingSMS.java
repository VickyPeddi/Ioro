
package ioc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InComingSMS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InComingSMS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mobile_no" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sms_content" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sms_receive_datetime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sms_receive_uniqueID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InComingSMS", propOrder = {
    "mobileNo",
    "smsContent",
    "smsReceiveDatetime",
    "smsReceiveUniqueID",
    "userID",
    "password"
})
public class InComingSMS {

    @XmlElement(name = "mobile_no")
    protected String mobileNo;
    @XmlElement(name = "sms_content")
    protected String smsContent;
    @XmlElement(name = "sms_receive_datetime")
    protected String smsReceiveDatetime;
    @XmlElement(name = "sms_receive_uniqueID")
    protected String smsReceiveUniqueID;
    protected String userID;
    protected String password;

    /**
     * Gets the value of the mobileNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets the value of the mobileNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobileNo(String value) {
        this.mobileNo = value;
    }

    /**
     * Gets the value of the smsContent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmsContent() {
        return smsContent;
    }

    /**
     * Sets the value of the smsContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmsContent(String value) {
        this.smsContent = value;
    }

    /**
     * Gets the value of the smsReceiveDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmsReceiveDatetime() {
        return smsReceiveDatetime;
    }

    /**
     * Sets the value of the smsReceiveDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmsReceiveDatetime(String value) {
        this.smsReceiveDatetime = value;
    }

    /**
     * Gets the value of the smsReceiveUniqueID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmsReceiveUniqueID() {
        return smsReceiveUniqueID;
    }

    /**
     * Sets the value of the smsReceiveUniqueID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmsReceiveUniqueID(String value) {
        this.smsReceiveUniqueID = value;
    }

    /**
     * Gets the value of the userID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the value of the userID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserID(String value) {
        this.userID = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

}
