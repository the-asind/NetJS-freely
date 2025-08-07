package dev.kostromdan.mods.netjs.tasks;

import dev.kostromdan.mods.netjs.callbacks.NetJSICallback;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class HttpRequestTask extends AbstractNetJSTask {
        private final String method;
        private final String body;

        public HttpRequestTask(String url, String method, String body, NetJSICallback c) {
                super(url);
                this.callback = c;
                this.method = method != null ? method.toUpperCase() : "GET";
                this.body = body;
        }

        @Override
        public void run() {
                Connection connection = Jsoup.connect(id).ignoreContentType(true).method(Connection.Method.valueOf(method));
                if (body != null && !body.isEmpty()) {
                        connection.requestBody(body);
                }
                Connection.Response response;
                String raw_text;
                try {
                        response = connection.execute();
                        raw_text = response.body();
                } catch (IOException ioe) {
                        exception(ioe);
                        return;
                }
                int response_code = response.statusCode();
                result.put("response_code", response_code);
                result.put("raw_text", raw_text);
                success();
        }
}
