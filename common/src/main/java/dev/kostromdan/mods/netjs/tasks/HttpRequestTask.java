package dev.kostromdan.mods.netjs.tasks;

import dev.kostromdan.mods.netjs.callbacks.NetJSICallback;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class HttpRequestTask extends AbstractNetJSTask {
        private final Connection.Method method;
        private final String body;

        public HttpRequestTask(String url, String method, String body, NetJSICallback c) {
                super(url);
                this.callback = c;
                Connection.Method parsed;
                if (method == null) {
                        parsed = Connection.Method.GET;
                } else {
                        try {
                                parsed = Connection.Method.valueOf(method.toUpperCase());
                        } catch (IllegalArgumentException iae) {
                                parsed = Connection.Method.GET;
                        }
                }
                this.method = parsed;
                this.body = body;
        }

        @Override
        public void run() {
                Connection connection = Jsoup.connect(id).ignoreContentType(true).method(method);
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
                int status_code = response.statusCode();
                result.put("status_code", status_code);
                result.put("raw_text", raw_text);
                success();
        }
}
