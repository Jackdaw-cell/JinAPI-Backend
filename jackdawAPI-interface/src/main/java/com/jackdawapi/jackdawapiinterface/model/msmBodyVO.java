package com.jackdawapi.jackdawapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class msmBodyVO {
    /**
     *
     {"phoneNumbers":"18138769734","signName":"本居铃奈庵","templateCode":"SMS_460745222","regionId":"default","accessKey":"LTAI5tBdZ6AjQyhovG1b1kYF","secretKey":"2edid9kIbwvCAs4w9IktuaIVoLO4dF"}
     */
    String accessKey;
    String secretKey;
    String regionId;
    String signName;
    String templateCode;
    String phoneNumbers;
}
