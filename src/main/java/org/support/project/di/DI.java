package org.support.project.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DIで制御する事を表すアノテーション
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DI {
	
	/**
	 * 実装クラス
	 * @return
	 */
	Class<?> impl() default NoImpl.class;
	
	/**
	 * インスタンス
	 * @return
	 */
	Instance instance() default Instance.Prototype;
	
	
	/**
	 * キーでインスタンスのクラスを振り分ける場合のキー
	 * @return
	 */
	String[] keys() default {};
	/**
	 * キーでインスタンスのクラスを振り分ける場合のクラス(キーと同じ個数の配列にすること)
	 * @return
	 */
	Class<?>[] impls() default {};
	
	
	
}

