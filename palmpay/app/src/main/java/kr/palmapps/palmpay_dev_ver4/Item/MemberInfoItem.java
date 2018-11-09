package kr.palmapps.palmpay_dev_ver4.Item;

/**
 * 사용자 정보를 저장하는 객체
 */

@org.parceler.Parcel
public class MemberInfoItem {
    public int seq;
    public String email;
    public String password;
    public String username;
    public String nickname;
    public String phone;
    public String sex;

    @Override
    public String toString() {
        return "MemberInfoItem{" +
                "seq=" + seq +
                ", email='" + email + '\'' +
                ", password'" + password + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
