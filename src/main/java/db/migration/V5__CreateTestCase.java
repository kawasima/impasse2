package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.sql.Connection;
import java.sql.Statement;

import static org.jooq.impl.DSL.*;

public class V5__CreateTestCase implements JdbcMigration {
    @Override
    public void migrate(Connection connection) throws Exception {
        try(Statement stmt = connection.createStatement()) {
            DSLContext create = DSL.using(connection);
            String ddl = create.createTable(table("test_cases"))
                    .column(field("test_case_id", SQLDataType.BIGINT.identity(true)))
                    .column(field("summary", SQLDataType.CLOB.nullable(true)))
                    .column(field("preconditions", SQLDataType.CLOB.nullable(true)))
                    .column(field("importance", SQLDataType.INTEGER.nullable(true)))
                    .column(field("active", SQLDataType.BOOLEAN.nullable(false)))
                    .column(field("node_id", SQLDataType.BIGINT.nullable(false)))
                    .constraints(
                            foreignKey(field("node_id")).references(table("nodes"), field("node_id"))
                    )
                    .getSQL();
            stmt.execute(ddl);
        }

    }
}
