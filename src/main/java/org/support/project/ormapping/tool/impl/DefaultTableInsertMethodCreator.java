package org.support.project.ormapping.tool.impl;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.support.project.common.config.INT_FLAG;
import org.support.project.common.log.Log;
import org.support.project.common.log.LogFactory;
import org.support.project.common.util.StringUtils;
import org.support.project.ormapping.common.NameConvertor;
import org.support.project.ormapping.entity.ColumnDefinition;
import org.support.project.ormapping.tool.DaoGenConfig;

/**
 * Daoの中のInsertMethodを生成する
 * 
 * @author Koda
 */
public class DefaultTableInsertMethodCreator {
    /** ログ */
    private static Log log = LogFactory.getLog(DefaultTableSelectMethodCreator.class);

    private CreatorHelper helper = new CreatorHelper();
    private NameConvertor nameConvertor = new NameConvertor();

    private DaoGenConfig config;
    private DefaultTableSQLCreator sqlCreator;

    public void writeInsertMethod(DaoGenConfig config, PrintWriter pw) {
        this.config = config;
        this.sqlCreator = new DefaultTableSQLCreator(config);

        writeCreateRowId(pw);
        writeRawPhysicalInsert(pw);
        writePhysicalInsert(pw);
        writeInsertOnUser(pw);
        writeInsert(pw);
    }

    private void writeInsert(PrintWriter pw) {
        // コメント
        pw.println("\t/**");
        pw.println("\t * 登録");
        pw.println("\t */");

        pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");

        // メソッド定義
        pw.print("\tpublic ");
        pw.print(config.getEntityClassName());
        pw.print(" insert(");
        pw.print(config.getEntityClassName());
        pw.println(" entity) {");
        if (StringUtils.isEmpty(config.getCommonInsertUserName())) {
            pw.println("\t\treturn physicalInsert(entity);");
        } else {
            pw.println("\t\tDBUserPool pool = Container.getComp(DBUserPool.class);");
            pw.print("\t\t");
            pw.print(config.getCommonUseridType());
            pw.print(" userId = (");
            pw.print(config.getCommonUseridType());
            pw.println(") pool.getUser();");
            pw.println("\t\treturn insert(userId, entity);");
        }
        pw.println("\t}");
    }

    private void writeInsertOnUser(PrintWriter pw) {
        // コメント
        pw.println("\t/**");
        pw.println("\t * 登録(登録ユーザを指定) ");
        pw.println("\t */");

        pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");

        // メソッド定義
        pw.print("\tpublic ");
        pw.print(config.getEntityClassName());
        pw.print(" insert(");
        pw.print(config.getCommonUseridType());
        pw.print(" user, ");
        pw.print(config.getEntityClassName());
        pw.println(" entity) {");

        if (StringUtils.isNotEmpty(config.getCommonInsertUserName())) {
            // ユーザ名の列定義
            List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
            ColumnDefinition userColumn = null;
            ColumnDefinition datetimeColumn = null;

            ColumnDefinition updateUserColumn = null;
            ColumnDefinition updateDatetimeColumn = null;

            for (ColumnDefinition columnDefinition : columnDefinitions) {
                if (columnDefinition.getColumn_name().toLowerCase().equals(config.getCommonInsertUserName().toLowerCase())) {
                    userColumn = columnDefinition;
                } else if (columnDefinition.getColumn_name().toLowerCase().equals(config.getCommonInsertDateTime().toLowerCase())) {
                    datetimeColumn = columnDefinition;
                }

                if (columnDefinition.getColumn_name().toLowerCase().equals(config.getCommonUpdateUserName().toLowerCase())) {
                    updateUserColumn = columnDefinition;
                } else if (columnDefinition.getColumn_name().toLowerCase().equals(config.getCommonUpdateDateTime().toLowerCase())) {
                    updateDatetimeColumn = columnDefinition;
                }

            }
            if (userColumn != null) {
                // 登録ユーザをセット
                String feildName = nameConvertor.colmnNameToFeildName(userColumn.getColumn_name());
                pw.print("\t\tentity.");
                pw.print(helper.feildNameToSetter(feildName));
                pw.println("(user);");
            }
            if (datetimeColumn != null) {
                // 登録ユーザをセット
                String feildName = nameConvertor.colmnNameToFeildName(datetimeColumn.getColumn_name());
                pw.print("\t\tentity.");
                pw.print(helper.feildNameToSetter(feildName));
                pw.println("(new Timestamp(new java.util.Date().getTime()));");
            }

            if (updateUserColumn != null) {
                // 登録ユーザをセット
                String feildName = nameConvertor.colmnNameToFeildName(updateUserColumn.getColumn_name());
                pw.print("\t\tentity.");
                pw.print(helper.feildNameToSetter(feildName));
                pw.println("(user);");
            }
            if (updateDatetimeColumn != null) {
                // 登録ユーザをセット
                String feildName = nameConvertor.colmnNameToFeildName(updateDatetimeColumn.getColumn_name());
                pw.print("\t\tentity.");
                pw.print(helper.feildNameToSetter(feildName));
                pw.println("(new Timestamp(new java.util.Date().getTime()));");
            }

        }

        if (StringUtils.isNotEmpty(config.getCommonDeleteFlag())) {
            // 削除フラグをセット
            String feildName = nameConvertor.colmnNameToFeildName(config.getCommonDeleteFlag());
            pw.print("\t\tentity.");
            pw.print(helper.feildNameToSetter(feildName));
            pw.print("(");
            pw.print(INT_FLAG.OFF.getValue());
            pw.println(");");
        }

        if (StringUtils.isNotEmpty(config.getRowIdColumn())) {
            pw.print("\t\tentity.");
            String f = nameConvertor.colmnNameToFeildName(config.getRowIdColumn());
            pw.print(helper.feildNameToSetter(f));
            pw.println("(createRowId());");
        }

        pw.println("\t\treturn physicalInsert(entity);");
        pw.println("\t}");
    }

