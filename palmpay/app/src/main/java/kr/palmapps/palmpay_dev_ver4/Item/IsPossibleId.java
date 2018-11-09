//package kr.palmapps.palmpay_dev_ver4.Item;
//
//import android.widget.EditText;
//
//import com.google.gson.JsonObject;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class IsPossibleId {
//    public String email;
//
//    public void setEmail(EditText editText) {
//        this.email = editText.getText().toString();
//    }
//
//    public JsonObject makeJson() {
//        JsonObject jsonObject = new JsonObject();
//        JsonObject body = new JsonObject();
//
//        if ( email == null) {
//            return null;
//        }
//
//        body.addProperty("email", email);
//        jsonObject.add("body", body);
//
//        return jsonObject;
//    }
//}
