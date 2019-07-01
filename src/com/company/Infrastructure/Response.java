package com.company.Infrastructure;

public class Response {
    public ResponseMeta meta;
    public Object data;

    public Response(ResponseMeta meta, Object data){
        this.meta = meta;
        this.data = data;
    }


}
