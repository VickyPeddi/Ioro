
package ioc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ioc package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _JSONException_QNAME = new QName("http://ioc/", "JSONException");
    private final static QName _InComingSMSResponse_QNAME = new QName("http://ioc/", "InComingSMSResponse");
    private final static QName _OutGoingSMS_QNAME = new QName("http://ioc/", "OutGoingSMS");
    private final static QName _OutGoingSMSResponse_QNAME = new QName("http://ioc/", "OutGoingSMSResponse");
    private final static QName _InComingSMS_QNAME = new QName("http://ioc/", "InComingSMS");
    private final static QName _DeliveredSMSUpdate_QNAME = new QName("http://ioc/", "DeliveredSMS_Update");
    private final static QName _DeliveredSMSUpdateResponse_QNAME = new QName("http://ioc/", "DeliveredSMS_UpdateResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ioc
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InComingSMS }
     * 
     */
    public InComingSMS createInComingSMS() {
        return new InComingSMS();
    }

    /**
     * Create an instance of {@link DeliveredSMSUpdate }
     * 
     */
    public DeliveredSMSUpdate createDeliveredSMSUpdate() {
        return new DeliveredSMSUpdate();
    }

    /**
     * Create an instance of {@link DeliveredSMSUpdateResponse }
     * 
     */
    public DeliveredSMSUpdateResponse createDeliveredSMSUpdateResponse() {
        return new DeliveredSMSUpdateResponse();
    }

    /**
     * Create an instance of {@link OutGoingSMSResponse }
     * 
     */
    public OutGoingSMSResponse createOutGoingSMSResponse() {
        return new OutGoingSMSResponse();
    }

    /**
     * Create an instance of {@link OutGoingSMS }
     * 
     */
    public OutGoingSMS createOutGoingSMS() {
        return new OutGoingSMS();
    }

    /**
     * Create an instance of {@link InComingSMSResponse }
     * 
     */
    public InComingSMSResponse createInComingSMSResponse() {
        return new InComingSMSResponse();
    }

    /**
     * Create an instance of {@link JSONException }
     * 
     */
    public JSONException createJSONException() {
        return new JSONException();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link JSONException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ioc/", name = "JSONException")
    public JAXBElement<JSONException> createJSONException(JSONException value) {
        return new JAXBElement<JSONException>(_JSONException_QNAME, JSONException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InComingSMSResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ioc/", name = "InComingSMSResponse")
    public JAXBElement<InComingSMSResponse> createInComingSMSResponse(InComingSMSResponse value) {
        return new JAXBElement<InComingSMSResponse>(_InComingSMSResponse_QNAME, InComingSMSResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutGoingSMS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ioc/", name = "OutGoingSMS")
    public JAXBElement<OutGoingSMS> createOutGoingSMS(OutGoingSMS value) {
        return new JAXBElement<OutGoingSMS>(_OutGoingSMS_QNAME, OutGoingSMS.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutGoingSMSResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ioc/", name = "OutGoingSMSResponse")
    public JAXBElement<OutGoingSMSResponse> createOutGoingSMSResponse(OutGoingSMSResponse value) {
        return new JAXBElement<OutGoingSMSResponse>(_OutGoingSMSResponse_QNAME, OutGoingSMSResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InComingSMS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ioc/", name = "InComingSMS")
    public JAXBElement<InComingSMS> createInComingSMS(InComingSMS value) {
        return new JAXBElement<InComingSMS>(_InComingSMS_QNAME, InComingSMS.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeliveredSMSUpdate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ioc/", name = "DeliveredSMS_Update")
    public JAXBElement<DeliveredSMSUpdate> createDeliveredSMSUpdate(DeliveredSMSUpdate value) {
        return new JAXBElement<DeliveredSMSUpdate>(_DeliveredSMSUpdate_QNAME, DeliveredSMSUpdate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeliveredSMSUpdateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ioc/", name = "DeliveredSMS_UpdateResponse")
    public JAXBElement<DeliveredSMSUpdateResponse> createDeliveredSMSUpdateResponse(DeliveredSMSUpdateResponse value) {
        return new JAXBElement<DeliveredSMSUpdateResponse>(_DeliveredSMSUpdateResponse_QNAME, DeliveredSMSUpdateResponse.class, null, value);
    }

}