    private void writePhysicalInsert(PrintWriter pw) {
        // コメント
        pw.println("\t/**");
        pw.println("\t * 登録(データを生で操作) ");
        pw.println("\t */");

        pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");

        // メソッド定義
        pw.print("\tpublic ");
        pw.print(config.getEntityClassName());
        pw.print(" physicalInsert(");
        pw.print(config.getEntityClassName());
        pw.println(" entity) {");

        // SQLの取得
        pw.print("\t\tString sql = SQLManager.getInstance().getSql(\"");
        pw.print(config.getSqlPackagePath());
        pw.print("/");
        pw.print(sqlCreator.getInsertSqlFileName());
        pw.println("\");");

        pw.print("\t\t");

        // プライマリキーが自動生成であるかチェック
        List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
        Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);
        boolean keygen = false;
        ColumnDefinition keycol = null;
        if (primaryKeys.size() == 1) {
            keycol = new ArrayList<>(primaryKeys).get(0);
            // 主キーは１つ、かつオートインクリメント
            String auto = keycol.getIs_autoincrement();
            if (auto != null) {
                auto = auto.toLowerCase();
            }
            if ("yes".equals(auto)) {
                keygen = true;
            }
        }

        if (keygen) {
            pw.println(
                    "Class<?> type = PropertyUtil.getPropertyType(entity, \"" + nameConvertor.colmnNameToFeildName(keycol.getColumn_name()) + "\");");
            pw.println("\t\tObject key = executeInsert(sql, type, ");
            int count = 0;

            List<String> primaryKeyName = new ArrayList<>();
            for (ColumnDefinition column : primaryKeys) {
                primaryKeyName.add(column.getColumn_name());
            }

            for (ColumnDefinition column : columnDefinitions) {
                if (!primaryKeyName.contains(column.getColumn_name())) {
                    pw.print("\t\t\t");
                    if (count > 0) {
                        pw.print(", ");
                    }
                    pw.print("entity.");
                    String feildName = nameConvertor.colmnNameToFeildName(column.getColumn_name());
                    pw.print(helper.feildNameToGetter(feildName));
                    pw.println("()");
                    count++;
                }
            }
            pw.print("\t\t");
            pw.println(");");
            // 採番したキーをセット
            pw.println("\t\tPropertyUtil.setPropertyValue(entity, \"" + nameConvertor.colmnNameToFeildName(keycol.getColumn_name()) + "\", key);");
        } else {
            pw.println("executeUpdate(sql, ");
            int count = 0;

            List<String> primaryKeyName = new ArrayList<>();
            for (ColumnDefinition column : primaryKeys) {
                pw.print("\t\t\t");
                if (count > 0) {
                    pw.print(", ");
                }
                pw.print("entity.");
                String feildName = nameConvertor.colmnNameToFeildName(column.getColumn_name());
                pw.print(helper.feildNameToGetter(feildName));
                pw.println("()");
                count++;

                primaryKeyName.add(column.getColumn_name());
            }

            for (ColumnDefinition column : columnDefinitions) {
                if (!primaryKeyName.contains(column.getColumn_name())) {
                    pw.print("\t\t\t");
                    if (count > 0) {
                        pw.print(", ");
                    }
                    pw.print("entity.");
                    String feildName = nameConvertor.colmnNameToFeildName(column.getColumn_name());
                    pw.print(helper.feildNameToGetter(feildName));
                    pw.println("()");
                    count++;
                }
            }
            pw.print("\t\t");
            pw.println(");");
        }

