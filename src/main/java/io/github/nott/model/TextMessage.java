package io.github.nott.model;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import io.github.nott.common.CommonUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Nott
 * @date 2024-8-1
 */
@Data
public class TextMessage {

    private String sign;

    private String message;

    private Long code;

    @Override
    public String toString() {
        return "TextMessage{" +
                "sign='" + sign + '\'' +
                ", message='" + message + '\'' +
                ", code=" + code +
                '}';
    }

    public void setSign(String privateKey,String publicKey) {
        CommonUtils.requireFalse(StringUtils.isBlank(this.message),StringUtils.isBlank(this.code + ""));
        Sign signObj = SecureUtil.sign(SignAlgorithm.SHA256withRSA, privateKey, publicKey);
        this.sign = Base64Encoder.encode(signObj.sign(this.message + this.code));
    }
}
