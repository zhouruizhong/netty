package com.zrz.netty.protocol.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.http.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaderUtil.setContentLength;
import static io.netty.handler.codec.http.HttpMethod.CONNECT;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

/**
 * @author zrz
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    public HttpFileServerHandler(String url){

    }

    @Override
    protected void messageReceived(ChannelHandlerContext context, FullHttpRequest request) throws Exception {
        if (!request.decoderResult().isSuccess()){
            //sendError(context, BAD_REQUEST);
            return;
        }

        if (request.method() != GET){
            //sendError(context, METHOD_NOT_ALLOWED);
            return;
        }

        final String uri = request.uri();
        //final String path = sanitizeUri(uri);
        final String path = "";
        if (path == null){
            //sendError(context, FORBIDDEN);
            return;
        }

        File file = new File(path);
        if (!file.isFile()){
            //sendError(context, FORBIDDEN);
            return;
        }

        RandomAccessFile randomAccessFile = null;
        try {
            // 以只读的方式打开文件
            randomAccessFile = new RandomAccessFile(file, "r");
        }catch (FileNotFoundException e){
            e.printStackTrace();
            //sendError(context, NOT_FOUND);
            return;
        }

        long fileLength = randomAccessFile.length();
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, OK);
        setContentLength(response, fileLength);
        //setContentTypeHeader(response, file);
        if (isKeepAlive(request)){
            //response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
    }
}
