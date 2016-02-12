package org.support.project.ormapping.tool.impl;

import java.io.PrintWriter;
import java.sql.Types;
import java.util.List;

import org.support.project.common.log.Log;
import org.support.project.common.log.LogFactory;
import org.support.project.ormapping.common.NameConvertor;
import org.support.project.ormapping.entity.ColumnDefinition;

/**
 * 入力チェックメソッドの作成
 * @author Koda
 *
 */
public class DefaultValidateCreator {
	/** ログ */
	private static Log log = LogFactory.getLog(DefaultEntityCteatorImpl.class);

	private CreatorHelper helper = new CreatorHelper();
	private NameConvertor nameConvertor = new NameConvertor();
	
	public DefaultValidateCreator(CreatorHelper helper, NameConvertor nameConvertor) {
		super();
		this.helper = helper;
		this.nameConvertor = nameConvertor;
	}
	
	/**
	 * DBの列定義を用いて、入力チェックメソッドを生成
	 * @param columnDefinitions
	 * @param pw
	 */
	public void writeValidate(List<ColumnDefinition> columnDefinitions, PrintWriter pw) {
		pw.println("\t/**");
		pw.println("\t * validate ");
		pw.println("\t */");
		
		pw.println("\tpublic List<ValidateError> validate() {");
		
		pw.println("\t\tList<ValidateError> errors = new ArrayList<>();");
		pw.println("\t\tValidator validator;");
		pw.println("\t\tValidateError error;");
		
		for (ColumnDefinition columnDefinition : columnDefinitions) {
			if (helper.isnot(columnDefinition.getIs_autoincrement())
				&& helper.isnot(columnDefinition.getIs_nullable())) {
				// NULLは許さない
				pw.println("\t\tvalidator = ValidatorFactory.getInstance(Validator.REQUIRED);");
				pw.print("\t\terror = validator.validate(");
				pw.print("this.");
				pw.print(nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
				pw.print(", convLabelName(\"");
				pw.print(nameConvertor.colmnNameToLabelName(columnDefinition.getColumn_name()));
				pw.println("\"));");
				pw.println("\t\tif (error != null) {");
				pw.println("\t\t\terrors.add(error);");
				pw.println("\t\t}");
			}
			short type = columnDefinition.getData_type();
			if (type == Types.INTEGER) {
				// Intの型チェック
				pw.println("\t\tvalidator = ValidatorFactory.getInstance(Validator.INTEGER);");
				pw.print("\t\terror = validator.validate(");
				pw.print("this.");
				pw.print(nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
				pw.print(", convLabelName(\"");
				pw.print(nameConvertor.colmnNameToLabelName(columnDefinition.getColumn_name()));
				pw.println("\"));");
				pw.println("\t\tif (error != null) {");
				pw.println("\t\t\terrors.add(error);");
				pw.println("\t\t}");
			} else if (type == Types.VARCHAR) {
				// VARCHAR なので文字数チェック
				pw.println("\t\tvalidator = ValidatorFactory.getInstance(Validator.MAX_LENGTH);");
				pw.print("\t\terror = validator.validate(");
				pw.print("this.");
				pw.print(nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
				pw.print(", convLabelName(\"");
				pw.print(nameConvertor.colmnNameToLabelName(columnDefinition.getColumn_name()));
				pw.print("\"), ");
				pw.print(columnDefinition.getCharacter_maximum_length());
				pw.println(");");
				
				pw.println("\t\tif (error != null) {");
				pw.println("\t\t\terrors.add(error);");
				pw.println("\t\t}");
			}
		}
		
		pw.println("\t\treturn errors;");
		
		pw.println("\t}");		
	}
	
	
	
	/**
	 * DBの列定義を用いて、入力チェックメソッドを生成
	 * クラスに紐づく、staticメソッドを作成
	 * @param columnDefinitions
	 * @param pw
	 */
	public void writeClassValidate(List<ColumnDefinition> columnDefinitions, PrintWriter pw) {
		pw.println("\t/**");
		pw.println("\t * validate ");
		pw.println("\t */");
		
		pw.println("\tpublic List<ValidateError> validate(Map<String, String> values) {");
		
		pw.println("\t\tList<ValidateError> errors = new ArrayList<>();");
		pw.println("\t\tValidator validator;");
		pw.println("\t\tValidateError error;");
		
		for (ColumnDefinition columnDefinition : columnDefinitions) {
			if (helper.isnot(columnDefinition.getIs_autoincrement())
					&& helper.isnot(columnDefinition.getIs_nullable())) {
				// NULLは許さない
				pw.println("\t\tvalidator = ValidatorFactory.getInstance(Validator.REQUIRED);");
				pw.print("\t\terror = validator.validate(");
				pw.print("values.get(\"");
				pw.print(nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
				pw.print("\"), convLabelName(\"");
				pw.print(nameConvertor.colmnNameToLabelName(columnDefinition.getColumn_name()));
				pw.println("\"));");
				pw.println("\t\tif (error != null) {");
				pw.println("\t\t\terrors.add(error);");
				pw.println("\t\t}");
			}
			short type = columnDefinition.getData_type();
			if (type == Types.INTEGER) {
				// Intの型チェック
				pw.println("\t\tvalidator = ValidatorFactory.getInstance(Validator.INTEGER);");
				pw.print("\t\terror = validator.validate(");
				pw.print("values.get(\"");
				pw.print(nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
				pw.print("\"), convLabelName(\"");
				pw.print(nameConvertor.colmnNameToLabelName(columnDefinition.getColumn_name()));
				pw.println("\"));");
				pw.println("\t\tif (error != null) {");
				pw.println("\t\t\terrors.add(error);");
				pw.println("\t\t}");
			} else if (type == Types.VARCHAR) {
				// VARCHAR なので文字数チェック
				pw.println("\t\tvalidator = ValidatorFactory.getInstance(Validator.MAX_LENGTH);");
				pw.print("\t\terror = validator.validate(");
				pw.print("values.get(\"");
				pw.print(nameConvertor.colmnNameToFeildName(columnDefinition.getColumn_name()));
				pw.print("\"), convLabelName(\"");
				pw.print(nameConvertor.colmnNameToLabelName(columnDefinition.getColumn_name()));
				pw.print("\"), ");
				pw.print(columnDefinition.getCharacter_maximum_length());
				pw.println(");");
				
				pw.println("\t\tif (error != null) {");
				pw.println("\t\t\terrors.add(error);");
				pw.println("\t\t}");
			}
		}
		
		pw.println("\t\treturn errors;");
		
		pw.println("\t}");
	}
	
}
