package org.neo4j.ogm.drivers.http.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.neo4j.ogm.model.GraphModel;
import org.neo4j.ogm.response.Response;
import org.neo4j.ogm.result.ResultGraphModel;
import org.neo4j.ogm.exception.ResultProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author vince
 */
public class GraphModelResponse extends AbstractHttpResponse implements Response<GraphModel> {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphModelResponse.class);
    private static final String SCAN_TOKEN = "\"graph";

    private final CloseableHttpResponse response;

    public GraphModelResponse(CloseableHttpResponse httpResponse) throws IOException {
        super(httpResponse.getEntity().getContent());
        this.response = httpResponse;
    }

    @Override
    public GraphModel next() {

        String json = super.nextRecord();

        if (json != null) {
            try {
                return mapper.readValue(json, ResultGraphModel.class).model();
            } catch (Exception e) {
                LOGGER.error("failed to parse: " + json);
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    @Override
    public void close() {
        super.close();
        try {
            response.close();
        } catch (IOException e) {
            throw new ResultProcessingException("Could not close response", e);
        }
    }

    @Override
    public String scanToken() {
        return SCAN_TOKEN;
    }
}
