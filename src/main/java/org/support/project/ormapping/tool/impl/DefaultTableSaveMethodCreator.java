package org.support.project.ormapping.tool.impl;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import org.support.project.common.log.Log;
import org.support.project.common.log.LogFactory;
import org.support.project.ormapping.common.NameConvertor;
import org.support.project.ormapping.entity.ColumnDefinition;
import org.support.project.ormapping.tool.DaoGenConfig;

public class DefaultTableSaveMethodCreator {
	/** ログ */
	private static Log log = LogFactory
			.getLog(DefaultTableSaveMethodCreator.class);

	private CreatorHelper helper = new CreatorHelper();
	private NameConvertor nameConvertor = new NameConvertor();

	private DaoGenConfig config;
	private DefaultTableSQLCreator sqlCreator;

	public void writeSaveMethod(DaoGenConfig config, PrintWriter pw) {
		this.config = config;
		this.sqlCreator = new DefaultTableSQLCreator(config);

		writeSaveOnUser(pw);
		writeSave(pw);
	}

	private void writeSave(PrintWriter pw) {
		// コメント
		pw.println("\t/**");
		pw.println("\t * 保存(存在しなければ登録、存在すれば更新) ");
		pw.println("\t */");

		pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");
		// メソッド定義
		pw.print("\tpublic ");
		pw.print(config.getEntityClassName());
		pw.print(" save(");
		pw.print(config.getEntityClassName());
		pw.println(" entity) {");

		// DBの存在チェック
		List<ColumnDefinition> columnDefinitions = config.getTableDefinition()
				.getColumns();
		pw.print("\t\t");
		pw.print(config.getEntityClassName());
		pw.print(" db = selectOnKey(");
		Collection<ColumnDefinition> primaryKeys = config
				.getPrimaryKeys(columnDefinitions);
		int count = 0;
		for (ColumnDefinition columnDefinition : primaryKeys) {
			if (count > 0) {
				pw.print(", ");
			}
			pw.print("entity.");
			String feildName = nameConvertor
					.colmnNameToFeildName(columnDefinition.getColumn_name());
			pw.print(helper.feildNameToGetter(feildName));
			pw.print("()");
			count++;
		}
		pw.println(");");

		pw.println("\t\tif (db == null) {");
		pw.println("\t\t\treturn insert(entity);");
		pw.println("\t\t} else {");
		pw.println("\t\t\treturn update(entity);");
		pw.println("\t\t}");

		pw.println("\t}");
	}

	private void writeSaveOnUser(PrintWriter pw) {
		// コメント
		pw.println("\t/**");
		pw.println("\t * 保存(ユーザを指定) ");
		pw.println("\t */");

		pw.println("\t@Aspect(advice=org.support.project.ormapping.transaction.Transaction.class)");
		// メソッド定義
		pw.print("\tpublic ");
		pw.print(config.getEntityClassName());
		pw.print(" save(");
		pw.print(config.getCommonUseridType());
		pw.print(" user, ");
		pw.print(config.getEntityClassName());
		pw.println(" entity) {");

		// DBの存在チェック
		List<ColumnDefinition> columnDefinitions = config.getTableDefinition()
				.getColumns();
		pw.print("\t\t");
		pw.print(config.getEntityClassName());
		pw.print(" db = selectOnKey(");
		Collection<ColumnDefinition> primaryKeys = config
				.getPrimaryKeys(columnDefinitions);
		int count = 0;
		for (ColumnDefinition columnDefinition : primaryKeys) {
			if (count > 0) {
				pw.print(", ");
			}
			pw.print("entity.");
			String feildName = nameConvertor
					.colmnNameToFeildName(columnDefinition.getColumn_name());
			pw.print(helper.feildNameToGetter(feildName));
			pw.print("()");
			count++;
		}
		pw.println(");");

		pw.println("\t\tif (db == null) {");
		pw.println("\t\t\treturn insert(user, entity);");
		pw.println("\t\t} else {");
		pw.println("\t\t\treturn update(user, entity);");
		pw.println("\t\t}");

		pw.println("\t}");

	}

}
