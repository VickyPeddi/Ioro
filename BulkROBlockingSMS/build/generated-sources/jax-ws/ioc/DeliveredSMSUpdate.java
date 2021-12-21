
package ioc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DeliveredSMS_Update complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DeliveredSMS_Update">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="uniqueID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SMS_STATUS_VENDOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SMS_DELIV_DTTIME_VENDOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SMS_FAILED_REASON_VENDOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "DeliveredSMS_Update", propOrder = {
    "uniqueID",
    "smsstatusvendor",
    "smsdelivdttimevendor",
    "smsfailedreasonvendor",
    "userID",
    "password"
})
public class DeliveredSMSUpdate {

    protected String uniqueID;
    @XmlElement(name = "SMS_STATUS_VENDOR")
    protected String smsstatusvendor;
    @XmlElement(name = "SMS_DELIV_DTTIME_VENDOR")
    protected String smsdelivdttimevendor;
    @XmlElement(name = "SMS_FAILED_REASON_VENDOR")
    protected String smsfailedreasonvendor;
    protected String userID;
    protected String password;

    /**
     * Gets the value of the uniqueID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniqueID() {
        return uniqueID;
    }

    /**
     * Sets the value of the uniqueID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniqueID(String value) {
        this.uniqueID = value;
    }

    /**
     * Gets the value of the smsstatusvendor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSMSSTATUSVENDOR() {
        return smsstatusvendor;
    }

    /**
     * Sets the value of the smsstatusvendor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSMSSTATUSVENDOR(String value) {
        this.smsstatusvendor = value;
    }

    /**
     * Gets the value of the smsdelivdttimevendor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSMSDELIVDTTIMEVENDOR() {
        return smsdelivdttimevendor;
    }

    /**
     * Sets the value of the smsdelivdttimevendor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSMSDELIVDTTIMEVENDOR(String value) {
        this.smsdelivdttimevendor = value;
    }

    /**
     * Gets the value of the smsfailedreasonvendor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSMSFAILEDREASONVENDOR() {
        return smsfailedreasonvendor;
    }

    /**
     * Sets the value of the smsfailedreasonvendor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSMSFAILEDREASONVENDOR(String value) {
        this.smsfailedreasonvendor = value;
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