        pw.print("\t\t");
        pw.println("return entity;");

        pw.println("\t}");
    }

    private void writeRawPhysicalInsert(PrintWriter pw) {
        // コメント
        pw.println("\t/**");
        pw.println("\t * 登録(データを生で操作/DBの採番機能のカラムも自分でセット) ");
        pw.println("\t */");

        pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");

        // メソッド定義
        pw.print("\tpublic ");
        pw.print(config.getEntityClassName());
        pw.print(" rawPhysicalInsert(");
        pw.print(config.getEntityClassName());
        pw.println(" entity) {");

        // SQLの取得
        pw.print("\t\tString sql = SQLManager.getInstance().getSql(\"");
        pw.print(config.getSqlPackagePath());
        pw.print("/");
        pw.print(sqlCreator.getRawInsertSqlFileName());
        pw.println("\");");

        pw.print("\t\t");

        // プライマリキーが自動生成であるかチェック
        List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
        Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);

        pw.println("executeUpdate(sql, ");
        int count = 0;

        List<String> primaryKeyName = new ArrayList<>();
        for (ColumnDefinition column : primaryKeys) {
            pw.print("\t\t\t");
            if (count > 0) {
                pw.print(", ");
            }
            pw.print("entity.");
            String feildName = nameConvertor.colmnNameToFeildName(column.getColumn_name());
            pw.print(helper.feildNameToGetter(feildName));
            pw.println("()");
            count++;

            primaryKeyName.add(column.getColumn_name());
        }

        for (ColumnDefinition column : columnDefinitions) {
            if (!primaryKeyName.contains(column.getColumn_name())) {
                pw.print("\t\t\t");
                if (count > 0) {
                    pw.print(", ");
                }
                pw.print("entity.");
                String feildName = nameConvertor.colmnNameToFeildName(column.getColumn_name());
                pw.print(helper.feildNameToGetter(feildName));
                pw.println("()");
                count++;
            }
        }
        pw.print("\t\t");
        pw.println(");");

        boolean keygen = false;
        ColumnDefinition keycol = null;
        if (primaryKeys.size() == 1) {
            keycol = new ArrayList<>(primaryKeys).get(0);
            // 主キーは１つ、かつオートインクリメント
            String auto = keycol.getIs_autoincrement();
            if (auto != null) {
                auto = auto.toLowerCase();
            }
            if ("yes".equals(auto)) {
                keygen = true;
            }
        }

        if (keygen) {
            // Postgresの場合、シーケンスを再設定する必要あり
            String tableName = config.getTableDefinition().getTable_name();
            String primary = keycol.getColumn_name();
            String seq = tableName + "_" + primary + "_" + "seq";
            String sql = "\"select setval('" + seq + "', (select max(" + primary + ") from " + tableName + "));\"";

            pw.println("\t\tString driverClass = ConnectionManager.getInstance().getDriverClass(getConnectionName());");
            pw.println("\t\tif (ORMappingParameter.DRIVER_NAME_POSTGRESQL.equals(driverClass)) {");
            // pw.println("\t\t\tString setValSql = \"select setval('likes_no_seq',(select max(no) from likes));\";");
            pw.println("\t\t\tString setValSql = " + sql + ";");
            pw.println("\t\t\texecuteQuerySingle(setValSql, Long.class);");
            pw.println("\t\t}");
        }

        pw.print("\t\t");
        pw.println("return entity;");

        pw.println("\t}");

    }

    private void writeCreateRowId(PrintWriter pw) {
        if (StringUtils.isEmpty(config.getRowIdColumn())) {
            return;
        }

        pw.println("\t/**");
        pw.println("\t * 行を一意に特定するIDを生成");
        pw.println("\t */");

        // メソッド定義
        pw.print("\tprotected String");
        pw.print(" createRowId(");
        pw.println(") {");
        pw.print("\t\treturn IDGen.get().gen(\"");
        pw.print(config.getTableDefinition().getTable_name());
        pw.println("\");");
        pw.println("\t}");
    }

}
