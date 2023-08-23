package com.jackdawapi.jackdawapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class chatBodyVO {
    /**
     *
     {"accessKey":"m3myozewahwadx4gvdjxnjrmm3c3k4f0","secretKey":"6xw04z98gjuq1gv5qhtnsr951pxmitou","modelId":1651468516836098050,"message":"浮夸"}
     */
    String accessKey;
    String secretKey;
    Long modelId;
    String message;
}
