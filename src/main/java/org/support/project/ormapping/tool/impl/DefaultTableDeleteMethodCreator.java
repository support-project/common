package org.support.project.ormapping.tool.impl;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import org.support.project.common.config.INT_FLAG;
import org.support.project.common.log.Log;
import org.support.project.common.log.LogFactory;
import org.support.project.common.util.StringUtils;
import org.support.project.ormapping.common.NameConvertor;
import org.support.project.ormapping.entity.ColumnDefinition;
import org.support.project.ormapping.tool.DaoGenConfig;

public class DefaultTableDeleteMethodCreator {

    /** ログ */
    private static Log log = LogFactory.getLog(DefaultTableDeleteMethodCreator.class);

    private CreatorHelper helper = new CreatorHelper();
    private NameConvertor nameConvertor = new NameConvertor();

    private DaoGenConfig config;
    private DefaultTableSQLCreator sqlCreator;

    public void writedeleteMethod(DaoGenConfig config, PrintWriter pw) {
        this.config = config;
        this.sqlCreator = new DefaultTableSQLCreator(config);

        writePhysicalDelete(pw);
        writePhysicalDeleteEntity(pw);

        writeDeleteOnUser(pw);
        writeDelete(pw);

        writeDeleteEntityOnUser(pw);
        writeDeleteEntity(pw);

        // 論理削除の解除メソッド(復帰)
        writeaAtivationOnUser(pw);
        writeAtivation(pw);

        writeAtivationEntityOnUser(pw);
        writeAtivationEntity(pw);
    }

