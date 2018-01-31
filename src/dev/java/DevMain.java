import enkan.component.DataSourceComponent;
import enkan.system.Repl;
import enkan.system.devel.DevelCommandRegister;
import enkan.system.repl.PseudoRepl;
import enkan.system.repl.ReplBoot;
import enkan.system.repl.SystemCommandRegister;
import enkan.system.repl.pseudo.ReplClient;
import kotowari.system.KotowariCommandRegister;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.util.*;

import static enkan.system.ReplResponse.ResponseStatus.DONE;

public class DevMain {
    static Set<String> DML_KEYWORDS = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    static {
        DML_KEYWORDS.addAll(Arrays.asList(
                "INSERT", "UPDATE", "DELETE", "MERGE",
                "CREATE", "DROP"));
    }

    static SystemCommandRegister commandRegister = repl -> {
        repl.registerCommand("sql", (system, transport, args) -> {
            String sql = String.join(" ", args);
            boolean isDML = Arrays.stream(args).anyMatch(s -> DML_KEYWORDS.contains(s));
            DataSourceComponent dataSourceComponent = system.getComponent("datasource");
            try(Connection connection = dataSourceComponent.getDataSource().getConnection();
                Statement stmt = connection.createStatement()) {

                if (isDML) {
                    int cnt = stmt.executeUpdate(sql);
                    connection.commit();
                    transport.sendOut(cnt + " updated.\n");
                } else {
                    try (ResultSet rs = stmt.executeQuery(sql)) {
                        ResultSetMetaData rsMeta = rs.getMetaData();
                        int cols = rsMeta.getColumnCount();
                        List<String> colNames = new ArrayList<>();
                        for (int i = 1; i <= cols; i++) {
                            colNames.add(rsMeta.getColumnName(i));
                        }
                        transport.sendOut(String.join("|", colNames) + "\n");
                        transport.sendOut("\n");

                        while (rs.next()) {
                            List<String> values = new ArrayList<>();
                            for (int i = 1; i <= cols; i++) {
                                values.add(rs.getString(i));
                            }
                            transport.sendOut(String.join("|", values) + "\n");
                        }
                    }
                }
            } catch (SQLException e) {
                transport.sendErr(e.getLocalizedMessage(), DONE);
            }
            return true;
        });
    };

    public static void main(String[] args) throws Exception {
        PseudoRepl repl = new PseudoRepl("net.unit8.impasse.ImpasseSystemFactory");
        ReplBoot.start(repl,
                new KotowariCommandRegister(),
                new DevelCommandRegister(),
                commandRegister);

        new ReplClient().start(repl.getPort().get());
    }
}
