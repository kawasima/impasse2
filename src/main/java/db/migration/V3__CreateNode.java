package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.sql.Connection;
import java.sql.Statement;

import static org.jooq.impl.DSL.*;

public class V3__CreateNode implements JdbcMigration {
    @Override
    public void migrate(Connection connection) throws Exception {
        try(Statement stmt = connection.createStatement()) {
            DSLContext create = DSL.using(connection);
            String ddl = create.createTable(table("nodes"))
                    .column(field("node_id", SQLDataType.BIGINT.identity(true)))
                    .column(field("name", SQLDataType.BIGINT.nullable(false)))
                    .column(field("type", SQLDataType.SMALLINT.nullable(false)))
                    .column(field("node_order", SQLDataType.INTEGER.nullable(false)))
                    .column(field("parent_id", SQLDataType.BIGINT.nullable(false)))
                    .constraints(
                            foreignKey(field("parent_id")).references(table("nodes"), field("node_id"))
                    )
                    .getSQL();
            stmt.execute(ddl);
        }
    }
}