    private void writeAtivationEntity(PrintWriter pw) {
        pw.println("\t/**");
        pw.println("\t * 復元(論理削除されていたものを有効化) ");
        pw.println("\t */");

        pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");
        // メソッド定義
        pw.print("\tpublic ");
        pw.print("void");
        pw.print(" activation(");
        pw.print(config.getEntityClassName());
        pw.println(" entity) {");

        pw.print("\t\tactivation(");
        List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
        Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);
        int count = 0;
        for (ColumnDefinition columnDefinition : primaryKeys) {
            if (count > 0) {
                pw.print(", ");
            }
            String feildName = nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name());
            pw.print("entity." + helper.feildNameToGetter(feildName));
            pw.print("()");
            count++;
        }
        pw.println(");");
        pw.println();

        pw.println("\t}");
    }

    private void writeAtivationEntityOnUser(PrintWriter pw) {
        pw.println("\t/**");
        pw.println("\t * 復元(論理削除されていたものを有効化) ");
        pw.println("\t */");

        pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");
        // メソッド定義
        pw.print("\tpublic ");
        pw.print("void");
        pw.print(" activation(");
        pw.print(config.getCommonUseridType());
        pw.print(" user, ");
        pw.print(config.getEntityClassName());
        pw.println(" entity) {");

        pw.print("\t\tactivation(user, ");
        List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
        Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);
        int count = 0;
        for (ColumnDefinition columnDefinition : primaryKeys) {
            if (count > 0) {
                pw.print(", ");
            }
            String feildName = nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name());
            pw.print("entity." + helper.feildNameToGetter(feildName));
            pw.print("()");
            count++;
        }
        pw.println(");");
        pw.println();

        pw.println("\t}");
    }

    private void writeAtivation(PrintWriter pw) {
        pw.println("\t/**");
        pw.println("\t * 復元(論理削除されていたものを有効化) ");
        pw.println("\t */");

        pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");
        // メソッド定義
        pw.print("\tpublic void");
        pw.print(" activation(");
        List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
        Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);
        int count = 0;
        for (ColumnDefinition columnDefinition : primaryKeys) {
            if (count > 0) {
                pw.print(", ");
            }
            pw.print(helper.getColumnClass(columnDefinition));
            pw.print(" " + nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
            count++;
        }
        pw.println(") {");

        pw.println("\t\tDBUserPool pool = Container.getComp(DBUserPool.class);");
        pw.print("\t\t");
        pw.print(config.getCommonUseridType());
        pw.print(" user = (");
        pw.print(config.getCommonUseridType());
        pw.println(") pool.getUser();");

        pw.print("\t\tactivation(user, ");
        count = 0;
        for (ColumnDefinition columnDefinition : primaryKeys) {
            if (count > 0) {
                pw.print(", ");
            }
            pw.print(nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
            count++;
        }
        pw.println(");");

        pw.println("\t}");
    }

    private void writeaAtivationOnUser(PrintWriter pw) {
        // コメント
        pw.println("\t/**");
        pw.println("\t 復元(論理削除されていたものを有効化) ");
        pw.println("\t */");

        pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");
        // メソッド定義
        pw.print("\tpublic void");
        pw.print(" activation(");
        pw.print(config.getCommonUseridType());
        pw.print(" user, ");
        List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
        Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);
        int count = 0;
        for (ColumnDefinition columnDefinition : primaryKeys) {
            if (count > 0) {
                pw.print(", ");
            }
            pw.print(helper.getColumnClass(columnDefinition));
            pw.print(" " + nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
            count++;
        }
        pw.println(") {");

        if (StringUtils.isEmpty(config.getCommonDeleteFlag())) {
            // 削除フラグが存在しないので、実行できない
            pw.println("\t\tthrow new ORMappingException(\"delete flag is not exists.\");");
        } else {
            pw.print("\t\t");
            pw.print(config.getEntityClassName());
            pw.print(" db = physicalSelectOnKey(");
            count = 0;
            for (ColumnDefinition columnDefinition : primaryKeys) {
                if (count > 0) {
                    pw.print(", ");
                }
                pw.print(nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
                count++;
            }
            pw.println(");");

            String feildName = nameConvertor.colmnNameToFeildName(config.getCommonDeleteFlag());
            pw.print("\t\tdb.");
            pw.print(helper.feildNameToSetter(feildName));
            pw.print("(");
            pw.print(INT_FLAG.OFF.getValue());
            pw.println(");");

            // pw.println("\t\tupdate(user, db);");

            ColumnDefinition userColumn = null;
            ColumnDefinition datetimeColumn = null;

            for (ColumnDefinition columnDefinition : columnDefinitions) {
                if (columnDefinition.getColumn_name().toLowerCase().equals(config.getCommonUpdateUserName().toLowerCase())) {
                    userColumn = columnDefinition;
                } else if (columnDefinition.getColumn_name().toLowerCase().equals(config.getCommonUpdateDateTime().toLowerCase())) {
                    datetimeColumn = columnDefinition;
                }
            }

            if (userColumn != null) {
                // 更新ユーザをセット
                feildName = nameConvertor.colmnNameToFeildName(userColumn.getColumn_name());
                pw.print("\t\tdb.");
                pw.print(helper.feildNameToSetter(feildName));
                pw.println("(user);");
            }
            if (datetimeColumn != null) {
                // 更新ユーザをセット
                feildName = nameConvertor.colmnNameToFeildName(datetimeColumn.getColumn_name());
                pw.print("\t\tdb.");
                pw.print(helper.feildNameToSetter(feildName));
                pw.println("(new Timestamp(new java.util.Date().getTime()));");
            }
            pw.println("\t\tphysicalUpdate(db);");
        }

        pw.println("\t}");
    }

    private void writeDeleteEntity(PrintWriter pw) {
        pw.println("\t/**");
        pw.println("\t * 削除(論理削除があれば論理削除) ");
        pw.println("\t */");

        pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");
        // メソッド定義
        pw.print("\tpublic ");
        pw.print("void");
        pw.print(" delete(");
        pw.print(config.getEntityClassName());
        pw.println(" entity) {");

        pw.print("\t\tdelete(");
        List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
        Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);
        int count = 0;
        for (ColumnDefinition columnDefinition : primaryKeys) {
            if (count > 0) {
                pw.print(", ");
            }
            String feildName = nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name());
            pw.print("entity." + helper.feildNameToGetter(feildName));
            pw.print("()");
            count++;
        }
        pw.println(");");
        pw.println();

        pw.println("\t}");
    }

    private void writeDeleteEntityOnUser(PrintWriter pw) {
        pw.println("\t/**");
        pw.println("\t * 削除(削除ユーザを指定／論理削除があれば論理削除) ");
        pw.println("\t */");

        pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");
        // メソッド定義
        pw.print("\tpublic ");
        pw.print("void");
        pw.print(" delete(");
        pw.print(config.getCommonUseridType());
        pw.print(" user, ");
        pw.print(config.getEntityClassName());
        pw.println(" entity) {");

        pw.print("\t\tdelete(user, ");
        List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
        Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);
        int count = 0;
        for (ColumnDefinition columnDefinition : primaryKeys) {
            if (count > 0) {
                pw.print(", ");
            }
            String feildName = nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name());
            pw.print("entity." + helper.feildNameToGetter(feildName));
            pw.print("()");
            count++;
        }
        pw.println(");");
        pw.println();

        pw.println("\t}");
    }

    private void writeDelete(PrintWriter pw) {
        pw.println("\t/**");
        pw.println("\t * 削除(論理削除があれば論理削除) ");
        pw.println("\t */");

        pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");
        // メソッド定義
        pw.print("\tpublic void");
        pw.print(" delete(");
        List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
        Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);
        int count = 0;
        for (ColumnDefinition columnDefinition : primaryKeys) {
            if (count > 0) {
                pw.print(", ");
            }
            pw.print(helper.getColumnClass(columnDefinition));
            pw.print(" " + nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
            count++;
        }
        pw.println(") {");

        pw.println("\t\tDBUserPool pool = Container.getComp(DBUserPool.class);");
        pw.print("\t\t");
        pw.print(config.getCommonUseridType());
        pw.print(" user = (");
        pw.print(config.getCommonUseridType());
        pw.println(") pool.getUser();");

        pw.print("\t\tdelete(user, ");
        count = 0;
        for (ColumnDefinition columnDefinition : primaryKeys) {
            if (count > 0) {
                pw.print(", ");
            }
            pw.print(nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
            count++;
        }
        pw.println(");");

        pw.println("\t}");
    }

    private void writeDeleteOnUser(PrintWriter pw) {
        // コメント
        pw.println("\t/**");
        pw.println("\t * 削除(削除ユーザを指定／論理削除があれば論理削除) ");
        pw.println("\t */");

        pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");
        // メソッド定義
        pw.print("\tpublic void");
        pw.print(" delete(");
        pw.print(config.getCommonUseridType());
        pw.print(" user, ");
        List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
        Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);
        int count = 0;
        for (ColumnDefinition columnDefinition : primaryKeys) {
            if (count > 0) {
                pw.print(", ");
            }
            pw.print(helper.getColumnClass(columnDefinition));
            pw.print(" " + nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
            count++;
        }
        pw.println(") {");

        if (StringUtils.isEmpty(config.getCommonDeleteFlag())) {
            // 削除フラグが存在しないので、物理削除
            pw.print("\t\tphysicalDelete(");
            count = 0;
            for (ColumnDefinition columnDefinition : primaryKeys) {
                if (count > 0) {
                    pw.print(", ");
                }
                pw.print(nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
                count++;
            }
            pw.println(");");

        } else {
            pw.print("\t\t");
            pw.print(config.getEntityClassName());
            pw.print(" db = selectOnKey(");
            count = 0;
            for (ColumnDefinition columnDefinition : primaryKeys) {
                if (count > 0) {
                    pw.print(", ");
                }
                pw.print(nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
                count++;
            }
            pw.println(");");

            String feildName = nameConvertor.colmnNameToFeildName(config.getCommonDeleteFlag());
            pw.print("\t\tdb.");
            pw.print(helper.feildNameToSetter(feildName));
            pw.print("(");
            pw.print(INT_FLAG.ON.getValue());
            pw.println(");");

            // pw.println("\t\tupdate(user, db);");

            ColumnDefinition userColumn = null;
            ColumnDefinition datetimeColumn = null;

            for (ColumnDefinition columnDefinition : columnDefinitions) {
                if (columnDefinition.getColumn_name().toLowerCase().equals(config.getCommonUpdateUserName().toLowerCase())) {
                    userColumn = columnDefinition;
                } else if (columnDefinition.getColumn_name().toLowerCase().equals(config.getCommonUpdateDateTime().toLowerCase())) {
                    datetimeColumn = columnDefinition;
                }
            }

            if (userColumn != null) {
                // 更新ユーザをセット
                feildName = nameConvertor.colmnNameToFeildName(userColumn.getColumn_name());
                pw.print("\t\tdb.");
                pw.print(helper.feildNameToSetter(feildName));
                pw.println("(user);");
            }
            if (datetimeColumn != null) {
                // 更新ユーザをセット
                feildName = nameConvertor.colmnNameToFeildName(datetimeColumn.getColumn_name());
                pw.print("\t\tdb.");
                pw.print(helper.feildNameToSetter(feildName));
                pw.println("(new Timestamp(new java.util.Date().getTime()));");
            }
            pw.println("\t\tphysicalUpdate(db);");
        }

        pw.println("\t}");
    }

    private void writePhysicalDeleteEntity(PrintWriter pw) {
        pw.println("\t/**");
        pw.println("\t * 削除(データを生で操作/物理削除) ");
        pw.println("\t */");

        pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");
        // メソッド定義
        pw.print("\tpublic ");
        pw.print("void");
        pw.print(" physicalDelete(");
        pw.print(config.getEntityClassName());
        pw.println(" entity) {");

        pw.print("\t\tphysicalDelete(");
        List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
        Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);
        int count = 0;
        for (ColumnDefinition columnDefinition : primaryKeys) {
            if (count > 0) {
                pw.print(", ");
            }
            String feildName = nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name());
            pw.print("entity." + helper.feildNameToGetter(feildName));
            pw.print("()");
            count++;
        }
        pw.println(");");
        pw.println();

        pw.println("\t}");
    }

    private void writePhysicalDelete(PrintWriter pw) {
        // コメント
        pw.println("\t/**");
        pw.println("\t * 削除(データを生で操作/物理削除) ");
        pw.println("\t */");

        pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");
        // メソッド定義
        pw.print("\tpublic ");
        pw.print("void");
        pw.print(" physicalDelete(");
        List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
        Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);
        int count = 0;
        for (ColumnDefinition columnDefinition : primaryKeys) {
            if (count > 0) {
                pw.print(", ");
            }
            pw.print(helper.getColumnClass(columnDefinition));
            pw.print(" " + nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
            count++;
        }
        pw.println(") {");

        // SQLの取得
        pw.print("\t\tString sql = SQLManager.getInstance().getSql(\"");
        pw.print(config.getSqlPackagePath());
        pw.print("/");
        pw.print(sqlCreator.getDeleteSqlFileName());
        pw.println("\");");

        // SQLの実行
        pw.print("\t\texecuteUpdate(sql, ");
        count = 0;
        for (ColumnDefinition columnDefinition : primaryKeys) {
            if (count > 0) {
                pw.print(", ");
            }
            pw.print(nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
            count++;
        }
        pw.println(");");

        pw.println("\t}");
    }

}
