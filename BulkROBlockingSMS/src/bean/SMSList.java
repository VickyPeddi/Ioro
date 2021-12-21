/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

/**
 *
 * @author t_sunita
 */
public class SMSList {

    private String mobileNo;
    private String smsContent;
    private String blockId;
    private String ROcode;

    public String getROcode() {
        return ROcode;
    }

    public void setROcode(String ROcode) {
        this.ROcode = ROcode;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

 
}
