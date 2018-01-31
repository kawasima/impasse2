package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.sql.Connection;
import java.sql.Statement;

import static org.jooq.impl.DSL.*;

public class V4__CreateTestSuite implements JdbcMigration {
    @Override
    public void migrate(Connection connection) throws Exception {
        try(Statement stmt = connection.createStatement()) {
            DSLContext create = DSL.using(connection);
            String ddl = create.createTable(table("test_suites"))
                    .column(field("test_suite_id", SQLDataType.BIGINT.identity(true)))
                    .column(field("description", SQLDataType.CLOB.nullable(true)))
                    .column(field("node_id", SQLDataType.BIGINT.nullable(false)))
                    .constraints(
                            foreignKey(field("node_id")).references(table("nodes"), field("node_id"))
                    )
                    .getSQL();
            stmt.execute(ddl);
        }
    }
}
