/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SMS;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import static SMS.SHA.getSHA;
import static SMS.SHA.toHexString;

/**
 *
 * @author 00512788
 */
public class SmsApiCall {

//    static String appId = "RDB";
    static String appCategory = "";
    static String salt = "f32f3778b36772afa602488555411af1";
    static String authenticationToken = "";
//    static int isDynamicToken = 0;

    public static String callSmsApi(String smsPayload,String authToken,String appUrl,String appId,String subCatId,int dynamicToken) {

        appCategory = subCatId;

        String apiUrl = appUrl; 


        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost post = new HttpPost(apiUrl);

            post.setEntity(new StringEntity(smsPayload));
            post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            post.setHeader("appId", appId);
            post.setHeader("appCategory", appCategory);
            post.setHeader("msgType", "ENG");
            if (dynamicToken == 0) {
                authenticationToken = authToken;
            } else {
                authenticationToken = getAuthenticationToken(smsPayload,appId);
            }
            System.out.println(smsPayload);
//            System.out.println(authenticationToken);
            post.setHeader("authenticationToken", authenticationToken);

            CloseableHttpResponse httpResponse;
            httpResponse = httpclient.execute(post);
            String response = EntityUtils.toString(httpResponse.getEntity());
            System.out.println(response);           

            
//            JSONParser parser = new JSONParser();
//            JSONObject jsonResponse = (JSONObject) parser.parse(response);
//            String respCode = (String) jsonResponse.get("responseCode");
//            String msg = "";
//            if (respCode.equalsIgnoreCase("SUCCESS")) {
//                msg = "S";
//            } else {
//                msg = "F";
//            }
            return response;
//            return msg;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "{\"responseCode\":\"FAILED\",\"responseMessage\":Error in SMS API Call\"\"}";
        }
    }

    public static String getAuthenticationToken(String postBody,String appId) {
        try {
            String appIdSha = toHexString(getSHA(appId));
            System.out.println("1 " + appIdSha);
            String appCategorySha = toHexString(getSHA(appCategory));
            System.out.println("2 " + appCategorySha);
            String saltSha = toHexString(getSHA(salt));
            System.out.println("3 " + saltSha);
            String appConcatenatedSha = toHexString(getSHA(appIdSha + appCategorySha + saltSha));
            System.out.println("4 " + appConcatenatedSha);
            String bodySha = toHexString(getSHA(postBody));
            System.out.println("5 " + bodySha);
            String appBodyConcatenatedValue = appConcatenatedSha + bodySha;
            System.out.println("6 " + appBodyConcatenatedValue);
            String authenticationToken = toHexString(getSHA(appBodyConcatenatedValue));
            System.out.println("7 " + authenticationToken);
            return authenticationToken;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SmsApiCall.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

}
