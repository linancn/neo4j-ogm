package org.neo4j.ogm.drivers.embedded.response;

import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.ogm.response.model.DefaultRowModel;
import org.neo4j.ogm.result.ResultAdapter;

import java.util.Map;

/**
 * @author vince
 */
public class RowModelResponse extends EmbeddedResponse<DefaultRowModel> {

    private final ResultAdapter<Map<String, Object>, DefaultRowModel> adapter = new RowModelAdapter();

    public RowModelResponse(Transaction tx, Result result) {
        super(tx, result);
        ((RowModelAdapter) adapter).setColumns(result.columns());
    }

    @Override
    public DefaultRowModel next() {
        if (result.hasNext()) {
            return adapter.adapt(result.next());
        }
        close();
        return null;
    }

}
