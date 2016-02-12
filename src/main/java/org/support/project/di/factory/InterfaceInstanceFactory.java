package org.support.project.di.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.support.project.aop.Advice;
import org.support.project.aop.Aspect;
import org.support.project.aop.GenericProxyFactory;
import org.support.project.common.classanalysis.ClassAnalysisForDI;
import org.support.project.common.classanalysis.ClassAnalysisForDIFactory;
import org.support.project.common.log.Log;
import org.support.project.common.log.LogFactory;
import org.support.project.di.DI;
import org.support.project.di.exception.DIException;

public class InterfaceInstanceFactory {
	/** ログ */
	private static Log logger = LogFactory.getLog(InterfaceInstanceFactory.class);
	
	/** キーに対する実装クラスを保持するマップ */
	private static Map<String, Class<?>> keyMap = null;
	
	/**
	 * シングルトンのインスタンスを管理するマップを取得
	 * @return
	 */
	private synchronized  static Map<String, Class<?>> getKeyMap() {
		return keyMap;
	}

	public synchronized static void init() {
		if (keyMap == null) {
			keyMap = new HashMap<>();
			if (logger.isTraceEnabled()) {
				logger.trace("The keyMap holding Singleton's instance was generated.");
			}
		}
	}
	
	

	
	
	public static <T> T newInstance(String key, final Class<? extends T> type) throws InstantiationException, IllegalAccessException {
		T object;
		// Interfaceであった場合、Java標準の動的プロクシを利用する
		DI di = type.getAnnotation(DI.class);
		if (di == null) {
			throw new DIException("errors.di.no.setting", type.getName());
		}
		Class<?> impl = null;
		
		if (getKeyMap().containsKey(key)) {
			impl = getKeyMap().get(key);
		} else {
			if (key.equals(type.getName())){
				impl = di.impl();
				getKeyMap().put(key, impl);
			} else {
				String[] keys = di.keys();
				Class<?>[] impls = di.impls();
				
				if (keys.length != impls.length) {
					throw new DIException("errors.di.impl.wrong", key, type.getName());
				}
				//DIに設定されている全ての情報を読み出す
				for (int i = 0; i < keys.length; i++) {
					getKeyMap().put(keys[i], impls[i]);
				}
				if (getKeyMap().containsKey(key)) {
					impl = getKeyMap().get(key);
				} else {
					throw new DIException("errors.di.impl.wrong", key, type.getName());
				}
			}
		}
		
		if (!type.isAssignableFrom(impl)) {
			throw new DIException("errors.di.impl.wrong", impl.getName(), type.getName());
		}
		
		object = (T) impl.newInstance();
		if (logger.isTraceEnabled()) {
			logger.trace("The instance of " + impl.getName() + " was generated.");
		}
		
		ClassAnalysisForDI analysis = ClassAnalysisForDIFactory.getClassAnalysis(type);
		if (!analysis.isAspectAnnotationExists()) {
			//AOPが存在しない → プロクシでくるむ必要無し
			return object;
		}
		
		object = GenericProxyFactory.getProxy(type, object);
		if (logger.isTraceEnabled()) {
			logger.trace("Proxy was generated and AOP was applied. " + type.getName());
		}
		
		return object;
	}
	
}
